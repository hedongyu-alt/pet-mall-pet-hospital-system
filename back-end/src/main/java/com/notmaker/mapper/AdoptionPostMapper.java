package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.AdoptionPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物领养帖子Mapper接口
 */
@Mapper
public interface AdoptionPostMapper extends BaseMapper<AdoptionPost> {
    
}

