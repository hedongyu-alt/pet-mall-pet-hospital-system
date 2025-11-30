// pages/claim-list/claim-list.js
const app = getApp();
const util = require('../../utils/util.js');

Page({
  data: {
    claimList: [],
    searchStatus: ''
  },

  onLoad() {
    this.loadClaimList();
  },

  onShow() {
    this.loadClaimList();
  },

  // 按状态筛选
  filterByStatus(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({
      searchStatus: status
    });
    this.loadClaimList();
  },

  loadClaimList() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      return;
    }

    app.showLoading();
    
    const data = {
      userId: userInfo.id,
      page: 1,
      size: 100
    };
    
    // 如果有状态筛选
    if (this.data.searchStatus) {
      data.status = this.data.searchStatus;
    }

    app.request({
      url: '/insurance-claim/user/list',
      method: 'GET',
      data: data
    }).then(res => {
      if (res.success) {
        // 格式化数据
        const claimList = (res.data.records || []).map(item => {
          return {
            ...item,
            createTime: util.formatDateTime(item.createTime),
            images: item.evidenceImages ? item.evidenceImages.split(',').filter(img => img.trim()) : []
          };
        });
        this.setData({
          claimList: claimList
        });
      }
    }).catch(err => {
      app.showToast('加载失败');
      console.error(err);
    }).finally(() => {
      app.hideLoading();
    });
  },

  // 获取状态样式类
  getStatusClass(status) {
    const classMap = {
      '待审核': 'warning',
      '审核通过': 'success',
      '审核拒绝': 'danger',
      '已打款': 'success'
    };
    return classMap[status] || 'warning';
  },

  // 预览图片
  previewImage(e) {
    const images = e.currentTarget.dataset.images;
    const current = e.currentTarget.dataset.current;
    wx.previewImage({
      urls: images,
      current: current
    });
  }
});


