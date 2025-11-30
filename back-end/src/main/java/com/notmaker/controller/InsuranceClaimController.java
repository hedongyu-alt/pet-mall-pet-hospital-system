package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.InsuranceClaim;
import com.notmaker.entity.InsuranceOrder;
import com.notmaker.service.InsuranceClaimService;
import com.notmaker.service.InsuranceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 理赔申请Controller
 */
@RestController
@RequestMapping("/api/insurance-claim")
public class InsuranceClaimController {
    
    @Autowired
    private InsuranceClaimService insuranceClaimService;
    
    @Autowired
    private InsuranceOrderService insuranceOrderService;
    
    /**
     * 用户提交理赔申请
     * @param claimRequest 理赔申请请求
     * @return 提交结果
     */
    @PostMapping("/submit")
    public Map<String, Object> submitClaim(@RequestBody Map<String, Object> claimRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取并校验参数
            Object userIdObj = claimRequest.get("userId");
            Object orderIdObj = claimRequest.get("orderId");
            String reason = (String) claimRequest.get("reason");
            String description = (String) claimRequest.get("description");
            String evidenceImages = (String) claimRequest.get("evidenceImages");
            
            // 参数校验
            if (userIdObj == null) {
                result.put("success", false);
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            if (orderIdObj == null) {
                result.put("success", false);
                result.put("message", "保单ID不能为空");
                return result;
            }
            
            if (reason == null || reason.isEmpty()) {
                result.put("success", false);
                result.put("message", "申请原因不能为空");
                return result;
            }
            
            if (description == null || description.isEmpty()) {
                result.put("success", false);
                result.put("message", "情况描述不能为空");
                return result;
            }
            
            Long userId = Long.valueOf(userIdObj.toString());
            Long orderId = Long.valueOf(orderIdObj.toString());
            
            // 查询保险订单
            InsuranceOrder order = insuranceOrderService.getOrderDetail(orderId, userId);
            if (order == null) {
                result.put("success", false);
                result.put("message", "保单不存在");
                return result;
            }
            
            // 创建理赔申请
            InsuranceClaim insuranceClaim = new InsuranceClaim();
            insuranceClaim.setUserId(userId);
            insuranceClaim.setOrderId(orderId);
            insuranceClaim.setInsuranceName(order.getInsuranceName());
            insuranceClaim.setPetName(order.getPetName());
            insuranceClaim.setReason(reason);
            insuranceClaim.setDescription(description);
            insuranceClaim.setEvidenceImages(evidenceImages);
            
            // 调用服务提交理赔申请
            boolean success = insuranceClaimService.submitClaim(insuranceClaim);
            
            if (success) {
                result.put("success", true);
                result.put("message", "提交成功");
            } else {
                result.put("success", false);
                result.put("message", "提交失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "提交失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 用户查询自己的理赔申请列表
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @param status 状态筛选
     * @return 理赔申请列表
     */
    @GetMapping("/user/list")
    public Map<String, Object> getUserClaims(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<InsuranceClaim> pageResult = insuranceClaimService.getUserClaims(userId, page, size, status);
            
            // 手动构建分页数据
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
     * 用户查询理赔申请详情
     * @param claimId 理赔ID
     * @param userId 用户ID
     * @return 理赔详情
     */
    @GetMapping("/user/detail/{claimId}")
    public Map<String, Object> getClaimDetail(
            @PathVariable Long claimId,
            @RequestParam Long userId) {
        
        Map<String, Object> result = new HashMap<>();
        
        InsuranceClaim claim = insuranceClaimService.getClaimDetail(claimId, userId);
        if (claim != null) {
            result.put("success", true);
            result.put("data", claim);
        } else {
            result.put("success", false);
            result.put("message", "理赔申请不存在");
        }
        
        return result;
    }
    
    /**
     * 管理员查询所有理赔申请列表
     * @param page 当前页
     * @param size 每页大小
     * @param status 状态筛选
     * @param reason 申请原因筛选
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 理赔申请列表
     */
    @GetMapping("/admin/list")
    public Map<String, Object> getAdminClaims(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<InsuranceClaim> pageResult = insuranceClaimService.getAdminClaims(page, size, status, reason, startTime, endTime);
            
            // 手动构建分页数据
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
     * 管理员查询理赔申请详情
     * @param claimId 理赔ID
     * @return 理赔详情
     */
    @GetMapping("/admin/detail/{claimId}")
    public Map<String, Object> getAdminClaimDetail(@PathVariable Long claimId) {
        Map<String, Object> result = new HashMap<>();
        
        InsuranceClaim claim = insuranceClaimService.getAdminClaimDetail(claimId);
        if (claim != null) {
            result.put("success", true);
            result.put("data", claim);
        } else {
            result.put("success", false);
            result.put("message", "理赔申请不存在");
        }
        
        return result;
    }
    
    /**
     * 管理员审核通过理赔申请
     * @param approveRequest 审核通过请求
     * @return 审核结果
     */
    @PostMapping("/admin/approve")
    public Map<String, Object> approveClaim(@RequestBody Map<String, Object> approveRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object claimIdObj = approveRequest.get("claimId");
            String claimAmount = (String) approveRequest.get("claimAmount");
            String adminRemark = (String) approveRequest.get("adminRemark");
            
            // 参数校验
            if (claimIdObj == null) {
                result.put("success", false);
                result.put("message", "理赔ID不能为空");
                return result;
            }
            
            if (claimAmount == null || claimAmount.isEmpty()) {
                result.put("success", false);
                result.put("message", "理赔金额不能为空");
                return result;
            }
            
            if (adminRemark == null || adminRemark.isEmpty()) {
                result.put("success", false);
                result.put("message", "管理员备注不能为空");
                return result;
            }
            
            Long claimId = Long.valueOf(claimIdObj.toString());
            
            boolean success = insuranceClaimService.approveClaim(claimId, claimAmount, adminRemark);
            
            if (success) {
                result.put("success", true);
                result.put("message", "审核通过成功");
            } else {
                result.put("success", false);
                result.put("message", "审核通过失败，请检查理赔状态");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "审核失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理员审核拒绝理赔申请
     * @param rejectRequest 审核拒绝请求
     * @return 审核结果
     */
    @PostMapping("/admin/reject")
    public Map<String, Object> rejectClaim(@RequestBody Map<String, Object> rejectRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object claimIdObj = rejectRequest.get("claimId");
            String rejectReason = (String) rejectRequest.get("rejectReason");
            String adminRemark = (String) rejectRequest.get("adminRemark");
            
            // 参数校验
            if (claimIdObj == null) {
                result.put("success", false);
                result.put("message", "理赔ID不能为空");
                return result;
            }
            
            if (rejectReason == null || rejectReason.isEmpty()) {
                result.put("success", false);
                result.put("message", "拒绝原因不能为空");
                return result;
            }
            
            if (adminRemark == null || adminRemark.isEmpty()) {
                result.put("success", false);
                result.put("message", "管理员备注不能为空");
                return result;
            }
            
            Long claimId = Long.valueOf(claimIdObj.toString());
            
            boolean success = insuranceClaimService.rejectClaim(claimId, rejectReason, adminRemark);
            
            if (success) {
                result.put("success", true);
                result.put("message", "审核拒绝成功");
            } else {
                result.put("success", false);
                result.put("message", "审核拒绝失败，请检查理赔状态");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "审核失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理员打款
     * @param payRequest 打款请求
     * @return 打款结果
     */
    @PostMapping("/admin/pay")
    public Map<String, Object> payClaim(@RequestBody Map<String, Object> payRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object claimIdObj = payRequest.get("claimId");
            
            // 参数校验
            if (claimIdObj == null) {
                result.put("success", false);
                result.put("message", "理赔ID不能为空");
                return result;
            }
            
            Long claimId = Long.valueOf(claimIdObj.toString());
            
            boolean success = insuranceClaimService.payClaim(claimId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "打款成功");
            } else {
                result.put("success", false);
                result.put("message", "打款失败，请检查理赔状态");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "打款失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理员更新理赔申请
     * @param insuranceClaim 理赔申请信息
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateClaim(@RequestBody InsuranceClaim insuranceClaim) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            if (insuranceClaim.getId() == null) {
                result.put("success", false);
                result.put("message", "理赔ID不能为空");
                return result;
            }
            
            boolean success = insuranceClaimService.adminUpdateClaim(insuranceClaim);
            
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
}

