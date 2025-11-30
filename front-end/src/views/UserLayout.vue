<template>
  <div class="user-layout">
    <!-- 顶部导航栏 -->
    <div class="top-navbar">
      <div class="navbar-content">
        <div class="logo">
          <span class="pet-icon">🐾</span>
          <span class="logo-text">{{ systemName }}</span>
        </div>
        
        <!-- 汉堡菜单按钮 (仅手机端显示) -->
        <div class="mobile-menu-btn" @click="drawerVisible = true">
          <i class="el-icon-s-unfold"></i>
        </div>
        
        <!-- PC端导航菜单 -->
        <div class="nav-menu">
          <div class="nav-item" :class="{ active: $route.path === '/user/home' }" @click="goTo('/user/home')">
            <i class="el-icon-s-home"></i>
            <span>首页</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path.startsWith('/user/product') }" @click="goTo('/user/product-list')">
            <i class="el-icon-shopping-bag-2"></i>
            <span>商品一览</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/insurance-list' }" @click="goTo('/user/insurance-list')">
            <i class="el-icon-s-home"></i>
            <span>宠物保险</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/policy-list' }" @click="goTo('/user/policy-list')">
            <i class="el-icon-document-checked"></i>
            <span>我的保单</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/claim-list' }" @click="goTo('/user/claim-list')">
            <i class="el-icon-warning-outline"></i>
            <span>申保记录</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path.startsWith('/user/hospital') }" @click="goTo('/user/hospital-list')">
            <i class="el-icon-office-building"></i>
            <span>宠物医院</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/appointment-list' }" @click="goTo('/user/appointment-list')">
            <i class="el-icon-s-order"></i>
            <span>预约记录</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path.startsWith('/user/breeding') }" @click="goTo('/user/breeding-list')">
            <i class="el-icon-search"></i>
            <span>寻找配种</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path.startsWith('/user/foster') }" @click="goTo('/user/foster-list')">
            <i class="el-icon-house"></i>
            <span>宠物寄养</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path.startsWith('/user/adoption') }" @click="goTo('/user/adoption-list')">
            <i class="el-icon-present"></i>
            <span>宠物领养</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path.startsWith('/user/forum') }" @click="goTo('/user/forum')">
            <i class="el-icon-chat-dot-square"></i>
            <span>论坛</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/cart' }" @click="goTo('/user/cart')">
            <i class="el-icon-shopping-cart-2"></i>
            <span>购物车</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/order-list' }" @click="goTo('/user/order-list')">
            <i class="el-icon-s-order"></i>
            <span>我的订单</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/review-list' }" @click="goTo('/user/review-list')">
            <i class="el-icon-chat-line-square"></i>
            <span>我的评价</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/profile' }" @click="goTo('/user/profile')">
            <i class="el-icon-user"></i>
            <span>个人中心</span>
          </div>
          <div class="nav-item" :class="{ active: $route.path === '/user/change-password' }" @click="goTo('/user/change-password')">
            <i class="el-icon-lock"></i>
            <span>修改密码</span>
          </div>
        </div>
        
        <div class="user-info">
          <el-avatar :src="userInfo.avatar || defaultAvatar" :size="40"></el-avatar>
          <span class="username">{{ userInfo.nickname }}</span>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人资料</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    
    <!-- 移动端侧边导航抽屉 -->
    <el-drawer
      :visible.sync="drawerVisible"
      direction="ltr"
      size="280px"
      :with-header="false"
      custom-class="mobile-nav-drawer">
      <div class="drawer-content">
        <!-- 用户信息 -->
        <div class="drawer-user-info">
          <el-avatar :src="userInfo.avatar || defaultAvatar" :size="60"></el-avatar>
          <div class="drawer-username">{{ userInfo.nickname }}</div>
        </div>
        
        <!-- 导航菜单 -->
        <div class="drawer-nav-menu">
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/home' }" @click="goToMobile('/user/home')">
            <i class="el-icon-s-home"></i>
            <span>首页</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path.startsWith('/user/product') }" @click="goToMobile('/user/product-list')">
            <i class="el-icon-shopping-bag-2"></i>
            <span>商品一览</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/insurance-list' }" @click="goToMobile('/user/insurance-list')">
            <i class="el-icon-s-home"></i>
            <span>宠物保险</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/policy-list' }" @click="goToMobile('/user/policy-list')">
            <i class="el-icon-document-checked"></i>
            <span>我的保单</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/claim-list' }" @click="goToMobile('/user/claim-list')">
            <i class="el-icon-warning-outline"></i>
            <span>申保记录</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path.startsWith('/user/hospital') }" @click="goToMobile('/user/hospital-list')">
            <i class="el-icon-office-building"></i>
            <span>宠物医院</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/appointment-list' }" @click="goToMobile('/user/appointment-list')">
            <i class="el-icon-s-order"></i>
            <span>预约记录</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path.startsWith('/user/breeding') }" @click="goToMobile('/user/breeding-list')">
            <i class="el-icon-search"></i>
            <span>寻找配种</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path.startsWith('/user/foster') }" @click="goToMobile('/user/foster-list')">
            <i class="el-icon-house"></i>
            <span>宠物寄养</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path.startsWith('/user/adoption') }" @click="goToMobile('/user/adoption-list')">
            <i class="el-icon-present"></i>
            <span>宠物领养</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path.startsWith('/user/forum') }" @click="goToMobile('/user/forum')">
            <i class="el-icon-chat-dot-square"></i>
            <span>论坛</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/cart' }" @click="goToMobile('/user/cart')">
            <i class="el-icon-shopping-cart-2"></i>
            <span>购物车</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/order-list' }" @click="goToMobile('/user/order-list')">
            <i class="el-icon-s-order"></i>
            <span>我的订单</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/review-list' }" @click="goToMobile('/user/review-list')">
            <i class="el-icon-chat-line-square"></i>
            <span>我的评价</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/profile' }" @click="goToMobile('/user/profile')">
            <i class="el-icon-user"></i>
            <span>个人中心</span>
          </div>
          <div class="drawer-nav-item" :class="{ active: $route.path === '/user/change-password' }" @click="goToMobile('/user/change-password')">
            <i class="el-icon-lock"></i>
            <span>修改密码</span>
          </div>
        </div>
        
        <!-- 退出按钮 -->
        <div class="drawer-logout-btn" @click="handleMobileLogout">
          <i class="el-icon-switch-button"></i>
          <span>退出登录</span>
        </div>
      </div>
    </el-drawer>
    
    <!-- 子页面内容 -->
    <div class="main-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import { getSystemConfig } from '@/api/systemConfig';

export default {
  name: 'UserLayout',
  data() {
    return {
      userInfo: {},
      systemName: '伴侣宠物之家',
      defaultAvatar: 'https://img1.baidu.com/it/u=1666852494,3843174075&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
      drawerVisible: false
    };
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
            document.title = config.systemName;
          }
        }
      }).catch(error => {
        console.error('获取系统配置失败', error);
      });
    },
    goTo(path) {
      if (this.$route.path !== path) {
        this.$router.push(path);
      }
    },
    goToMobile(path) {
      this.drawerVisible = false;
      if (this.$route.path !== path) {
        this.$router.push(path);
      }
    },
    handleCommand(command) {
      if (command === 'logout') {
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
      } else if (command === 'profile') {
        this.$router.push('/user/profile');
      } else if (command === 'settings') {
        this.$notify({
          title: '提示',
          message: '系统设置功能开发中',
          type: 'info',
          duration: 2000
        });
      }
    },
    handleMobileLogout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.drawerVisible = false;
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
.user-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 顶部导航栏 */
.top-navbar {
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar-content {
  width: 100%;
  padding: 0 30px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
  flex-shrink: 0;
}

.pet-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
}

.nav-menu {
  display: flex;
  gap: 6px;
  flex: 1;
  justify-content: center;
  max-width: 1200px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  color: rgba(255, 255, 255, 0.8);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 13px;
  white-space: nowrap;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  font-weight: 600;
}

.nav-item i {
  font-size: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  color: white;
  flex-shrink: 0;
}

.username {
  font-size: 15px;
  font-weight: 500;
}

.el-dropdown-link {
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
}

/* 主体内容 */
.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px;
}

/* 汉堡菜单按钮 (默认隐藏，仅在移动端显示) */
.mobile-menu-btn {
  display: none;
  align-items: center;
  justify-content: center;
  width: 45px;
  height: 45px;
  color: white;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.3s;
  margin-left: auto;
}

.mobile-menu-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.mobile-menu-btn i {
  font-size: 28px;
}

/* 移动端抽屉样式 */
.drawer-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #2c5f2d 0%, #97bc62 100%);
}

.drawer-user-info {
  padding: 40px 20px 30px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.drawer-username {
  color: white;
  font-size: 18px;
  font-weight: 600;
  margin-top: 15px;
}

.drawer-nav-menu {
  flex: 1;
  padding: 20px 0;
  overflow-y: auto;
}

.drawer-nav-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 25px;
  color: rgba(255, 255, 255, 0.9);
  cursor: pointer;
  transition: all 0.3s;
  font-size: 16px;
}

.drawer-nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.drawer-nav-item.active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  font-weight: 600;
  border-left: 4px solid white;
}

.drawer-nav-item i {
  font-size: 20px;
  width: 24px;
  text-align: center;
}

.drawer-logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 20px;
  margin: 20px;
  background: rgba(255, 255, 255, 0.15);
  color: white;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 16px;
  font-weight: 600;
}

.drawer-logout-btn:hover {
  background: rgba(255, 255, 255, 0.25);
}

.drawer-logout-btn i {
  font-size: 20px;
}

/* 响应式设计 */
@media (max-width: 1800px) {
  .logo-text {
    font-size: 18px;
  }
  
  .nav-menu {
    gap: 4px;
    max-width: 1100px;
  }
  
  .nav-item {
    font-size: 12px;
    padding: 7px 10px;
    gap: 5px;
  }
  
  .nav-item i {
    font-size: 15px;
  }
  
  .username {
    font-size: 14px;
  }
}

@media (max-width: 1600px) {
  .navbar-content {
    padding: 0 20px;
  }
  
  .logo-text {
    font-size: 16px;
  }
  
  .pet-icon {
    font-size: 24px;
  }
  
  .nav-menu {
    gap: 3px;
  }
  
  .nav-item {
    font-size: 10px;
    padding: 5px 6px;
    gap: 3px;
  }
  
  .nav-item i {
    font-size: 13px;
  }
  
  .username {
    font-size: 12px;
  }
}

@media (max-width: 1400px) {
  .navbar-content {
    padding: 0 15px;
  }
  
  .logo-text {
    font-size: 15px;
  }
  
  .pet-icon {
    font-size: 22px;
  }
  
  .nav-menu {
    gap: 2px;
  }
  
  .nav-item {
    font-size: 9px;
    padding: 4px 5px;
    gap: 2px;
  }
  
  .nav-item i {
    font-size: 12px;
  }
  
  .username {
    display: none;
  }
}

@media (max-width: 1200px) {
  .navbar-content {
    padding: 0 10px;
  }
  
  .logo-text {
    font-size: 14px;
  }
  
  .pet-icon {
    font-size: 20px;
  }
  
  .nav-menu {
    gap: 1px;
  }
  
  .nav-item {
    font-size: 8px;
    padding: 3px 4px;
    gap: 1px;
  }
  
  .nav-item i {
    font-size: 11px;
  }
  
  .username {
    display: none;
  }
}

@media (max-width: 992px) {
  .navbar-content {
    padding: 0 10px;
    height: 60px;
  }
  
  .logo-text {
    font-size: 13px;
  }
  
  .pet-icon {
    font-size: 18px;
  }
  
  .nav-menu {
    gap: 1px;
  }
  
  .nav-item {
    font-size: 7px;
    padding: 2px 3px;
  }
  
  .nav-item i {
    font-size: 10px;
  }
}

@media (max-width: 768px) {
  .navbar-content {
    padding: 0 15px;
    height: 60px;
  }
  
  .nav-menu {
    display: none;
  }
  
  .mobile-menu-btn {
    display: flex;
  }
  
  .user-info {
    display: none;
  }
  
  .main-content {
    padding: 20px 15px;
  }
}

@media (max-width: 480px) {
  .navbar-content {
    padding: 0 10px;
  }
  
  .logo-text {
    font-size: 16px;
  }
  
  .pet-icon {
    font-size: 22px;
  }
  
  .mobile-menu-btn {
    width: 40px;
    height: 40px;
  }
  
  .mobile-menu-btn i {
    font-size: 24px;
  }
  
  .main-content {
    padding: 15px 10px;
  }
}
</style>

