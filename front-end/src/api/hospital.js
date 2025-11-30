import axios from 'axios';

// 配置axios基础URL
const baseURL = 'http://localhost:18007/api';

// 创建axios实例
const request = axios.create({
  baseURL: baseURL,
  timeout: 30000,
  withCredentials: true
});

/**
 * 分页查询宠物医院列表
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function getHospitalList(params) {
  return request({
    url: '/hospital/list',
    method: 'get',
    params: params
  });
}

/**
 * 获取宠物医院详情
 * @param {Number} id 医院ID
 * @returns Promise
 */
export function getHospitalDetail(id) {
  return request({
    url: `/hospital/detail/${id}`,
    method: 'get'
  });
}

/**
 * 新增宠物医院
 * @param {Object} data 医院信息
 * @returns Promise
 */
export function addHospital(data) {
  return request({
    url: '/hospital/add',
    method: 'post',
    data: data
  });
}

/**
 * 更新宠物医院
 * @param {Object} data 医院信息
 * @returns Promise
 */
export function updateHospital(data) {
  return request({
    url: '/hospital/update',
    method: 'post',
    data: data
  });
}

/**
 * 删除宠物医院
 * @param {Number} id 医院ID
 * @returns Promise
 */
export function deleteHospital(id) {
  return request({
    url: `/hospital/delete/${id}`,
    method: 'delete'
  });
}

/**
 * 导出Excel（返回导出URL）
 * @param {Object} params 查询参数
 * @returns String
 */
export function exportHospitals(params) {
  const queryString = new URLSearchParams();
  
  if (params.name) queryString.append('name', params.name);
  if (params.services) queryString.append('services', params.services);
  if (params.address) queryString.append('address', params.address);
  
  return `${baseURL}/hospital/export?${queryString.toString()}`;
}

