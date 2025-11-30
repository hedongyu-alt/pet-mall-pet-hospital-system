package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.Insurance;

import java.math.BigDecimal;
import java.util.List;

/**
 * 宠物保险Service
 */
public interface InsuranceService {
    
    /**
     * 管理端分页查询保险列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 保险名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 分页结果
     */
    IPage<Insurance> adminQueryInsurances(int page, int size, String name, BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * 管理端查询所有保险（用于导出）
     * @param name 保险名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 保险列表
     */
    List<Insurance> adminQueryAllInsurances(String name, BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * 根据ID查询保险详情
     * @param id 保险ID
     * @return 保险信息
     */
    Insurance getInsuranceById(Long id);
    
    /**
     * 管理端新增保险
     * @param insurance 保险信息
     * @return 是否成功
     */
    boolean adminAddInsurance(Insurance insurance);
    
    /**
     * 管理端更新保险
     * @param insurance 保险信息
     * @return 是否成功
     */
    boolean adminUpdateInsurance(Insurance insurance);
    
    /**
     * 管理端删除保险
     * @param id 保险ID
     * @return 是否成功
     */
    boolean adminDeleteInsurance(Long id);
}

