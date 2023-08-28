package com.gjz.controller;

import com.gjz.Aspect.LogAnnotation;
import com.gjz.service.LoginService;
import com.gjz.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    // 后台管理登出
    @LogAnnotation(module = "LogoutController", operation = "logout")
    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }

}
