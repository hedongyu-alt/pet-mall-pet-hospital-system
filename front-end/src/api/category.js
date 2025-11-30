import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/category';

/**
 * 获取商品分类列表
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function getCategoryList(params) {
  return axios.get(`${BASE_URL}/admin/list`, { params });
}

/**
 * 获取商品分类详情
 * @param {Number} id 商品分类ID
 * @returns Promise
 */
export function getCategoryDetail(id) {
  return axios.get(`${BASE_URL}/admin/detail/${id}`);
}

/**
 * 新增商品分类
 * @param {Object} data 商品分类数据
 * @returns Promise
 */
export function addCategory(data) {
  return axios.post(`${BASE_URL}/admin/add`, data);
}

/**
 * 更新商品分类
 * @param {Object} data 商品分类数据
 * @returns Promise
 */
export function updateCategory(data) {
  return axios.post(`${BASE_URL}/admin/update`, data);
}

/**
 * 删除商品分类
 * @param {Number} id 商品分类ID
 * @returns Promise
 */
export function deleteCategory(id) {
  return axios.delete(`${BASE_URL}/admin/delete/${id}`);
}

/**
 * 导出商品分类Excel
 * @param {Object} params 查询参数
 * @returns 导出URL
 */
export function exportCategories(params) {
  const queryString = new URLSearchParams(params).toString();
  return `${BASE_URL}/admin/export?${queryString}`;
}

/**
 * 用户端获取所有商品分类列表（用于筛选）
 * @returns Promise
 */
export function getAllCategories() {
  return axios.get(`${BASE_URL}/admin/list`, { 
    params: { page: 1, size: 1000 } 
  });
}

