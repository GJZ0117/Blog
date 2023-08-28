package com.gjz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gjz.dao.BlogDao;
import com.gjz.pojo.Blog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    @Async("taskExecutor")
    public void updateBlogViews(BlogDao blogDao, Blog blog) {
        Blog blogUpdate = new Blog();
        blogUpdate.setViews(blog.getViews() + 1);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getId, blog.getId());
        // 多线程访问使用乐观锁更新views
        queryWrapper.eq(Blog::getViews, blog.getViews());
        int updateRow = 0;
        int testTimes = 0;
        while (updateRow == 0 && testTimes < 3){
            updateRow = blogDao.update(blogUpdate, queryWrapper);
            testTimes++;
        }
    }
}
