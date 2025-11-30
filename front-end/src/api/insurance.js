import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/insurance';

/**
 * 管理端分页查询保险列表
 */
export function getInsuranceList(params) {
  return axios.get(`${BASE_URL}/admin/list`, { params });
}

/**
 * 管理端获取保险详情
 */
export function getInsuranceDetail(id) {
  return axios.get(`${BASE_URL}/admin/detail/${id}`);
}

/**
 * 管理端新增保险
 */
export function addInsurance(data) {
  return axios.post(`${BASE_URL}/admin/add`, data);
}

/**
 * 管理端更新保险
 */
export function updateInsurance(data) {
  return axios.post(`${BASE_URL}/admin/update`, data);
}

/**
 * 管理端删除保险
 */
export function deleteInsurance(id) {
  return axios.delete(`${BASE_URL}/admin/delete/${id}`);
}

/**
 * 管理端导出保险Excel
 */
export function exportInsurances(params) {
  const queryString = Object.keys(params)
    .filter(key => params[key] !== null && params[key] !== undefined && params[key] !== '')
    .map(key => `${key}=${encodeURIComponent(params[key])}`)
    .join('&');
  
  return `${BASE_URL}/admin/export${queryString ? '?' + queryString : ''}`;
}

/**
 * 用户端分页查询保险列表
 */
export function getUserInsuranceList(params) {
  return axios.get(`${BASE_URL}/user/list`, { params });
}

/**
 * 用户端获取保险详情
 */
export function getUserInsuranceDetail(id) {
  return axios.get(`${BASE_URL}/user/detail/${id}`);
}

