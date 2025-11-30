// pages/order-confirm/order-confirm.js
const app = getApp();

Page({
  data: {
    orderItems: [],
    totalAmount: 0,
    formData: {
      receiverName: '',
      receiverPhone: '',
      receiverAddress: ''
    }
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
        wx.navigateTo({
          url: '/pages/login/login'
        });
      }, 1500);
      return;
    }

    // 获取购物车选中的商品
    const checkoutItems = app.globalData.checkoutItems;
    if (!checkoutItems || checkoutItems.length === 0) {
      wx.showToast({
        title: '没有选中的商品',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
      return;
    }

    // 计算总金额并为每个商品添加小计，同时处理图片URL
    const itemsWithTotal = checkoutItems.map(item => ({
      ...item,
      itemTotal: (item.productPrice * item.quantity).toFixed(2),
      // 处理图片：购物车数据包含productImages（复数），提取第一张
      displayImage: this.getFirstImage(item.productImages)
    }));
    
    const totalAmount = checkoutItems.reduce((sum, item) => {
      return sum + (item.productPrice * item.quantity);
    }, 0);

    this.setData({
      orderItems: itemsWithTotal,
      totalAmount: totalAmount.toFixed(2)
    });

    // 加载用户收货信息（如果之前保存过）
    this.loadUserAddress();
  },

  // 加载用户收货信息
  loadUserAddress() {
    const savedAddress = wx.getStorageSync('userAddress');
    if (savedAddress) {
      this.setData({
        formData: savedAddress
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

  // 获取第一张图片
  getFirstImage(images) {
    if (!images) {
      return 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
    }
    const imageArray = images.split(',');
    return imageArray[0] || 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
  },

  // 表单验证
  validateForm() {
    const { formData } = this.data;
    
    if (!formData.receiverName.trim()) {
      wx.showToast({ title: '请输入收货人姓名', icon: 'none' });
      return false;
    }
    if (!formData.receiverPhone.trim()) {
      wx.showToast({ title: '请输入联系电话', icon: 'none' });
      return false;
    }
    
    // 验证手机号格式
    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(formData.receiverPhone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' });
      return false;
    }
    
    if (!formData.receiverAddress.trim()) {
      wx.showToast({ title: '请输入收货地址', icon: 'none' });
      return false;
    }
    
    return true;
  },

  // 提交订单
  handleSubmit() {
    if (!this.validateForm()) {
      return;
    }

    const { formData, orderItems, totalAmount } = this.data;
    const userInfo = wx.getStorageSync('userInfo');
    
    // 构造订单详情
    const orderDetails = orderItems.map(item => ({
      productId: item.productId,
      productName: item.productName,
      productImage: this.getFirstImage(item.productImages),
      price: item.productPrice,
      quantity: item.quantity
    }));
    
    // 获取购物车ID列表
    const cartIds = orderItems.map(item => item.id);
    
    const submitData = {
      userId: userInfo.id,
      totalAmount: totalAmount,
      receiverName: formData.receiverName,
      receiverPhone: formData.receiverPhone,
      receiverAddress: formData.receiverAddress,
      orderDetails: orderDetails,
      cartIds: cartIds
    };

    // 保存收货地址
    wx.setStorageSync('userAddress', formData);

    app.showLoading('提交中...');
    app.request({
      url: '/order/create',
      method: 'POST',
      data: submitData
    }).then(res => {
      if (res.success) {
        wx.showToast({
          title: '订单提交成功',
          icon: 'success'
        });
        
        // 清除全局数据
        app.globalData.checkoutItems = null;
        
        // 跳转到订单列表页
        setTimeout(() => {
          wx.redirectTo({
            url: '/pages/order-list/order-list'
          });
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '订单提交失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.showToast({
        title: '订单提交失败',
        icon: 'none'
      });
    }).finally(() => {
      app.hideLoading();
    });
  }
});

