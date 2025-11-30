package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Appointment;
import com.notmaker.mapper.AppointmentMapper;
import com.notmaker.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医院预约Service实现类
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {
    
    @Autowired
    private AppointmentMapper appointmentMapper;
    
    /**
     * 分页查询预约列表
     * @param page 分页参数
     * @param userId 用户ID
     * @param hospitalName 医院名称
     * @param status 状态
     * @return 分页结果
     */
    @Override
    public IPage<Appointment> getAppointmentPage(Page<Appointment> page, Long userId, String hospitalName, String status) {
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        
        // 用户ID筛选
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        
        // 医院名称筛选
        if (hospitalName != null && !hospitalName.trim().isEmpty()) {
            queryWrapper.like("hospital_name", hospitalName);
        }
        
        // 状态筛选
        if (status != null && !status.trim().isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");
        
        return appointmentMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 管理端分页查询预约列表（支持时间范围）
     * @param page 分页参数
     * @param status 状态
     * @param conditionDescription 病情描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    @Override
    public IPage<Appointment> getAdminAppointmentPage(Page<Appointment> page, String status, String conditionDescription, String startTime, String endTime) {
        QueryWrapper<Appointment> queryWrapper = buildQueryWrapper(status, conditionDescription, startTime, endTime);
        return appointmentMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 获取全部预约列表（用于导出）
     * @param status 状态
     * @param conditionDescription 病情描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 预约列表
     */
    @Override
    public List<Appointment> getAllAppointments(String status, String conditionDescription, String startTime, String endTime) {
        QueryWrapper<Appointment> queryWrapper = buildQueryWrapper(status, conditionDescription, startTime, endTime);
        return appointmentMapper.selectList(queryWrapper);
    }
    
    /**
     * 构建查询条件
     * @param status 状态
     * @param conditionDescription 病情描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return QueryWrapper
     */
    private QueryWrapper<Appointment> buildQueryWrapper(String status, String conditionDescription, String startTime, String endTime) {
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        
        // 状态筛选
        if (status != null && !status.trim().isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        // 病情描述筛选
        if (conditionDescription != null && !conditionDescription.trim().isEmpty()) {
            queryWrapper.like("condition_description", conditionDescription);
        }
        
        // 时间范围筛选
        if (startTime != null && !startTime.trim().isEmpty()) {
            queryWrapper.ge("create_time", startTime);
        }
        if (endTime != null && !endTime.trim().isEmpty()) {
            queryWrapper.le("create_time", endTime);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");
        
        return queryWrapper;
    }
    
    /**
     * 根据ID获取预约详情
     * @param id 预约ID
     * @return 预约信息
     */
    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentMapper.selectById(id);
    }
    
    /**
     * 新增预约
     * @param appointment 预约信息
     * @return 操作结果
     */
    @Override
    public boolean addAppointment(Appointment appointment) {
        return appointmentMapper.insert(appointment) > 0;
    }
    
    /**
     * 更新预约状态
     * @param id 预约ID
     * @param status 状态
     * @return 操作结果
     */
    @Override
    public boolean updateAppointmentStatus(Long id, String status) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus(status);
        return appointmentMapper.updateById(appointment) > 0;
    }
    
    /**
     * 取消预约
     * @param id 预约ID
     * @return 操作结果
     */
    @Override
    public boolean cancelAppointment(Long id) {
        return updateAppointmentStatus(id, "已取消");
    }
    
    /**
     * 管理端更新预约
     * @param appointment 预约信息
     * @return 更新结果
     */
    @Override
    public boolean adminUpdateAppointment(Appointment appointment) {
        // 查询预约是否存在
        Appointment existingAppointment = appointmentMapper.selectById(appointment.getId());
        if (existingAppointment == null) {
            return false;
        }
        
        return appointmentMapper.updateById(appointment) > 0;
    }
}

