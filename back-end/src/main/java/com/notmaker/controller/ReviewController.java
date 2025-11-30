package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评价Controller
 */
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * 创建评价
     * @param reviewRequest 评价请求参数
     * @return 创建结果
     */
    @PostMapping("/create")
    public Map<String, Object> createReview(@RequestBody Map<String, Object> reviewRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取参数
            Object userIdObj = reviewRequest.get("userId");
            Object orderIdObj = reviewRequest.get("orderId");
            Object productIdObj = reviewRequest.get("productId");
            String content = (String) reviewRequest.get("content");
            Object ratingObj = reviewRequest.get("rating");
            
            // 参数校验
            if (userIdObj == null || orderIdObj == null || productIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            if (content == null || content.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "评价内容不能为空");
                return result;
            }
            
            if (ratingObj == null) {
                result.put("success", false);
                result.put("message", "评分不能为空");
                return result;
            }
            
            Long userId = Long.valueOf(userIdObj.toString());
            Long orderId = Long.valueOf(orderIdObj.toString());
            Long productId = Long.valueOf(productIdObj.toString());
            Integer rating = Integer.valueOf(ratingObj.toString());
            
            // 评分范围校验
            if (rating < 1 || rating > 5) {
                result.put("success", false);
                result.put("message", "评分必须在1-5之间");
                return result;
            }
            
            // 创建评价
            Long reviewId = reviewService.createReview(userId, orderId, productId, content, rating);
            
            result.put("success", true);
            result.put("message", "评价发布成功");
            result.put("data", reviewId);
        } catch (RuntimeException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发布评价失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 更新评价
     * @param updateRequest 更新请求参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public Map<String, Object> updateReview(@RequestBody Map<String, Object> updateRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取参数
            Object reviewIdObj = updateRequest.get("reviewId");
            Object userIdObj = updateRequest.get("userId");
            String content = (String) updateRequest.get("content");
            Object ratingObj = updateRequest.get("rating");
            
            // 参数校验
            if (reviewIdObj == null || userIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            if (content == null || content.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "评价内容不能为空");
                return result;
            }
            
            if (ratingObj == null) {
                result.put("success", false);
                result.put("message", "评分不能为空");
                return result;
            }
            
            Long reviewId = Long.valueOf(reviewIdObj.toString());
            Long userId = Long.valueOf(userIdObj.toString());
            Integer rating = Integer.valueOf(ratingObj.toString());
            
            // 评分范围校验
            if (rating < 1 || rating > 5) {
                result.put("success", false);
                result.put("message", "评分必须在1-5之间");
                return result;
            }
            
            // 更新评价
            boolean success = reviewService.updateReview(reviewId, userId, content, rating);
            
            if (success) {
                result.put("success", true);
                result.put("message", "评价更新成功");
            } else {
                result.put("success", false);
                result.put("message", "评价更新失败");
            }
        } catch (RuntimeException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新评价失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 删除评价
     * @param deleteRequest 删除请求参数
     * @return 删除结果
     */
    @PostMapping("/delete")
    public Map<String, Object> deleteReview(@RequestBody Map<String, Object> deleteRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取参数
            Object reviewIdObj = deleteRequest.get("reviewId");
            Object userIdObj = deleteRequest.get("userId");
            
            // 参数校验
            if (reviewIdObj == null || userIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            Long reviewId = Long.valueOf(reviewIdObj.toString());
            Long userId = Long.valueOf(userIdObj.toString());
            
            // 删除评价
            boolean success = reviewService.deleteReview(reviewId, userId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "评价删除成功");
            } else {
                result.put("success", false);
                result.put("message", "评价删除失败");
            }
        } catch (RuntimeException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除评价失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 用户端分页查询评价列表
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @return 评价列表
     */
    @GetMapping("/list")
    public Map<String, Object> getUserReviewList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Map<String, Object>> pageResult = reviewService.getUserReviewList(userId, page, size);
            
            // 构建分页数据
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageResult.getRecords());
            pageData.put("total", pageResult.getTotal());
            pageData.put("size", pageResult.getSize());
            pageData.put("current", pageResult.getCurrent());
            pageData.put("pages", pageResult.getPages());
            
            result.put("success", true);
            result.put("data", pageData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 检查订单的商品是否已评价
     * @param orderId 订单ID
     * @param productId 商品ID
     * @param userId 用户ID
     * @return 是否已评价
     */
    @GetMapping("/check")
    public Map<String, Object> checkReviewed(
            @RequestParam Long orderId,
            @RequestParam Long productId,
            @RequestParam Long userId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean reviewed = reviewService.checkReviewed(orderId, productId, userId);
            
            result.put("success", true);
            result.put("data", reviewed);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端分页查询评价列表
     * @param rating 评分筛选
     * @param content 评价内容搜索
     * @param page 当前页
     * @param size 每页大小
     * @return 评价列表
     */
    @GetMapping("/admin/list")
    public Map<String, Object> getAdminReviewList(
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String content,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Map<String, Object>> pageResult = reviewService.getAdminReviewList(rating, content, page, size);
            
            // 构建分页数据
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageResult.getRecords());
            pageData.put("total", pageResult.getTotal());
            pageData.put("size", pageResult.getSize());
            pageData.put("current", pageResult.getCurrent());
            pageData.put("pages", pageResult.getPages());
            
            result.put("success", true);
            result.put("data", pageData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端更新评价
     * @param updateRequest 更新请求参数
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateReview(@RequestBody Map<String, Object> updateRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取参数
            Object reviewIdObj = updateRequest.get("reviewId");
            String content = (String) updateRequest.get("content");
            Object ratingObj = updateRequest.get("rating");
            
            // 参数校验
            if (reviewIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            if (content == null || content.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "评价内容不能为空");
                return result;
            }
            
            if (ratingObj == null) {
                result.put("success", false);
                result.put("message", "评分不能为空");
                return result;
            }
            
            Long reviewId = Long.valueOf(reviewIdObj.toString());
            Integer rating = Integer.valueOf(ratingObj.toString());
            
            // 评分范围校验
            if (rating < 1 || rating > 5) {
                result.put("success", false);
                result.put("message", "评分必须在1-5之间");
                return result;
            }
            
            // 更新评价
            boolean success = reviewService.adminUpdateReview(reviewId, content, rating);
            
            if (success) {
                result.put("success", true);
                result.put("message", "评价更新成功");
            } else {
                result.put("success", false);
                result.put("message", "评价更新失败");
            }
        } catch (RuntimeException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新评价失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端删除评价
     * @param deleteRequest 删除请求参数
     * @return 删除结果
     */
    @PostMapping("/admin/delete")
    public Map<String, Object> adminDeleteReview(@RequestBody Map<String, Object> deleteRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取参数
            Object reviewIdObj = deleteRequest.get("reviewId");
            
            // 参数校验
            if (reviewIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            Long reviewId = Long.valueOf(reviewIdObj.toString());
            
            // 删除评价
            boolean success = reviewService.adminDeleteReview(reviewId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "评价删除成功");
            } else {
                result.put("success", false);
                result.put("message", "评价删除失败");
            }
        } catch (RuntimeException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除评价失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 根据商品ID分页查询评价列表
     * @param productId 商品ID
     * @param page 当前页
     * @param size 每页大小
     * @return 评价列表
     */
    @GetMapping("/product/list")
    public Map<String, Object> getProductReviewList(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Map<String, Object>> pageResult = reviewService.getProductReviewList(productId, page, size);
            
            // 构建分页数据
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageResult.getRecords());
            pageData.put("total", pageResult.getTotal());
            pageData.put("size", pageResult.getSize());
            pageData.put("current", pageResult.getCurrent());
            pageData.put("pages", pageResult.getPages());
            
            result.put("success", true);
            result.put("data", pageData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端导出评价Excel
     * @param rating 评分
     * @param content 评价内容
     * @param response HTTP响应
     */
    @GetMapping("/admin/export")
    public void exportReviews(
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String content,
            javax.servlet.http.HttpServletResponse response) {
        
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=reviews_" + System.currentTimeMillis() + ".xls");
            
            // 获取所有符合条件的评价数据（不分页）
            IPage<Map<String, Object>> pageResult = reviewService.getAdminReviewList(rating, content, 1, 100000);
            List<Map<String, Object>> reviewList = pageResult.getRecords();
            
            // 构建Excel内容（简单的HTML表格格式）
            StringBuilder sb = new StringBuilder();
            sb.append("<html><head><meta charset='UTF-8'></head><body>");
            sb.append("<table border='1'>");
            sb.append("<tr>");
            sb.append("<th>评价ID</th>");
            sb.append("<th>订单编号</th>");
            sb.append("<th>商品名称</th>");
            sb.append("<th>用户昵称</th>");
            sb.append("<th>评分</th>");
            sb.append("<th>评价内容</th>");
            sb.append("<th>评价时间</th>");
            sb.append("</tr>");
            
            for (Map<String, Object> review : reviewList) {
                sb.append("<tr>");
                sb.append("<td>").append(review.get("id") != null ? review.get("id") : "").append("</td>");
                sb.append("<td>").append(review.get("orderNo") != null ? review.get("orderNo") : "").append("</td>");
                sb.append("<td>").append(review.get("productName") != null ? review.get("productName") : "").append("</td>");
                sb.append("<td>").append(review.get("userNickname") != null ? review.get("userNickname") : "").append("</td>");
                sb.append("<td>").append(review.get("rating") != null ? review.get("rating") : "").append("</td>");
                sb.append("<td>").append(review.get("content") != null ? review.get("content") : "").append("</td>");
                sb.append("<td>").append(review.get("createTime") != null ? review.get("createTime") : "").append("</td>");
                sb.append("</tr>");
            }
            
            sb.append("</table>");
            sb.append("</body></html>");
            
            // 写入响应流
            response.getWriter().write(sb.toString());
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

