package com.gjz.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gjz.pojo.Tag;
import lombok.Data;
import java.util.List;

@Data
public class BlogVo {

    private Long id;

    private String title;

    private String content;

    @TableField("create_time")
    private String createTime;

    private String createYear;

    @TableField("type_id")
    private Long typeId;

    private String typeName;

    @TableField("first_picture")
    private String firstPicture;

    private List<Tag> tags;

    private String first_picture;

    private Integer views;

    private String description;

    private Boolean authorship;
}
