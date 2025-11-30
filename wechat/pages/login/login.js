// pages/login/login.js
const app = getApp();

Page({
  data: {
    username: '',
    password: ''
  },

  onLoad() {
    // 页面加载完成
  },


  // 用户名输入
  onUsernameInput(e) {
    this.setData({
      username: e.detail.value
    });
  },

  // 密码输入
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  // 登录
  handleLogin() {
    const { username, password } = this.data;

    // 表单验证
    if (!username) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      });
      return;
    }

    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      });
      return;
    }

    app.showLoading('登录中...');

    // 发送登录请求
    app.request({
      url: '/user/wechat/login',
      method: 'POST',
      data: {
        username: username,
        password: password,
        role: '普通用户'
      }
    }).then(res => {
      if (res.success && res.data) {
        // 保存用户信息和token
        wx.setStorageSync('userInfo', res.data);
        if (res.data.token) {
          wx.setStorageSync('token', res.data.token);
        }
        app.globalData.userInfo = res.data;
        
        wx.showToast({
          title: '登录成功',
          icon: 'success'
        });
        
        // 跳转到首页
        setTimeout(() => {
          wx.switchTab({
            url: '/pages/home/home'
          });
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '登录失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.showToast({
        title: '登录失败，请稍后重试',
        icon: 'none'
      });
      console.error('登录失败', err);
    }).finally(() => {
      app.hideLoading();
    });
  },

  // 跳转注册
  goRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  }
});


