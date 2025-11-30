// pages/insurance-list/insurance-list.js
const app = getApp();

Page({
  data: {
    insuranceList: []
  },

  onLoad() {
    this.loadInsuranceList();
  },

  loadInsuranceList() {
    app.showLoading();
    app.request({
      url: '/insurance/user/list',
      method: 'GET',
      data: { page: 1, size: 100 }
    }).then(res => {
      if (res.success) {
        this.setData({
          insuranceList: res.data.records || []
        });
      }
    }).catch(err => {
      app.showToast('加载失败');
      console.error(err);
    }).finally(() => {
      app.hideLoading();
    });
  },

  viewDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/insurance-detail/insurance-detail?id=${id}`
    });
  }
});


