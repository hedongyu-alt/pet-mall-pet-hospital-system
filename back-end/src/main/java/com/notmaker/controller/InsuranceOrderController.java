package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.Insurance;
import com.notmaker.entity.InsuranceOrder;
import com.notmaker.service.InsuranceOrderService;
import com.notmaker.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 保险订单Controller
 */
@RestController
@RequestMapping("/api/insurance-order")
public class InsuranceOrderController {
    
    @Autowired
    private InsuranceOrderService insuranceOrderService;
    
    @Autowired
    private InsuranceService insuranceService;
    
    /**
     * 用户购买保险
     * @param purchaseRequest 购买请求
     * @return 购买结果
     */
    @PostMapping("/purchase")
    public Map<String, Object> purchaseInsurance(@RequestBody Map<String, Object> purchaseRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取并校验参数
            Object userIdObj = purchaseRequest.get("userId");
            Object insuranceIdObj = purchaseRequest.get("insuranceId");
            String petName = (String) purchaseRequest.get("petName");
            String petType = (String) purchaseRequest.get("petType");
            Object petAgeObj = purchaseRequest.get("petAge");
            String petBreed = (String) purchaseRequest.get("petBreed");
            String payeeName = (String) purchaseRequest.get("payeeName");
            String payeePhone = (String) purchaseRequest.get("payeePhone");
            String payeeAccount = (String) purchaseRequest.get("payeeAccount");
            String startDateStr = (String) purchaseRequest.get("startDate");
            
            // 参数校验
            if (userIdObj == null) {
                result.put("success", false);
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            if (insuranceIdObj == null) {
                result.put("success", false);
                result.put("message", "保险ID不能为空");
                return result;
            }
            
            if (petName == null || petName.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物名称不能为空");
                return result;
            }
            
            if (petType == null || petType.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物类型不能为空");
                return result;
            }
            
            if (petAgeObj == null) {
                result.put("success", false);
                result.put("message", "宠物年龄不能为空");
                return result;
            }
            
            if (payeeName == null || payeeName.isEmpty()) {
                result.put("success", false);
                result.put("message", "收款人姓名不能为空");
                return result;
            }
            
            if (payeePhone == null || payeePhone.isEmpty()) {
                result.put("success", false);
                result.put("message", "收款人电话不能为空");
                return result;
            }
            
            if (payeeAccount == null || payeeAccount.isEmpty()) {
                result.put("success", false);
                result.put("message", "收款账号不能为空");
                return result;
            }
            
            if (startDateStr == null || startDateStr.isEmpty()) {
                result.put("success", false);
                result.put("message", "保险生效日期不能为空");
                return result;
            }
            
            Long userId = Long.valueOf(userIdObj.toString());
            Long insuranceId = Long.valueOf(insuranceIdObj.toString());
            Integer petAge = Integer.valueOf(petAgeObj.toString());
            
            // 查询保险信息
            Insurance insurance = insuranceService.getInsuranceById(insuranceId);
            if (insurance == null) {
                result.put("success", false);
                result.put("message", "保险不存在");
                return result;
            }
            
            // 解析生效日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            
            // 计算到期日期（生效日期 + 1年）
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.YEAR, 1);
            Date endDate = calendar.getTime();
            
            // 创建保险订单
            InsuranceOrder insuranceOrder = new InsuranceOrder();
            insuranceOrder.setUserId(userId);
            insuranceOrder.setInsuranceId(insuranceId);
            insuranceOrder.setInsuranceName(insurance.getName());
            insuranceOrder.setPrice(insurance.getPrice());
            insuranceOrder.setPetName(petName);
            insuranceOrder.setPetType(petType);
            insuranceOrder.setPetAge(petAge);
            insuranceOrder.setPetBreed(petBreed);
            insuranceOrder.setPayeeName(payeeName);
            insuranceOrder.setPayeePhone(payeePhone);
            insuranceOrder.setPayeeAccount(payeeAccount);
            insuranceOrder.setStartDate(startDate);
            insuranceOrder.setEndDate(endDate);
            
            // 调用服务购买保险
            boolean success = insuranceOrderService.purchaseInsurance(insuranceOrder);
            
            if (success) {
                result.put("success", true);
                result.put("message", "购买成功");
            } else {
                result.put("success", false);
                result.put("message", "购买失败");
            }
        } catch (ParseException e) {
            result.put("success", false);
            result.put("message", "日期格式错误");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "购买失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 用户查询自己的保单列表
     * @param userId 用户ID
     * @param page 当前页
     * @param size 每页大小
     * @return 保单列表
     */
    @GetMapping("/user/list")
    public Map<String, Object> getUserOrders(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<InsuranceOrder> pageResult = insuranceOrderService.getUserOrders(userId, page, size);
            
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
     * 用户查询保单详情
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 订单详情
     */
    @GetMapping("/user/detail/{orderId}")
    public Map<String, Object> getOrderDetail(
            @PathVariable Long orderId,
            @RequestParam Long userId) {
        
        Map<String, Object> result = new HashMap<>();
        
        InsuranceOrder order = insuranceOrderService.getOrderDetail(orderId, userId);
        if (order != null) {
            result.put("success", true);
            result.put("data", order);
        } else {
            result.put("success", false);
            result.put("message", "订单不存在");
        }
        
        return result;
    }
}

