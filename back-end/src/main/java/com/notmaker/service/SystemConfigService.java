package com.notmaker.service;

import com.notmaker.entity.SystemConfig;

/**
 * 系统配置Service接口
 */
public interface SystemConfigService {
    
    /**
     * 获取系统配置
     * @return 系统配置信息
     */
    SystemConfig getSystemConfig();
    
    /**
     * 更新系统配置
     * @param systemConfig 系统配置
     * @return 是否成功
     */
    boolean updateSystemConfig(SystemConfig systemConfig);
    
    /**
     * 修改管理员密码
     * @param adminId 管理员ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updateAdminPassword(Long adminId, String oldPassword, String newPassword);
}

