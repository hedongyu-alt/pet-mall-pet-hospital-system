package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Category;
import com.notmaker.entity.Product;
import com.notmaker.mapper.CategoryMapper;
import com.notmaker.mapper.ProductMapper;
import com.notmaker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品Service实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    /**
     * 管理端分页查询商品列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 商品名称
     * @param brand 品牌
     * @param categoryId 商品分类ID
     * @param petType 适用宠物类型
     * @param status 状态
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 分页结果
     */
    @Override
    public IPage<Product> adminQueryProducts(int page, int size, String name, String brand, 
                                             Long categoryId, String petType, String status, BigDecimal minPrice, BigDecimal maxPrice) {
        Page<Product> pageParam = new Page<>(page, size);
        QueryWrapper<Product> wrapper = buildQueryWrapper(name, brand, categoryId, petType, status, minPrice, maxPrice);
        return productMapper.selectPage(pageParam, wrapper);
    }
    
    /**
     * 管理端查询所有商品（用于导出）
     * @param name 商品名称
     * @param brand 品牌
     * @param categoryId 商品分类ID
     * @param petType 适用宠物类型
     * @param status 状态
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 商品列表
     */
    @Override
    public List<Product> adminQueryAllProducts(String name, String brand, Long categoryId, 
                                               String petType, String status, BigDecimal minPrice, BigDecimal maxPrice) {
        QueryWrapper<Product> wrapper = buildQueryWrapper(name, brand, categoryId, petType, status, minPrice, maxPrice);
        return productMapper.selectList(wrapper);
    }
    
    /**
     * 构建查询条件
     * @param name 商品名称
     * @param brand 品牌
     * @param categoryId 商品分类ID
     * @param petType 适用宠物类型
     * @param status 状态
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 查询条件
     */
    private QueryWrapper<Product> buildQueryWrapper(String name, String brand, Long categoryId, 
                                                     String petType, String status, BigDecimal minPrice, BigDecimal maxPrice) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        
        // 商品名称模糊查询
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        
        // 品牌模糊查询
        if (brand != null && !brand.isEmpty()) {
            wrapper.like("brand", brand);
        }
        
        // 商品分类精确查询
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        
        // 适用宠物类型精确查询
        if (petType != null && !petType.isEmpty()) {
            wrapper.eq("pet_type", petType);
        }
        
        // 状态精确查询
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        // 价格范围查询
        if (minPrice != null) {
            wrapper.ge("price", minPrice);
        }
        if (maxPrice != null) {
            wrapper.le("price", maxPrice);
        }
        
        // 按创建时间降序
        wrapper.orderByDesc("create_time");
        
        return wrapper;
    }
    
    /**
     * 根据ID获取商品
     * @param id 商品ID
     * @return 商品对象
     */
    @Override
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }
    
    /**
     * 管理端新增商品
     * @param product 商品对象
     * @return 新增结果
     */
    @Override
    public boolean adminAddProduct(Product product) {
        // 设置创建时间
        product.setCreateTime(new Date());
        
        // 如果没有设置状态，默认为上架
        if (product.getStatus() == null || product.getStatus().isEmpty()) {
            product.setStatus("上架");
        }
        
        return productMapper.insert(product) > 0;
    }
    
    /**
     * 管理端更新商品
     * @param product 商品对象
     * @return 更新结果
     */
    @Override
    public boolean adminUpdateProduct(Product product) {
        // 检查商品是否存在
        Product existProduct = productMapper.selectById(product.getId());
        if (existProduct == null) {
            return false;
        }
        
        // 更新商品信息（不更新创建时间）
        product.setCreateTime(null);
        
        return productMapper.updateById(product) > 0;
    }
    
    /**
     * 管理端删除商品
     * @param id 商品ID
     * @return 删除结果
     */
    @Override
    public boolean adminDeleteProduct(Long id) {
        // 检查商品是否存在
        Product product = productMapper.selectById(id);
        if (product == null) {
            return false;
        }
        
        return productMapper.deleteById(id) > 0;
    }
    
    /**
     * 管理端修改商品状态（上下架）
     * @param id 商品ID
     * @param status 状态：上架、下架
     * @return 修改结果
     */
    @Override
    public boolean adminUpdateProductStatus(Long id, String status) {
        // 检查商品是否存在
        Product product = productMapper.selectById(id);
        if (product == null) {
            return false;
        }
        
        // 更新状态
        product.setStatus(status);
        product.setCreateTime(null);
        
        return productMapper.updateById(product) > 0;
    }
    
    /**
     * 用户端分页查询商品列表（只显示上架商品，包含分类名称）
     * @param page 当前页
     * @param size 每页大小
     * @param name 商品名称
     * @param brand 品牌
     * @param categoryId 商品分类ID
     * @param petType 适用宠物类型
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 分页结果（包含分类名称的Map列表）
     */
    @Override
    public IPage<Map<String, Object>> userQueryProducts(int page, int size, String name, String brand, 
                                                         Long categoryId, String petType, BigDecimal minPrice, BigDecimal maxPrice) {
        // 构建查询条件，只查询上架商品
        Page<Product> pageParam = new Page<>(page, size);
        QueryWrapper<Product> wrapper = buildQueryWrapper(name, brand, categoryId, petType, "上架", minPrice, maxPrice);
        
        // 查询商品列表
        IPage<Product> productPage = productMapper.selectPage(pageParam, wrapper);
        
        // 转换为包含分类名称的Map列表
        List<Map<String, Object>> records = productPage.getRecords().stream().map(product -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("images", product.getImages());
            map.put("name", product.getName());
            map.put("categoryId", product.getCategoryId());
            map.put("description", product.getDescription());
            map.put("brand", product.getBrand());
            map.put("petType", product.getPetType());
            map.put("price", product.getPrice());
            map.put("status", product.getStatus());
            map.put("createTime", product.getCreateTime());
            
            // 查询分类名称
            Category category = categoryMapper.selectById(product.getCategoryId());
            map.put("categoryName", category != null ? category.getName() : "");
            
            return map;
        }).collect(Collectors.toList());
        
        // 构建分页结果
        Page<Map<String, Object>> resultPage = new Page<>(page, size);
        resultPage.setRecords(records);
        resultPage.setTotal(productPage.getTotal());
        
        return resultPage;
    }
    
    /**
     * 用户端获取商品详情（只显示上架商品，包含分类名称）
     * @param id 商品ID
     * @return 商品详情Map（包含分类名称）
     */
    @Override
    public Map<String, Object> userGetProductDetail(Long id) {
        // 查询商品
        Product product = productMapper.selectById(id);
        
        // 检查商品是否存在且已上架
        if (product == null || !"上架".equals(product.getStatus())) {
            return null;
        }
        
        // 转换为Map并添加分类名称
        Map<String, Object> map = new HashMap<>();
        map.put("id", product.getId());
        map.put("images", product.getImages());
        map.put("name", product.getName());
        map.put("categoryId", product.getCategoryId());
        map.put("description", product.getDescription());
        map.put("brand", product.getBrand());
        map.put("petType", product.getPetType());
        map.put("price", product.getPrice());
        map.put("status", product.getStatus());
        map.put("createTime", product.getCreateTime());
        
        // 查询分类名称
        Category category = categoryMapper.selectById(product.getCategoryId());
        map.put("categoryName", category != null ? category.getName() : "");
        
        return map;
    }
}

