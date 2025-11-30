import axios from 'axios';

const API_BASE_URL = 'http://localhost:18007/api';

/**
 * 获取管理端数据概览统计
 */
export function getOverview() {
  return axios.get(`${API_BASE_URL}/stats/overview`);
}

/**
 * 获取商品分类分布数据
 */
export function getCategoryDistribution() {
  return axios.get(`${API_BASE_URL}/stats/category-distribution`);
}

/**
 * 获取订单状态分布数据
 */
export function getOrderStatusDistribution() {
  return axios.get(`${API_BASE_URL}/stats/order-status-distribution`);
}

/**
 * 获取宠物类型分布数据
 */
export function getPetTypeDistribution() {
  return axios.get(`${API_BASE_URL}/stats/pet-type-distribution`);
}

/**
 * 获取社区帖子分布数据
 */
export function getCommunityPostDistribution() {
  return axios.get(`${API_BASE_URL}/stats/community-post-distribution`);
}

/**
 * 获取理赔申请状态分布数据
 */
export function getClaimStatusDistribution() {
  return axios.get(`${API_BASE_URL}/stats/claim-status-distribution`);
}


