package com.gjz.controller;

import com.gjz.Aspect.LogAnnotation;
import com.gjz.service.CommentService;
import com.gjz.vo.CommentVo;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 根据博客id加载评论列表
    @LogAnnotation(module = "CommentController", operation = "findCommentsByBlogId")
    @PostMapping("/findCommentsByBlogId")
    public Result findCommentsByBlogId(@RequestBody PageParams pageParams){
        return commentService.findCommentsByBlogId(pageParams);
    }

    // 添加评论
    @LogAnnotation(module = "CommentController", operation = "addComment")
    @PostMapping("/addComment")
    public Result addComment(@RequestBody CommentVo commentVo){
        return commentService.addComment(commentVo);
    }

    // 按照评论id删除评论
    @LogAnnotation(module = "CommentController", operation = "deleteCommentByCommentId")
    @PostMapping("/deleteCommentByCommentId")
    public Result deleteCommentByCommentId(@RequestBody CommentVo commentVo) {
        return commentService.deleteCommentByCommentId(commentVo);
    }
}
