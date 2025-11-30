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
 * 分页查询预约列表
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function getAppointmentList(params) {
  return request({
    url: '/appointment/list',
    method: 'get',
    params: params
  });
}

/**
 * 获取预约详情
 * @param {Number} id 预约ID
 * @returns Promise
 */
export function getAppointmentDetail(id) {
  return request({
    url: `/appointment/detail/${id}`,
    method: 'get'
  });
}

/**
 * 新增预约
 * @param {Object} data 预约信息
 * @returns Promise
 */
export function addAppointment(data) {
  return request({
    url: '/appointment/add',
    method: 'post',
    data: data
  });
}

/**
 * 更新预约状态
 * @param {Number} id 预约ID
 * @param {String} status 状态
 * @returns Promise
 */
export function updateAppointmentStatus(id, status) {
  return request({
    url: '/appointment/updateStatus',
    method: 'post',
    params: {
      id: id,
      status: status
    }
  });
}

/**
 * 取消预约
 * @param {Number} id 预约ID
 * @returns Promise
 */
export function cancelAppointment(id) {
  return request({
    url: `/appointment/cancel/${id}`,
    method: 'post'
  });
}

/**
 * 管理端分页查询预约列表
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function getAdminAppointmentList(params) {
  return request({
    url: '/appointment/admin/list',
    method: 'get',
    params: params
  });
}

/**
 * 管理端完成预约
 * @param {Number} id 预约ID
 * @returns Promise
 */
export function completeAppointment(id) {
  return request({
    url: `/appointment/admin/complete/${id}`,
    method: 'post'
  });
}

/**
 * 管理端更新预约
 * @param {Object} data 预约信息
 * @returns Promise
 */
export function adminUpdateAppointment(data) {
  return request({
    url: '/appointment/admin/update',
    method: 'post',
    data: data
  });
}

/**
 * 导出预约记录Excel
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function exportAppointments(params) {
  const queryString = new URLSearchParams(params).toString();
  window.open(`http://localhost:18007/api/appointment/admin/export?${queryString}`, '_blank');
}

