package com.gjz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gjz.dao.CommentDao;
import com.gjz.pojo.Comment;
import com.gjz.service.CommentService;
import com.gjz.vo.CommentVo;
import com.gjz.vo.ErrorCode;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    // 新增评论
    @Override
    public Result addComment(CommentVo commentVo) {
        Comment comment = new Comment();
        comment.setContent(commentVo.getContent());
        comment.setEmail(commentVo.getEmail());
        comment.setNickname(commentVo.getNickname());
        comment.setBlogId(commentVo.getBlogId());
        comment.setParentCommentId(commentVo.getParentCommentId());
        comment.setAdminComment(commentVo.getAdminComment());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        comment.setCreateTime(sdf.format(date));
        int count = commentDao.insert(comment);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail(ErrorCode.COMMENT_FAIL.getCode(), ErrorCode.COMMENT_FAIL.getMsg());
        }
    }

    @Transactional
    @Override
    public Result deleteCommentByCommentId(CommentVo commentVo) {
        Long commentId = commentVo.getId();
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentCommentId, commentId);
        List<Comment> childrenComments = commentDao.selectList(queryWrapper);
        queryWrapper.clear();
        if (childrenComments.size() == 0) {
            queryWrapper.eq(Comment::getId, commentId);
            int row = commentDao.delete(queryWrapper);
            queryWrapper.clear();
            return Result.success(row);
        }
        queryWrapper.eq(Comment::getId, commentId);
        Comment comment = commentDao.selectOne(queryWrapper);
        Long parentCommentId = comment.getParentCommentId();
        for (Comment childComment : childrenComments) {
            childComment.setParentCommentId(parentCommentId);
            commentDao.updateById(childComment);
        }
        int row = commentDao.delete(queryWrapper);
        return Result.success(row);
    }

    // 通过博客id找到相关联的评论
    @Override
    public Result findCommentsByBlogId(PageParams pageParams) {
        Long blogId = pageParams.getBlogId();
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getBlogId, blogId).isNull(Comment::getParentCommentId);
        List<Comment> parentComments = commentDao.selectList(queryWrapper);
        List<CommentVo> parentCommentVos = new ArrayList<>();
        for (Comment parentComment : parentComments) {
            List<CommentVo> childrenComments = bfsReplyComments(parentComment, blogId);
            CommentVo commentVo = convertCommentVofromComment(parentComment);
            commentVo.setChildrenComment(childrenComments);
            parentCommentVos.add(commentVo);
        }
        return Result.success(parentCommentVos);
    }

    // 将comment转换为commentVo
    private CommentVo convertCommentVofromComment(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setChildrenComment(new ArrayList<>());
        return commentVo;
    }

    // 采用广度优先遍历思想搜索评论的评论
    private List<CommentVo> bfsReplyComments(Comment comment, Long blogId) {
        List<CommentVo> replyComments = new ArrayList<>();
        Deque<Comment> queue = new LinkedList<>();
        queue.add(comment);
        Map<Long, String> parentIdAndParentNicknameMap = new HashMap<>();
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        while (!queue.isEmpty()) {
            Comment curComment = queue.pollLast();
            CommentVo curCommentVo = convertCommentVofromComment(curComment);
            parentIdAndParentNicknameMap.put(curCommentVo.getId(), curCommentVo.getNickname());
            if (curCommentVo.getParentCommentId() != null) {
                curCommentVo.setParentCommentName(parentIdAndParentNicknameMap.get(curCommentVo.getParentCommentId()));
            }
            replyComments.add(curCommentVo);
            Long curCommentId = curComment.getId();
            queryWrapper.eq(Comment::getBlogId, blogId).eq(Comment::getParentCommentId, curCommentId);
            List<Comment> childrenComments = commentDao.selectList(queryWrapper);
            queue.addAll(childrenComments);
            queryWrapper.clear();
        }
        replyComments.remove(0);
        return replyComments;
    }
}
