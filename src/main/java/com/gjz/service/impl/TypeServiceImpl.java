package com.gjz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjz.dao.BlogDao;
import com.gjz.dao.TypeDao;
import com.gjz.pojo.Blog;
import com.gjz.pojo.Type;
import com.gjz.service.TypeService;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import com.gjz.vo.TypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private TypeDao typeDao;

    // 加载所有类别数据
//    @Override
//    public Result listTypes() {
//        return Result.success(typeDao.selectList(new QueryWrapper<>()));
//    }

    // 找到最热门的几个类别
    @Override
    public Result findHotTypes(int limit) {
        return Result.success(typeDao.findHotTypes(limit));
    }

    // 加载后台管理分类管理页数据
    @Override
    public Result adminListTypes(PageParams pageParams) {
        Page<Type> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Type::getId, Type::getName);
        Page<Type> typePage = typeDao.selectPage(page, queryWrapper);
        List<Type> typeList = typePage.getRecords();
        List<TypeVo> typeVoList = new ArrayList<>();

        LambdaQueryWrapper<Blog> countQueryWrapper = new LambdaQueryWrapper<>();
        for (Type type : typeList) {
            TypeVo typeVo = convertTypeVoFromType(type);
            countQueryWrapper.eq(Blog::getTypeId, typeVo.getTypeId());
            typeVo.setNum(blogDao.selectCount(countQueryWrapper));
            typeVoList.add(typeVo);
            countQueryWrapper.clear();
        }
        return Result.success(typeVoList);
    }

    // 统计所有类别数
    @Override
    public Result getTypeCount() {
        return Result.success(typeDao.selectCount(new QueryWrapper<>()));
    }

    // 编辑类别名称
    @Override
    public Result editTypeByTypeId(PageParams pageParams) {
        Type newType = new Type();
        newType.setId(pageParams.getTypeId());
        newType.setName(pageParams.getNewTypeName());
        int row = typeDao.updateById(newType);
        return Result.success(row);
    }

    // 根据类别id删除分类
    @Override
    public Result deleteTypeByTypeId(PageParams pageParams) {
        int row = typeDao.deleteById(pageParams.getTypeId());
        return Result.success(row);
    }

    // 新增类别
    @Override
    public Result addType(PageParams pageParams) {
        Type type = new Type();
        type.setName(pageParams.getNewTypeName());
        int row = typeDao.insert(type);
        return Result.success(row);
    }

    // 编辑文章页展示所有标签
    @Override
    public Result adminListAllTypes() {
        return Result.success(typeDao.selectList(new QueryWrapper<>()));
    }

    // 将type转换为typeVo
    public TypeVo convertTypeVoFromType(Type type) {
        TypeVo typeVo = new TypeVo();
        typeVo.setTypeId(type.getId());
        typeVo.setName(type.getName());
        return typeVo;
    }
}
