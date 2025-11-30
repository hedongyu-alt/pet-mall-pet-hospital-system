package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Insurance;
import com.notmaker.mapper.InsuranceMapper;
import com.notmaker.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 宠物保险Service实现类
 */
@Service
public class InsuranceServiceImpl implements InsuranceService {
    
    @Autowired
    private InsuranceMapper insuranceMapper;
    
    /**
     * 管理端分页查询保险列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 保险名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 分页结果
     */
    @Override
    public IPage<Insurance> adminQueryInsurances(int page, int size, String name, BigDecimal minPrice, BigDecimal maxPrice) {
        Page<Insurance> pageParam = new Page<>(page, size);
        QueryWrapper<Insurance> queryWrapper = new QueryWrapper<>();
        
        // 保险名称模糊查询
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        
        // 价格范围查询
        if (minPrice != null) {
            queryWrapper.ge("price", minPrice);
        }
        if (maxPrice != null) {
            queryWrapper.le("price", maxPrice);
        }
        
        // 按创建时间降序
        queryWrapper.orderByDesc("create_time");
        
        return insuranceMapper.selectPage(pageParam, queryWrapper);
    }
    
    /**
     * 管理端查询所有保险（用于导出）
     * @param name 保险名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 保险列表
     */
    @Override
    public List<Insurance> adminQueryAllInsurances(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        QueryWrapper<Insurance> queryWrapper = new QueryWrapper<>();
        
        // 保险名称模糊查询
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        
        // 价格范围查询
        if (minPrice != null) {
            queryWrapper.ge("price", minPrice);
        }
        if (maxPrice != null) {
            queryWrapper.le("price", maxPrice);
        }
        
        // 按创建时间降序
        queryWrapper.orderByDesc("create_time");
        
        return insuranceMapper.selectList(queryWrapper);
    }
    
    /**
     * 根据ID查询保险详情
     * @param id 保险ID
     * @return 保险信息
     */
    @Override
    public Insurance getInsuranceById(Long id) {
        return insuranceMapper.selectById(id);
    }
    
    /**
     * 管理端新增保险
     * @param insurance 保险信息
     * @return 是否成功
     */
    @Override
    public boolean adminAddInsurance(Insurance insurance) {
        return insuranceMapper.insert(insurance) > 0;
    }
    
    /**
     * 管理端更新保险
     * @param insurance 保险信息
     * @return 是否成功
     */
    @Override
    public boolean adminUpdateInsurance(Insurance insurance) {
        return insuranceMapper.updateById(insurance) > 0;
    }
    
    /**
     * 管理端删除保险
     * @param id 保险ID
     * @return 是否成功
     */
    @Override
    public boolean adminDeleteInsurance(Long id) {
        return insuranceMapper.deleteById(id) > 0;
    }
}

