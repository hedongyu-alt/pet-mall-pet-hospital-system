// pages/insurance-detail/insurance-detail.js
const app = getApp();

Page({
  data: {
    insuranceId: null,
    insurance: {},
    showPurchaseModal: false,
    purchaseForm: {
      petName: '',
      petType: '',
      petAge: '',
      petBreed: '',
      payeeName: '',
      payeePhone: '',
      payeeAccount: '',
      startDate: ''
    }
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ insuranceId: options.id });
      this.loadInsuranceDetail(options.id);
    }
  },

  loadInsuranceDetail(id) {
    app.showLoading();
    app.request({
      url: `/insurance/user/detail/${id}`,
      method: 'GET'
    }).then(res => {
      if (res.success && res.data) {
        this.setData({ insurance: res.data });
      }
    }).catch(err => {
      app.showToast('加载失败');
      console.error(err);
    }).finally(() => {
      app.hideLoading();
    });
  },

  // 显示购买弹窗
  buyInsurance() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      app.showToast('请先登录');
      wx.navigateTo({
        url: '/pages/login/login'
      });
      return;
    }

    // 重置表单
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const startDate = tomorrow.toISOString().split('T')[0];

    this.setData({
      showPurchaseModal: true,
      purchaseForm: {
        petName: '',
        petType: '狗',
        petAge: '',
        petBreed: '',
        payeeName: userInfo.nickname || '',
        payeePhone: userInfo.phone || '',
        payeeAccount: '',
        startDate: startDate
      }
    });
  },

  // 关闭弹窗
  closePurchaseModal() {
    this.setData({
      showPurchaseModal: false
    });
  },

  // 阻止冒泡
  stopPropagation() {},

  // 表单输入
  onFormInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      [`purchaseForm.${field}`]: e.detail.value
    });
  },

  // 选择宠物类型
  selectPetType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({
      'purchaseForm.petType': type
    });
  },

  // 选择日期
  onDateChange(e) {
    this.setData({
      'purchaseForm.startDate': e.detail.value
    });
  },

  // 提交购买
  submitPurchase() {
    const form = this.data.purchaseForm;

    // 验证表单
    if (!form.petName) {
      app.showToast('请输入宠物名称');
      return;
    }
    if (!form.petType) {
      app.showToast('请选择宠物类型');
      return;
    }
    if (!form.petAge) {
      app.showToast('请输入宠物年龄');
      return;
    }
    if (!form.payeeName) {
      app.showToast('请输入收款人姓名');
      return;
    }
    if (!form.payeePhone) {
      app.showToast('请输入收款人电话');
      return;
    }
    if (!/^1[3-9]\d{9}$/.test(form.payeePhone)) {
      app.showToast('请输入正确的手机号码');
      return;
    }
    if (!form.payeeAccount) {
      app.showToast('请输入收款账号');
      return;
    }
    if (!form.startDate) {
      app.showToast('请选择生效日期');
      return;
    }

    const userInfo = wx.getStorageSync('userInfo');
    const purchaseData = {
      userId: userInfo.id,
      insuranceId: this.data.insuranceId,
      petName: form.petName,
      petType: form.petType,
      petAge: parseInt(form.petAge),
      petBreed: form.petBreed,
      payeeName: form.payeeName,
      payeePhone: form.payeePhone,
      payeeAccount: form.payeeAccount,
      startDate: form.startDate
    };

    app.showLoading();
    app.request({
      url: '/insurance-order/purchase',
      method: 'POST',
      data: purchaseData
    }).then(res => {
      if (res.success) {
        app.showToast('购买成功', 'success');
        this.closePurchaseModal();
        setTimeout(() => {
          wx.navigateTo({
            url: '/pages/policy-list/policy-list'
          });
        }, 1000);
      } else {
        app.showToast(res.message || '购买失败');
      }
    }).catch(err => {
      app.showToast('购买失败，请稍后重试');
      console.error('购买保险失败', err);
    }).finally(() => {
      app.hideLoading();
    });
  }
});


