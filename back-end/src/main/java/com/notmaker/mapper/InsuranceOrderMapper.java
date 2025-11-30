package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.InsuranceOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 保险订单Mapper
 */
@Mapper
public interface InsuranceOrderMapper extends BaseMapper<InsuranceOrder> {
}

