package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛帖子Mapper接口
 */
@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {
}

