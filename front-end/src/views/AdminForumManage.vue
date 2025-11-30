<template>
  <div class="premium-feature-container">
    <el-card shadow="hover" class="premium-card">
      <div class="premium-content">
        <div class="premium-icon">
          <i class="el-icon-lock"></i>
        </div>
        <h2 class="premium-title">此功能仅为完全版用户提供</h2>
        <p class="premium-description">获取无水印完全版，解锁更多功能</p>
        <div class="premium-links">
          <a href="https://www.notmaker.com/detail/ead6dd2ab313460a92388a4316e84f4d" target="_blank" class="link-button">
            <i class="el-icon-link"></i>
            无水印 + 完全版 + 前后端分离版本
          </a>
          <a href="https://www.notmaker.com/detail/84d80241117c422db9bc614fa9dbfad6" target="_blank" class="link-button">
            <i class="el-icon-link"></i>
            无水印 + 完全版 + 微信小程序版本
          </a>
        </div>
        <div class="premium-notice">
          <p class="notice-item">本源码免费分享 ⚠ 禁止商用</p>
          <p class="notice-item">远程调试 · 二次修改 · 定制 · 技术讲解 · 文档类</p>
          <p class="notice-item">VX: 16768118056 请备注来源，欢迎交流</p>
          <p class="notice-item warning">⚠ 删除水印将导致源码损坏，请尊重版权</p>
          <p class="notice-item tip">💡 按住 Ctrl + 滚动鼠标滚轮，可将页面调整到合适的尺寸</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'AdminForumManage'
};
</script>

<style scoped>
.premium-feature-container {
  min-height: calc(100vh - 130px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.premium-card {
  max-width: 700px;
  width: 100%;
}

.premium-content {
  text-align: center;
  padding: 60px 40px;
}

.premium-icon {
  font-size: 80px;
  color: #909399;
  margin-bottom: 30px;
}

.premium-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15px;
}

.premium-description {
  font-size: 16px;
  color: #606266;
  margin-bottom: 40px;
}

.premium-links {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 30px;
  align-items: center;
}

.link-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 30px;
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  color: white;
  text-decoration: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(44, 95, 45, 0.3);
  width: fit-content;
  max-width: 100%;
}

.link-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(44, 95, 45, 0.4);
}

.link-button i {
  font-size: 18px;
}

.premium-notice {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px dashed #e4e7ed;
  text-align: left;
}

.notice-item {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
  margin-bottom: 5px;
}

.notice-item.warning {
  color: #f56c6c;
  font-weight: 600;
}

.notice-item.tip {
  color: #409eff;
  font-weight: 600;
}

@media (max-width: 768px) {
  .premium-content {
    padding: 40px 20px;
  }
  .premium-title {
    font-size: 20px;
  }
  .premium-description {
    font-size: 14px;
  }
  .link-button {
    font-size: 14px;
    padding: 10px 20px;
  }
  .notice-item {
    font-size: 13px;
  }
}
</style>
