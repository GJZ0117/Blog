package com.gjz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjz.pojo.Tag;
import com.gjz.vo.TagVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TagDao extends BaseMapper<Tag> {
    @Select("select id, name, count(*) as num from (select * from t_tag right join t_blog_tag on t_tag.id=t_blog_tag.tag_id) as multi_table group by id order by count(*) desc limit #{limit}")
    public List<TagVo> findHotTags(int limit);

    @Select("select id, name from (select * from t_blog_tag right join t_tag on t_blog_tag.tag_id=t_tag.id) as multi_table where blog_id=#{blogId}")
    public List<Tag> findTagsByBlogId(Long blogId);

}
