package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.notmaker.entity.Cart;
import com.notmaker.mapper.CartMapper;
import com.notmaker.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    /**
     * 添加商品到购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addToCart(Cart cart) {
        // 检查该用户是否已经添加过该商品
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId());
        queryWrapper.eq("product_id", cart.getProductId());
        
        Cart existCart = cartMapper.selectOne(queryWrapper);
        
        if (existCart != null) {
            // 如果已存在，更新数量
            existCart.setQuantity(existCart.getQuantity() + cart.getQuantity());
            return cartMapper.updateById(existCart) > 0;
        } else {
            // 如果不存在，新增
            cart.setCreateTime(LocalDateTime.now());
            return cartMapper.insert(cart) > 0;
        }
    }

    /**
     * 获取用户购物车列表
     */
    @Override
    public List<Cart> getCartList(Long userId) {
        return cartMapper.selectCartListWithProduct(userId);
    }

    /**
     * 更新购物车商品数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuantity(Long id, Integer quantity) {
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            return false;
        }
        
        if (quantity <= 0) {
            // 如果数量小于等于0，删除该项
            return cartMapper.deleteById(id) > 0;
        }
        
        cart.setQuantity(quantity);
        return cartMapper.updateById(cart) > 0;
    }

    /**
     * 删除购物车项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCartItem(Long id, Long userId) {
        // 验证该购物车项是否属于该用户
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        
        return cartMapper.delete(queryWrapper) > 0;
    }

    /**
     * 批量删除购物车项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCartItems(List<Long> ids, Long userId) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        queryWrapper.eq("user_id", userId);
        
        return cartMapper.delete(queryWrapper) > 0;
    }

    /**
     * 清空用户购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearCart(Long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        
        return cartMapper.delete(queryWrapper) > 0;
    }
}

