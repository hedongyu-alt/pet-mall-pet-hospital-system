// pages/policy-list/policy-list.js
const app = getApp();
const util = require('../../utils/util.js');

Page({
  data: {
    policyList: []
  },

  onLoad() {
    this.loadPolicyList();
  },

  onShow() {
    this.loadPolicyList();
  },

  loadPolicyList() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      return;
    }

    app.showLoading();
    app.request({
      url: '/insurance-order/user/list',
      method: 'GET',
      data: {
        userId: userInfo.id,
        page: 1,
        size: 100
      }
    }).then(res => {
      if (res.success) {
        // 格式化日期
        const policyList = (res.data.records || []).map(item => {
          return {
            ...item,
            startDate: util.formatDate(item.startDate),
            endDate: util.formatDate(item.endDate)
          };
        });
        this.setData({
          policyList: policyList
        });
      }
    }).catch(err => {
      app.showToast('加载失败');
      console.error(err);
    }).finally(() => {
      app.hideLoading();
    });
  },

  makeClaim(e) {
    const id = e.currentTarget.dataset.id;
    const policy = this.data.policyList.find(p => p.id === id);
    
    if (!policy) return;
    
    // 跳转到理赔申请页面，传递保单信息
    wx.navigateTo({
      url: `/pages/claim-create/claim-create?orderId=${policy.id}&insuranceName=${encodeURIComponent(policy.insuranceName)}&petName=${encodeURIComponent(policy.petName)}&petType=${encodeURIComponent(policy.petType)}`
    });
  }
});


