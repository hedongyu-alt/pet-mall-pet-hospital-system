package com.notmaker.controller;

import com.notmaker.entity.SystemConfig;
import com.notmaker.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置Controller
 */
@RestController
@RequestMapping("/api/system-config")
public class SystemConfigController {
    
    @Autowired
    private SystemConfigService systemConfigService;
    
    /**
     * 获取系统配置
     * @return 系统配置信息
     */
    @GetMapping("/get")
    public Map<String, Object> getSystemConfig() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            SystemConfig config = systemConfigService.getSystemConfig();
            result.put("success", true);
            result.put("data", config);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取系统配置失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 更新系统配置
     * @param configRequest 配置请求参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public Map<String, Object> updateSystemConfig(@RequestBody Map<String, Object> configRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String systemName = (String) configRequest.get("systemName");
            String carouselImages = (String) configRequest.get("carouselImages");
            
            // 参数校验
            if (systemName == null || systemName.isEmpty()) {
                result.put("success", false);
                result.put("message", "系统名称不能为空");
                return result;
            }
            
            // 创建配置对象
            SystemConfig config = new SystemConfig();
            config.setSystemName(systemName);
            config.setCarouselImages(carouselImages);
            
            // 调用服务更新
            boolean success = systemConfigService.updateSystemConfig(config);
            
            if (success) {
                result.put("success", true);
                result.put("message", "系统配置更新成功");
            } else {
                result.put("success", false);
                result.put("message", "系统配置更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "系统配置更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 修改管理员密码
     * @param passwordRequest 密码修改请求参数
     * @return 修改结果
     */
    @PostMapping("/updateAdminPassword")
    public Map<String, Object> updateAdminPassword(@RequestBody Map<String, String> passwordRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String adminIdStr = passwordRequest.get("adminId");
            String oldPassword = passwordRequest.get("oldPassword");
            String newPassword = passwordRequest.get("newPassword");
            
            // 参数校验
            if (adminIdStr == null || adminIdStr.isEmpty()) {
                result.put("success", false);
                result.put("message", "管理员ID不能为空");
                return result;
            }
            
            if (oldPassword == null || oldPassword.isEmpty()) {
                result.put("success", false);
                result.put("message", "旧密码不能为空");
                return result;
            }
            
            if (newPassword == null || newPassword.isEmpty()) {
                result.put("success", false);
                result.put("message", "新密码不能为空");
                return result;
            }
            
            Long adminId = Long.valueOf(adminIdStr);
            
            // 调用服务修改密码
            boolean success = systemConfigService.updateAdminPassword(adminId, oldPassword, newPassword);
            
            if (success) {
                result.put("success", true);
                result.put("message", "密码修改成功");
            } else {
                result.put("success", false);
                result.put("message", "旧密码错误或管理员不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "密码修改失败：" + e.getMessage());
        }
        
        return result;
    }
}

