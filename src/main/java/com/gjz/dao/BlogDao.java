package com.gjz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjz.pojo.Blog;
import com.gjz.vo.BlogVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogDao extends BaseMapper<Blog> {
    @Select("select id, title, create_time, type_id, first_picture, views, description from (select * from t_blog right join t_blog_tag on t_blog.id = t_blog_tag.blog_id) as multi_table where tag_id=#{tagId} order by create_time desc limit #{limit} offset #{offset}")
    public List<BlogVo> findBlogsByTagId(Long tagId, int limit, int offset);
}
