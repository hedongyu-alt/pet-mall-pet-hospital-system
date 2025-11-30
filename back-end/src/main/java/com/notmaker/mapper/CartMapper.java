package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    
    /**
     * 查询用户的购物车列表，包含商品信息
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Select("SELECT c.id, c.user_id, c.product_id, c.quantity, c.create_time, " +
            "p.name AS product_name, p.images AS product_images, p.price AS product_price, " +
            "p.brand AS product_brand, p.status AS product_status, cat.name AS category_name " +
            "FROM cart c " +
            "LEFT JOIN product p ON c.product_id = p.id " +
            "LEFT JOIN category cat ON p.category_id = cat.id " +
            "WHERE c.user_id = #{userId} " +
            "ORDER BY c.create_time DESC")
    List<Cart> selectCartListWithProduct(Long userId);
}

