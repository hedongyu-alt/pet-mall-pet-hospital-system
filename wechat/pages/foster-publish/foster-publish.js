// pages/foster-publish/foster-publish.js
const app = getApp();

Page({
  data: {
    formData: {
      title: '',
      petName: '',
      petType: '',
      petGender: '',
      breed: '',
      health: '',
      description: '',
      contact: '',
      location: ''
    },
    petTypes: ['狗', '猫', '其他'],
    petTypeIndex: -1,
    genders: ['公', '母'],
    genderIndex: -1,
    imageList: [],
    isEdit: false,
    postId: null
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

    // 检查是否为编辑模式
    if (options.id && options.mode === 'edit') {
      this.setData({
        isEdit: true,
        postId: options.id
      });
      wx.setNavigationBarTitle({
        title: '编辑寄养帖子'
      });
      this.loadFosterData(options.id);
    } else {
      wx.setNavigationBarTitle({
        title: '发布寄养帖子'
      });
    }
  },

  // 加载寄养数据（编辑模式）
  loadFosterData(id) {
    app.showLoading();
    app.request({
      url: `/foster/user/detail/${id}`,
      method: 'GET'
    }).then(res => {
      if (res.success && res.data) {
        const data = res.data;
        this.setData({
          formData: {
            title: data.title || '',
            petName: data.petName || '',
            petType: data.petType || '',
            petGender: data.petGender || '',
            breed: data.breed || '',
            health: data.vaccineStatus || '',
            description: data.description || '',
            contact: data.ownerInfo || '',
            location: data.location || ''
          },
          petTypeIndex: this.data.petTypes.indexOf(data.petType),
          genderIndex: this.data.genders.indexOf(data.petGender),
          imageList: data.photos ? data.photos.split(',') : []
        });
      } else {
        app.showToast('加载失败');
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }
    }).catch(err => {
      app.showToast('加载失败');
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }).finally(() => {
      app.hideLoading();
    });
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

  // 性别选择
  onGenderChange(e) {
    const index = e.detail.value;
    this.setData({
      genderIndex: index,
      'formData.petGender': this.data.genders[index]
    });
  },

  // 选择图片
  chooseImage() {
    const maxCount = 6 - this.data.imageList.length;
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
    const { formData, imageList } = this.data;
    
    if (!formData.title.trim()) {
      wx.showToast({ title: '请输入标题', icon: 'none' });
      return false;
    }
    if (!formData.petName.trim()) {
      wx.showToast({ title: '请输入宠物名称', icon: 'none' });
      return false;
    }
    if (!formData.petType) {
      wx.showToast({ title: '请选择宠物类型', icon: 'none' });
      return false;
    }
    if (!formData.petGender) {
      wx.showToast({ title: '请选择宠物性别', icon: 'none' });
      return false;
    }
    if (!formData.breed.trim()) {
      wx.showToast({ title: '请输入品种', icon: 'none' });
      return false;
    }
    if (!formData.health.trim()) {
      wx.showToast({ title: '请输入疫苗情况', icon: 'none' });
      return false;
    }
    if (!formData.description.trim()) {
      wx.showToast({ title: '请输入详细描述', icon: 'none' });
      return false;
    }
    if (!formData.contact.trim()) {
      wx.showToast({ title: '请输入联系方式', icon: 'none' });
      return false;
    }
    if (!formData.location.trim()) {
      wx.showToast({ title: '请输入地址', icon: 'none' });
      return false;
    }
    if (imageList.length === 0) {
      wx.showToast({ title: '请至少上传一张照片', icon: 'none' });
      return false;
    }
    
    return true;
  },

  // 提交表单
  handleSubmit() {
    if (!this.validateForm()) {
      return;
    }

    const { formData, imageList, isEdit, postId } = this.data;
    const userInfo = wx.getStorageSync('userInfo');
    
    const submitData = {
      title: formData.title,
      petName: formData.petName,
      petType: formData.petType,
      petGender: formData.petGender,
      breed: formData.breed,
      startDate: formData.startDate,
      endDate: formData.endDate,
      vaccineStatus: formData.health,
      description: formData.description,
      ownerInfo: formData.contact,
      location: formData.location,
      photos: imageList.join(','),
      userId: userInfo.id,
      status: '寻找中'
    };

    // 编辑模式需要添加ID
    if (isEdit) {
      submitData.id = postId;
      submitData.currentUserId = userInfo.id;
    }

    const loadingText = isEdit ? '更新中...' : '发布中...';
    const successText = isEdit ? '更新成功' : '发布成功';
    const failText = isEdit ? '更新失败' : '发布失败';
    const apiUrl = isEdit ? '/foster/user/update' : '/foster/user/add';

    app.showLoading(loadingText);
    app.request({
      url: apiUrl,
      method: 'POST',
      data: submitData
    }).then(res => {
      if (res.success) {
        wx.showToast({
          title: successText,
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || failText,
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.showToast({
        title: failText,
        icon: 'none'
      });
    }).finally(() => {
      app.hideLoading();
    });
  }
});

