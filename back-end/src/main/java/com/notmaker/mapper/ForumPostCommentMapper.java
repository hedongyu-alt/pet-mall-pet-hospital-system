package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.ForumPostComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛帖子评论Mapper接口
 */
@Mapper
public interface ForumPostCommentMapper extends BaseMapper<ForumPostComment> {
}

