package com.gjz.service;

import com.gjz.vo.PageParams;
import com.gjz.vo.Result;

public interface TypeService {

//    Result listTypes();

    Result findHotTypes(int limit);

    Result adminListTypes(PageParams pageParams);

    Result getTypeCount();

    Result editTypeByTypeId(PageParams pageParams);

    Result deleteTypeByTypeId(PageParams pageParams);

    Result addType(PageParams pageParams);

    Result adminListAllTypes();
}
