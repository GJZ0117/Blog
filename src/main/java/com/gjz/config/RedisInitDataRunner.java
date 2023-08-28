package com.gjz.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gjz.dao.BlogDao;
import com.gjz.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class RedisInitDataRunner {
    @Autowired
    private BlogDao blogDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // SpringBoot启动时初始化redis中浏览数据
    @PostConstruct
    public void initRedisData() {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Blog::getId, Blog::getViews);
        List<Blog> blogList = blogDao.selectList(queryWrapper);
        int totalViewCount = 0;
        for (Blog blog : blogList) {
            totalViewCount += blog.getViews();
            redisTemplate.opsForValue().set("BlogId_" + blog.getId(), JSON.toJSONString(blog.getViews()));
        }
        redisTemplate.opsForValue().set("totalViewCount", JSON.toJSONString(totalViewCount));
    }
}
