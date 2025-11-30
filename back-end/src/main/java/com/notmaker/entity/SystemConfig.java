package com.notmaker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 系统配置实体类
 */
@TableName("system_config")
public class SystemConfig {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 轮播图URL列表（JSON格式）
     */
    private String carouselImages;

    /**
     * 创建时间
     */
    private Date createTime;

    public SystemConfig() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCarouselImages() {
        return carouselImages;
    }

    public void setCarouselImages(String carouselImages) {
        this.carouselImages = carouselImages;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemConfig{" +
                "id=" + id +
                ", systemName='" + systemName + '\'' +
                ", carouselImages='" + carouselImages + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

