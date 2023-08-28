package com.gjz.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
    private Long id;

    private String content;

    private String createTime;

    private String email;

    private String nickname;

    private Long blogId;

    private Long parentCommentId;

    private String parentCommentName;

    private Boolean adminComment;

    private List<CommentVo> childrenComment;
}
