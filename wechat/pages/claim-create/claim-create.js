// pages/claim-create/claim-create.js
const app = getApp();

Page({
  data: {
    orderId: '',
    insuranceName: '',
    petName: '',
    petType: '',
    formData: {
      reason: '',
      description: ''
    },
    imageList: []
  },

  onLoad(options) {
    // 检查登录状态
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/login/login'
        });
      }, 1500);
      return;
    }

    // 获取保单信息
    if (options.orderId) {
      this.setData({
        orderId: options.orderId,
        insuranceName: options.insuranceName ? decodeURIComponent(options.insuranceName) : '',
        petName: options.petName ? decodeURIComponent(options.petName) : '',
        petType: options.petType ? decodeURIComponent(options.petType) : ''
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

  // 选择图片
  chooseImage() {
    const maxCount = 5 - this.data.imageList.length;
    wx.chooseImage({
      count: maxCount,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePaths = res.tempFilePaths;
        this.uploadImages(tempFilePaths);
      }
    });
  },

  // 上传图片
  uploadImages(filePaths) {
    app.showLoading('上传中...');
    const uploadPromises = filePaths.map(filePath => {
      return new Promise((resolve, reject) => {
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
                resolve(data.data);
              } else {
                reject(data.message);
              }
            } catch (e) {
              reject('上传失败');
            }
          },
          fail: reject
        });
      });
    });

    Promise.all(uploadPromises).then(urls => {
      this.setData({
        imageList: this.data.imageList.concat(urls)
      });
      wx.showToast({
        title: '上传成功',
        icon: 'success'
      });
    }).catch(err => {
      wx.showToast({
        title: err || '上传失败',
        icon: 'none'
      });
    }).finally(() => {
      app.hideLoading();
    });
  },

  // 删除图片
  deleteImage(e) {
    const index = e.currentTarget.dataset.index;
    const imageList = this.data.imageList;
    imageList.splice(index, 1);
    this.setData({
      imageList: imageList
    });
  },

  // 表单验证
  validateForm() {
    const { formData, imageList } = this.data;
    
    if (!formData.reason.trim()) {
      wx.showToast({ title: '请输入申请原因', icon: 'none' });
      return false;
    }
    if (!formData.description.trim()) {
      wx.showToast({ title: '请输入情况描述', icon: 'none' });
      return false;
    }
    if (imageList.length === 0) {
      wx.showToast({ title: '请至少上传一张佐证图片', icon: 'none' });
      return false;
    }
    
    return true;
  },

  // 提交理赔申请
  handleSubmit() {
    if (!this.validateForm()) {
      return;
    }

    const { formData, imageList, orderId } = this.data;
    const userInfo = wx.getStorageSync('userInfo');
    
    const submitData = {
      userId: userInfo.id,
      orderId: orderId,
      reason: formData.reason,
      description: formData.description,
      evidenceImages: imageList.join(',')
    };

    app.showLoading('提交中...');
    app.request({
      url: '/insurance-claim/submit',
      method: 'POST',
      data: submitData
    }).then(res => {
      if (res.success) {
        wx.showToast({
          title: '申请已提交',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '提交失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.showToast({
        title: '提交失败',
        icon: 'none'
      });
    }).finally(() => {
      app.hideLoading();
    });
  }
});

