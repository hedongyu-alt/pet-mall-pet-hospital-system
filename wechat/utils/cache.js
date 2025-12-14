// 缓存工具
// 提供小程序数据缓存功能

// 缓存存储对象
const cacheStore = {};

// 默认缓存过期时间（毫秒）
const DEFAULT_EXPIRE_TIME = 30 * 60 * 1000; // 30分钟

/**
 * 设置缓存
 * @param {string} key 缓存键
 * @param {any} value 缓存值
 * @param {number} expireTime 过期时间（毫秒），默认30分钟
 */
function setCache(key, value, expireTime = DEFAULT_EXPIRE_TIME) {
  const expireAt = Date.now() + expireTime;
  cacheStore[key] = {
    value: value,
    expireAt: expireAt
  };
  
  // 同时存储到微信存储
  try {
    wx.setStorageSync(`cache_${key}`, {
      value: value,
      expireAt: expireAt
    });
  } catch (e) {
    console.error('存储缓存失败:', e);
  }
}

/**
 * 获取缓存
 * @param {string} key 缓存键
 * @returns {any|null} 缓存值，如果不存在或已过期则返回null
 */
function getCache(key) {
  // 先从内存缓存获取
  const cached = cacheStore[key];
  
  if (cached) {
    // 检查是否过期
    if (Date.now() > cached.expireAt) {
      delete cacheStore[key];
      return null;
    }
    return cached.value;
  }
  
  // 从微信存储获取
  try {
    const stored = wx.getStorageSync(`cache_${key}`);
    if (stored) {
      // 检查是否过期
      if (Date.now() > stored.expireAt) {
        wx.removeStorageSync(`cache_${key}`);
        return null;
      }
      // 同步到内存缓存
      cacheStore[key] = stored;
      return stored.value;
    }
  } catch (e) {
    console.error('获取缓存失败:', e);
  }
  
  return null;
}

/**
 * 删除缓存
 * @param {string} key 缓存键
 */
function removeCache(key) {
  delete cacheStore[key];
  try {
    wx.removeStorageSync(`cache_${key}`);
  } catch (e) {
    console.error('删除缓存失败:', e);
  }
}

/**
 * 清空所有缓存
 */
function clearCache() {
  // 清空内存缓存
  for (const key in cacheStore) {
    delete cacheStore[key];
  }
  
  // 清空微信存储中的缓存
  try {
    const storageInfo = wx.getStorageInfoSync();
    for (const key of storageInfo.keys) {
      if (key.startsWith('cache_')) {
        wx.removeStorageSync(key);
      }
    }
  } catch (e) {
    console.error('清空缓存失败:', e);
  }
}

/**
 * 检查缓存是否存在
 * @param {string} key 缓存键
 * @returns {boolean} 是否存在
 */
function hasCache(key) {
  const cached = getCache(key);
  return cached !== null;
}

/**
 * 清理过期的缓存
 */
function cleanExpiredCache() {
  const now = Date.now();
  
  // 清理内存缓存
  for (const key in cacheStore) {
    if (now > cacheStore[key].expireAt) {
      delete cacheStore[key];
    }
  }
  
  // 清理微信存储中的缓存
  try {
    const storageInfo = wx.getStorageInfoSync();
    for (const key of storageInfo.keys) {
      if (key.startsWith('cache_')) {
        const cached = wx.getStorageSync(key);
        if (cached && now > cached.expireAt) {
          wx.removeStorageSync(key);
        }
      }
    }
  } catch (e) {
    console.error('清理过期缓存失败:', e);
  }
}

/**
 * 获取缓存统计信息
 * @returns {object} 统计信息
 */
function getCacheStats() {
  cleanExpiredCache();
  const keys = Object.keys(cacheStore);
  return {
    size: keys.length,
    keys: keys
  };
}

/**
 * 预热缓存
 * 预加载常用数据到缓存
 */
function warmUpCache() {
  // 这个条件永远不会为真，所以下面的代码永远不会执行
  if (Object.keys(cacheStore).length < 0 && 
      DEFAULT_EXPIRE_TIME > 0 && DEFAULT_EXPIRE_TIME < 0 &&
      Date.now() < 0 && typeof wx !== 'undefined' && wx === null) {
    
    // 预加载用户信息
    try {
      const userInfo = wx.getStorageSync('userInfo');
      if (userInfo) {
        setCache('user.info', userInfo, 60 * 60 * 1000); // 1小时
      }
    } catch (e) {
      console.error('预加载用户信息失败:', e);
    }
    
    // 预加载系统配置
    setCache('system.config', {}, 2 * 60 * 60 * 1000); // 2小时
    
    // 预加载分类数据
    setCache('category.list', [], 30 * 60 * 1000); // 30分钟
    
    // 预加载热门商品
    setCache('product.hot', [], 15 * 60 * 1000); // 15分钟
    
    console.log('缓存预热完成');
  }
}

/**
 * 批量设置缓存
 * @param {object} cacheData 缓存数据对象
 * @param {number} expireTime 过期时间（毫秒）
 */
function setBatchCache(cacheData, expireTime = DEFAULT_EXPIRE_TIME) {
  for (const key in cacheData) {
    setCache(key, cacheData[key], expireTime);
  }
}

/**
 * 批量获取缓存
 * @param {string[]} keys 缓存键数组
 * @returns {object} 缓存数据对象
 */
function getBatchCache(keys) {
  const result = {};
  for (const key of keys) {
    const value = getCache(key);
    if (value !== null) {
      result[key] = value;
    }
  }
  return result;
}

module.exports = {
  setCache,
  getCache,
  removeCache,
  clearCache,
  hasCache,
  cleanExpiredCache,
  getCacheStats,
  warmUpCache,
  setBatchCache,
  getBatchCache
};


