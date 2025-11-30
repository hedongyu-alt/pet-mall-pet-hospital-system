package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.service.CartService;
import com.notmaker.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单Controller
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    /**
     * 创建订单
     * @param orderRequest 订单请求参数
     * @return 创建结果
     */
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取参数
            Object userIdObj = orderRequest.get("userId");
            String totalAmount = (String) orderRequest.get("totalAmount");
            String receiverName = (String) orderRequest.get("receiverName");
            String receiverPhone = (String) orderRequest.get("receiverPhone");
            String receiverAddress = (String) orderRequest.get("receiverAddress");
            List<Map<String, Object>> orderDetails = (List<Map<String, Object>>) orderRequest.get("orderDetails");
            List<Long> cartIds = (List<Long>) orderRequest.get("cartIds");
            
            // 参数校验
            if (userIdObj == null) {
                result.put("success", false);
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            if (receiverName == null || receiverName.isEmpty()) {
                result.put("success", false);
                result.put("message", "收货人姓名不能为空");
                return result;
            }
            
            if (receiverPhone == null || receiverPhone.isEmpty()) {
                result.put("success", false);
                result.put("message", "收货人电话不能为空");
                return result;
            }
            
            if (receiverAddress == null || receiverAddress.isEmpty()) {
                result.put("success", false);
                result.put("message", "收货地址不能为空");
                return result;
            }
            
            if (orderDetails == null || orderDetails.isEmpty()) {
                result.put("success", false);
                result.put("message", "订单商品不能为空");
                return result;
            }
            
            Long userId = Long.valueOf(userIdObj.toString());
            
            // 创建订单
            Long orderId = orderService.createOrder(userId, totalAmount, receiverName, 
                                                   receiverPhone, receiverAddress, orderDetails);
            
            // 删除购物车中已下单的商品
            if (cartIds != null && !cartIds.isEmpty()) {
                cartService.batchDeleteCartItems(cartIds, userId);
            }
            
            result.put("success", true);
            result.put("message", "订单创建成功");
            result.put("data", orderId);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建订单失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 用户端分页查询订单列表
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @param status 订单状态
     * @return 订单列表
     */
    @GetMapping("/list")
    public Map<String, Object> getUserOrderList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Map<String, Object>> pageResult = orderService.getUserOrderList(userId, page, size, status);
            
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
     * 获取订单详情
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 订单详情
     */
    @GetMapping("/detail")
    public Map<String, Object> getOrderDetail(
            @RequestParam Long orderId,
            @RequestParam Long userId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> orderDetail = orderService.getOrderDetail(orderId, userId);
            
            if (orderDetail != null) {
                result.put("success", true);
                result.put("data", orderDetail);
            } else {
                result.put("success", false);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 取消订单
     * @param cancelRequest 取消请求参数
     * @return 取消结果
     */
    @PostMapping("/cancel")
    public Map<String, Object> cancelOrder(@RequestBody Map<String, Object> cancelRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object orderIdObj = cancelRequest.get("orderId");
            Object userIdObj = cancelRequest.get("userId");
            
            if (orderIdObj == null || userIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            Long orderId = Long.valueOf(orderIdObj.toString());
            Long userId = Long.valueOf(userIdObj.toString());
            
            boolean success = orderService.cancelOrder(orderId, userId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "订单已取消");
            } else {
                result.put("success", false);
                result.put("message", "取消订单失败，订单不存在或状态不允许取消");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "取消订单失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 确认收货
     * @param confirmRequest 确认收货请求参数
     * @return 确认结果
     */
    @PostMapping("/confirm")
    public Map<String, Object> confirmReceipt(@RequestBody Map<String, Object> confirmRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object orderIdObj = confirmRequest.get("orderId");
            Object userIdObj = confirmRequest.get("userId");
            
            if (orderIdObj == null || userIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            Long orderId = Long.valueOf(orderIdObj.toString());
            Long userId = Long.valueOf(userIdObj.toString());
            
            boolean success = orderService.confirmReceipt(orderId, userId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "确认收货成功");
            } else {
                result.put("success", false);
                result.put("message", "确认收货失败，订单不存在或状态不允许确认收货");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "确认收货失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端分页查询订单列表
     * @param page 当前页
     * @param size 每页大小
     * @param status 订单状态
     * @param productName 商品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @GetMapping("/admin/list")
    public Map<String, Object> getAdminOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Map<String, Object>> pageResult = orderService.getAdminOrderList(
                page, size, status, productName, startTime, endTime);
            
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
     * 管理端获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/admin/detail")
    public Map<String, Object> getAdminOrderDetail(@RequestParam Long orderId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> orderDetail = orderService.getAdminOrderDetail(orderId);
            
            if (orderDetail != null) {
                result.put("success", true);
                result.put("data", orderDetail);
            } else {
                result.put("success", false);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端取消订单
     * @param cancelRequest 取消请求参数
     * @return 取消结果
     */
    @PostMapping("/admin/cancel")
    public Map<String, Object> adminCancelOrder(@RequestBody Map<String, Object> cancelRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object orderIdObj = cancelRequest.get("orderId");
            
            if (orderIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            Long orderId = Long.valueOf(orderIdObj.toString());
            
            boolean success = orderService.adminCancelOrder(orderId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "订单已取消");
            } else {
                result.put("success", false);
                result.put("message", "取消订单失败，订单不存在或状态不允许取消");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "取消订单失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 发货
     * @param shipRequest 发货请求参数
     * @return 发货结果
     */
    @PostMapping("/admin/ship")
    public Map<String, Object> shipOrder(@RequestBody Map<String, Object> shipRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object orderIdObj = shipRequest.get("orderId");
            
            if (orderIdObj == null) {
                result.put("success", false);
                result.put("message", "参数错误");
                return result;
            }
            
            Long orderId = Long.valueOf(orderIdObj.toString());
            
            boolean success = orderService.shipOrder(orderId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "发货成功");
            } else {
                result.put("success", false);
                result.put("message", "发货失败，订单不存在或状态不允许发货");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发货失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端导出订单Excel
     * @param status 订单状态
     * @param productName 商品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return Excel文件
     */
    @GetMapping("/admin/export")
    public void exportOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            javax.servlet.http.HttpServletResponse response) {
        
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=orders_" + System.currentTimeMillis() + ".xls");
            
            // 获取所有符合条件的订单数据（不分页）
            IPage<Map<String, Object>> pageResult = orderService.getAdminOrderList(
                1, 100000, status, productName, startTime, endTime);
            List<Map<String, Object>> orderList = pageResult.getRecords();
            
            // 构建Excel内容（简单的HTML表格格式）
            StringBuilder sb = new StringBuilder();
            sb.append("<html><head><meta charset='UTF-8'></head><body>");
            sb.append("<table border='1'>");
            sb.append("<tr>");
            sb.append("<th>订单ID</th>");
            sb.append("<th>订单编号</th>");
            sb.append("<th>用户ID</th>");
            sb.append("<th>订单总金额</th>");
            sb.append("<th>收货人</th>");
            sb.append("<th>收货电话</th>");
            sb.append("<th>收货地址</th>");
            sb.append("<th>订单状态</th>");
            sb.append("<th>商品名称</th>");
            sb.append("<th>创建时间</th>");
            sb.append("</tr>");
            
            for (Map<String, Object> order : orderList) {
                List<Map<String, Object>> details = (List<Map<String, Object>>) order.get("details");
                String productNames = "";
                if (details != null && !details.isEmpty()) {
                    StringBuilder productNameBuilder = new StringBuilder();
                    for (Map<String, Object> detail : details) {
                        if (productNameBuilder.length() > 0) {
                            productNameBuilder.append("; ");
                        }
                        productNameBuilder.append(detail.get("productName"))
                                         .append(" x")
                                         .append(detail.get("quantity"));
                    }
                    productNames = productNameBuilder.toString();
                }
                
                sb.append("<tr>");
                sb.append("<td>").append(order.get("id")).append("</td>");
                sb.append("<td>").append(order.get("orderNo")).append("</td>");
                sb.append("<td>").append(order.get("userId")).append("</td>");
                sb.append("<td>").append(order.get("totalAmount")).append("</td>");
                sb.append("<td>").append(order.get("receiverName")).append("</td>");
                sb.append("<td>").append(order.get("receiverPhone")).append("</td>");
                sb.append("<td>").append(order.get("receiverAddress")).append("</td>");
                sb.append("<td>").append(order.get("status")).append("</td>");
                sb.append("<td>").append(productNames).append("</td>");
                sb.append("<td>").append(order.get("createTime")).append("</td>");
                sb.append("</tr>");
            }
            
            sb.append("</table>");
            sb.append("</body></html>");
            
            // 写入响应
            response.getWriter().write(sb.toString());
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

