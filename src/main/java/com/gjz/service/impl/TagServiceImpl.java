package com.gjz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjz.dao.BlogTagDao;
import com.gjz.dao.TagDao;
import com.gjz.pojo.BlogTag;
import com.gjz.pojo.Tag;
import com.gjz.service.TagService;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import com.gjz.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private BlogTagDao blogTagDao;

    // 找到最热门的几条标签
    @Override
    public Result findHotTags(int limit) {
        return Result.success(tagDao.findHotTags(limit));
    }

    // 列出所有标签数据
//    @Override
//    public Result listTags() {
//        return Result.success(tagDao.selectList(new QueryWrapper<>()));
//    }

    // 加载后台管理标签管理页数据
    @Override
    public Result adminListTags(PageParams pageParams) {
        Page<Tag> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getName);
        Page<Tag> tagPage = tagDao.selectPage(page, queryWrapper);
        List<Tag> tagList = tagPage.getRecords();
        List<TagVo> tagVoList = new ArrayList<>();
        LambdaQueryWrapper<BlogTag> blogtagQueryWrapper = new LambdaQueryWrapper<>();
        for (Tag tag : tagList) {
            TagVo tagVo = convertTagVoFromTag(tag);
            blogtagQueryWrapper.eq(BlogTag::getTagId, tagVo.getId());
            Integer num = blogTagDao.selectCount(blogtagQueryWrapper);
            tagVo.setNum(num);
            tagVoList.add(tagVo);
            blogtagQueryWrapper.clear();
        }
        return Result.success(tagVoList);
    }

    // 统计所有标签数
    @Override
    public Result getTagCount() {
        return Result.success(tagDao.selectCount(new QueryWrapper<>()));
    }

    // 编辑标签
    @Override
    public Result editTagInput(PageParams pageParams) {
        Tag newTag = new Tag();
        newTag.setId(pageParams.getTagId());
        newTag.setName(pageParams.getNewTagName());
        int row = tagDao.updateById(newTag);
        return Result.success(row);
    }

    // 根据标签id删除标签
    @Override
    public Result deleteTagByTagId(PageParams pageParams) {
        LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTag::getTagId, pageParams.getTagId());
        blogTagDao.delete(queryWrapper);
        int row = tagDao.deleteById(pageParams.getTagId());
        return Result.success(row);
    }

    // 新增标签
    @Override
    public Result addTag(PageParams pageParams) {
        Tag tag = new Tag();
        tag.setName(pageParams.getNewTagName());
        int row = tagDao.insert(tag);
        return Result.success(row);
    }

    // 编辑文章页加载所有标签数据
    @Override
    public Result adminListAllTags() {
        return Result.success(tagDao.selectList(new QueryWrapper<>()));
    }

    @Override
    public Result findMinTagId() {
        return Result.success(tagDao.selectOne(new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getId).last("limit 1")));
    }

    // 将tag转换为tagVo
    public TagVo convertTagVoFromTag(Tag tag) {
        TagVo tagVo = new TagVo();
        tagVo.setId(tag.getId());
        tagVo.setName(tag.getName());
        return tagVo;
    }
}
