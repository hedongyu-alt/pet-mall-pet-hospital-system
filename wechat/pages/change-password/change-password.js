// pages/change-password/change-password.js
const app = getApp();

Page({
  data: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  },

  onOldPasswordInput(e) {
    this.setData({ oldPassword: e.detail.value });
  },

  onNewPasswordInput(e) {
    this.setData({ newPassword: e.detail.value });
  },

  onConfirmPasswordInput(e) {
    this.setData({ confirmPassword: e.detail.value });
  },

  handleSubmit() {
    const { oldPassword, newPassword, confirmPassword } = this.data;

    // 表单验证
    if (!oldPassword) {
      app.showToast('请输入原密码');
      return;
    }

    if (!newPassword) {
      app.showToast('请输入新密码');
      return;
    }

    if (newPassword.length < 6 || newPassword.length > 20) {
      app.showToast('新密码长度为6-20位');
      return;
    }

    if (!confirmPassword) {
      app.showToast('请再次输入新密码');
      return;
    }

    if (newPassword !== confirmPassword) {
      app.showToast('两次输入密码不一致');
      return;
    }

    if (oldPassword === newPassword) {
      app.showToast('新密码不能与原密码相同');
      return;
    }

    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      app.showToast('请先登录');
      wx.redirectTo({
        url: '/pages/login/login'
      });
      return;
    }

    app.showLoading('修改中...');

    // 发送修改密码请求
    app.request({
      url: '/user/changePassword',
      method: 'POST',
      data: {
        userId: userInfo.id.toString(),
        oldPassword: oldPassword,
        newPassword: newPassword
      }
    }).then(res => {
      if (res.success) {
        wx.showModal({
          title: '成功',
          content: '密码修改成功，请重新登录',
          showCancel: false,
          success: () => {
            // 清除用户信息
            wx.removeStorageSync('userInfo');
            app.globalData.userInfo = null;

            // 跳转到登录页
            wx.redirectTo({
              url: '/pages/login/login'
            });
          }
        });
      } else {
        app.showToast(res.message || '修改失败');
      }
    }).catch(err => {
      app.showToast('修改失败，请稍后重试');
      console.error('修改密码失败', err);
    }).finally(() => {
      app.hideLoading();
    });
  }
});


