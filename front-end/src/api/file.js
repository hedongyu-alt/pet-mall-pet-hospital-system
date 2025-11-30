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
 * 上传单个文件
 * @param {File} file 文件对象
 * @returns Promise
 */
export function uploadFile(file) {
  const formData = new FormData();
  formData.append('file', file);
  
  return request({
    url: '/file/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 批量上传文件
 * @param {File[]} files 文件对象数组
 * @returns Promise
 */
export function batchUploadFiles(files) {
  const formData = new FormData();
  files.forEach(file => {
    formData.append('files', file);
  });
  
  return request({
    url: '/file/batchUpload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

