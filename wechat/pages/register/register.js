// pages/register/register.js
const app = getApp();

Page({
  data: {
    username: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    phone: '',
    email: '',
    usernameChecked: false,
    usernameExists: false
  },

  onLoad() {
    // 页面加载完成
  },


  // 输入事件
  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
    // 检查用户名是否可用
    if (e.detail.value.length >= 4) {
      this.checkUsername(e.detail.value);
    }
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  onConfirmPasswordInput(e) {
    this.setData({ confirmPassword: e.detail.value });
  },

  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value });
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  onEmailInput(e) {
    this.setData({ email: e.detail.value });
  },

  // 检查用户名
  checkUsername(username) {
    app.request({
      url: '/user/checkUsername',
      method: 'GET',
      data: { username: username }
    }).then(res => {
      this.setData({
        usernameChecked: true,
        usernameExists: res.exists || false
      });
    }).catch(err => {
      console.error('检查用户名失败', err);
    });
  },

  // 注册
  handleRegister() {
    const { username, password, confirmPassword, nickname, phone, email, usernameExists } = this.data;

    // 表单验证
    if (!username) {
      app.showToast('请输入用户名');
      return;
    }

    if (username.length < 4 || username.length > 20) {
      app.showToast('用户名长度为4-20位');
      return;
    }

    if (usernameExists) {
      app.showToast('用户名已存在，请更换');
      return;
    }

    if (!password) {
      app.showToast('请输入密码');
      return;
    }

    if (password.length < 6 || password.length > 20) {
      app.showToast('密码长度为6-20位');
      return;
    }

    if (!confirmPassword) {
      app.showToast('请再次输入密码');
      return;
    }

    if (password !== confirmPassword) {
      app.showToast('两次输入密码不一致');
      return;
    }

    if (!nickname) {
      app.showToast('请输入昵称');
      return;
    }

    if (phone && !/^1[3-9]\d{9}$/.test(phone)) {
      app.showToast('手机号格式不正确');
      return;
    }

    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      app.showToast('邮箱格式不正确');
      return;
    }

    app.showLoading('注册中...');

    // 发送注册请求
    app.request({
      url: '/user/wechat/register',
      method: 'POST',
      data: {
        username: username,
        password: password,
        nickname: nickname,
        phone: phone || null,
        email: email || null
      }
    }).then(res => {
      if (res.success) {
        app.showToast('注册成功，即将跳转到登录页面', 'success');
        
        // 延迟跳转
        setTimeout(() => {
          wx.redirectTo({
            url: '/pages/login/login'
          });
        }, 1500);
      } else {
        app.showToast(res.message || '注册失败');
      }
    }).catch(err => {
      app.showToast('注册失败，请稍后重试');
      console.error('注册失败', err);
    }).finally(() => {
      app.hideLoading();
    });
  },

  // 跳转登录
  goToLogin() {
    wx.redirectTo({
      url: '/pages/login/login'
    });
  }
});


