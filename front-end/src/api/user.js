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
// 注意: captcha参数已改为前端验证,不再传递给后端
export function login(username, password, role, captcha = null) {
  const data = {
    username,
    password,
    role
  };
  
  // 如果传递了captcha参数,则添加到请求中(兼容后端验证的情况)
  if (captcha !== null && captcha !== undefined) {
    data.captcha = captcha;
  }
  
  return request({
    url: '/user/login',
    method: 'post',
    data
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

