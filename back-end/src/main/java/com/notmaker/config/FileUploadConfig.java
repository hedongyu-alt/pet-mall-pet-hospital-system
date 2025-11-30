package com.notmaker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件上传配置类
 * 配置静态资源访问映射
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    /**
     * 文件上传根路径（从配置文件读取）
     */
    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 配置静态资源处理器
     * 将/uploads/**的请求映射到实际的文件存储路径
     * 
     * @param registry 资源处理器注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置文件访问路径映射
        // 访问路径：http://localhost:18007/uploads/**
        // 实际路径：file:D:/pet_home_uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}

