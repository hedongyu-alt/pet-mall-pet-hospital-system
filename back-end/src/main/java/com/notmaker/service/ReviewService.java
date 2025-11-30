package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
 * 评价Service接口
 */
public interface ReviewService {
    
    /**
     * 创建评价
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param productId 商品ID
     * @param content 评价内容
     * @param rating 评分
     * @return 评价ID
     */
    Long createReview(Long userId, Long orderId, Long productId, String content, Integer rating);
    
    /**
     * 更新评价
     * @param reviewId 评价ID
     * @param userId 用户ID
     * @param content 评价内容
     * @param rating 评分
     * @return 是否成功
     */
    boolean updateReview(Long reviewId, Long userId, String content, Integer rating);
    
    /**
     * 删除评价
     * @param reviewId 评价ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteReview(Long reviewId, Long userId);
    
    /**
     * 用户端分页查询评价列表
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @return 评价分页数据
     */
    IPage<Map<String, Object>> getUserReviewList(Long userId, int page, int size);
    
    /**
     * 检查订单的商品是否已评价
     * @param orderId 订单ID
     * @param productId 商品ID
     * @param userId 用户ID
     * @return 是否已评价
     */
    boolean checkReviewed(Long orderId, Long productId, Long userId);
    
    /**
     * 管理端分页查询评价列表（支持搜索）
     * @param rating 评分筛选
     * @param content 评价内容搜索
     * @param page 当前页
     * @param size 每页大小
     * @return 评价分页数据
     */
    IPage<Map<String, Object>> getAdminReviewList(Integer rating, String content, int page, int size);
    
    /**
     * 管理端更新评价
     * @param reviewId 评价ID
     * @param content 评价内容
     * @param rating 评分
     * @return 是否成功
     */
    boolean adminUpdateReview(Long reviewId, String content, Integer rating);
    
    /**
     * 管理端删除评价
     * @param reviewId 评价ID
     * @return 是否成功
     */
    boolean adminDeleteReview(Long reviewId);
    
    /**
     * 根据商品ID分页查询评价列表
     * @param productId 商品ID
     * @param page 当前页
     * @param size 每页大小
     * @return 评价分页数据
     */
    IPage<Map<String, Object>> getProductReviewList(Long productId, int page, int size);
}

