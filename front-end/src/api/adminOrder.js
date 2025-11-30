import axios from 'axios'

const baseURL = 'http://localhost:18007/api/order'

/**
 * 管理端获取订单列表
 */
export function getAdminOrderList(params) {
  return axios.get(`${baseURL}/admin/list`, { params })
}

/**
 * 管理端获取订单详情
 */
export function getAdminOrderDetail(orderId) {
  return axios.get(`${baseURL}/admin/detail`, { params: { orderId } })
}

/**
 * 管理端取消订单
 */
export function adminCancelOrder(orderId) {
  return axios.post(`${baseURL}/admin/cancel`, { orderId })
}

/**
 * 发货
 */
export function shipOrder(orderId) {
  return axios.post(`${baseURL}/admin/ship`, { orderId })
}

/**
 * 导出订单Excel
 */
export function exportOrders(params) {
  const query = new URLSearchParams()
  if (params.status) query.append('status', params.status)
  if (params.productName) query.append('productName', params.productName)
  if (params.startTime) query.append('startTime', params.startTime)
  if (params.endTime) query.append('endTime', params.endTime)
  
  return `${baseURL}/admin/export?${query.toString()}`
}

