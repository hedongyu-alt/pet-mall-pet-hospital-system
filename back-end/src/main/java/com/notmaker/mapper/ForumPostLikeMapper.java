package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.ForumPostLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛帖子点赞Mapper接口
 */
@Mapper
public interface ForumPostLikeMapper extends BaseMapper<ForumPostLike> {
}

