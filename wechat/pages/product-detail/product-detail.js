// pages/product-detail/product-detail.js
const app = getApp();
const util = require('../../utils/util.js');

Page({
  data: {
    productId: null,
    product: {},
    reviewList: [],
    showCartModal: false,
    cartQuantity: 1
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ productId: options.id });
      this.loadProductDetail();
      this.loadProductReviews();
    }
  },

  loadProductDetail() {
    app.showLoading();
    app.request({
      url: `/product/user/detail/${this.data.productId}`,
      method: 'GET'
    }).then(res => {
      if (res.success && res.data) {
        const images = res.data.images ? res.data.images.split(',') : [];
        this.setData({
          product: {
            ...res.data,
            images: images
          }
        });
      }
    }).catch(err => {
      app.showToast('加载失败');
      console.error('加载商品详情失败', err);
    }).finally(() => {
      app.hideLoading();
    });
  },

  // 加载商品评价
  loadProductReviews() {
    app.request({
      url: '/review/product/list',
      method: 'GET',
      data: {
        productId: this.data.productId,
        page: 1,
        size: 100
      }
    }).then(res => {
      if (res.success) {
        const reviews = (res.data.records || []).map(item => ({
          ...item,
          images: item.images ? item.images.split(',') : [],
          createTime: util.formatDateTime(item.createTime),
          rating: parseInt(item.rating) || 0
        }));
        this.setData({ reviewList: reviews });
      }
    }).catch(err => {
      console.error('加载评价失败', err);
    });
  },

  // 预览图片
  previewImage(e) {
    const images = e.currentTarget.dataset.images;
    const current = e.currentTarget.dataset.current;
    wx.previewImage({
      current: current,
      urls: images
    });
  },

  // 加入购物车
  addToCart() {
    this.setData({
      cartQuantity: 1,
      showCartModal: true
    });
  },

  // 关闭购物车弹窗
  closeCartModal() {
    this.setData({
      showCartModal: false
    });
  },

  // 阻止冒泡
  stopPropagation() {},

  // 减少数量
  decreaseQuantity() {
    if (this.data.cartQuantity > 1) {
      this.setData({
        cartQuantity: this.data.cartQuantity - 1
      });
    }
  },

  // 增加数量
  increaseQuantity() {
    this.setData({
      cartQuantity: this.data.cartQuantity + 1
    });
  },

  // 确认加入购物车
  confirmAddToCart() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      app.showToast('请先登录');
      wx.navigateTo({
        url: '/pages/login/login'
      });
      return;
    }

    const cartData = {
      userId: userInfo.id,
      productId: this.data.product.id,
      quantity: this.data.cartQuantity
    };

    app.request({
      url: '/cart/add',
      method: 'POST',
      data: cartData
    }).then(res => {
      if (res.success) {
        app.showToast('已加入购物车', 'success');
        this.closeCartModal();
      } else {
        app.showToast(res.message || '加入购物车失败');
      }
    }).catch(err => {
      app.showToast('加入购物车失败，请稍后重试');
      console.error('加入购物车失败', err);
    });
  }
});


