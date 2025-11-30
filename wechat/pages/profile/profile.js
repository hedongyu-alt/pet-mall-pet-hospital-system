// pages/profile/profile.js
const app = getApp();

Page({
  data: {
    isLogin: false,
    userInfo: {},
    defaultAvatar: 'https://img1.baidu.com/it/u=1666852494,3843174075&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500'
  },

  onLoad() {
    this.loadUserInfo();
  },

  onShow() {
    this.loadUserInfo();
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        isLogin: true,
        userInfo: userInfo
      });
    } else {
      this.setData({
        isLogin: false,
        userInfo: {}
      });
    }
  },

  // 跳转到登录页
  goLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 跳转页面
  navigateTo(e) {
    const url = e.currentTarget.dataset.url;
    
    // 宠物医院、商品、保险列表等可以不登录访问
    const noLoginPages = [
      '/pages/hospital-list/hospital-list',
      '/pages/product-list/product-list',
      '/pages/insurance-list/insurance-list',
      '/pages/breeding-list/breeding-list',
      '/pages/foster-list/foster-list',
      '/pages/adoption-list/adoption-list'
    ];
    
    // 判断是否需要登录
    const needLogin = !noLoginPages.includes(url);
    
    if (needLogin && !this.data.isLogin) {
      app.showToast('请先登录');
      this.goLogin();
      return;
    }

    wx.navigateTo({ url });
  },

  // 退出登录
  handleLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('userInfo');
          app.globalData.userInfo = null;
          this.setData({
            isLogin: false,
            userInfo: {}
          });
          app.showToast('已退出登录', 'success');
        }
      }
    });
  }
});


