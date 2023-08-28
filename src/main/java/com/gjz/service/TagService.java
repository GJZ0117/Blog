package com.gjz.service;

import com.gjz.vo.PageParams;
import com.gjz.vo.Result;

public interface TagService {
    Result findHotTags(int limit);

//    Result listTags();

    Result adminListTags(PageParams pageParams);

    Result getTagCount();

    Result editTagInput(PageParams pageParams);

    Result deleteTagByTagId(PageParams pageParams);

    Result addTag(PageParams pageParams);

    Result adminListAllTags();

    Result findMinTagId();
}
