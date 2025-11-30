import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/insurance-claim';

/**
 * 用户提交理赔申请
 */
export function submitClaim(data) {
  return axios.post(`${BASE_URL}/submit`, data);
}

/**
 * 用户查询自己的理赔申请列表
 */
export function getUserClaims(params) {
  return axios.get(`${BASE_URL}/user/list`, { params });
}

/**
 * 用户查询理赔申请详情
 */
export function getClaimDetail(claimId, userId) {
  return axios.get(`${BASE_URL}/user/detail/${claimId}`, { params: { userId } });
}

/**
 * 管理员查询所有理赔申请列表
 */
export function getAdminClaims(params) {
  return axios.get(`${BASE_URL}/admin/list`, { params });
}

/**
 * 管理员查询理赔申请详情
 */
export function getAdminClaimDetail(claimId) {
  return axios.get(`${BASE_URL}/admin/detail/${claimId}`);
}

/**
 * 管理员审核通过理赔申请
 */
export function approveClaim(data) {
  return axios.post(`${BASE_URL}/admin/approve`, data);
}

/**
 * 管理员审核拒绝理赔申请
 */
export function rejectClaim(data) {
  return axios.post(`${BASE_URL}/admin/reject`, data);
}

/**
 * 管理员打款
 */
export function payClaim(data) {
  return axios.post(`${BASE_URL}/admin/pay`, data);
}

/**
 * 管理员更新理赔申请
 */
export function adminUpdateClaim(data) {
  return axios.post(`${BASE_URL}/admin/update`, data);
}

