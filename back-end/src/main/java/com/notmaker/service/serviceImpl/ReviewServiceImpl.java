package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Order;
import com.notmaker.entity.Product;
import com.notmaker.entity.Review;
import com.notmaker.entity.User;
import com.notmaker.mapper.OrderMapper;
import com.notmaker.mapper.ProductMapper;
import com.notmaker.mapper.ReviewMapper;
import com.notmaker.mapper.UserMapper;
import com.notmaker.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评价Service实现类
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewMapper reviewMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 创建评价
     */
    @Override
    public Long createReview(Long userId, Long orderId, Long productId, String content, Integer rating) {
        // 检查订单是否存在且属于该用户
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("id", orderId);
        orderQueryWrapper.eq("user_id", userId);
        Order order = orderMapper.selectOne(orderQueryWrapper);
        
        if (order == null) {
            throw new RuntimeException("订单不存在或无权限");
        }
        
        // 检查订单是否已收货
        if (!"已收货".equals(order.getStatus())) {
            throw new RuntimeException("只有已收货的订单才能评价");
        }
        
        // 检查是否已评价
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("user_id", userId);
        Review existReview = reviewMapper.selectOne(queryWrapper);
        
        if (existReview != null) {
            throw new RuntimeException("该商品已评价");
        }
        
        // 创建评价
        Review review = new Review();
        review.setUserId(userId);
        review.setOrderId(orderId);
        review.setProductId(productId);
        review.setContent(content);
        review.setRating(rating);
        
        reviewMapper.insert(review);
        
        return review.getId();
    }
    
    /**
     * 更新评价
     */
    @Override
    public boolean updateReview(Long reviewId, Long userId, String content, Integer rating) {
        // 检查评价是否存在且属于该用户
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", reviewId);
        queryWrapper.eq("user_id", userId);
        Review review = reviewMapper.selectOne(queryWrapper);
        
        if (review == null) {
            throw new RuntimeException("评价不存在或无权限");
        }
        
        // 更新评价
        review.setContent(content);
        review.setRating(rating);
        
        return reviewMapper.updateById(review) > 0;
    }
    
    /**
     * 删除评价
     */
    @Override
    public boolean deleteReview(Long reviewId, Long userId) {
        // 检查评价是否存在且属于该用户
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", reviewId);
        queryWrapper.eq("user_id", userId);
        Review review = reviewMapper.selectOne(queryWrapper);
        
        if (review == null) {
            throw new RuntimeException("评价不存在或无权限");
        }
        
        return reviewMapper.deleteById(reviewId) > 0;
    }
    
    /**
     * 用户端分页查询评价列表
     */
    @Override
    public IPage<Map<String, Object>> getUserReviewList(Long userId, int page, int size) {
        // 创建分页对象
        Page<Review> pageParam = new Page<>(page, size);
        
        // 查询条件
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        // 查询评价列表
        IPage<Review> reviewPage = reviewMapper.selectPage(pageParam, queryWrapper);
        
        // 构建返回数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Review review : reviewPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", review.getId());
            map.put("orderId", review.getOrderId());
            map.put("productId", review.getProductId());
            map.put("content", review.getContent());
            map.put("rating", review.getRating());
            map.put("createTime", review.getCreateTime());
            
            // 获取订单信息
            Order order = orderMapper.selectById(review.getOrderId());
            if (order != null) {
                map.put("orderNo", order.getOrderNo());
            }
            
            // 获取商品信息
            Product product = productMapper.selectById(review.getProductId());
            if (product != null) {
                map.put("productName", product.getName());
                // 获取第一张图片
                String productImages = product.getImages();
                if (productImages != null && !productImages.isEmpty()) {
                    String firstImage = productImages.split(",")[0];
                    map.put("productImage", firstImage);
                } else {
                    map.put("productImages", "");
                }
            }
            
            resultList.add(map);
        }
        
        // 创建返回的分页对象
        Page<Map<String, Object>> resultPage = new Page<>(page, size);
        resultPage.setRecords(resultList);
        resultPage.setTotal(reviewPage.getTotal());
        
        return resultPage;
    }
    
    /**
     * 检查订单的商品是否已评价
     */
    @Override
    public boolean checkReviewed(Long orderId, Long productId, Long userId) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("user_id", userId);
        
        return reviewMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 管理端分页查询评价列表（支持搜索）
     */
    @Override
    public IPage<Map<String, Object>> getAdminReviewList(Integer rating, String content, int page, int size) {
        // 创建分页对象
        Page<Review> pageParam = new Page<>(page, size);
        
        // 查询条件
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        
        // 评分筛选
        if (rating != null && rating > 0) {
            queryWrapper.eq("rating", rating);
        }
        
        // 评价内容搜索
        if (content != null && !content.trim().isEmpty()) {
            queryWrapper.like("content", content.trim());
        }
        
        queryWrapper.orderByDesc("create_time");
        
        // 查询评价列表
        IPage<Review> reviewPage = reviewMapper.selectPage(pageParam, queryWrapper);
        
        // 构建返回数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Review review : reviewPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", review.getId());
            map.put("orderId", review.getOrderId());
            map.put("productId", review.getProductId());
            map.put("content", review.getContent());
            map.put("rating", review.getRating());
            map.put("createTime", review.getCreateTime());
            
            // 获取订单信息
            Order order = orderMapper.selectById(review.getOrderId());
            if (order != null) {
                map.put("orderNo", order.getOrderNo());
                map.put("userId", order.getUserId());
            }
            
            // 获取商品信息
            Product product = productMapper.selectById(review.getProductId());
            if (product != null) {
                map.put("productName", product.getName());
                // 获取第一张图片
                String images = product.getImages();
                if (images != null && !images.isEmpty()) {
                    String firstImage = images.split(",")[0];
                    map.put("productImage", firstImage);
                }
            }
            
            // 获取用户昵称
            if (order != null) {
                User user = userMapper.selectById(order.getUserId());
                if (user != null) {
                    map.put("userNickname", user.getNickname());
                }
            }
            
            resultList.add(map);
        }
        
        // 创建返回的分页对象
        Page<Map<String, Object>> resultPage = new Page<>(page, size);
        resultPage.setRecords(resultList);
        resultPage.setTotal(reviewPage.getTotal());
        
        return resultPage;
    }
    
    /**
     * 管理端更新评价
     */
    @Override
    public boolean adminUpdateReview(Long reviewId, String content, Integer rating) {
        Review review = reviewMapper.selectById(reviewId);
        
        if (review == null) {
            throw new RuntimeException("评价不存在");
        }
        
        // 更新评价
        review.setContent(content);
        review.setRating(rating);
        
        return reviewMapper.updateById(review) > 0;
    }
    
    /**
     * 管理端删除评价
     */
    @Override
    public boolean adminDeleteReview(Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        
        if (review == null) {
            throw new RuntimeException("评价不存在");
        }
        
        return reviewMapper.deleteById(reviewId) > 0;
    }
    
    /**
     * 根据商品ID分页查询评价列表
     */
    @Override
    public IPage<Map<String, Object>> getProductReviewList(Long productId, int page, int size) {
        // 创建分页对象
        Page<Review> pageParam = new Page<>(page, size);
        
        // 查询条件
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.orderByDesc("create_time");
        
        // 查询评价列表
        IPage<Review> reviewPage = reviewMapper.selectPage(pageParam, queryWrapper);
        
        // 构建返回数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Review review : reviewPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", review.getId());
            map.put("userId", review.getUserId());
            map.put("content", review.getContent());
            map.put("rating", review.getRating());
            map.put("createTime", review.getCreateTime());
            
            // 获取用户昵称和头像
            User user = userMapper.selectById(review.getUserId());
            if (user != null) {
                map.put("userName", user.getUsername());
                map.put("userNickname", user.getNickname());
                map.put("userAvatar", user.getAvatar());
            }
            
            resultList.add(map);
        }
        
        // 创建返回的分页对象
        Page<Map<String, Object>> resultPage = new Page<>(page, size);
        resultPage.setRecords(resultList);
        resultPage.setTotal(reviewPage.getTotal());
        
        return resultPage;
    }
}

