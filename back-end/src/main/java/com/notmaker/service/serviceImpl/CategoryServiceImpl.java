package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Category;
import com.notmaker.mapper.CategoryMapper;
import com.notmaker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品分类Service实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    /**
     * 用户端获取所有商品分类列表
     * @return 分类列表
     */
    @Override
    public List<Category> getAllCategories() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("name");
        return categoryMapper.selectList(wrapper);
    }
    
    /**
     * 管理端分页查询商品分类列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 分类名称
     * @return 分页结果
     */
    @Override
    public IPage<Category> adminQueryCategories(int page, int size, String name) {
        Page<Category> pageParam = new Page<>(page, size);
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        
        // 分类名称模糊查询
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        
        // 按创建时间降序
        wrapper.orderByDesc("create_time");
        
        return categoryMapper.selectPage(pageParam, wrapper);
    }
    
    /**
     * 管理端查询所有商品分类（用于导出）
     * @param name 分类名称
     * @return 商品分类列表
     */
    @Override
    public List<Category> adminQueryAllCategories(String name) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        
        // 分类名称模糊查询
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        
        // 按创建时间降序
        wrapper.orderByDesc("create_time");
        
        return categoryMapper.selectList(wrapper);
    }
    
    /**
     * 根据ID获取商品分类
     * @param id 商品分类ID
     * @return 商品分类对象
     */
    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }
    
    /**
     * 管理端新增商品分类
     * @param category 商品分类对象
     * @return 新增结果
     */
    @Override
    public boolean adminAddCategory(Category category) {
        // 检查分类名称是否已存在
        if (checkCategoryNameExists(category.getName(), null)) {
            return false;
        }
        
        // 设置创建时间
        category.setCreateTime(new Date());
        
        return categoryMapper.insert(category) > 0;
    }
    
    /**
     * 管理端更新商品分类
     * @param category 商品分类对象
     * @return 更新结果
     */
    @Override
    public boolean adminUpdateCategory(Category category) {
        // 检查分类是否存在
        Category existCategory = categoryMapper.selectById(category.getId());
        if (existCategory == null) {
            return false;
        }
        
        // 检查分类名称是否已被其他分类使用
        if (checkCategoryNameExists(category.getName(), category.getId())) {
            return false;
        }
        
        // 更新分类信息（不更新创建时间）
        category.setCreateTime(null);
        
        return categoryMapper.updateById(category) > 0;
    }
    
    /**
     * 管理端删除商品分类
     * @param id 商品分类ID
     * @return 删除结果
     */
    @Override
    public boolean adminDeleteCategory(Long id) {
        // 检查分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            return false;
        }
        
        return categoryMapper.deleteById(id) > 0;
    }
    
    /**
     * 检查分类名称是否已存在
     * @param name 分类名称
     * @param excludeId 排除的ID（编辑时使用）
     * @return 是否存在
     */
    @Override
    public boolean checkCategoryNameExists(String name, Long excludeId) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        
        // 如果是编辑操作，排除当前分类ID
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        
        return categoryMapper.selectCount(wrapper) > 0;
    }
}

