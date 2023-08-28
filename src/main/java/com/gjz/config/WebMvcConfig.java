package com.gjz.config;

import com.gjz.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    // 登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/blog/admin/listBlogs")
                .addPathPatterns("/api/blog/admin/deleteBlog")
                .addPathPatterns("/api/blog/admin/blogDetails")
                .addPathPatterns("/api/blog/admin/publish")
                .addPathPatterns("/api/upload")
                .addPathPatterns("/api/type/admin/listTypes")
                .addPathPatterns("/api/type/admin/listAllTypes")
                .addPathPatterns("/api/type/editTypeByTypeId")
                .addPathPatterns("/api/type/deleteTypeByTypeId")
                .addPathPatterns("/api/type/addType")
                .addPathPatterns("/api/tag/admin/listTags")
                .addPathPatterns("/api/tag/admin/listAllTags")
                .addPathPatterns("/api/tag/editTagByTagId")
                .addPathPatterns("/api/tag/deleteTagByTagId")
                .addPathPatterns("/api/tag/deleteTagByTagId")
                .addPathPatterns("/api/tag/addTag")
                .addPathPatterns("/api/admin/logout")
                .addPathPatterns("/api/comment/deleteCommentByCommentId");
    }

    // 跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
}
