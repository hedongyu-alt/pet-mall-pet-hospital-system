package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Appointment;

import java.util.List;

/**
 * 医院预约Service接口
 */
public interface AppointmentService {
    
    /**
     * 分页查询预约列表
     * @param page 分页参数
     * @param userId 用户ID
     * @param hospitalName 医院名称
     * @param status 状态
     * @return 分页结果
     */
    IPage<Appointment> getAppointmentPage(Page<Appointment> page, Long userId, String hospitalName, String status);
    
    /**
     * 管理端分页查询预约列表（支持时间范围）
     * @param page 分页参数
     * @param status 状态
     * @param conditionDescription 病情描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<Appointment> getAdminAppointmentPage(Page<Appointment> page, String status, String conditionDescription, String startTime, String endTime);
    
    /**
     * 获取全部预约列表（用于导出）
     * @param status 状态
     * @param conditionDescription 病情描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 预约列表
     */
    List<Appointment> getAllAppointments(String status, String conditionDescription, String startTime, String endTime);
    
    /**
     * 根据ID获取预约详情
     * @param id 预约ID
     * @return 预约信息
     */
    Appointment getAppointmentById(Long id);
    
    /**
     * 新增预约
     * @param appointment 预约信息
     * @return 操作结果
     */
    boolean addAppointment(Appointment appointment);
    
    /**
     * 更新预约状态
     * @param id 预约ID
     * @param status 状态
     * @return 操作结果
     */
    boolean updateAppointmentStatus(Long id, String status);
    
    /**
     * 取消预约
     * @param id 预约ID
     * @return 操作结果
     */
    boolean cancelAppointment(Long id);
    
    /**
     * 管理端更新预约
     * @param appointment 预约信息
     * @return 更新结果
     */
    boolean adminUpdateAppointment(Appointment appointment);
}

