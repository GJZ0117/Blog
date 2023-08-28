package com.gjz.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Tag {

    @TableId(type= IdType.AUTO)
    private Long id;

    private String name;

    public Tag(Long id) {
        this.id = id;
    }
}
