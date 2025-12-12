<template>
  <div class="admin-layout">
    <!-- 左侧导航栏 -->
    <div class="sidebar">
      <div class="sidebar-header">
        <div class="logo">
          <span class="pet-icon">🐾</span>
          <span class="logo-text">{{ systemName }} 管理</span>
        </div>
      </div>
      
      <div class="sidebar-menu">
        <div class="menu-item" :class="{ active: $route.path === '/admin/home' }" @click="handleMenuClick('/admin/home')">
          <i class="el-icon-s-data"></i>
          <span>数据概览</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/user-manage' }" @click="handleMenuClick('/admin/user-manage')">
          <i class="el-icon-user"></i>
          <span>用户管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/category-manage' }" @click="handleMenuClick('/admin/category-manage')">
          <i class="el-icon-menu"></i>
          <span>商品分类管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/product-manage' }" @click="handleMenuClick('/admin/product-manage')">
          <i class="el-icon-s-goods"></i>
          <span>商品管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/order-manage' }" @click="handleMenuClick('/admin/order-manage')">
          <i class="el-icon-s-order"></i>
          <span>订单管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/review-manage' }" @click="handleMenuClick('/admin/review-manage')">
          <i class="el-icon-s-comment"></i>
          <span>订单评价管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/insurance-manage' }" @click="handleMenuClick('/admin/insurance-manage')">
          <i class="el-icon-s-shop"></i>
          <span>宠物保险管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/claim-manage' }" @click="handleMenuClick('/admin/claim-manage')">
          <i class="el-icon-document-checked"></i>
          <span>保险理赔审核</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/hospital-manage' }" @click="handleMenuClick('/admin/hospital-manage')">
          <i class="el-icon-office-building"></i>
          <span>宠物医院管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/appointment-manage' }" @click="handleMenuClick('/admin/appointment-manage')">
          <i class="el-icon-date"></i>
          <span>医院预约记录管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/breeding-manage' }" @click="handleMenuClick('/admin/breeding-manage')">
          <i class="el-icon-present"></i>
          <span>寻找配种管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/foster-manage' }" @click="handleMenuClick('/admin/foster-manage')">
          <i class="el-icon-house"></i>
          <span>宠物寄养管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/adoption-manage' }" @click="handleMenuClick('/admin/adoption-manage')">
          <i class="el-icon-present"></i>
          <span>宠物领养管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/forum-manage' }" @click="handleMenuClick('/admin/forum-manage')">
          <i class="el-icon-chat-line-square"></i>
          <span>论坛管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/forum-comment-manage' }" @click="handleMenuClick('/admin/forum-comment-manage')">
          <i class="el-icon-chat-dot-square"></i>
          <span>论坛评论管理</span>
        </div>
        <div class="menu-item" :class="{ active: $route.path === '/admin/system-setting' }" @click="handleMenuClick('/admin/system-setting')">
          <i class="el-icon-setting"></i>
          <span>系统设置</span>
        </div>
      </div>
      
      <div class="sidebar-footer">
        <el-button type="danger" size="small" @click="handleLogout" plain>
          <i class="el-icon-switch-button"></i>
          退出登录
        </el-button>
      </div>
    </div>
    
    <!-- 右侧主体内容 -->
    <div class="main-container">
      <!-- 顶部栏 -->
      <div class="top-bar">
        <div class="page-title">
          <h2>{{ pageTitle }}</h2>
          <p>{{ pageDescription }}</p>
        </div>
        
        <div class="admin-info">
          <span class="welcome-text">欢迎，{{ userInfo.nickname }}</span>
          <el-avatar :src="userInfo.avatar" :size="36"></el-avatar>
        </div>
      </div>
      
      <!-- 子页面内容 -->
      <div class="content-wrapper">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { getSystemConfig } from '@/api/systemConfig';

export default {
  name: 'AdminLayout',
  data() {
    return {
      userInfo: {},
      systemName: '伴侣宠物之家'
    };
  },
  computed: {
    pageTitle() {
      const titleMap = {
        '/admin/home': '数据概览',
        '/admin/user-manage': '用户管理',
        '/admin/category-manage': '商品分类管理',
        '/admin/product-manage': '商品管理',
        '/admin/order-manage': '订单管理',
        '/admin/review-manage': '订单评价管理',
        '/admin/insurance-manage': '宠物保险管理',
        '/admin/claim-manage': '保险理赔审核',
        '/admin/hospital-manage': '宠物医院管理',
        '/admin/appointment-manage': '医院预约记录管理',
        '/admin/breeding-manage': '寻找配种管理',
        '/admin/foster-manage': '宠物寄养管理',
        '/admin/adoption-manage': '宠物领养管理',
        '/admin/forum-manage': '论坛管理',
        '/admin/forum-comment-manage': '论坛评论管理',
        '/admin/system-setting': '系统设置'
      };
      return titleMap[this.$route.path] || '管理后台';
    },
    pageDescription() {
      const descMap = {
        '/admin/home': '实时监控系统运营数据',
        '/admin/user-manage': '用户信息管理与维护',
        '/admin/category-manage': '商品分类信息管理与维护',
        '/admin/product-manage': '商品信息管理与维护',
        '/admin/order-manage': '订单信息管理与维护',
        '/admin/review-manage': '用户评价信息管理与维护',
        '/admin/insurance-manage': '宠物保险信息管理与维护',
        '/admin/claim-manage': '保险理赔申请审核与打款管理',
        '/admin/hospital-manage': '宠物医院信息管理与维护',
        '/admin/appointment-manage': '医院预约记录查看与状态管理',
        '/admin/breeding-manage': '寻找配种帖子管理与删除',
        '/admin/foster-manage': '宠物寄养帖子管理与删除',
        '/admin/adoption-manage': '宠物领养帖子管理与删除',
        '/admin/forum-manage': '论坛帖子管理与删除',
        '/admin/forum-comment-manage': '论坛评论管理与删除',
        '/admin/system-setting': '系统名称、轮播图、管理员密码管理'
      };
      return descMap[this.$route.path] || '系统管理';
    }
  },
  created() {
    this.loadUserInfo();
    this.loadSystemConfig();
  },
  methods: {
    loadUserInfo() {
      const userInfoStr = localStorage.getItem('userInfo');
      if (userInfoStr) {
        this.userInfo = JSON.parse(userInfoStr);
        if (this.userInfo.role !== '管理员') {
          this.$router.push('/user/home');
        }
      } else {
        this.$router.push('/login');
      }
    },
    loadSystemConfig() {
      getSystemConfig().then(response => {
        if (response.data.success) {
          const config = response.data.data;
          if (config.systemName) {
            this.systemName = config.systemName;
            document.title = config.systemName + ' - 管理后台';
          }
        }
      }).catch(error => {
        console.error('获取系统配置失败', error);
      });
    },
    handleMenuClick(path) {
      if (this.$route.path !== path) {
        this.$router.push(path);
      }
    },
    handleLogout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('userInfo');
        this.$notify({
          title: '成功',
          message: '已退出登录',
          type: 'success',
          duration: 2000
        });
        this.$router.push('/login');
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  background: #f0f2f5;
}

/* 左侧导航栏 */
.sidebar {
  width: 250px;
  background: linear-gradient(180deg, #2c5f2d 0%, #1e4620 100%);
  color: white;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.sidebar-header {
  padding: 25px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.pet-icon {
  font-size: 32px;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
}

.sidebar-menu {
  flex: 1;
  padding: 20px 0;
  overflow-y: auto;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
  margin: 3px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.menu-item.active {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  font-weight: 600;
}

.menu-item i {
  font-size: 18px;
}

.sidebar-footer {
  padding: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-footer .el-button {
  width: 100%;
}

/* 右侧主体内容 */
.main-container {
  flex: 1;
  overflow-y: auto;
  padding: 30px;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  background: white;
  padding: 20px 30px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.page-title h2 {
  color: #2c5f2d;
  font-size: 26px;
  margin: 0 0 5px 0;
  font-weight: 600;
}

.page-title p {
  color: #666;
  font-size: 14px;
  margin: 0;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.welcome-text {
  color: #333;
  font-size: 13px;
  font-weight: 500;
}

.content-wrapper {
  /* 子页面内容容器 */
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .sidebar {
    width: 220px;
  }
  
  .logo-text {
    font-size: 15px;
  }
  
  .pet-icon {
    font-size: 28px;
  }
  
  .menu-item {
    font-size: 13px;
    padding: 9px 18px;
    gap: 8px;
  }
  
  .menu-item i {
    font-size: 16px;
  }
}

@media (max-width: 1200px) {
  .sidebar {
    width: 200px;
  }
  
  .logo-text {
    font-size: 14px;
  }
  
  .pet-icon {
    font-size: 26px;
  }
  
  .menu-item {
    font-size: 12px;
    padding: 8px 15px;
    gap: 7px;
  }
  
  .menu-item i {
    font-size: 15px;
  }
  
  .page-title h2 {
    font-size: 22px;
  }
  
  .page-title p {
    font-size: 13px;
  }
  
  .welcome-text {
    font-size: 14px;
  }
}

@media (max-width: 992px) {
  .sidebar {
    width: 180px;
  }
  
  .logo-text {
    font-size: 13px;
  }
  
  .pet-icon {
    font-size: 24px;
  }
  
  .menu-item {
    font-size: 11px;
    padding: 7px 12px;
    gap: 6px;
  }
  
  .menu-item i {
    font-size: 14px;
  }
  
  .page-title h2 {
    font-size: 20px;
  }
  
  .page-title p {
    font-size: 12px;
  }
}

@media (max-width: 768px) {
  .sidebar {
    display: none;
  }
}
</style>

