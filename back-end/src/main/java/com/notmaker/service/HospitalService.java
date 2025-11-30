package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Hospital;

import java.util.Map;

/**
 * 宠物医院Service接口
 */
public interface HospitalService {
    
    /**
     * 分页查询宠物医院列表
     * @param page 分页参数
     * @param name 医院名称（模糊查询）
     * @param services 服务项目（模糊查询）
     * @param address 医院地址（模糊查询）
     * @return 分页结果
     */
    IPage<Hospital> getHospitalPage(Page<Hospital> page, String name, String services, String address);
    
    /**
     * 根据ID查询宠物医院详情
     * @param id 医院ID
     * @return 医院详情
     */
    Hospital getHospitalById(Long id);
    
    /**
     * 新增宠物医院
     * @param hospital 医院信息
     * @return 是否成功
     */
    boolean addHospital(Hospital hospital);
    
    /**
     * 更新宠物医院
     * @param hospital 医院信息
     * @return 是否成功
     */
    boolean updateHospital(Hospital hospital);
    
    /**
     * 删除宠物医院
     * @param id 医院ID
     * @return 是否成功
     */
    boolean deleteHospital(Long id);
    
    /**
     * 导出Excel
     * @param name 医院名称（模糊查询）
     * @param services 服务项目（模糊查询）
     * @param address 医院地址（模糊查询）
     * @return Excel数据
     */
    Map<String, Object> exportExcel(String name, String services, String address);
}

