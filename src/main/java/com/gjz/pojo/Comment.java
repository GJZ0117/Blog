package com.gjz.pojo;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment {

    @TableId(type= IdType.AUTO)
    private Long id;

    private String content;

    @TableField("create_time")
    private String createTime;

    private String email;

    private String nickname;

    @TableField("blog_id")
    private Long blogId;

    @TableField(value = "parent_comment_id", updateStrategy = FieldStrategy.IGNORED)
    private Long parentCommentId;

    @TableField("admin_comment")
    private Boolean adminComment;
}
