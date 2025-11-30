import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/product';

/**
 * 获取商品列表
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function getProductList(params) {
  return axios.get(`${BASE_URL}/admin/list`, { params });
}

/**
 * 获取商品详情
 * @param {Number} id 商品ID
 * @returns Promise
 */
export function getProductDetail(id) {
  return axios.get(`${BASE_URL}/admin/detail/${id}`);
}

/**
 * 新增商品
 * @param {Object} data 商品数据
 * @returns Promise
 */
export function addProduct(data) {
  return axios.post(`${BASE_URL}/admin/add`, data);
}

/**
 * 更新商品
 * @param {Object} data 商品数据
 * @returns Promise
 */
export function updateProduct(data) {
  return axios.post(`${BASE_URL}/admin/update`, data);
}

/**
 * 删除商品
 * @param {Number} id 商品ID
 * @returns Promise
 */
export function deleteProduct(id) {
  return axios.delete(`${BASE_URL}/admin/delete/${id}`);
}

/**
 * 修改商品状态（上下架）
 * @param {Number} id 商品ID
 * @param {String} status 状态
 * @returns Promise
 */
export function updateProductStatus(id, status) {
  return axios.post(`${BASE_URL}/admin/updateStatus/${id}`, { status });
}

/**
 * 导出商品Excel
 * @param {Object} params 查询参数
 * @returns 导出URL
 */
export function exportProducts(params) {
  const queryString = new URLSearchParams(params).toString();
  return `${BASE_URL}/admin/export?${queryString}`;
}

/**
 * 用户端获取商品列表（只显示上架商品）
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function getUserProductList(params) {
  return axios.get(`${BASE_URL}/user/list`, { params });
}

/**
 * 用户端获取商品详情（只显示上架商品）
 * @param {Number} id 商品ID
 * @returns Promise
 */
export function getUserProductDetail(id) {
  return axios.get(`${BASE_URL}/user/detail/${id}`);
}

