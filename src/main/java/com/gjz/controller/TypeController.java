package com.gjz.controller;

import com.gjz.Aspect.LogAnnotation;
import com.gjz.service.TypeService;
import com.gjz.vo.PageParams;
import com.gjz.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/type")
public class TypeController {
    @Autowired
    private TypeService typeService;

    // 加载所有分类数据
//    @LogAnnotation(module = "TypeController", operation = "listTypes")
//    @GetMapping("/listTypes")
//    public Result listTypes() {
//        return typeService.listTypes();
//    }

    // 首页中加载最热的几个分类
    @LogAnnotation(module = "TypeController", operation = "hotTypes")
    @GetMapping("/hotTypes")
    public Result hotTypes() {
        return typeService.findHotTypes(5);
    }

    // 分类展示页所有类别
    @LogAnnotation(module = "TypeController", operation = "allHotTypes")
    @GetMapping("/allHotTypes")
    public Result allHotTypes() {
        return typeService.findHotTypes(1000);
    }

    // 后台管理分类管理页列表
    @LogAnnotation(module = "TypeController", operation = "adminListTypes")
    @PostMapping("/admin/listTypes")
    public Result adminListTypes(@RequestBody PageParams pageParams) {
        return typeService.adminListTypes(pageParams);
    }

    // 编辑文章页加载所有类
    @LogAnnotation(module = "TypeController", operation = "adminListAllTypes")
    @GetMapping("/admin/listAllTypes")
    public Result adminListAllTypes() {
        return typeService.adminListAllTypes();
    }

    // 统计分类总数
    @LogAnnotation(module = "TypeController", operation = "getTypeCount")
    @GetMapping("/getTypeCount")
    public Result getTypeCount() {
        return typeService.getTypeCount();
    }

    // 根据类别id编辑分类
    @LogAnnotation(module = "TypeController", operation = "editTypeByTypeId")
    @PostMapping("/editTypeByTypeId")
    public Result editTypeByTypeId(@RequestBody PageParams pageParams) {
        return typeService.editTypeByTypeId(pageParams);
    }

    // 根据类别id删除分类
    @LogAnnotation(module = "TypeController", operation = "deleteTypeByTypeId")
    @PostMapping("/deleteTypeByTypeId")
    public Result deleteTypeByTypeId(@RequestBody PageParams pageParams) {
        return typeService.deleteTypeByTypeId(pageParams);
    }

    // 新增分类
    @LogAnnotation(module = "TypeController", operation = "addType")
    @PostMapping("/addType")
    public Result addType(@RequestBody PageParams pageParams) {
        return typeService.addType(pageParams);
    }
}
