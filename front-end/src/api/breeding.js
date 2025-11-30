import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/breeding';

/**
 * 用户端分页查询配种帖子列表
 */
export function getUserBreedingList(params) {
  return axios.get(`${BASE_URL}/user/list`, { params });
}

/**
 * 用户端获取配种帖子详情
 */
export function getUserBreedingDetail(id) {
  return axios.get(`${BASE_URL}/user/detail/${id}`);
}

/**
 * 用户端发布配种帖子
 */
export function addBreedingPost(data) {
  return axios.post(`${BASE_URL}/user/add`, data);
}

/**
 * 用户端更新配种帖子
 */
export function updateBreedingPost(data) {
  return axios.post(`${BASE_URL}/user/update`, data);
}

/**
 * 用户端修改配种帖子状态
 */
export function updateBreedingPostStatus(id, data) {
  return axios.post(`${BASE_URL}/user/updateStatus/${id}`, data);
}

/**
 * 管理端分页查询配种帖子列表
 */
export function getAdminBreedingList(params) {
  return axios.get(`${BASE_URL}/admin/list`, { params });
}

/**
 * 管理端获取配种帖子详情
 */
export function getAdminBreedingDetail(id) {
  return axios.get(`${BASE_URL}/admin/detail/${id}`);
}

/**
 * 管理端删除配种帖子
 */
export function deleteBreedingPost(id) {
  return axios.post(`${BASE_URL}/admin/delete/${id}`);
}

/**
 * 管理端更新配种帖子
 */
export function adminUpdateBreedingPost(data) {
  return axios.post(`${BASE_URL}/admin/update`, data);
}

/**
 * 管理端导出配种帖子Excel
 */
export function exportBreeding(params) {
  const queryString = Object.keys(params)
    .filter(key => params[key] !== null && params[key] !== undefined && params[key] !== '')
    .map(key => `${key}=${encodeURIComponent(params[key])}`)
    .join('&');
  
  return `${BASE_URL}/admin/export${queryString ? '?' + queryString : ''}`;
}

