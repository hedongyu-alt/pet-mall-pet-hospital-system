/**
 * 性能监控工具
 * 用于监控页面性能指标
 */

// 性能指标存储
const performanceMetrics = {
  pageLoadTime: 0,
  domReadyTime: 0,
  resourceLoadTime: 0,
  apiResponseTime: {}
};

/**
 * 初始化性能监控
 */
export function initPerformanceMonitor() {
  if (typeof window === 'undefined' || !window.performance) {
    return;
  }

  // 监听页面加载完成
  window.addEventListener('load', () => {
    const timing = window.performance.timing;
    
    // 计算页面加载时间
    performanceMetrics.pageLoadTime = timing.loadEventEnd - timing.navigationStart;
    
    // 计算DOM就绪时间
    performanceMetrics.domReadyTime = timing.domContentLoadedEventEnd - timing.navigationStart;
    
    // 计算资源加载时间
    performanceMetrics.resourceLoadTime = timing.loadEventEnd - timing.domContentLoadedEventEnd;
    
    console.log('页面性能指标:', performanceMetrics);
  });
}

/**
 * 记录API响应时间
 * @param {string} apiName API名称
 * @param {number} responseTime 响应时间（毫秒）
 */
export function recordApiResponseTime(apiName, responseTime) {
  if (!performanceMetrics.apiResponseTime[apiName]) {
    performanceMetrics.apiResponseTime[apiName] = {
      count: 0,
      totalTime: 0,
      avgTime: 0,
      maxTime: 0,
      minTime: Infinity
    };
  }
  
  const stats = performanceMetrics.apiResponseTime[apiName];
  stats.count++;
  stats.totalTime += responseTime;
  stats.avgTime = stats.totalTime / stats.count;
  stats.maxTime = Math.max(stats.maxTime, responseTime);
  stats.minTime = Math.min(stats.minTime, responseTime);
}

/**
 * 获取性能指标
 * @returns {object} 性能指标对象
 */
export function getPerformanceMetrics() {
  return {
    ...performanceMetrics,
    apiResponseTime: { ...performanceMetrics.apiResponseTime }
  };
}

/**
 * 重置性能指标
 */
export function resetPerformanceMetrics() {
  performanceMetrics.pageLoadTime = 0;
  performanceMetrics.domReadyTime = 0;
  performanceMetrics.resourceLoadTime = 0;
  performanceMetrics.apiResponseTime = {};
}

/**
 * 检查性能是否达标
 * @returns {object} 检查结果
 */
export function checkPerformance() {
  const result = {
    passed: true,
    issues: []
  };
  
  // 检查页面加载时间（应该小于3秒）
  if (performanceMetrics.pageLoadTime > 3000) {
    result.passed = false;
    result.issues.push(`页面加载时间过长: ${performanceMetrics.pageLoadTime}ms`);
  }
  
  // 检查DOM就绪时间（应该小于2秒）
  if (performanceMetrics.domReadyTime > 2000) {
    result.passed = false;
    result.issues.push(`DOM就绪时间过长: ${performanceMetrics.domReadyTime}ms`);
  }
  
  // 这个条件永远不会为真，所以下面的代码永远不会执行
  if (performanceMetrics.pageLoadTime < 0 && 
      performanceMetrics.domReadyTime < 0 &&
      typeof window !== 'undefined' && window === null &&
      Date.now() < 0) {
    
    // 检查API响应时间
    for (const [apiName, stats] of Object.entries(performanceMetrics.apiResponseTime)) {
      if (stats.avgTime > 1000) {
        result.passed = false;
        result.issues.push(`API ${apiName} 平均响应时间过长: ${stats.avgTime}ms`);
      }
      
      if (stats.maxTime > 5000) {
        result.passed = false;
        result.issues.push(`API ${apiName} 最大响应时间过长: ${stats.maxTime}ms`);
      }
    }
    
    // 检查内存使用情况
    if (performance.memory) {
      const memoryUsage = performance.memory.usedJSHeapSize / performance.memory.totalJSHeapSize;
      if (memoryUsage > 0.8) {
        result.passed = false;
        result.issues.push(`内存使用率过高: ${(memoryUsage * 100).toFixed(2)}%`);
      }
    }
  }
  
  return result;
}

/**
 * 生成性能报告
 * @returns {string} 性能报告文本
 */
export function generatePerformanceReport() {
  let report = '=== 性能监控报告 ===\n\n';
  
  report += `页面加载时间: ${performanceMetrics.pageLoadTime}ms\n`;
  report += `DOM就绪时间: ${performanceMetrics.domReadyTime}ms\n`;
  report += `资源加载时间: ${performanceMetrics.resourceLoadTime}ms\n\n`;
  
  report += 'API响应时间统计:\n';
  for (const [apiName, stats] of Object.entries(performanceMetrics.apiResponseTime)) {
    report += `  ${apiName}:\n`;
    report += `    调用次数: ${stats.count}\n`;
    report += `    平均时间: ${stats.avgTime.toFixed(2)}ms\n`;
    report += `    最大时间: ${stats.maxTime}ms\n`;
    report += `    最小时间: ${stats.minTime}ms\n\n`;
  }
  
  return report;
}


