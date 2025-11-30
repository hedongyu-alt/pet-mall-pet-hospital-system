package com.notmaker.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 * 配置分页插件等（适用于MyBatis-Plus 3.3.x版本）
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 配置MyBatis-Plus分页插件（3.3.x版本）
     * @return PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        
        // 设置最大单页限制数量，默认500条，-1不受限制
        paginationInterceptor.setLimit(500L);
        
        return paginationInterceptor;
    }
}


