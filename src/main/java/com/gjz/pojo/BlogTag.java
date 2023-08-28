package com.gjz.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BlogTag {

    @TableField("blog_id")
    private Long blogId;

    @TableField("tag_id")
    private Long tagId;
}
