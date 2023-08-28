package com.gjz.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gjz.dao.BlogDao;
import com.gjz.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public class QuartzConfig {
    @Autowired
    private BlogDao blogDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 每隔一小时把redis中的浏览量更新到mysql
//    @Scheduled(cron = "0 0 */1 * * ?")
    @Scheduled(cron = "0 */1 * * * ?")
    public void updateTotalViews() throws Exception {
        // 获取存储在redis中的所有博客id的key
        Set<String> keys = redisTemplate.keys("BlogId_*");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();

        Blog blog = new Blog();
        for (String key : keys) {
            int views = Integer.parseInt(redisTemplate.opsForValue().get(key));
            long blogId = Long.parseLong(key.split("_")[1]);
            blog.setId(blogId);
            blog.setViews(views);
            blogDao.updateById(blog);
        }

//        queryWrapper.select(Blog::getViews);
//        List<Blog> blogList = blogDao.selectList(queryWrapper);
//        Long totalViewCount = 0L;
//        for (Blog blog : blogList) {
//            totalViewCount += blog.getViews();
//        }
//        redisTemplate.opsForValue().set("totalViewCount", JSON.toJSONString(totalViewCount));
    }
}
