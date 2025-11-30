package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Appointment;
import com.notmaker.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医院预约Controller
 */
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    /**
     * 分页查询预约列表
     * @param page 当前页
     * @param size 每页大小
     * @param userId 用户ID
     * @param hospitalName 医院名称
     * @param status 状态
     * @return 查询结果
     */
    @GetMapping("/list")
    public Map<String, Object> listAppointments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String hospitalName,
            @RequestParam(required = false) String status) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Appointment> pageParam = new Page<>(page, size);
            IPage<Appointment> pageResult = appointmentService.getAppointmentPage(pageParam, userId, hospitalName, status);
            
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
     * 获取预约详情
     * @param id 预约ID
     * @return 预约信息
     */
    @GetMapping("/detail/{id}")
    public Map<String, Object> getAppointmentDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            if (appointment != null) {
                result.put("success", true);
                result.put("data", appointment);
            } else {
                result.put("success", false);
                result.put("message", "预约不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 新增预约
     * @param appointment 预约信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public Map<String, Object> addAppointment(@RequestBody Appointment appointment) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            if (appointment.getHospitalId() == null) {
                result.put("success", false);
                result.put("message", "医院ID不能为空");
                return result;
            }
            
            if (appointment.getPetName() == null || appointment.getPetName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物名称不能为空");
                return result;
            }
            
            if (appointment.getPetType() == null || appointment.getPetType().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物类型不能为空");
                return result;
            }
            
            if (appointment.getConditionDescription() == null || appointment.getConditionDescription().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "病情描述不能为空");
                return result;
            }
            
            if (appointment.getAppointmentDate() == null) {
                result.put("success", false);
                result.put("message", "预约日期不能为空");
                return result;
            }
            
            if (appointment.getAppointmentTime() == null || appointment.getAppointmentTime().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "预约时间不能为空");
                return result;
            }
            
            if (appointment.getContactPhone() == null || appointment.getContactPhone().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "联系电话不能为空");
                return result;
            }
            
            // 设置默认状态
            if (appointment.getStatus() == null || appointment.getStatus().trim().isEmpty()) {
                appointment.setStatus("待赴约");
            }
            
            boolean success = appointmentService.addAppointment(appointment);
            if (success) {
                result.put("success", true);
                result.put("message", "预约成功");
            } else {
                result.put("success", false);
                result.put("message", "预约失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "预约失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 更新预约状态
     * @param id 预约ID
     * @param status 状态
     * @return 操作结果
     */
    @PostMapping("/updateStatus")
    public Map<String, Object> updateAppointmentStatus(
            @RequestParam Long id,
            @RequestParam String status) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = appointmentService.updateAppointmentStatus(id, status);
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 取消预约
     * @param id 预约ID
     * @return 操作结果
     */
    @PostMapping("/cancel/{id}")
    public Map<String, Object> cancelAppointment(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = appointmentService.cancelAppointment(id);
            if (success) {
                result.put("success", true);
                result.put("message", "取消成功");
            } else {
                result.put("success", false);
                result.put("message", "取消失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "取消失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端分页查询预约列表
     * @param page 当前页
     * @param size 每页大小
     * @param status 状态
     * @param conditionDescription 病情描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 查询结果
     */
    @GetMapping("/admin/list")
    public Map<String, Object> adminListAppointments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String conditionDescription,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Appointment> pageParam = new Page<>(page, size);
            IPage<Appointment> pageResult = appointmentService.getAdminAppointmentPage(pageParam, status, conditionDescription, startTime, endTime);
            
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
     * 管理端完成预约
     * @param id 预约ID
     * @return 操作结果
     */
    @PostMapping("/admin/complete/{id}")
    public Map<String, Object> completeAppointment(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 先检查预约状态
            Appointment appointment = appointmentService.getAppointmentById(id);
            if (appointment == null) {
                result.put("success", false);
                result.put("message", "预约不存在");
                return result;
            }
            
            if (!"已到店".equals(appointment.getStatus())) {
                result.put("success", false);
                result.put("message", "只有已到店的预约才能完成");
                return result;
            }
            
            boolean success = appointmentService.updateAppointmentStatus(id, "已完成");
            if (success) {
                result.put("success", true);
                result.put("message", "完成成功");
            } else {
                result.put("success", false);
                result.put("message", "完成失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "完成失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端更新预约
     * @param appointment 预约信息
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateAppointment(@RequestBody Appointment appointment) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 查询预约是否存在
            Appointment existingAppointment = appointmentService.getAppointmentById(appointment.getId());
            if (existingAppointment == null) {
                result.put("success", false);
                result.put("message", "预约不存在");
                return result;
            }
            
            boolean success = appointmentService.adminUpdateAppointment(appointment);
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 导出预约记录Excel
     * @param status 状态
     * @param conditionDescription 病情描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param response HTTP响应
     */
    @GetMapping("/admin/export")
    public void exportAppointments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String conditionDescription,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            HttpServletResponse response) {
        
        try {
            // 获取数据
            List<Appointment> appointments = appointmentService.getAllAppointments(status, conditionDescription, startTime, endTime);
            
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("医院预约记录_" + System.currentTimeMillis(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
            
            // 构建Excel内容
            StringBuilder sb = new StringBuilder();
            
            // 表头
            sb.append("预约ID\t医院名称\t宠物名称\t宠物类型\t病情描述\t预约日期\t预约时间\t联系电话\t状态\t创建时间\n");
            
            // 数据行
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for (Appointment appointment : appointments) {
                sb.append(appointment.getId()).append("\t");
                sb.append(appointment.getHospitalName() != null ? appointment.getHospitalName() : "").append("\t");
                sb.append(appointment.getPetName() != null ? appointment.getPetName() : "").append("\t");
                sb.append(appointment.getPetType() != null ? appointment.getPetType() : "").append("\t");
                sb.append(appointment.getConditionDescription() != null ? appointment.getConditionDescription() : "").append("\t");
                sb.append(appointment.getAppointmentDate() != null ? dateFormat.format(appointment.getAppointmentDate()) : "").append("\t");
                sb.append(appointment.getAppointmentTime() != null ? appointment.getAppointmentTime() : "").append("\t");
                sb.append(appointment.getContactPhone() != null ? appointment.getContactPhone() : "").append("\t");
                sb.append(appointment.getStatus() != null ? appointment.getStatus() : "").append("\t");
                sb.append(appointment.getCreateTime() != null ? dateTimeFormat.format(appointment.getCreateTime()) : "").append("\n");
            }
            
            // 写入响应
            OutputStream out = response.getOutputStream();
            out.write(sb.toString().getBytes("GBK"));
            out.flush();
            out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

