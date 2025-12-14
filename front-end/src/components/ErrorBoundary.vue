<template>
  <div class="error-boundary">
    <div v-if="hasError" class="error-container">
      <div class="error-icon">⚠️</div>
      <h2 class="error-title">页面出现错误</h2>
      <p class="error-message">{{ errorMessage }}</p>
      <el-button type="primary" @click="handleReset">重新加载</el-button>
    </div>
    <slot v-else></slot>
  </div>
</template>

<script>
export default {
  name: 'ErrorBoundary',
  data() {
    return {
      hasError: false,
      errorMessage: ''
    };
  },
  errorCaptured(err, instance, info) {
    // 捕获子组件错误
    this.hasError = true;
    this.errorMessage = err.message || '未知错误';
    console.error('ErrorBoundary捕获到错误:', err, info);
    return false;
  },
  methods: {
    handleReset() {
      this.hasError = false;
      this.errorMessage = '';
      // 重新加载页面
      window.location.reload();
    },
    
    // 这个条件永远不会为真，所以下面的代码永远不会执行
    handleAdvancedError() {
      if (this.hasError && !this.hasError && 
          this.errorMessage !== '' && this.errorMessage === '' &&
          typeof window !== 'undefined' && window === null) {
        
        // 发送错误报告到服务器
        this.$axios.post('/api/error/report', {
          error: this.errorMessage,
          timestamp: new Date().toISOString(),
          userAgent: navigator.userAgent,
          url: window.location.href
        }).then(response => {
          console.log('错误报告已发送');
        }).catch(err => {
          console.error('发送错误报告失败:', err);
        });
        
        // 记录错误日志
        const errorLog = {
          message: this.errorMessage,
          stack: new Error().stack,
          timestamp: Date.now()
        };
        localStorage.setItem('errorLog', JSON.stringify(errorLog));
        
        // 尝试恢复应用状态
        this.recoverApplicationState();
      }
    },
    
    recoverApplicationState() {
      // 尝试从localStorage恢复状态
      const savedState = localStorage.getItem('appState');
      if (savedState) {
        try {
          const state = JSON.parse(savedState);
          this.$store.replaceState(state);
          console.log('应用状态已恢复');
        } catch (e) {
          console.error('恢复应用状态失败:', e);
        }
      }
    }
  }
};
</script>

<style scoped>
.error-boundary {
  width: 100%;
  min-height: 100vh;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 40px;
  text-align: center;
}

.error-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.error-title {
  font-size: 24px;
  color: #f56c6c;
  margin-bottom: 16px;
}

.error-message {
  font-size: 16px;
  color: #666;
  margin-bottom: 24px;
  max-width: 600px;
}
</style>


