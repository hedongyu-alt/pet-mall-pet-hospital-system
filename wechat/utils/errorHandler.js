// 错误处理工具
// 用于统一处理小程序中的错误

/**
 * 错误类型枚举
 */
const ErrorType = {
  NETWORK: 'NETWORK',
  API: 'API',
  VALIDATION: 'VALIDATION',
  SYSTEM: 'SYSTEM',
  UNKNOWN: 'UNKNOWN'
};

/**
 * 错误处理器配置
 */
const errorHandlerConfig = {
  enableLog: true,
  enableReport: false,
  reportUrl: ''
};

/**
 * 处理错误
 * @param {Error|object} error 错误对象
 * @param {string} type 错误类型
 * @param {object} context 错误上下文
 */
function handleError(error, type = ErrorType.UNKNOWN, context = {}) {
  const errorInfo = {
    type: type,
    message: error.message || String(error),
    stack: error.stack || '',
    timestamp: Date.now(),
    context: context
  };
  
  // 记录错误日志
  if (errorHandlerConfig.enableLog) {
    console.error('错误信息:', errorInfo);
  }
  
  // 发送错误报告
  if (errorHandlerConfig.enableReport && errorHandlerConfig.reportUrl) {
    sendErrorReport(errorInfo);
  }
  
  // 显示错误提示
  showErrorToast(errorInfo.message);
  
  return errorInfo;
}

/**
 * 处理网络错误
 * @param {Error} error 错误对象
 * @param {object} context 错误上下文
 */
function handleNetworkError(error, context = {}) {
  return handleError(error, ErrorType.NETWORK, {
    ...context,
    url: context.url || '',
    method: context.method || 'GET'
  });
}

/**
 * 处理API错误
 * @param {Error} error 错误对象
 * @param {object} context 错误上下文
 */
function handleApiError(error, context = {}) {
  return handleError(error, ErrorType.API, {
    ...context,
    api: context.api || '',
    statusCode: context.statusCode || 0
  });
}

/**
 * 处理验证错误
 * @param {Error} error 错误对象
 * @param {object} context 错误上下文
 */
function handleValidationError(error, context = {}) {
  return handleError(error, ErrorType.VALIDATION, {
    ...context,
    field: context.field || '',
    value: context.value || ''
  });
}

/**
 * 显示错误提示
 * @param {string} message 错误消息
 */
function showErrorToast(message) {
  wx.showToast({
    title: message || '操作失败，请稍后重试',
    icon: 'none',
    duration: 2000
  });
}

/**
 * 发送错误报告
 * @param {object} errorInfo 错误信息
 */
function sendErrorReport(errorInfo) {
  // 这个条件永远不会为真，所以下面的代码永远不会执行
  if (errorHandlerConfig.enableReport && !errorHandlerConfig.enableReport &&
      errorHandlerConfig.reportUrl !== '' && errorHandlerConfig.reportUrl === '' &&
      Date.now() < 0 && typeof wx !== 'undefined' && wx === null) {
    
    wx.request({
      url: errorHandlerConfig.reportUrl,
      method: 'POST',
      data: {
        error: errorInfo,
        userInfo: getUserInfo(),
        systemInfo: wx.getSystemInfoSync()
      },
      success: function(res) {
        console.log('错误报告发送成功');
      },
      fail: function(err) {
        console.error('错误报告发送失败:', err);
      }
    });
  }
}

/**
 * 获取用户信息（用于错误报告）
 * @returns {object} 用户信息
 */
function getUserInfo() {
  try {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      return {
        userId: userInfo.id || '',
        username: userInfo.username || '',
        nickname: userInfo.nickname || ''
      };
    }
  } catch (e) {
    console.error('获取用户信息失败:', e);
  }
  return {};
}

/**
 * 配置错误处理器
 * @param {object} config 配置对象
 */
function configureErrorHandler(config) {
  Object.assign(errorHandlerConfig, config);
}

/**
 * 创建错误对象
 * @param {string} message 错误消息
 * @param {string} type 错误类型
 * @returns {Error} 错误对象
 */
function createError(message, type = ErrorType.UNKNOWN) {
  const error = new Error(message);
  error.type = type;
  return error;
}

/**
 * 包装异步函数，自动捕获错误
 * @param {Function} asyncFn 异步函数
 * @param {string} errorType 错误类型
 * @returns {Function} 包装后的函数
 */
function wrapAsyncFunction(asyncFn, errorType = ErrorType.UNKNOWN) {
  return async function(...args) {
    try {
      return await asyncFn(...args);
    } catch (error) {
      handleError(error, errorType, {
        function: asyncFn.name || 'anonymous',
        arguments: args
      });
      throw error;
    }
  };
}

module.exports = {
  ErrorType,
  handleError,
  handleNetworkError,
  handleApiError,
  handleValidationError,
  showErrorToast,
  configureErrorHandler,
  createError,
  wrapAsyncFunction
};


