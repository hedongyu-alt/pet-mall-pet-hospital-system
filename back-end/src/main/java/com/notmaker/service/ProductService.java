package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品Service接口
 */
public interface ProductService {
    
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
    IPage<Product> adminQueryProducts(int page, int size, String name, String brand, 
                                      Long categoryId, String petType, String status, BigDecimal minPrice, BigDecimal maxPrice);
    
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
    List<Product> adminQueryAllProducts(String name, String brand, Long categoryId, 
                                        String petType, String status, BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * 根据ID获取商品
     * @param id 商品ID
     * @return 商品对象
     */
    Product getProductById(Long id);
    
    /**
     * 管理端新增商品
     * @param product 商品对象
     * @return 新增结果
     */
    boolean adminAddProduct(Product product);
    
    /**
     * 管理端更新商品
     * @param product 商品对象
     * @return 更新结果
     */
    boolean adminUpdateProduct(Product product);
    
    /**
     * 管理端删除商品
     * @param id 商品ID
     * @return 删除结果
     */
    boolean adminDeleteProduct(Long id);
    
    /**
     * 管理端修改商品状态（上下架）
     * @param id 商品ID
     * @param status 状态：上架、下架
     * @return 修改结果
     */
    boolean adminUpdateProductStatus(Long id, String status);
    
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
    IPage<Map<String, Object>> userQueryProducts(int page, int size, String name, String brand, 
                                                  Long categoryId, String petType, BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * 用户端获取商品详情（只显示上架商品，包含分类名称）
     * @param id 商品ID
     * @return 商品详情Map（包含分类名称）
     */
    Map<String, Object> userGetProductDetail(Long id);
}

