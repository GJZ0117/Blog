package com.gjz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjz.pojo.Type;
import com.gjz.vo.TypeVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TypeDao extends BaseMapper<Type> {
    @Select("select type_id, name, count(*) as num from (select type_id, name from t_type right join t_blog on t_type.id=t_blog.type_id) as multi_table group by type_id order by count(*) desc limit #{limit}")
    public List<TypeVo> findHotTypes(int limit);

    @Select("select t_type.id as id, t_type.name as name from t_blog right join t_type on t_blog.type_id=t_type.id where t_blog.id = #{blogId}")
    public Type findTypeByBlogId(Long blogId);
}
