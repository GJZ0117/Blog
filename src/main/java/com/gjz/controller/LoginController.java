package com.gjz.controller;

import com.gjz.Aspect.LogAnnotation;
import com.gjz.service.LoginService;
import com.gjz.vo.LoginParam;
import com.gjz.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 后台管理登录
    @LogAnnotation(module = "LoginController", operation = "login")
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
