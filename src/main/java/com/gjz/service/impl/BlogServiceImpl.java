package com.gjz.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjz.dao.*;
import com.gjz.pojo.*;
import com.gjz.service.BlogService;
import com.gjz.service.ThreadService;
import com.gjz.vo.BlogVo;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private BlogTagDao blogTagDao;

    // 按照分页数据查找首页博客列表
    @Override
    public Result listBlogs(PageParams pageParams) {
        Page<Blog> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Blog::getAuthorship, Blog::getCreateTime, Blog::getDescription, Blog::getFirstPicture, Blog::getId, Blog::getTitle, Blog::getTypeId);
        queryWrapper.orderByDesc(Blog::getCreateTime);
        IPage<Blog> blogIPage = blogDao.selectPage(page, queryWrapper);
        List<Blog> blogs = blogIPage.getRecords();

        LambdaQueryWrapper<Type> typeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        Map<Long, String> typeMap = new HashMap<>();
        List<BlogVo> blogVoList = new ArrayList<>(blogs.size());
        for (Blog blog : blogs) {
            BlogVo blogVo = convertBlogVoFromBlog(blog);
            if (typeMap.containsKey(blogVo.getTypeId())) {
                blogVo.setTypeName(typeMap.get(blogVo.getTypeId()));
            } else {
                typeLambdaQueryWrapper.eq(Type::getId, blogVo.getTypeId());
                Type type = typeDao.selectOne(typeLambdaQueryWrapper);
                blogVo.setTypeName(type.getName());
                typeMap.put(type.getId(), type.getName());
                typeLambdaQueryWrapper.clear();
            }
            blogVo.setViews(Integer.parseInt(redisTemplate.opsForValue().get("BlogId_" + blogVo.getId())));
            blogVoList.add(blogVo);
        }
        return Result.success(blogVoList);
    }

    // 统计博客总数
    @Override
    public Result getBlogCount() {
        return Result.success(blogDao.selectCount(new LambdaQueryWrapper<>()));
    }

    // 根据类别ID及分页相关信息查找博客
    @Override
    public Result findBlogsByTypeId(PageParams pageParams) {
        // 如果typeId为-1说明第一次进入Type分类页面，查找typeId最小的类别
        if (pageParams.getTypeId() == -1) {
            Type type = typeDao.selectOne(new LambdaQueryWrapper<Type>().orderByDesc(Type::getId).last("limit 1"));
            pageParams.setTypeId(type.getId());
        }
        Page<Blog> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Blog::getAuthorship, Blog::getCreateTime, Blog::getDescription, Blog::getId, Blog::getFirstPicture, Blog::getTypeId, Blog::getTitle);
        queryWrapper.eq(Blog::getTypeId, pageParams.getTypeId());
        queryWrapper.orderByDesc(Blog::getCreateTime);
        Page<Blog> blogPage = blogDao.selectPage(page, queryWrapper);
        List<Blog> blogs = blogPage.getRecords();
        for (Blog blog : blogs) {
            blog.setTypeId(pageParams.getTypeId());
            blog.setViews(Integer.parseInt(redisTemplate.opsForValue().get("BlogId_" + blog.getId())));
        }
        return Result.success(blogs);
    }

    // 根据标签ID及分页相关信息查找博客
    @Override
    public Result findBlogsByTagId(PageParams pageParams) {
        if (pageParams.getTagId() == -1) {
            Tag tag = tagDao.selectOne(new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getId).last("limit 1"));
            pageParams.setTagId(tag.getId());
        }
        int page = pageParams.getPage();
        int pageSize = pageParams.getPageSize();
        LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
        List<BlogVo> blogVoList = blogDao.findBlogsByTagId(pageParams.getTagId(), pageSize, (page - 1) * pageSize);
        for (BlogVo blog : blogVoList) {
            // 查找每篇博客对应的标签列表
            List<Tag> tagList = tagDao.findTagsByBlogId(blog.getId());
            blog.setTags(tagList);
            // 查找每篇博客typeId对应的名称
            queryWrapper.select(Type::getName).eq(Type::getId, blog.getTypeId());
            Type type = typeDao.selectOne(queryWrapper);
            blog.setTypeName(type.getName());
            blog.setViews(Integer.parseInt(redisTemplate.opsForValue().get("BlogId_" + blog.getId())));
            queryWrapper.clear();
        }
        return Result.success(blogVoList);
    }

    @Override
    public Result archiveBlogs() {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Blog::getId, Blog::getTitle, Blog::getCreateTime, Blog::getAuthorship);
        queryWrapper.orderByAsc(Blog::getCreateTime);
        List<Blog> blogs = blogDao.selectList(queryWrapper);
        return Result.success(blogs);
    }

    // 每次浏览博客详情页时使用线程池增加浏览数
    @Autowired
    private ThreadService threadService;

    // 通过博客id找到博客相关数据并增加浏览量
    @Override
    public Result findBlogByBlogId(PageParams pageParams) {
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getId, pageParams.getBlogId());
        Blog blog = blogDao.selectOne(queryWrapper);
        // 方法一：多线程加乐观锁解决浏览量并发增加问题
//        threadService.updateBlogViews(blogDao, blog);
        // 方法二：更新redis中缓存数据解决浏览量增加并发问题
        redisTemplate.opsForValue().increment("BlogId_" + blog.getId());
        redisTemplate.opsForValue().increment("totalViewCount");
        blog.setViews(Integer.parseInt(redisTemplate.opsForValue().get("BlogId_" + blog.getId())));
        map.put("blogData", blog);
        Type type = typeDao.findTypeByBlogId(pageParams.getBlogId());
        map.put("type", type);
        List<Tag> tags = tagDao.findTagsByBlogId(pageParams.getBlogId());
        map.put("tags", tags);
        return Result.success(map);
    }

    // 后台管理博客管理页数据
    @Override
    public Result adminListBlogs(PageParams pageParams) {
        Page<Blog> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        if (pageParams.getSearchData() != null && pageParams.getSearchData().length() > 0) {
            String searchData = pageParams.getSearchData();
            queryWrapper.like(Blog::getTitle, searchData).or().like(Blog::getContent, searchData);
        }
        queryWrapper.select(Blog::getId, Blog::getTitle, Blog::getTypeId, Blog::getCreateTime);
        queryWrapper.orderByDesc(Blog::getCreateTime);
        Page<Blog> pageData = blogDao.selectPage(page, queryWrapper);
        List<Blog> blogList = pageData.getRecords();
        List<BlogVo> blogVoList = new ArrayList<>();
        for (Blog blog : blogList) {
            BlogVo blogVo = convertBlogVoFromBlog(blog);
            blogVoList.add(blogVo);
        }
        List<Type> typeList = typeDao.selectList(new QueryWrapper<>());
        for (BlogVo blogVo : blogVoList) {
            for (Type type : typeList) {
                if (blogVo.getTypeId().equals(type.getId())) {
                    blogVo.setTypeName(type.getName());
                    break;
                }
            }
        }
        return Result.success(blogVoList);
    }

    // 后台管理博客管理页根据博客id删除博客
    @Transactional
    @Override
    public Result deleteBlogById(PageParams pageParams) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getBlogId, pageParams.getBlogId());
        commentDao.delete(commentLambdaQueryWrapper);

        LambdaQueryWrapper<BlogTag> blogTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogTagLambdaQueryWrapper.eq(BlogTag::getBlogId, pageParams.getBlogId());
        blogTagDao.delete(blogTagLambdaQueryWrapper);

        int row = blogDao.deleteById(pageParams.getBlogId());
        if (row != 0) {
            redisTemplate.delete("BlogId_" + String.valueOf(pageParams.getBlogId()));
        }
        return Result.success(row);
    }

    // 后台管理根据博客id加载某篇博客详情数据
    @Override
    public Result adminBlogDetails(PageParams pageParams) {
        LambdaQueryWrapper<Blog> blogQueryWrapper = new LambdaQueryWrapper<>();
        blogQueryWrapper.eq(Blog::getId, pageParams.getBlogId());
        Blog blog = blogDao.selectOne(blogQueryWrapper);
        BlogVo blogVo = convertBlogVoFromBlog(blog);
        blogVo.setTags(tagDao.findTagsByBlogId(pageParams.getBlogId()));
        LambdaQueryWrapper<Type> typeQueryWrapper = new LambdaQueryWrapper<>();
        typeQueryWrapper.eq(Type::getId, blog.getTypeId());
        Type type = typeDao.selectOne(typeQueryWrapper);
        blogVo.setTypeName(type.getName());
        return Result.success(blogVo);
    }

    // 新增or修改博客并发布
    @Transactional
    @Override
    public Result adminPublish(BlogVo blogVo) {
        int row = 0;
        if (blogVo.getId() == null) {
            Blog blog = new Blog();
            blog.setTitle(blogVo.getTitle());
            blog.setContent(blogVo.getContent());
            blog.setDescription(blogVo.getDescription());
            blog.setTypeId(blogVo.getTypeId());
            blog.setFirstPicture(blogVo.getFirstPicture());
            blog.setViews(1);
            blog.setAuthorship(blogVo.getAuthorship());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            blog.setCreateTime(sdf.format(date));
            row = blogDao.insert(blog);
            Long blogId = blog.getId();
            if (row != 0) {
                redisTemplate.opsForValue().set("BlogId_" + blogId, JSON.toJSONString(1));
            }
            for (Tag tag : blogVo.getTags()) {
                BlogTag blogTag = new BlogTag();
                blogTag.setBlogId(blogId);
                blogTag.setTagId(tag.getId());
                blogTagDao.insert(blogTag);
            }
        } else {
            LambdaQueryWrapper<Blog> blogQueryWrapper = new LambdaQueryWrapper<>();
            blogQueryWrapper.eq(Blog::getId, blogVo.getId());
            Blog blog = blogDao.selectOne(blogQueryWrapper);

            blog.setTitle(blogVo.getTitle());
            blog.setContent(blogVo.getContent());
            blog.setDescription(blogVo.getDescription());
            blog.setTypeId(blogVo.getTypeId());
            blog.setFirstPicture(blogVo.getFirstPicture());
            blog.setAuthorship(blogVo.getAuthorship());
            row = blogDao.updateById(blog);

            Long blogId = blog.getId();
            LambdaQueryWrapper<BlogTag> blogTagQueryWrapper = new LambdaQueryWrapper<>();
            blogTagQueryWrapper.eq(BlogTag::getBlogId, blogId);
            blogTagDao.delete(blogTagQueryWrapper);
            blogTagQueryWrapper.clear();
            for (Tag tag : blogVo.getTags()) {
                blogTagQueryWrapper.eq(BlogTag::getBlogId, blogVo.getId()).eq(BlogTag::getTagId, tag.getId());
                if (blogTagDao.selectOne(blogTagQueryWrapper) == null) {
                    BlogTag blogTag = new BlogTag();
                    blogTag.setBlogId(blogId);
                    blogTag.setTagId(tag.getId());
                    blogTagDao.insert(blogTag);
                }
                blogTagQueryWrapper.clear();
            }
        }
        return Result.success(row);
    }

    // 将blog转换为blogVo
    public BlogVo convertBlogVoFromBlog(Blog blog) {
        BlogVo blogVo = new BlogVo();
        BeanUtils.copyProperties(blog, blogVo);
        return blogVo;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 获取浏览总量
    @Override
    public Result totalViewCount() {
        long totalViewCount = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get("totalViewCount")));
        return Result.success(totalViewCount);
    }

    // 搜索结果
    @Override
    public Result search(PageParams pageParams) {
        String searchData = pageParams.getSearchData();
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Blog::getId, Blog::getTitle, Blog::getDescription, Blog::getFirstPicture, Blog::getCreateTime, Blog::getViews, Blog::getTypeId);
        queryWrapper.like(StringUtils.isNotBlank(searchData), Blog::getTitle, searchData).or().like(StringUtils.isNotBlank(searchData), Blog::getContent, searchData);
        List<Blog> blogList = blogDao.selectList(queryWrapper);
        List<BlogVo> blogVoList = new ArrayList<>();
        for (Blog blog : blogList) {
            BlogVo blogVo = convertBlogVoFromBlog(blog);
            blogVoList.add(blogVo);
        }
        Map<Long, String> typeMap = new HashMap<>();
        LambdaQueryWrapper<Type> typeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        for (BlogVo blogVo : blogVoList) {
            blogVo.setViews(Integer.parseInt(redisTemplate.opsForValue().get("BlogId_" + blogVo.getId())));
            if (typeMap.containsKey(blogVo.getTypeId())) {
                blogVo.setTypeName(typeMap.get(blogVo.getTypeId()));
            } else {
                typeLambdaQueryWrapper.eq(Type::getId, blogVo.getTypeId());
                Type type = typeDao.selectOne(typeLambdaQueryWrapper);
                blogVo.setTypeName(type.getName());
                typeMap.put(type.getId(), type.getName());
                typeLambdaQueryWrapper.clear();
            }
        }
        return Result.success(blogVoList);
    }
}
