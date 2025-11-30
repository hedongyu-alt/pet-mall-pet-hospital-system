package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.InsuranceClaim;

/**
 * 理赔申请Service接口
 */
public interface InsuranceClaimService {
    
    /**
     * 用户提交理赔申请
     * @param insuranceClaim 理赔申请信息
     * @return 是否成功
     */
    boolean submitClaim(InsuranceClaim insuranceClaim);
    
    /**
     * 用户查询自己的理赔申请列表（分页）
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @param status 状态筛选
     * @return 理赔申请列表
     */
    IPage<InsuranceClaim> getUserClaims(Long userId, int page, int size, String status);
    
    /**
     * 用户查询理赔申请详情
     * @param claimId 理赔ID
     * @param userId 用户ID
     * @return 理赔详情
     */
    InsuranceClaim getClaimDetail(Long claimId, Long userId);
    
    /**
     * 管理员查询所有理赔申请列表（分页）
     * @param page 当前页
     * @param size 每页大小
     * @param status 状态筛选
     * @param reason 申请原因筛选
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 理赔申请列表
     */
    IPage<InsuranceClaim> getAdminClaims(int page, int size, String status, String reason, String startTime, String endTime);
    
    /**
     * 管理员查询理赔申请详情
     * @param claimId 理赔ID
     * @return 理赔详情
     */
    InsuranceClaim getAdminClaimDetail(Long claimId);
    
    /**
     * 管理员审核通过理赔申请
     * @param claimId 理赔ID
     * @param claimAmount 理赔金额
     * @param adminRemark 管理员备注
     * @return 是否成功
     */
    boolean approveClaim(Long claimId, String claimAmount, String adminRemark);
    
    /**
     * 管理员审核拒绝理赔申请
     * @param claimId 理赔ID
     * @param rejectReason 拒绝原因
     * @param adminRemark 管理员备注
     * @return 是否成功
     */
    boolean rejectClaim(Long claimId, String rejectReason, String adminRemark);
    
    /**
     * 管理员打款
     * @param claimId 理赔ID
     * @return 是否成功
     */
    boolean payClaim(Long claimId);
    
    /**
     * 管理员更新理赔申请
     * @param insuranceClaim 理赔申请信息
     * @return 是否成功
     */
    boolean adminUpdateClaim(InsuranceClaim insuranceClaim);
}

