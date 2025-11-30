import axios from 'axios';

const API_BASE_URL = 'http://localhost:18007/api/user';

/**
 * 管理端分页查询用户列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getUserList(params) {
  return axios.get(`${API_BASE_URL}/admin/list`, { params });
}

/**
 * 管理端获取用户详情
 * @param {Number} id 用户ID
 * @returns {Promise}
 */
export function getUserDetail(id) {
  return axios.get(`${API_BASE_URL}/admin/detail/${id}`);
}

/**
 * 管理端新增用户
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function addUser(data) {
  return axios.post(`${API_BASE_URL}/admin/add`, data);
}

/**
 * 管理端更新用户
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function updateUser(data) {
  return axios.post(`${API_BASE_URL}/admin/update`, data);
}

/**
 * 管理端删除用户
 * @param {Number} id 用户ID
 * @returns {Promise}
 */
export function deleteUser(id) {
  return axios.delete(`${API_BASE_URL}/admin/delete/${id}`);
}

/**
 * 管理端切换用户状态
 * @param {Object} data 状态信息
 * @returns {Promise}
 */
export function toggleUserStatus(data) {
  return axios.post(`${API_BASE_URL}/admin/toggleStatus`, data);
}

/**
 * 管理端导出用户Excel
 * @param {Object} params 查询参数
 * @returns {String} 导出URL
 */
export function exportUsers(params) {
  const queryString = Object.keys(params)
    .filter(key => params[key] !== null && params[key] !== undefined && params[key] !== '')
    .map(key => `${key}=${encodeURIComponent(params[key])}`)
    .join('&');
  
  return `${API_BASE_URL}/admin/export${queryString ? '?' + queryString : ''}`;
}

