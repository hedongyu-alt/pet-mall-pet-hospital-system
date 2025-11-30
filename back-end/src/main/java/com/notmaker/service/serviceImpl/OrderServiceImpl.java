package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Order;
import com.notmaker.entity.OrderDetail;
import com.notmaker.mapper.OrderDetailMapper;
import com.notmaker.mapper.OrderMapper;
import com.notmaker.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    
    /**
     * 创建订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(Long userId, String totalAmount, String receiverName, String receiverPhone, 
                           String receiverAddress, List<Map<String, Object>> orderDetails) {
        // 生成订单编号
        String orderNo = generateOrderNo();
        
        // 创建订单对象
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(new BigDecimal(totalAmount));
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setStatus("待发货");
        
        // 保存订单
        orderMapper.insert(order);
        
        // 保存订单详情
        for (Map<String, Object> item : orderDetails) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getId());
            detail.setProductId(Long.valueOf(item.get("productId").toString()));
            detail.setProductName((String) item.get("productName"));
            detail.setProductImage((String) item.get("productImage"));
            detail.setPrice(new BigDecimal(item.get("price").toString()));
            detail.setQuantity(Integer.valueOf(item.get("quantity").toString()));
            
            orderDetailMapper.insert(detail);
        }
        
        return order.getId();
    }
    
    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        String random = String.format("%04d", new Random().nextInt(10000));
        return "ORD" + date + random;
    }
    
    /**
     * 用户端分页查询订单列表
     */
    @Override
    public IPage<Map<String, Object>> getUserOrderList(Long userId, int page, int size, String status) {
        // 创建分页对象
        Page<Order> pageObj = new Page<>(page, size);
        
        // 创建查询条件
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        wrapper.orderByDesc("create_time");
        
        // 查询订单列表
        IPage<Order> orderPage = orderMapper.selectPage(pageObj, wrapper);
        
        // 构造返回数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("receiverName", order.getReceiverName());
            orderMap.put("receiverPhone", order.getReceiverPhone());
            orderMap.put("receiverAddress", order.getReceiverAddress());
            orderMap.put("status", order.getStatus());
            orderMap.put("createTime", order.getCreateTime());
            
            // 查询订单详情
            QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
            detailWrapper.eq("order_id", order.getId());
            List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);
            
            // 转换详情数据
            List<Map<String, Object>> detailList = new ArrayList<>();
            for (OrderDetail detail : details) {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("id", detail.getId());
                detailMap.put("productId", detail.getProductId());
                detailMap.put("productName", detail.getProductName());
                detailMap.put("productImage", detail.getProductImage());
                detailMap.put("price", detail.getPrice());
                detailMap.put("quantity", detail.getQuantity());
                detailList.add(detailMap);
            }
            
            orderMap.put("details", detailList);
            resultList.add(orderMap);
        }
        
        // 创建返回的分页对象
        Page<Map<String, Object>> resultPage = new Page<>(page, size);
        resultPage.setRecords(resultList);
        resultPage.setTotal(orderPage.getTotal());
        
        return resultPage;
    }
    
    /**
     * 根据订单ID获取订单详情
     */
    @Override
    public Map<String, Object> getOrderDetail(Long orderId, Long userId) {
        // 查询订单
        Order order = orderMapper.selectById(orderId);
        
        if (order == null || !order.getUserId().equals(userId)) {
            return null;
        }
        
        // 构造订单数据
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.getId());
        orderMap.put("orderNo", order.getOrderNo());
        orderMap.put("totalAmount", order.getTotalAmount());
        orderMap.put("receiverName", order.getReceiverName());
        orderMap.put("receiverPhone", order.getReceiverPhone());
        orderMap.put("receiverAddress", order.getReceiverAddress());
        orderMap.put("status", order.getStatus());
        orderMap.put("createTime", order.getCreateTime());
        
        // 查询订单详情
        QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("order_id", orderId);
        List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);
        
        // 转换详情数据
        List<Map<String, Object>> detailList = new ArrayList<>();
        for (OrderDetail detail : details) {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("id", detail.getId());
            detailMap.put("productId", detail.getProductId());
            detailMap.put("productName", detail.getProductName());
            detailMap.put("productImage", detail.getProductImage());
            detailMap.put("price", detail.getPrice());
            detailMap.put("quantity", detail.getQuantity());
            detailList.add(detailMap);
        }
        
        orderMap.put("details", detailList);
        
        return orderMap;
    }
    
    /**
     * 取消订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, Long userId) {
        // 查询订单
        Order order = orderMapper.selectById(orderId);
        
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }
        
        // 只有待发货的订单可以取消
        if (!"待发货".equals(order.getStatus())) {
            return false;
        }
        
        // 删除订单
        orderMapper.deleteById(orderId);
        
        // 删除订单详情
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        orderDetailMapper.delete(wrapper);
        
        return true;
    }
    
    /**
     * 确认收货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceipt(Long orderId, Long userId) {
        // 查询订单
        Order order = orderMapper.selectById(orderId);
        
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }
        
        // 只有已发货的订单可以确认收货
        if (!"已发货".equals(order.getStatus())) {
            return false;
        }
        
        // 更新订单状态
        order.setStatus("已收货");
        orderMapper.updateById(order);
        
        return true;
    }
    
    /**
     * 管理端分页查询订单列表
     */
    @Override
    public IPage<Map<String, Object>> getAdminOrderList(int page, int size, String status, 
                                                         String productName, String startTime, String endTime) {
        // 创建分页对象
        Page<Order> pageObj = new Page<>(page, size);
        
        // 创建查询条件
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        
        // 按状态筛选
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        // 按时间范围筛选
        if (startTime != null && !startTime.isEmpty()) {
            wrapper.ge("create_time", startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            wrapper.le("create_time", endTime);
        }
        
        wrapper.orderByDesc("create_time");
        
        // 查询订单列表
        IPage<Order> orderPage = orderMapper.selectPage(pageObj, wrapper);
        
        // 构造返回数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            // 查询订单详情
            QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
            detailWrapper.eq("order_id", order.getId());
            List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);
            
            // 按商品名筛选
            if (productName != null && !productName.isEmpty()) {
                boolean hasProduct = false;
                for (OrderDetail detail : details) {
                    if (detail.getProductName().contains(productName)) {
                        hasProduct = true;
                        break;
                    }
                }
                // 如果订单中没有匹配的商品，跳过此订单
                if (!hasProduct) {
                    continue;
                }
            }
            
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("userId", order.getUserId());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("receiverName", order.getReceiverName());
            orderMap.put("receiverPhone", order.getReceiverPhone());
            orderMap.put("receiverAddress", order.getReceiverAddress());
            orderMap.put("status", order.getStatus());
            orderMap.put("createTime", order.getCreateTime());
            
            // 转换详情数据
            List<Map<String, Object>> detailList = new ArrayList<>();
            for (OrderDetail detail : details) {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("id", detail.getId());
                detailMap.put("productId", detail.getProductId());
                detailMap.put("productName", detail.getProductName());
                detailMap.put("productImage", detail.getProductImage());
                detailMap.put("price", detail.getPrice());
                detailMap.put("quantity", detail.getQuantity());
                detailList.add(detailMap);
            }
            
            orderMap.put("details", detailList);
            resultList.add(orderMap);
        }
        
        // 创建返回的分页对象
        Page<Map<String, Object>> resultPage = new Page<>(page, size);
        resultPage.setRecords(resultList);
        resultPage.setTotal(orderPage.getTotal());
        
        return resultPage;
    }
    
    /**
     * 管理端获取订单详情
     */
    @Override
    public Map<String, Object> getAdminOrderDetail(Long orderId) {
        // 查询订单
        Order order = orderMapper.selectById(orderId);
        
        if (order == null) {
            return null;
        }
        
        // 构造订单数据
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.getId());
        orderMap.put("orderNo", order.getOrderNo());
        orderMap.put("userId", order.getUserId());
        orderMap.put("totalAmount", order.getTotalAmount());
        orderMap.put("receiverName", order.getReceiverName());
        orderMap.put("receiverPhone", order.getReceiverPhone());
        orderMap.put("receiverAddress", order.getReceiverAddress());
        orderMap.put("status", order.getStatus());
        orderMap.put("createTime", order.getCreateTime());
        
        // 查询订单详情
        QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("order_id", orderId);
        List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);
        
        // 转换详情数据
        List<Map<String, Object>> detailList = new ArrayList<>();
        for (OrderDetail detail : details) {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("id", detail.getId());
            detailMap.put("productId", detail.getProductId());
            detailMap.put("productName", detail.getProductName());
            detailMap.put("productImage", detail.getProductImage());
            detailMap.put("price", detail.getPrice());
            detailMap.put("quantity", detail.getQuantity());
            detailList.add(detailMap);
        }
        
        orderMap.put("details", detailList);
        
        return orderMap;
    }
    
    /**
     * 管理端取消订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adminCancelOrder(Long orderId) {
        // 查询订单
        Order order = orderMapper.selectById(orderId);
        
        if (order == null) {
            return false;
        }
        
        // 只有待发货的订单可以取消
        if (!"待发货".equals(order.getStatus())) {
            return false;
        }
        
        // 删除订单
        orderMapper.deleteById(orderId);
        
        // 删除订单详情
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        orderDetailMapper.delete(wrapper);
        
        return true;
    }
    
    /**
     * 发货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean shipOrder(Long orderId) {
        // 查询订单
        Order order = orderMapper.selectById(orderId);
        
        if (order == null) {
            return false;
        }
        
        // 只有待发货的订单可以发货
        if (!"待发货".equals(order.getStatus())) {
            return false;
        }
        
        // 更新订单状态
        order.setStatus("已发货");
        orderMapper.updateById(order);
        
        return true;
    }
}

