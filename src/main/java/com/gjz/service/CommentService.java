package com.gjz.service;

import com.gjz.vo.CommentVo;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;

public interface CommentService {
    Result findCommentsByBlogId(PageParams pageParams);

    Result addComment(CommentVo commentVo);

    Result deleteCommentByCommentId(CommentVo commentVo);
}
