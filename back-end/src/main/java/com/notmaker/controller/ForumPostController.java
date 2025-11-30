package com.notmaker.controller;

import com.notmaker.entity.ForumPost;
import com.notmaker.entity.ForumPostComment;
import com.notmaker.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 论坛帖子Controller
 */
@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    /**
     * 分页查询帖子列表
     */
    @GetMapping("/posts")
    public Map<String, Object> getPostList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {

        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("keywords", keywords);
        params.put("publisher", publisher);
        params.put("orderBy", orderBy);
        params.put("userId", userId);
        params.put("page", page);
        params.put("size", size);

        Map<String, Object> result = forumPostService.getPostList(params);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", result);
        return response;
    }

    /**
     * 查询帖子详情
     */
    @GetMapping("/posts/{id}")
    public Map<String, Object> getPostById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        ForumPost post = forumPostService.getPostById(id);

        if (post != null) {
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", post);
        } else {
            response.put("code", 404);
            response.put("message", "帖子不存在");
        }

        return response;
    }

    /**
     * 创建帖子
     */
    @PostMapping("/posts")
    public Map<String, Object> createPost(@RequestBody ForumPost post, @RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();

        boolean result = forumPostService.createPost(post, userId);

        if (result) {
            response.put("code", 200);
            response.put("message", "发布成功");
        } else {
            response.put("code", 500);
            response.put("message", "发布失败");
        }

        return response;
    }

    /**
     * 更新帖子
     */
    @PostMapping("/posts/update")
    public Map<String, Object> updatePost(@RequestBody ForumPost post) {
        Map<String, Object> response = new HashMap<>();

        boolean result = forumPostService.updatePost(post);

        if (result) {
            response.put("code", 200);
            response.put("message", "更新成功");
        } else {
            response.put("code", 500);
            response.put("message", "更新失败");
        }

        return response;
    }

    /**
     * 点赞/取消点赞
     */
    @PostMapping("/posts/{id}/like")
    public Map<String, Object> toggleLike(@PathVariable Long id, @RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();

        boolean isLiked = forumPostService.toggleLike(id, userId);

        response.put("code", 200);
        response.put("message", isLiked ? "点赞成功" : "取消点赞成功");
        response.put("data", isLiked);

        return response;
    }

    /**
     * 检查是否已点赞
     */
    @GetMapping("/posts/{id}/like")
    public Map<String, Object> checkLike(@PathVariable Long id, @RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();

        boolean isLiked = forumPostService.checkLike(id, userId);

        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", isLiked);

        return response;
    }

    /**
     * 添加评论
     */
    @PostMapping("/posts/{id}/comments")
    public Map<String, Object> addComment(@PathVariable Long id, @RequestBody ForumPostComment comment, @RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();

        comment.setPostId(id);
        boolean result = forumPostService.addComment(comment, userId);

        if (result) {
            response.put("code", 200);
            response.put("message", "评论成功");
        } else {
            response.put("code", 500);
            response.put("message", "评论失败");
        }

        return response;
    }

    /**
     * 获取评论列表
     */
    @GetMapping("/posts/{id}/comments")
    public Map<String, Object> getCommentList(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        List<ForumPostComment> comments = forumPostService.getCommentList(id);

        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", comments);

        return response;
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/posts/{id}")
    public Map<String, Object> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();

        boolean result = forumPostService.deletePost(id, userId);

        if (result) {
            response.put("code", 200);
            response.put("message", "删除成功");
        } else {
            response.put("code", 500);
            response.put("message", "删除失败");
        }

        return response;
    }

    /**
     * 管理员分页查询帖子列表
     */
    @GetMapping("/admin/posts")
    public Map<String, Object> getAdminPostList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String keywords,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("keywords", keywords);
        params.put("page", page);
        params.put("size", size);

        Map<String, Object> result = forumPostService.getAdminPostList(params);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", result);
        return response;
    }

    /**
     * 管理员删除帖子
     */
    @DeleteMapping("/admin/posts/{id}")
    public Map<String, Object> adminDeletePost(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        boolean result = forumPostService.adminDeletePost(id);

        if (result) {
            response.put("code", 200);
            response.put("message", "删除成功");
        } else {
            response.put("code", 500);
            response.put("message", "删除失败");
        }

        return response;
    }

    /**
     * 管理员更新帖子
     */
    @PostMapping("/admin/posts/update")
    public Map<String, Object> adminUpdatePost(@RequestBody ForumPost post) {
        Map<String, Object> response = new HashMap<>();

        boolean result = forumPostService.adminUpdatePost(post);

        if (result) {
            response.put("code", 200);
            response.put("message", "更新成功");
        } else {
            response.put("code", 500);
            response.put("message", "更新失败");
        }

        return response;
    }

    /**
     * 管理员分页查询评论列表
     */
    @GetMapping("/admin/comments")
    public Map<String, Object> getAdminCommentList(
            @RequestParam(required = false) String postTitle,
            @RequestParam(required = false) String commenterName,
            @RequestParam(required = false) String content,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> params = new HashMap<>();
        params.put("postTitle", postTitle);
        params.put("commenterName", commenterName);
        params.put("content", content);
        params.put("page", page);
        params.put("size", size);

        Map<String, Object> result = forumPostService.getAdminCommentList(params);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", result);
        return response;
    }

    /**
     * 管理员删除评论
     */
    @DeleteMapping("/admin/comments/{id}")
    public Map<String, Object> adminDeleteComment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        boolean result = forumPostService.adminDeleteComment(id);

        if (result) {
            response.put("code", 200);
            response.put("message", "删除成功");
        } else {
            response.put("code", 500);
            response.put("message", "删除失败");
        }

        return response;
    }
    
    /**
     * 管理员更新评论
     */
    @PostMapping("/admin/comments/update")
    public Map<String, Object> adminUpdateComment(@RequestBody ForumPostComment comment) {
        Map<String, Object> response = new HashMap<>();

        boolean result = forumPostService.adminUpdateComment(comment);

        if (result) {
            response.put("code", 200);
            response.put("message", "更新成功");
        } else {
            response.put("code", 500);
            response.put("message", "更新失败");
        }

        return response;
    }
}

