package com.notmaker.controller;

import com.notmaker.entity.Cart;
import com.notmaker.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加商品到购物车
     * @param cart 购物车信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public Map<String, Object> addToCart(@RequestBody Cart cart) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (cart.getUserId() == null || cart.getProductId() == null || cart.getQuantity() == null) {
                result.put("success", false);
                result.put("message", "参数不完整");
                return result;
            }
            
            if (cart.getQuantity() <= 0) {
                result.put("success", false);
                result.put("message", "数量必须大于0");
                return result;
            }
            
            boolean success = cartService.addToCart(cart);
            
            if (success) {
                result.put("success", true);
                result.put("message", "添加成功");
            } else {
                result.put("success", false);
                result.put("message", "添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取用户购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    @GetMapping("/list")
    public Map<String, Object> getCartList(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Cart> cartList = cartService.getCartList(userId);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", cartList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新购物车商品数量
     * @param id 购物车ID
     * @param quantity 数量
     * @return 操作结果
     */
    @PutMapping("/update")
    public Map<String, Object> updateQuantity(@RequestParam Long id, @RequestParam Integer quantity) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (quantity < 0) {
                result.put("success", false);
                result.put("message", "数量不能小于0");
                return result;
            }
            
            boolean success = cartService.updateQuantity(id, quantity);
            
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除购物车项
     * @param id 购物车ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public Map<String, Object> deleteCartItem(@RequestParam Long id, @RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = cartService.deleteCartItem(id, userId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 批量删除购物车项
     * @param params 包含ids和userId的Map
     * @return 操作结果
     */
    @PostMapping("/batch-delete")
    public Map<String, Object> batchDeleteCartItems(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object idsObj = params.get("ids");
            Long userId = Long.valueOf(params.get("userId").toString());
            
            if (idsObj == null) {
                result.put("success", false);
                result.put("message", "请选择要删除的商品");
                return result;
            }
            
            List<Long> ids = new java.util.ArrayList<>();
            if (idsObj instanceof java.util.List) {
                for (Object obj : (java.util.List<?>) idsObj) {
                    if (obj instanceof Number) {
                        ids.add(((Number) obj).longValue());
                    }
                }
            }
            
            if (ids.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择要删除的商品");
                return result;
            }
            
            boolean success = cartService.batchDeleteCartItems(ids, userId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/clear")
    public Map<String, Object> clearCart(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = cartService.clearCart(userId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "清空成功");
            } else {
                result.put("success", false);
                result.put("message", "清空失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "清空失败：" + e.getMessage());
        }
        
        return result;
    }
}

