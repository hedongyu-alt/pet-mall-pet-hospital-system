package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.Category;

import java.util.List;

/**
 * 商品分类Service接口
 */
public interface CategoryService {
    
    /**
     * 用户端获取所有商品分类列表
     * @return 分类列表
     */
    List<Category> getAllCategories();
    
    /**
     * 管理端分页查询商品分类列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 分类名称
     * @return 分页结果
     */
    IPage<Category> adminQueryCategories(int page, int size, String name);
    
    /**
     * 管理端查询所有商品分类（用于导出）
     * @param name 分类名称
     * @return 商品分类列表
     */
    List<Category> adminQueryAllCategories(String name);
    
    /**
     * 根据ID获取商品分类
     * @param id 商品分类ID
     * @return 商品分类对象
     */
    Category getCategoryById(Long id);
    
    /**
     * 管理端新增商品分类
     * @param category 商品分类对象
     * @return 新增结果
     */
    boolean adminAddCategory(Category category);
    
    /**
     * 管理端更新商品分类
     * @param category 商品分类对象
     * @return 更新结果
     */
    boolean adminUpdateCategory(Category category);
    
    /**
     * 管理端删除商品分类
     * @param id 商品分类ID
     * @return 删除结果
     */
    boolean adminDeleteCategory(Long id);
    
    /**
     * 检查分类名称是否已存在
     * @param name 分类名称
     * @param excludeId 排除的ID（编辑时使用）
     * @return 是否存在
     */
    boolean checkCategoryNameExists(String name, Long excludeId);
}

