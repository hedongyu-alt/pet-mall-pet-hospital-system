package com.notmaker.service;

import com.notmaker.entity.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {
    
    /**
     * 添加商品到购物车
     * @param cart 购物车信息
     * @return 是否成功
     */
    boolean addToCart(Cart cart);
    
    /**
     * 获取用户购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> getCartList(Long userId);
    
    /**
     * 更新购物车商品数量
     * @param id 购物车ID
     * @param quantity 数量
     * @return 是否成功
     */
    boolean updateQuantity(Long id, Integer quantity);
    
    /**
     * 删除购物车项
     * @param id 购物车ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteCartItem(Long id, Long userId);
    
    /**
     * 批量删除购物车项
     * @param ids 购物车ID列表
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean batchDeleteCartItems(List<Long> ids, Long userId);
    
    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean clearCart(Long userId);
}

