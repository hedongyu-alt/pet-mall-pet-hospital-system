// pages/appointment-create/appointment-create.js
const app = getApp();

Page({
  data: {
    hospitalId: '',
    hospitalName: '',
    formData: {
      petName: '',
      petType: '',
      conditionDescription: '',
      appointmentDate: '',
      appointmentTime: '',
      contactPhone: ''
    },
    petTypes: ['狗', '猫'],
    petTypeIndex: -1,
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

    // 获取医院信息
    if (options.hospitalId && options.hospitalName) {
      this.setData({
        hospitalId: options.hospitalId,
        hospitalName: decodeURIComponent(options.hospitalName)
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

  // 宠物类型选择
  onPetTypeChange(e) {
    const index = e.detail.value;
    this.setData({
      petTypeIndex: index,
      'formData.petType': this.data.petTypes[index]
    });
  },

  // 日期选择
  onDateChange(e) {
    this.setData({
      'formData.appointmentDate': e.detail.value
    });
  },

  // 时间选择
  onTimeChange(e) {
    this.setData({
      'formData.appointmentTime': e.detail.value
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
    
    if (!formData.petName.trim()) {
      wx.showToast({ title: '请输入宠物名称', icon: 'none' });
      return false;
    }
    if (!formData.petType) {
      wx.showToast({ title: '请选择宠物类型', icon: 'none' });
      return false;
    }
    if (!formData.conditionDescription.trim()) {
      wx.showToast({ title: '请描述病情', icon: 'none' });
      return false;
    }
    if (!formData.appointmentDate) {
      wx.showToast({ title: '请选择预约日期', icon: 'none' });
      return false;
    }
    if (!formData.appointmentTime) {
      wx.showToast({ title: '请选择预约时间', icon: 'none' });
      return false;
    }
    if (!formData.contactPhone.trim()) {
      wx.showToast({ title: '请输入联系电话', icon: 'none' });
      return false;
    }
    
    // 验证手机号格式
    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(formData.contactPhone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' });
      return false;
    }
    
    return true;
  },

  // 提交预约
  handleSubmit() {
    if (!this.validateForm()) {
      return;
    }

    const { formData, imageList, hospitalId, hospitalName } = this.data;
    const userInfo = wx.getStorageSync('userInfo');
    
    const submitData = {
      userId: userInfo.id,
      hospitalId: hospitalId,
      hospitalName: hospitalName,
      petName: formData.petName,
      petType: formData.petType,
      conditionDescription: formData.conditionDescription,
      images: imageList.join(','),
      appointmentDate: formData.appointmentDate,
      appointmentTime: formData.appointmentTime,
      contactPhone: formData.contactPhone,
      status: '待赴约'
    };

    app.showLoading('提交中...');
    app.request({
      url: '/appointment/add',
      method: 'POST',
      data: submitData
    }).then(res => {
      if (res.success) {
        wx.showToast({
          title: '预约成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '预约失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.showToast({
        title: '预约失败',
        icon: 'none'
      });
    }).finally(() => {
      app.hideLoading();
    });
  }
});

