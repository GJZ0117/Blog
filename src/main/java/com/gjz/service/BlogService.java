package com.gjz.service;


import com.gjz.vo.BlogVo;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;

public interface BlogService {

    Result listBlogs(PageParams pageParams);

    Result getBlogCount();

    Result findBlogsByTypeId(PageParams pageParams);

    Result findBlogsByTagId(PageParams pageParams);

    Result archiveBlogs();

    Result findBlogByBlogId(PageParams pageParams);

    Result adminListBlogs(PageParams pageParams);

    Result deleteBlogById(PageParams pageParams);

    Result adminBlogDetails(PageParams pageParams);

    Result adminPublish(BlogVo blogVo);

    Result totalViewCount();

    Result search(PageParams pageParams);
}
