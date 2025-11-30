// pages/review-edit/review-edit.js
const app = getApp();

Page({
  data: {
    orderId: null,
    products: [],
    rating: 5,
    content: ''
  },

  onLoad(options) {
    if (options.orderId && options.products) {
      try {
        const products = JSON.parse(options.products);
        this.setData({
          orderId: options.orderId,
          products: products.map(p => ({
            ...p,
            productImage: p.productImage || 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500'
          }))
        });
      } catch (e) {
        console.error('解析商品信息失败', e);
        app.showToast('参数错误');
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }
    }
  },

  // 选择评分
  selectRating(e) {
    const rating = e.currentTarget.dataset.rating;
    this.setData({ rating });
  },

  // 输入评价内容
  onContentInput(e) {
    this.setData({ content: e.detail.value });
  },

  // 提交评价
  submitReview() {
    if (!this.data.content.trim()) {
      app.showToast('请输入评价内容');
      return;
    }

    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      app.showToast('请先登录');
      return;
    }

    // 为每个商品提交评价
    const reviewPromises = this.data.products.map(product => {
      const reviewData = {
        userId: userInfo.id,
        orderId: this.data.orderId,
        productId: product.productId,
        rating: this.data.rating,
        content: this.data.content,
        images: ''
      };

      return app.request({
        url: '/review/create',
        method: 'POST',
        data: reviewData
      });
    });

    app.showLoading('提交中...');
    Promise.all(reviewPromises)
      .then(results => {
        const allSuccess = results.every(res => res.success);
        if (allSuccess) {
          app.showToast('评价成功', 'success');
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        } else {
          app.showToast('评价失败，请重试');
        }
      })
      .catch(err => {
        app.showToast('评价失败，请重试');
        console.error('提交评价失败', err);
      })
      .finally(() => {
        app.hideLoading();
      });
  }
});

