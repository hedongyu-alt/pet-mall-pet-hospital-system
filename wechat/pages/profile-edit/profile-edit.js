// pages/profile-edit/profile-edit.js
const app = getApp();

Page({
  data: {
    formData: {
      nickname: '',
      avatar: '',
      phone: '',
      email: '',
      gender: '',
      address: ''
    },
    genders: ['男', '女', '保密'],
    genderIndex: -1,
    defaultAvatar: 'https://img1.baidu.com/it/u=1666852494,3843174075&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500'
  },

  onLoad() {
    // 检查登录状态
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      setTimeout(() => {
        wx.redirectTo({
          url: '/pages/login/login'
        });
      }, 1500);
      return;
    }

    this.loadUserInfo();
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      app.showLoading();
      app.request({
        url: '/user/getUserInfo',
        method: 'GET',
        data: { userId: userInfo.id }
      }).then(res => {
        if (res.success && res.data) {
          const data = res.data;
          this.setData({
            formData: {
              nickname: data.nickname || '',
              avatar: data.avatar || '',
              phone: data.phone || '',
              email: data.email || '',
              gender: data.gender || '',
              address: data.address || ''
            },
            genderIndex: this.data.genders.indexOf(data.gender)
          });
        }
      }).catch(err => {
        wx.showToast({
          title: '加载失败',
          icon: 'none'
        });
      }).finally(() => {
        app.hideLoading();
      });
    }
  },

  // 输入框变化
  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [`formData.${field}`]: value
    });
  },

  // 性别选择
  onGenderChange(e) {
    const index = e.detail.value;
    this.setData({
      genderIndex: index,
      'formData.gender': this.data.genders[index]
    });
  },

  // 上传头像
  uploadAvatar() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0];
        this.doUploadAvatar(tempFilePath);
      }
    });
  },

  // 执行头像上传
  doUploadAvatar(filePath) {
    app.showLoading('上传中...');
    
    wx.uploadFile({
      url: app.globalData.baseUrl + '/file/upload',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': wx.getStorageSync('token') || ''
      },
      success: (res) => {
        try {
          const data = JSON.parse(res.data);
          if (data.success) {
            this.setData({
              'formData.avatar': data.data
            });
            wx.showToast({
              title: '上传成功',
              icon: 'success'
            });
          } else {
            wx.showToast({
              title: data.message || '上传失败',
              icon: 'none'
            });
          }
        } catch (e) {
          wx.showToast({
            title: '上传失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
      },
      complete: () => {
        app.hideLoading();
      }
    });
  },

  // 表单验证
  validateForm() {
    const { formData } = this.data;
    
    if (!formData.nickname.trim()) {
      wx.showToast({ title: '请输入昵称', icon: 'none' });
      return false;
    }
    
    if (formData.email && !this.isValidEmail(formData.email)) {
      wx.showToast({ title: '请输入正确的邮箱格式', icon: 'none' });
      return false;
    }
    
    if (formData.phone && !this.isValidPhone(formData.phone)) {
      wx.showToast({ title: '请输入正确的手机号格式', icon: 'none' });
      return false;
    }
    
    return true;
  },

  // 验证邮箱格式
  isValidEmail(email) {
    const reg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    return reg.test(email);
  },

  // 验证手机号格式
  isValidPhone(phone) {
    const reg = /^1[3-9]\d{9}$/;
    return reg.test(phone);
  },

  // 提交表单
  handleSubmit() {
    if (!this.validateForm()) {
      return;
    }

    const { formData } = this.data;
    const userInfo = wx.getStorageSync('userInfo');
    
    const submitData = {
      id: userInfo.id,
      nickname: formData.nickname.trim(),
      avatar: formData.avatar,
      phone: formData.phone.trim(),
      email: formData.email.trim(),
      gender: formData.gender,
      address: formData.address.trim()
    };

    app.showLoading('保存中...');
    app.request({
      url: '/user/updateUserInfo',
      method: 'POST',
      data: submitData
    }).then(res => {
      if (res.success) {
        // 更新本地存储的用户信息
        const updatedUserInfo = {
          ...userInfo,
          nickname: submitData.nickname,
          avatar: submitData.avatar,
          phone: submitData.phone,
          email: submitData.email,
          gender: submitData.gender,
          address: submitData.address
        };
        wx.setStorageSync('userInfo', updatedUserInfo);

        wx.showToast({
          title: '保存成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '保存失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.showToast({
        title: '保存失败',
        icon: 'none'
      });
    }).finally(() => {
      app.hideLoading();
    });
  }
});

