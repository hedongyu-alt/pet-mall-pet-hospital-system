package com.notmaker.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.ForumPost;
import com.notmaker.entity.ForumPostComment;
import com.notmaker.entity.ForumPostLike;
import com.notmaker.entity.User;
import com.notmaker.mapper.ForumPostCommentMapper;
import com.notmaker.mapper.ForumPostLikeMapper;
import com.notmaker.mapper.ForumPostMapper;
import com.notmaker.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 论坛帖子Service实现类
 */
@Service
public class ForumPostServiceImpl implements ForumPostService {

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private ForumPostLikeMapper forumPostLikeMapper;

    @Autowired
    private ForumPostCommentMapper forumPostCommentMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询帖子列表
     */
    @Override
    public Map<String, Object> getPostList(Map<String, Object> params) {
        // 获取参数
        String title = (String) params.get("title");
        String keywords = (String) params.get("keywords");
        String publisher = (String) params.get("publisher");
        String orderBy = (String) params.get("orderBy");
        Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 12;

        // 构建查询条件
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();

        // 标题搜索
        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }

        // 关键词搜索
        if (keywords != null && !keywords.isEmpty()) {
            queryWrapper.like("keywords", keywords);
        }

        // 发布者筛选
        if (publisher != null && userId != null) {
            if ("me".equals(publisher)) {
                queryWrapper.eq("user_id", userId);
            } else if ("others".equals(publisher)) {
                queryWrapper.ne("user_id", userId);
            }
        }

        // 排序方式
        if (orderBy != null && !orderBy.isEmpty()) {
            switch (orderBy) {
                case "like":
                    queryWrapper.orderByDesc("like_count");
                    break;
                case "comment":
                    queryWrapper.orderByDesc("comment_count");
                    break;
                default:
                    queryWrapper.orderByDesc("create_time");
                    break;
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }

        // 分页查询
        Page<ForumPost> pageObj = new Page<>(page, size);
        IPage<ForumPost> pageResult = forumPostMapper.selectPage(pageObj, queryWrapper);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("page", page);
        result.put("size", size);

        return result;
    }

    /**
     * 根据ID查询帖子详情
     */
    @Override
    public ForumPost getPostById(Long id) {
        return forumPostMapper.selectById(id);
    }

    /**
     * 创建帖子
     */
    @Override
    @Transactional
    public boolean createPost(ForumPost post, Long userId) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 设置帖子信息
        post.setUserId(userId);
        post.setUsername(user.getUsername());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCreateTime(LocalDateTime.now());

        // 插入数据库
        return forumPostMapper.insert(post) > 0;
    }

    @Override
    public boolean updatePost(ForumPost post) {
        return forumPostMapper.updateById(post) > 0;
    }

    /**
     * 点赞/取消点赞
     */
    @Override
    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        // 检查是否已点赞
        QueryWrapper<ForumPostLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("user_id", userId);
        ForumPostLike existingLike = forumPostLikeMapper.selectOne(queryWrapper);

        ForumPost post = forumPostMapper.selectById(postId);
        if (post == null) {
            return false;
        }

        if (existingLike != null) {
            // 已点赞，取消点赞
            forumPostLikeMapper.deleteById(existingLike.getId());
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
            forumPostMapper.updateById(post);
            return false;
        } else {
            // 未点赞，添加点赞
            ForumPostLike like = new ForumPostLike();
            like.setPostId(postId);
            like.setUserId(userId);
            like.setCreateTime(LocalDateTime.now());
            forumPostLikeMapper.insert(like);
            post.setLikeCount(post.getLikeCount() + 1);
            forumPostMapper.updateById(post);
            return true;
        }
    }

    /**
     * 检查用户是否已点赞
     */
    @Override
    public boolean checkLike(Long postId, Long userId) {
        QueryWrapper<ForumPostLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("user_id", userId);
        return forumPostLikeMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 添加评论
     */
    @Override
    @Transactional
    public boolean addComment(ForumPostComment comment, Long userId) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 设置评论信息
        comment.setUserId(userId);
        comment.setUsername(user.getUsername());
        comment.setCreateTime(LocalDateTime.now());

        // 插入评论
        boolean result = forumPostCommentMapper.insert(comment) > 0;

        if (result) {
            // 更新帖子评论数
            ForumPost post = forumPostMapper.selectById(comment.getPostId());
            if (post != null) {
                post.setCommentCount(post.getCommentCount() + 1);
                forumPostMapper.updateById(post);
            }
        }

        return result;
    }

    /**
     * 获取帖子的评论列表
     */
    @Override
    public List<ForumPostComment> getCommentList(Long postId) {
        QueryWrapper<ForumPostComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.orderByDesc("create_time");
        return forumPostCommentMapper.selectList(queryWrapper);
    }

    /**
     * 删除帖子
     */
    @Override
    @Transactional
    public boolean deletePost(Long id, Long userId) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null || !post.getUserId().equals(userId)) {
            return false;
        }

        // 删除帖子相关的点赞记录
        QueryWrapper<ForumPostLike> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.eq("post_id", id);
        forumPostLikeMapper.delete(likeQueryWrapper);

        // 删除帖子相关的评论
        QueryWrapper<ForumPostComment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("post_id", id);
        forumPostCommentMapper.delete(commentQueryWrapper);

        // 删除帖子
        return forumPostMapper.deleteById(id) > 0;
    }

    /**
     * 管理员分页查询帖子列表
     */
    @Override
    public Map<String, Object> getAdminPostList(Map<String, Object> params) {
        // 获取参数
        String title = (String) params.get("title");
        String keywords = (String) params.get("keywords");
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        // 构建查询条件
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();

        // 标题搜索
        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }

        // 关键词搜索
        if (keywords != null && !keywords.isEmpty()) {
            queryWrapper.like("keywords", keywords);
        }

        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");

        // 分页查询
        Page<ForumPost> pageObj = new Page<>(page, size);
        IPage<ForumPost> pageResult = forumPostMapper.selectPage(pageObj, queryWrapper);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("page", page);
        result.put("size", size);

        return result;
    }

    /**
     * 管理员删除帖子
     */
    @Override
    @Transactional
    public boolean adminDeletePost(Long id) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            return false;
        }

        // 删除帖子相关的点赞记录
        QueryWrapper<ForumPostLike> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.eq("post_id", id);
        forumPostLikeMapper.delete(likeQueryWrapper);

        // 删除帖子相关的评论
        QueryWrapper<ForumPostComment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("post_id", id);
        forumPostCommentMapper.delete(commentQueryWrapper);

        // 删除帖子
        return forumPostMapper.deleteById(id) > 0;
    }

    @Override
    public boolean adminUpdatePost(ForumPost post) {
        return forumPostMapper.updateById(post) > 0;
    }

    /**
     * 管理员分页查询评论列表
     */
    @Override
    public Map<String, Object> getAdminCommentList(Map<String, Object> params) {
        // 获取参数
        String postTitle = (String) params.get("postTitle");
        String commenterName = (String) params.get("commenterName");
        String content = (String) params.get("content");
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        // 查询所有评论
        QueryWrapper<ForumPostComment> queryWrapper = new QueryWrapper<>();

        // 评论人昵称搜索
        if (commenterName != null && !commenterName.isEmpty()) {
            queryWrapper.like("username", commenterName);
        }

        // 评论内容搜索
        if (content != null && !content.isEmpty()) {
            queryWrapper.like("content", content);
        }

        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");

        // 分页查询
        Page<ForumPostComment> pageObj = new Page<>(page, size);
        IPage<ForumPostComment> pageResult = forumPostCommentMapper.selectPage(pageObj, queryWrapper);

        List<ForumPostComment> comments = pageResult.getRecords();
        if (comments.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("list", new java.util.ArrayList<>());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", size);
            return result;
        }

        // 收集所有需要查询的ID
        java.util.Set<Long> postIds = new java.util.HashSet<>();
        java.util.Set<Long> userIds = new java.util.HashSet<>();
        for (ForumPostComment comment : comments) {
            postIds.add(comment.getPostId());
            userIds.add(comment.getUserId());
        }

        // 批量查询帖子信息
        Map<Long, ForumPost> postMap = new HashMap<>();
        if (!postIds.isEmpty()) {
            QueryWrapper<ForumPost> postQueryWrapper = new QueryWrapper<>();
            postQueryWrapper.in("id", postIds);
            List<ForumPost> posts = forumPostMapper.selectList(postQueryWrapper);
            for (ForumPost post : posts) {
                postMap.put(post.getId(), post);
                userIds.add(post.getUserId()); // 同时收集发帖人ID
            }
        }

        // 批量查询用户信息
        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("id", userIds);
            List<User> users = userMapper.selectList(userQueryWrapper);
            for (User user : users) {
                userMap.put(user.getId(), user);
            }
        }

        // 组装评论详情列表
        List<Map<String, Object>> commentDetailList = new java.util.ArrayList<>();
        for (ForumPostComment comment : comments) {
            ForumPost post = postMap.get(comment.getPostId());
            if (post == null) {
                continue;
            }

            // 如果有帖子标题搜索条件，过滤不匹配的
            if (postTitle != null && !postTitle.isEmpty()) {
                if (!post.getTitle().contains(postTitle)) {
                    continue;
                }
            }

            User postUser = userMap.get(post.getUserId());
            User commentUser = userMap.get(comment.getUserId());

            Map<String, Object> commentDetail = new HashMap<>();
            commentDetail.put("id", comment.getId());
            commentDetail.put("postId", comment.getPostId());
            commentDetail.put("postTitle", post.getTitle());
            commentDetail.put("postImages", post.getImages());
            commentDetail.put("postUsername", post.getUsername());
            commentDetail.put("postUserNickname", postUser != null ? postUser.getNickname() : post.getUsername());
            commentDetail.put("commentUsername", comment.getUsername());
            commentDetail.put("commentUserNickname", commentUser != null ? commentUser.getNickname() : comment.getUsername());
            commentDetail.put("content", comment.getContent());
            commentDetail.put("createTime", comment.getCreateTime());

            commentDetailList.add(commentDetail);
        }

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", commentDetailList);
        result.put("total", pageResult.getTotal());
        result.put("page", page);
        result.put("size", size);

        return result;
    }

    /**
     * 管理员删除评论
     */
    @Override
    @Transactional
    public boolean adminDeleteComment(Long id) {
        ForumPostComment comment = forumPostCommentMapper.selectById(id);
        if (comment == null) {
            return false;
        }

        // 删除评论
        boolean result = forumPostCommentMapper.deleteById(id) > 0;

        if (result) {
            // 更新帖子评论数
            ForumPost post = forumPostMapper.selectById(comment.getPostId());
            if (post != null) {
                post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
                forumPostMapper.updateById(post);
            }
        }

        return result;
    }
    
    /**
     * 管理员更新评论
     */
    @Override
    public boolean adminUpdateComment(ForumPostComment comment) {
        if (comment == null || comment.getId() == null) {
            return false;
        }
        
        // 检查评论是否存在
        ForumPostComment existingComment = forumPostCommentMapper.selectById(comment.getId());
        if (existingComment == null) {
            return false;
        }
        
        // 只更新评论内容
        existingComment.setContent(comment.getContent());
        
        return forumPostCommentMapper.updateById(existingComment) > 0;
    }
}

