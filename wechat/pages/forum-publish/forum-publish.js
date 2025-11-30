// pages/forum-publish/forum-publish.js
const app = getApp();

Page({
  data: {
    formData: {
      title: '',
      content: '',
      keywords: ''
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
        wx.redirectTo({
          url: '/pages/login/login'
        });
      }, 1500);
      return;
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
    const maxCount = 9 - this.data.imageList.length;
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
        title: '上传失败',
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
    const { formData } = this.data;
    
    if (!formData.title.trim()) {
      wx.showToast({ title: '请输入帖子标题', icon: 'none' });
      return false;
    }
    if (formData.title.trim().length < 5) {
      wx.showToast({ title: '标题至少5个字符', icon: 'none' });
      return false;
    }
    if (!formData.content.trim()) {
      wx.showToast({ title: '请输入帖子内容', icon: 'none' });
      return false;
    }
    if (formData.content.trim().length < 10) {
      wx.showToast({ title: '内容至少10个字符', icon: 'none' });
      return false;
    }
    
    return true;
  },

  // 提交表单
  handleSubmit() {
    if (!this.validateForm()) {
      return;
    }

    const { formData, imageList } = this.data;
    const userInfo = wx.getStorageSync('userInfo');
    
    const submitData = {
      title: formData.title.trim(),
      description: formData.content.trim().substring(0, 100),
      content: formData.content.trim(),
      keywords: formData.keywords.trim(),
      images: imageList.join(',')
    };

    app.showLoading('发布中...');
    app.request({
      url: `/forum/posts?userId=${userInfo.id}`,
      method: 'POST',
      data: submitData
    }).then(res => {
      if (res.code === 200 || res.success) {
        wx.showToast({
          title: '发布成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '发布失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.showToast({
        title: '发布失败',
        icon: 'none'
      });
    }).finally(() => {
      app.hideLoading();
    });
  }
});

