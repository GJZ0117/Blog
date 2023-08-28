package com.gjz.controller;

import com.gjz.Aspect.LogAnnotation;
import com.gjz.service.BlogService;
import com.gjz.vo.BlogVo;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    // 首页博客列表
    @LogAnnotation(module = "BlogController", operation = "listBlogs")
    @GetMapping("/listBlogs")
    public Result listBlogs(PageParams pageParams) {
        return blogService.listBlogs(pageParams);
    }


    // 统计博客总数
    @LogAnnotation(module = "BlogController", operation = "getBlogCount")
    @GetMapping("/getBlogCount")
    public Result getBlogCount() {
        return blogService.getBlogCount();
    }

    // 通过分类id查找对应的博客列表
    @LogAnnotation(module = "BlogController", operation = "findBlogsByTypeId")
    @PostMapping("/findBlogsByTypeId")
    public Result findBlogsByTypeId(@RequestBody PageParams pageParams) {
        return blogService.findBlogsByTypeId(pageParams);
    }

    // 通过标签id查找对应的博客列表
    @LogAnnotation(module = "BlogController", operation = "findBlogsByTagId")
    @PostMapping("/findBlogsByTagId")
    public Result findBlogsByTagId(@RequestBody PageParams pageParams) {
        return blogService.findBlogsByTagId(pageParams);
    }

    // 通过博客id查找对应的博客内容
    @LogAnnotation(module = "BlogController", operation = "findBlogByBlogId")
    @PostMapping("/findBlogByBlogId")
    public Result findBlogByBlogId(@RequestBody PageParams pageParams) {
        return blogService.findBlogByBlogId(pageParams);
    }

    // 博客归档
    @LogAnnotation(module = "BlogController", operation = "archiveBlogs")
    @GetMapping("/archiveBlogs")
    public Result archiveBlogs() {
        return blogService.archiveBlogs();
    }

    // 后台管理博客列表
    @LogAnnotation(module = "BlogController", operation = "adminListBlog")
    @PostMapping("/admin/listBlogs")
    public Result adminListBlog(@RequestBody PageParams pageParams) {
        return blogService.adminListBlogs(pageParams);
    }

    // 后台管理删除博客
    @LogAnnotation(module = "BlogController", operation = "adminDeleteBlog")
    @PostMapping("/admin/deleteBlog")
    public Result adminDeleteBlog(@RequestBody PageParams pageParams) {
        return blogService.deleteBlogById(pageParams);
    }

    // 后台管理博客详情数据
    @LogAnnotation(module = "BlogController", operation = "adminBlogDetails")
    @PostMapping("/admin/blogDetails")
    public Result adminBlogDetails(@RequestBody PageParams pageParams) {
        return blogService.adminBlogDetails(pageParams);
    }

    // 新增or修改博客
    @LogAnnotation(module = "BlogController", operation = "adminPublish")
    @PostMapping("/admin/publish")
    public Result adminPublish(@RequestBody BlogVo blogVo) {
        return blogService.adminPublish(blogVo);
    }

    // 获取浏览总量
    @LogAnnotation(module = "BlogController", operation = "totalViewCount")
    @GetMapping("/totalViewCount")
    public Result totalViewCount() {
        return blogService.totalViewCount();
    }

    @LogAnnotation(module = "BlogController", operation = "search")
    @PostMapping("/search")
    public Result search(@RequestBody PageParams pageParams) {
        return blogService.search(pageParams);
    }
}
