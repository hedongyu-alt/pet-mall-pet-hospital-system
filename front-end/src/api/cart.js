import axios from 'axios';

const BASE_URL = 'http://localhost:18007';

/**
 * 添加商品到购物车
 */
export function addToCart(data) {
  return axios.post(`${BASE_URL}/api/cart/add`, data);
}

/**
 * 获取用户购物车列表
 */
export function getCartList(userId) {
  return axios.get(`${BASE_URL}/api/cart/list`, {
    params: { userId }
  });
}

/**
 * 更新购物车商品数量
 */
export function updateCartQuantity(id, quantity) {
  return axios.put(`${BASE_URL}/api/cart/update`, null, {
    params: { id, quantity }
  });
}

/**
 * 删除购物车项
 */
export function deleteCartItem(id, userId) {
  return axios.delete(`${BASE_URL}/api/cart/delete`, {
    params: { id, userId }
  });
}

/**
 * 批量删除购物车项
 */
export function batchDeleteCartItems(ids, userId) {
  return axios.post(`${BASE_URL}/api/cart/batch-delete`, { ids, userId });
}

/**
 * 清空购物车
 */
export function clearCart(userId) {
  return axios.delete(`${BASE_URL}/api/cart/clear`, {
    params: { userId }
  });
}

