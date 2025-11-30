package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单详情Mapper接口
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}

