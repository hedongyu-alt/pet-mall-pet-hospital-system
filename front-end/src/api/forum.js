import axios from 'axios'

const BASE_URL = 'http://localhost:18007/api/forum'

// 获取帖子列表
export function getPostList(params) {
  return axios.get(`${BASE_URL}/posts`, { params })
}

// 获取帖子详情
export function getPostById(id) {
  return axios.get(`${BASE_URL}/posts/${id}`)
}

// 创建帖子
export function createPost(data, userId) {
  return axios.post(`${BASE_URL}/posts`, data, {
    params: { userId }
  })
}

// 点赞/取消点赞
export function toggleLike(postId, userId) {
  return axios.post(`${BASE_URL}/posts/${postId}/like`, null, {
    params: { userId }
  })
}

// 检查是否已点赞
export function checkLike(postId, userId) {
  return axios.get(`${BASE_URL}/posts/${postId}/like`, {
    params: { userId }
  })
}

// 添加评论
export function addComment(postId, data, userId) {
  return axios.post(`${BASE_URL}/posts/${postId}/comments`, data, {
    params: { userId }
  })
}

// 获取评论列表
export function getCommentList(postId) {
  return axios.get(`${BASE_URL}/posts/${postId}/comments`)
}

// 删除帖子
export function deletePost(id, userId) {
  return axios.delete(`${BASE_URL}/posts/${id}`, {
    params: { userId }
  })
}

// ==================== 管理端接口 ====================

// 管理员获取帖子列表
export function getAdminPostList(params) {
  return axios.get(`${BASE_URL}/admin/posts`, { params })
}

// 管理员删除帖子
export function adminDeletePost(id) {
  return axios.delete(`${BASE_URL}/admin/posts/${id}`)
}

// 管理员获取评论列表
export function getAdminCommentList(params) {
  return axios.get(`${BASE_URL}/admin/comments`, { params })
}

// 管理员删除评论
export function adminDeleteComment(id) {
  return axios.delete(`${BASE_URL}/admin/comments/${id}`)
}

// 管理员更新帖子
export function adminUpdatePost(data) {
  return axios.post(`${BASE_URL}/admin/posts/update`, data)
}

// 管理员更新评论
export function adminUpdateComment(data) {
  return axios.post(`${BASE_URL}/admin/comments/update`, data)
}

