package com.notmaker.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.notmaker.entity.SystemConfig;
import com.notmaker.entity.User;
import com.notmaker.mapper.SystemConfigMapper;
import com.notmaker.mapper.UserMapper;
import com.notmaker.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统配置Service实现类
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {
    
    @Autowired
    private SystemConfigMapper systemConfigMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 获取系统配置
     * @return 系统配置信息
     */
    @Override
    public SystemConfig getSystemConfig() {
        QueryWrapper<SystemConfig> wrapper = new QueryWrapper<>();
        wrapper.last("LIMIT 1");
        SystemConfig config = systemConfigMapper.selectOne(wrapper);
        
        // 如果没有配置，创建默认配置
        if (config == null) {
            config = new SystemConfig();
            config.setSystemName("伴侣宠物之家系统");
            config.setCarouselImages("[]");
            config.setCreateTime(new Date());
            systemConfigMapper.insert(config);
        }
        
        return config;
    }
    
    /**
     * 更新系统配置
     * @param systemConfig 系统配置
     * @return 是否成功
     */
    @Override
    public boolean updateSystemConfig(SystemConfig systemConfig) {
        try {
            SystemConfig config = getSystemConfig();
            config.setSystemName(systemConfig.getSystemName());
            config.setCarouselImages(systemConfig.getCarouselImages());
            
            int result = systemConfigMapper.updateById(config);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 修改管理员密码
     * @param adminId 管理员ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    @Override
    public boolean updateAdminPassword(Long adminId, String oldPassword, String newPassword) {
        try {
            // 查询管理员信息
            User admin = userMapper.selectById(adminId);
            if (admin == null) {
                return false;
            }
            
            // 验证旧密码
            if (!admin.getPassword().equals(oldPassword)) {
                return false;
            }
            
            // 更新新密码
            admin.setPassword(newPassword);
            int result = userMapper.updateById(admin);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

