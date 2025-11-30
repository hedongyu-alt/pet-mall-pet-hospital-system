import axios from 'axios';

// 配置axios基础URL
const baseURL = 'http://localhost:18007/api';

// 创建axios实例
const request = axios.create({
  baseURL: baseURL,
  timeout: 10000,
  withCredentials: true  // 允许携带cookies，支持session
});

// 用户登录
export function login(username, password, role, captcha) {
  return request({
    url: '/user/login',
    method: 'post',
    data: {
      username,
      password,
      role,
      captcha
    }
  });
}

// 用户注册
export function register(userInfo) {
  return request({
    url: '/user/register',
    method: 'post',
    data: userInfo
  });
}

// 检查用户名是否已存在
export function checkUsername(username) {
  return request({
    url: '/user/checkUsername',
    method: 'get',
    params: {
      username
    }
  });
}

// 获取用户信息
export function getUserInfo(userId) {
  return request({
    url: '/user/getUserInfo',
    method: 'get',
    params: {
      userId
    }
  });
}

// 更新用户信息
export function updateUserInfo(userInfo) {
  return request({
    url: '/user/updateUserInfo',
    method: 'post',
    data: userInfo
  });
}

// 修改密码
export function changePassword(userId, oldPassword, newPassword) {
  return request({
    url: '/user/changePassword',
    method: 'post',
    data: {
      userId,
      oldPassword,
      newPassword
    }
  });
}

