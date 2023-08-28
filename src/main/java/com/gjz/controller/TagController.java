package com.gjz.controller;

import com.gjz.Aspect.LogAnnotation;
import com.gjz.service.TagService;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    // 加载所有标签数据
//    @LogAnnotation(module = "TagController", operation = "listTags")
//    @GetMapping("/listTags")
//    public Result listTags(){
//        return tagService.listTags();
//    }

    // 博客首页最热的几个标签
    @LogAnnotation(module = "TagController", operation = "hotTags")
    @GetMapping("/hotTags")
    public Result hotTags(){
        return tagService.findHotTags(5);
    }

    // 标签展示页所有标签
    @LogAnnotation(module = "TagController", operation = "allHotTags")
    @GetMapping("/allHotTags")
    public Result allHotTags() {
        return tagService.findHotTags(1000);
    }

    // 后台管理标签管理页列表
    @LogAnnotation(module = "TagController", operation = "adminListTags")
    @PostMapping("/admin/listTags")
    public Result adminListTags(@RequestBody PageParams pageParams) {
        return tagService.adminListTags(pageParams);
    }

    // 编辑文章页加载所有标签
    @LogAnnotation(module = "TagController", operation = "adminListAllTags")
    @GetMapping("/admin/listAllTags")
    public Result adminListAllTags() {
        return tagService.adminListAllTags();
    }

    // 统计标签数
    @LogAnnotation(module = "TagController", operation = "getTagCount")
    @GetMapping("/getTagCount")
    public Result getTagCount() {
        return tagService.getTagCount();
    }

    // 编辑标签
    @LogAnnotation(module = "TagController", operation = "editTagInput")
    @PostMapping("/editTagByTagId")
    public Result editTagInput(@RequestBody PageParams pageParams) {
        return tagService.editTagInput(pageParams);
    }

    // 根据标签id删除标签
    @LogAnnotation(module = "TagController", operation = "deleteTagByTagId")
    @PostMapping("/deleteTagByTagId")
    public Result deleteTagByTagId(@RequestBody PageParams pageParams) {
        return tagService.deleteTagByTagId(pageParams);
    }

    // 新增标签
    @LogAnnotation(module = "TagController", operation = "addTag")
    @PostMapping("/addTag")
    public Result addTag(@RequestBody PageParams pageParams) {
        return tagService.addTag(pageParams);
    }

    @GetMapping("/findMinTagId")
    public Result findMinTagId() {
        return tagService.findMinTagId();
    }
}
