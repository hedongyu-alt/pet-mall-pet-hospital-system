import axios from 'axios'

const BASE_URL = 'http://localhost:18007'

/**
 * 创建订单
 * @param {Object} orderData 订单数据
 * @returns {Promise} axios响应
 */
export function createOrder(orderData) {
  return axios.post(`${BASE_URL}/api/order/create`, orderData)
}

/**
 * 获取用户订单列表
 * @param {Number} userId 用户ID
 * @param {Number} page 当前页
 * @param {Number} size 每页大小
 * @param {String} status 订单状态
 * @returns {Promise} axios响应
 */
export function getUserOrderList(userId, page = 1, size = 10, status = '') {
  return axios.get(`${BASE_URL}/api/order/list`, {
    params: {
      userId,
      page,
      size,
      status
    }
  })
}

/**
 * 获取订单详情
 * @param {Number} orderId 订单ID
 * @param {Number} userId 用户ID
 * @returns {Promise} axios响应
 */
export function getOrderDetail(orderId, userId) {
  return axios.get(`${BASE_URL}/api/order/detail`, {
    params: {
      orderId,
      userId
    }
  })
}

/**
 * 取消订单
 * @param {Number} orderId 订单ID
 * @param {Number} userId 用户ID
 * @returns {Promise} axios响应
 */
export function cancelOrder(orderId, userId) {
  return axios.post(`${BASE_URL}/api/order/cancel`, {
    orderId,
    userId
  })
}

/**
 * 确认收货
 * @param {Number} orderId 订单ID
 * @param {Number} userId 用户ID
 * @returns {Promise} axios响应
 */
export function confirmReceipt(orderId, userId) {
  return axios.post(`${BASE_URL}/api/order/confirm`, {
    orderId,
    userId
  })
}

