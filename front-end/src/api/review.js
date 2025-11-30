import axios from 'axios';

const BASE_URL = 'http://localhost:18007';

/**
 * 创建评价
 */
export function createReview(data) {
  return axios.post(`${BASE_URL}/api/review/create`, data);
}

/**
 * 更新评价
 */
export function updateReview(data) {
  return axios.post(`${BASE_URL}/api/review/update`, data);
}

/**
 * 删除评价
 */
export function deleteReview(data) {
  return axios.post(`${BASE_URL}/api/review/delete`, data);
}

/**
 * 用户端分页查询评价列表
 */
export function getUserReviewList(userId, page, size) {
  return axios.get(`${BASE_URL}/api/review/list`, {
    params: {
      userId,
      page,
      size
    }
  });
}

/**
 * 检查订单的商品是否已评价
 */
export function checkReviewed(orderId, productId, userId) {
  return axios.get(`${BASE_URL}/api/review/check`, {
    params: {
      orderId,
      productId,
      userId
    }
  });
}

/**
 * 管理端分页查询评价列表
 */
export function getAdminReviewList(params) {
  return axios.get(`${BASE_URL}/api/review/admin/list`, {
    params
  });
}

/**
 * 管理端更新评价
 */
export function adminUpdateReview(data) {
  return axios.post(`${BASE_URL}/api/review/admin/update`, data);
}

/**
 * 管理端删除评价
 */
export function adminDeleteReview(data) {
  return axios.post(`${BASE_URL}/api/review/admin/delete`, data);
}

/**
 * 根据商品ID分页查询评价列表
 */
export function getProductReviewList(productId, page, size) {
  return axios.get(`${BASE_URL}/api/review/product/list`, {
    params: {
      productId,
      page,
      size
    }
  });
}

/**
 * 管理端导出评价Excel
 */
export function exportReviews(params) {
  const queryString = Object.keys(params)
    .filter(key => params[key] !== null && params[key] !== undefined && params[key] !== '')
    .map(key => `${key}=${encodeURIComponent(params[key])}`)
    .join('&');
  
  return `${BASE_URL}/api/review/admin/export${queryString ? '?' + queryString : ''}`;
}

