import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/foster';

/**
 * 用户端分页查询寄养帖子列表
 */
export function getUserFosterList(params) {
  return axios.get(`${BASE_URL}/user/list`, { params });
}

/**
 * 用户端获取寄养帖子详情
 */
export function getUserFosterDetail(id) {
  return axios.get(`${BASE_URL}/user/detail/${id}`);
}

/**
 * 用户端发布寄养帖子
 */
export function addFosterPost(data) {
  return axios.post(`${BASE_URL}/user/add`, data);
}

/**
 * 用户端更新寄养帖子
 */
export function updateFosterPost(data) {
  return axios.post(`${BASE_URL}/user/update`, data);
}

/**
 * 用户端修改寄养帖子状态
 */
export function updateFosterPostStatus(id, data) {
  return axios.post(`${BASE_URL}/user/updateStatus/${id}`, data);
}

/**
 * 管理端分页查询寄养帖子列表
 */
export function getAdminFosterList(params) {
  return axios.get(`${BASE_URL}/admin/list`, { params });
}

/**
 * 管理端获取寄养帖子详情
 */
export function getAdminFosterDetail(id) {
  return axios.get(`${BASE_URL}/admin/detail/${id}`);
}

/**
 * 管理端删除寄养帖子
 */
export function deleteFosterPost(id) {
  return axios.post(`${BASE_URL}/admin/delete/${id}`);
}

/**
 * 管理端更新寄养帖子
 */
export function adminUpdateFosterPost(data) {
  return axios.post(`${BASE_URL}/admin/update`, data);
}

/**
 * 管理端导出寄养帖子Excel
 */
export function exportFoster(params) {
  const queryString = Object.keys(params)
    .filter(key => params[key] !== null && params[key] !== undefined && params[key] !== '')
    .map(key => `${key}=${encodeURIComponent(params[key])}`)
    .join('&');
  
  return `${BASE_URL}/admin/export${queryString ? '?' + queryString : ''}`;
}

