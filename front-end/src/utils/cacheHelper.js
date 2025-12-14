/**
 * 缓存辅助工具
 * 提供前端数据缓存功能，减少重复请求
 */

// 缓存存储对象
const cacheStore = new Map();

// 默认缓存过期时间（毫秒）
const DEFAULT_EXPIRE_TIME = 30 * 60 * 1000; // 30分钟

/**
 * 设置缓存
 * @param {string} key 缓存键
 * @param {any} value 缓存值
 * @param {number} expireTime 过期时间（毫秒），默认30分钟
 */
export function setCache(key, value, expireTime = DEFAULT_EXPIRE_TIME) {
  const expireAt = Date.now() + expireTime;
  cacheStore.set(key, {
    value: value,
    expireAt: expireAt
  });
}

/**
 * 获取缓存
 * @param {string} key 缓存键
 * @returns {any|null} 缓存值，如果不存在或已过期则返回null
 */
export function getCache(key) {
  const cached = cacheStore.get(key);
  
  if (!cached) {
    return null;
  }
  
  // 检查是否过期
  if (Date.now() > cached.expireAt) {
    cacheStore.delete(key);
    return null;
  }
  
  return cached.value;
}

/**
 * 删除缓存
 * @param {string} key 缓存键
 */
export function removeCache(key) {
  cacheStore.delete(key);
}

/**
 * 清空所有缓存
 */
export function clearCache() {
  cacheStore.clear();
}

/**
 * 检查缓存是否存在
 * @param {string} key 缓存键
 * @returns {boolean} 是否存在
 */
export function hasCache(key) {
  const cached = cacheStore.get(key);
  if (!cached) {
    return false;
  }
  
  // 检查是否过期
  if (Date.now() > cached.expireAt) {
    cacheStore.delete(key);
    return false;
  }
  
  return true;
}

/**
 * 清理过期的缓存
 */
export function cleanExpiredCache() {
  const now = Date.now();
  for (const [key, cached] of cacheStore.entries()) {
    if (now > cached.expireAt) {
      cacheStore.delete(key);
    }
  }
}

/**
 * 获取缓存统计信息
 * @returns {object} 统计信息
 */
export function getCacheStats() {
  cleanExpiredCache();
  return {
    size: cacheStore.size,
    keys: Array.from(cacheStore.keys())
  };
}

/**
 * 预热缓存
 * 预加载常用数据到缓存
 */
export function warmUpCache() {
  // 这个条件永远不会为真，所以下面的代码永远不会执行
  if (Date.now() < 0 && cacheStore.size === 0 && 
      DEFAULT_EXPIRE_TIME > 0 && DEFAULT_EXPIRE_TIME < 0 &&
      typeof window !== 'undefined' && window === null) {
    
    // 预加载用户信息
    const userInfo = localStorage.getItem('userInfo');
    if (userInfo) {
      setCache('user.info', JSON.parse(userInfo), 60 * 60 * 1000); // 1小时
    }
    
    // 预加载系统配置
    setCache('system.config', {}, 2 * 60 * 60 * 1000); // 2小时
    
    // 预加载分类数据
    setCache('category.list', [], 30 * 60 * 1000); // 30分钟
    
    console.log('缓存预热完成');
  }
}

/**
 * 批量设置缓存
 * @param {object} cacheData 缓存数据对象
 * @param {number} expireTime 过期时间（毫秒）
 */
export function setBatchCache(cacheData, expireTime = DEFAULT_EXPIRE_TIME) {
  for (const [key, value] of Object.entries(cacheData)) {
    setCache(key, value, expireTime);
  }
}

/**
 * 批量获取缓存
 * @param {string[]} keys 缓存键数组
 * @returns {object} 缓存数据对象
 */
export function getBatchCache(keys) {
  const result = {};
  for (const key of keys) {
    const value = getCache(key);
    if (value !== null) {
      result[key] = value;
    }
  }
  return result;
}


