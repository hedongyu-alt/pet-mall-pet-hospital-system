package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Hospital;
import com.notmaker.mapper.HospitalMapper;
import com.notmaker.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 宠物医院Service实现类
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    
    @Autowired
    private HospitalMapper hospitalMapper;
    
    /**
     * 分页查询宠物医院列表
     * @param page 分页参数
     * @param name 医院名称（模糊查询）
     * @param services 服务项目（模糊查询）
     * @param address 医院地址（模糊查询）
     * @return 分页结果
     */
    @Override
    public IPage<Hospital> getHospitalPage(Page<Hospital> page, String name, String services, String address) {
        QueryWrapper<Hospital> queryWrapper = new QueryWrapper<>();
        
        // 医院名称模糊查询
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like("name", name.trim());
        }
        
        // 服务项目模糊查询
        if (services != null && !services.trim().isEmpty()) {
            queryWrapper.like("services", services.trim());
        }
        
        // 医院地址模糊查询
        if (address != null && !address.trim().isEmpty()) {
            queryWrapper.like("address", address.trim());
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return hospitalMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询宠物医院详情
     * @param id 医院ID
     * @return 医院详情
     */
    @Override
    public Hospital getHospitalById(Long id) {
        return hospitalMapper.selectById(id);
    }
    
    /**
     * 新增宠物医院
     * @param hospital 医院信息
     * @return 是否成功
     */
    @Override
    public boolean addHospital(Hospital hospital) {
        hospital.setCreateTime(new Date());
        int rows = hospitalMapper.insert(hospital);
        return rows > 0;
    }
    
    /**
     * 更新宠物医院
     * @param hospital 医院信息
     * @return 是否成功
     */
    @Override
    public boolean updateHospital(Hospital hospital) {
        int rows = hospitalMapper.updateById(hospital);
        return rows > 0;
    }
    
    /**
     * 删除宠物医院
     * @param id 医院ID
     * @return 是否成功
     */
    @Override
    public boolean deleteHospital(Long id) {
        int rows = hospitalMapper.deleteById(id);
        return rows > 0;
    }
    
    /**
     * 导出Excel
     * @param name 医院名称（模糊查询）
     * @param services 服务项目（模糊查询）
     * @param address 医院地址（模糊查询）
     * @return Excel数据
     */
    @Override
    public Map<String, Object> exportExcel(String name, String services, String address) {
        QueryWrapper<Hospital> queryWrapper = new QueryWrapper<>();
        
        // 医院名称模糊查询
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like("name", name.trim());
        }
        
        // 服务项目模糊查询
        if (services != null && !services.trim().isEmpty()) {
            queryWrapper.like("services", services.trim());
        }
        
        // 医院地址模糊查询
        if (address != null && !address.trim().isEmpty()) {
            queryWrapper.like("address", address.trim());
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        // 查询所有符合条件的数据
        List<Hospital> hospitalList = hospitalMapper.selectList(queryWrapper);
        
        // 准备Excel数据
        List<String[]> excelData = new ArrayList<>();
        
        // 添加表头
        excelData.add(new String[]{
            "医院ID", "医院名称", "医院介绍", "服务项目", "医院地址", "联系电话", "营业时间", "创建时间"
        });
        
        // 添加数据行
        for (Hospital hospital : hospitalList) {
            excelData.add(new String[]{
                hospital.getId() != null ? hospital.getId().toString() : "",
                hospital.getName() != null ? hospital.getName() : "",
                hospital.getIntroduction() != null ? hospital.getIntroduction() : "",
                hospital.getServices() != null ? hospital.getServices() : "",
                hospital.getAddress() != null ? hospital.getAddress() : "",
                hospital.getPhone() != null ? hospital.getPhone() : "",
                hospital.getBusinessHours() != null ? hospital.getBusinessHours() : "",
                hospital.getCreateTime() != null ? hospital.getCreateTime().toString() : ""
            });
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", excelData);
        result.put("filename", "宠物医院列表_" + System.currentTimeMillis() + ".csv");
        
        return result;
    }
}

