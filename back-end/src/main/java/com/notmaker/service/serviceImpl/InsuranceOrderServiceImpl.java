package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.InsuranceOrder;
import com.notmaker.mapper.InsuranceOrderMapper;
import com.notmaker.service.InsuranceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 保险订单Service实现类
 */
@Service
public class InsuranceOrderServiceImpl implements InsuranceOrderService {
    
    @Autowired
    private InsuranceOrderMapper insuranceOrderMapper;
    
    @Override
    public boolean purchaseInsurance(InsuranceOrder insuranceOrder) {
        // 生成订单编号
        String orderNo = "INS" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + 
                        String.format("%04d", (int)(Math.random() * 10000));
        insuranceOrder.setOrderNo(orderNo);
        
        // 设置订单状态
        insuranceOrder.setStatus("生效中");
        
        // 设置创建时间
        insuranceOrder.setCreateTime(new Date());
        
        // 插入数据库
        return insuranceOrderMapper.insert(insuranceOrder) > 0;
    }
    
    @Override
    public IPage<InsuranceOrder> getUserOrders(Long userId, int page, int size) {
        Page<InsuranceOrder> pageParam = new Page<>(page, size);
        
        QueryWrapper<InsuranceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        
        IPage<InsuranceOrder> pageResult = insuranceOrderMapper.selectPage(pageParam, wrapper);
        
        // 检查每个保单是否过期，并更新状态
        Date currentDate = new Date();
        for (InsuranceOrder order : pageResult.getRecords()) {
            if (order.getEndDate() != null && order.getEndDate().before(currentDate)) {
                // 保单已过期，更新状态
                if (!"已过期".equals(order.getStatus())) {
                    order.setStatus("已过期");
                    insuranceOrderMapper.updateById(order);
                }
            }
        }
        
        return pageResult;
    }
    
    @Override
    public InsuranceOrder getOrderDetail(Long orderId, Long userId) {
        QueryWrapper<InsuranceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("id", orderId);
        wrapper.eq("user_id", userId);
        
        InsuranceOrder order = insuranceOrderMapper.selectOne(wrapper);
        
        // 检查保单是否过期
        if (order != null) {
            Date currentDate = new Date();
            if (order.getEndDate() != null && order.getEndDate().before(currentDate)) {
                // 保单已过期，更新状态
                if (!"已过期".equals(order.getStatus())) {
                    order.setStatus("已过期");
                    insuranceOrderMapper.updateById(order);
                }
            }
        }
        
        return order;
    }
}

