package com.gjz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjz.pojo.BlogTag;

public interface BlogTagDao extends BaseMapper<BlogTag> {
//    @Select("select tag_id from t_blog_tag where blog_id=#{blogId}")
//    public List<Long> findTagIdByBlodId(Long blogId);
}
