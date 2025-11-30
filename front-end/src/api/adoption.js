import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/adoption';

/**
 * 用户端分页查询领养帖子列表
 */
export function getUserAdoptionList(params) {
  return axios.get(`${BASE_URL}/user/list`, { params });
}

/**
 * 用户端获取领养帖子详情
 */
export function getUserAdoptionDetail(id) {
  return axios.get(`${BASE_URL}/user/detail/${id}`);
}

/**
 * 用户端发布领养帖子
 */
export function addAdoptionPost(data) {
  return axios.post(`${BASE_URL}/user/add`, data);
}

/**
 * 用户端更新领养帖子
 */
export function updateAdoptionPost(data) {
  return axios.post(`${BASE_URL}/user/update`, data);
}

/**
 * 用户端修改领养帖子状态
 */
export function updateAdoptionPostStatus(id, data) {
  return axios.post(`${BASE_URL}/user/updateStatus/${id}`, data);
}

/**
 * 管理端分页查询领养帖子列表
 */
export function getAdminAdoptionList(params) {
  return axios.get(`${BASE_URL}/admin/list`, { params });
}

/**
 * 管理端获取领养帖子详情
 */
export function getAdminAdoptionDetail(id) {
  return axios.get(`${BASE_URL}/admin/detail/${id}`);
}

/**
 * 管理端删除领养帖子
 */
export function deleteAdoptionPost(id) {
  return axios.post(`${BASE_URL}/admin/delete/${id}`);
}

/**
 * 管理端更新领养帖子
 */
export function adminUpdateAdoptionPost(data) {
  return axios.post(`${BASE_URL}/admin/update`, data);
}

/**
 * 管理端导出领养帖子Excel
 */
export function exportAdoption(params) {
  const queryString = Object.keys(params)
    .filter(key => params[key] !== null && params[key] !== undefined && params[key] !== '')
    .map(key => `${key}=${encodeURIComponent(params[key])}`)
    .join('&');
  
  return `${BASE_URL}/admin/export${queryString ? '?' + queryString : ''}`;
}

