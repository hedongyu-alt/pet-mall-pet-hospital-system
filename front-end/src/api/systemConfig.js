import axios from 'axios';

const BASE_URL = 'http://localhost:18007/api/system-config';

// 获取系统配置
export function getSystemConfig() {
  return axios.get(`${BASE_URL}/get`);
}

// 更新系统配置
export function updateSystemConfig(data) {
  return axios.post(`${BASE_URL}/update`, data);
}

// 修改管理员密码
export function updateAdminPassword(data) {
  return axios.post(`${BASE_URL}/updateAdminPassword`, data);
}

