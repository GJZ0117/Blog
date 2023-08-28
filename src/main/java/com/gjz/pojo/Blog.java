package com.gjz.pojo;

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
public class Blog {

    @TableId(type= IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    @TableField("create_time")
    private String createTime;

    @TableField("type_id")
    private Long typeId;

    @TableField("first_picture")
    private String firstPicture;

    private Integer views;

    private String description;

    private Boolean authorship;
}
