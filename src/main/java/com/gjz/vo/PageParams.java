package com.gjz.vo;

import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class PageParams {

//    private int page = 1;
    private Integer page;

//    private int pageSize = 5;
    private Integer pageSize;

    private String searchData = null;

    private Long blogId;

    private Long typeId;

    private String newTypeName;

    private Long tagId;

    private String newTagName;
}
