package com.notmaker.service;

import com.notmaker.entity.ForumPost;
import com.notmaker.entity.ForumPostComment;

import java.util.List;
import java.util.Map;

/**
 * 论坛帖子Service接口
 */
public interface ForumPostService {

    /**
     * 分页查询帖子列表
     * @param params 查询参数（title, keywords, publisher, orderBy, page, size）
     * @return 分页结果
     */
    Map<String, Object> getPostList(Map<String, Object> params);

    /**
     * 根据ID查询帖子详情
     * @param id 帖子ID
     * @return 帖子详情
     */
    ForumPost getPostById(Long id);

    /**
     * 创建帖子
     * @param post 帖子对象
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean createPost(ForumPost post, Long userId);

    /**
     * 更新帖子
     * @param post 帖子对象
     * @return 是否成功
     */
    boolean updatePost(ForumPost post);

    /**
     * 点赞/取消点赞
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    boolean toggleLike(Long postId, Long userId);

    /**
     * 检查用户是否已点赞
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    boolean checkLike(Long postId, Long userId);

    /**
     * 添加评论
     * @param comment 评论对象
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean addComment(ForumPostComment comment, Long userId);

    /**
     * 获取帖子的评论列表
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<ForumPostComment> getCommentList(Long postId);

    /**
     * 删除帖子
     * @param id 帖子ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deletePost(Long id, Long userId);

    /**
     * 管理员分页查询帖子列表
     * @param params 查询参数（title, keywords, page, size）
     * @return 分页结果
     */
    Map<String, Object> getAdminPostList(Map<String, Object> params);

    /**
     * 管理员删除帖子
     * @param id 帖子ID
     * @return 是否成功
     */
    boolean adminDeletePost(Long id);

    /**
     * 管理员更新帖子
     * @param post 帖子对象
     * @return 是否成功
     */
    boolean adminUpdatePost(ForumPost post);

    /**
     * 管理员分页查询评论列表
     * @param params 查询参数（postTitle, commenterName, content, page, size）
     * @return 分页结果
     */
    Map<String, Object> getAdminCommentList(Map<String, Object> params);

    /**
     * 管理员删除评论
     * @param id 评论ID
     * @return 是否成功
     */
    boolean adminDeleteComment(Long id);
    
    /**
     * 管理员更新评论
     * @param comment 评论对象
     * @return 是否成功
     */
    boolean adminUpdateComment(ForumPostComment comment);
}

