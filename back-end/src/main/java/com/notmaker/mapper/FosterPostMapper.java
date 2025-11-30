package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.FosterPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 寻找寄养帖子Mapper接口
 */
@Mapper
public interface FosterPostMapper extends BaseMapper<FosterPost> {
}

