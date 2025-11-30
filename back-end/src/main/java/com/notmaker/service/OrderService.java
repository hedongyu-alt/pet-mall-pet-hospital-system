package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * 订单Service接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     * @param userId 用户ID
     * @param totalAmount 订单总金额
     * @param receiverName 收货人姓名
     * @param receiverPhone 收货人电话
     * @param receiverAddress 收货地址
     * @param orderDetails 订单详情列表
     * @return 订单ID
     */
    Long createOrder(Long userId, String totalAmount, String receiverName, String receiverPhone, 
                     String receiverAddress, List<Map<String, Object>> orderDetails);
    
    /**
     * 用户端分页查询订单列表
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @param status 订单状态
     * @return 订单分页数据
     */
    IPage<Map<String, Object>> getUserOrderList(Long userId, int page, int size, String status);
    
    /**
     * 根据订单ID获取订单详情
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 订单详情
     */
    Map<String, Object> getOrderDetail(Long orderId, Long userId);
    
    /**
     * 取消订单
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean cancelOrder(Long orderId, Long userId);
    
    /**
     * 确认收货
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean confirmReceipt(Long orderId, Long userId);
    
    /**
     * 管理端分页查询订单列表
     * @param page 当前页
     * @param size 每页大小
     * @param status 订单状态
     * @param productName 商品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单分页数据
     */
    IPage<Map<String, Object>> getAdminOrderList(int page, int size, String status, 
                                                   String productName, String startTime, String endTime);
    
    /**
     * 管理端获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    Map<String, Object> getAdminOrderDetail(Long orderId);
    
    /**
     * 管理端取消订单
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean adminCancelOrder(Long orderId);
    
    /**
     * 发货
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean shipOrder(Long orderId);
}

