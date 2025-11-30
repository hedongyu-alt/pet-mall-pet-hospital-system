import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/insurance-order';

/**
 * 用户购买保险
 */
export function purchaseInsurance(data) {
  return axios.post(`${BASE_URL}/purchase`, data);
}

/**
 * 用户查询自己的保单列表
 */
export function getUserOrders(params) {
  return axios.get(`${BASE_URL}/user/list`, { params });
}

/**
 * 用户查询保单详情
 */
export function getOrderDetail(orderId, userId) {
  return axios.get(`${BASE_URL}/user/detail/${orderId}`, { params: { userId } });
}

