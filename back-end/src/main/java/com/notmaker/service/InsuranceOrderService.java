package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.InsuranceOrder;

/**
 * 保险订单Service接口
 */
public interface InsuranceOrderService {
    
    /**
     * 用户购买保险
     * @param insuranceOrder 保险订单信息
     * @return 是否成功
     */
    boolean purchaseInsurance(InsuranceOrder insuranceOrder);
    
    /**
     * 用户查询自己的保单列表（分页）
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @return 保单列表
     */
    IPage<InsuranceOrder> getUserOrders(Long userId, int page, int size);
    
    /**
     * 用户查询保单详情
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 订单详情
     */
    InsuranceOrder getOrderDetail(Long orderId, Long userId);
}

