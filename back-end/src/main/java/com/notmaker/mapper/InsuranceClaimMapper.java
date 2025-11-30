package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.InsuranceClaim;
import org.apache.ibatis.annotations.Mapper;

/**
 * 理赔申请Mapper
 */
@Mapper
public interface InsuranceClaimMapper extends BaseMapper<InsuranceClaim> {
}

