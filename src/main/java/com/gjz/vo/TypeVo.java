package com.gjz.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class TypeVo {
    @TableField("type_id")
    private Long typeId;

    private String name;

    private Integer num;
}
