package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.InsuranceClaim;
import com.notmaker.mapper.InsuranceClaimMapper;
import com.notmaker.service.InsuranceClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 理赔申请Service实现类
 */
@Service
public class InsuranceClaimServiceImpl implements InsuranceClaimService {
    
    @Autowired
    private InsuranceClaimMapper insuranceClaimMapper;
    
    @Override
    public boolean submitClaim(InsuranceClaim insuranceClaim) {
        // 生成理赔编号
        String claimNo = "CLM" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + 
                        String.format("%04d", (int)(Math.random() * 10000));
        insuranceClaim.setClaimNo(claimNo);
        
        // 设置状态为待审核
        insuranceClaim.setStatus("待审核");
        
        // 设置创建时间
        insuranceClaim.setCreateTime(new Date());
        
        // 插入数据库
        return insuranceClaimMapper.insert(insuranceClaim) > 0;
    }
    
    @Override
    public IPage<InsuranceClaim> getUserClaims(Long userId, int page, int size, String status) {
        Page<InsuranceClaim> pageParam = new Page<>(page, size);
        
        QueryWrapper<InsuranceClaim> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        
        // 如果有状态筛选
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        wrapper.orderByDesc("create_time");
        
        return insuranceClaimMapper.selectPage(pageParam, wrapper);
    }
    
    @Override
    public InsuranceClaim getClaimDetail(Long claimId, Long userId) {
        QueryWrapper<InsuranceClaim> wrapper = new QueryWrapper<>();
        wrapper.eq("id", claimId);
        wrapper.eq("user_id", userId);
        
        return insuranceClaimMapper.selectOne(wrapper);
    }
    
    @Override
    public IPage<InsuranceClaim> getAdminClaims(int page, int size, String status, String reason, String startTime, String endTime) {
        Page<InsuranceClaim> pageParam = new Page<>(page, size);
        
        QueryWrapper<InsuranceClaim> wrapper = new QueryWrapper<>();
        
        // 状态筛选
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        // 申请原因筛选（模糊查询）
        if (reason != null && !reason.isEmpty()) {
            wrapper.like("reason", reason);
        }
        
        // 时间范围筛选
        if (startTime != null && !startTime.isEmpty()) {
            wrapper.ge("create_time", startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            wrapper.le("create_time", endTime);
        }
        
        wrapper.orderByDesc("create_time");
        
        return insuranceClaimMapper.selectPage(pageParam, wrapper);
    }
    
    @Override
    public InsuranceClaim getAdminClaimDetail(Long claimId) {
        return insuranceClaimMapper.selectById(claimId);
    }
    
    @Override
    public boolean approveClaim(Long claimId, String claimAmount, String adminRemark) {
        InsuranceClaim claim = insuranceClaimMapper.selectById(claimId);
        if (claim == null) {
            return false;
        }
        
        // 只有待审核状态才能审核
        if (!"待审核".equals(claim.getStatus())) {
            return false;
        }
        
        claim.setStatus("审核通过");
        claim.setClaimAmount(new BigDecimal(claimAmount));
        claim.setAdminRemark(adminRemark);
        claim.setRejectReason(null);
        
        return insuranceClaimMapper.updateById(claim) > 0;
    }
    
    @Override
    public boolean rejectClaim(Long claimId, String rejectReason, String adminRemark) {
        InsuranceClaim claim = insuranceClaimMapper.selectById(claimId);
        if (claim == null) {
            return false;
        }
        
        // 只有待审核状态才能审核
        if (!"待审核".equals(claim.getStatus())) {
            return false;
        }
        
        claim.setStatus("审核拒绝");
        claim.setClaimAmount(BigDecimal.ZERO);
        claim.setRejectReason(rejectReason);
        claim.setAdminRemark(adminRemark);
        
        return insuranceClaimMapper.updateById(claim) > 0;
    }
    
    @Override
    public boolean payClaim(Long claimId) {
        InsuranceClaim claim = insuranceClaimMapper.selectById(claimId);
        if (claim == null) {
            return false;
        }
        
        // 只有审核通过状态才能打款
        if (!"审核通过".equals(claim.getStatus())) {
            return false;
        }
        
        claim.setStatus("已打款");
        
        return insuranceClaimMapper.updateById(claim) > 0;
    }
    
    @Override
    public boolean adminUpdateClaim(InsuranceClaim insuranceClaim) {
        if (insuranceClaim == null || insuranceClaim.getId() == null) {
            return false;
        }
        
        // 检查理赔申请是否存在
        InsuranceClaim existingClaim = insuranceClaimMapper.selectById(insuranceClaim.getId());
        if (existingClaim == null) {
            return false;
        }
        
        // 更新允许编辑的字段：申请原因、情况描述、佐证图片
        existingClaim.setReason(insuranceClaim.getReason());
        existingClaim.setDescription(insuranceClaim.getDescription());
        existingClaim.setEvidenceImages(insuranceClaim.getEvidenceImages());
        
        return insuranceClaimMapper.updateById(existingClaim) > 0;
    }
}

