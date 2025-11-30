// pages/product-list/product-list.js
const app = getApp();

Page({
  data: {
    searchKeyword: '',
    categoryId: '',
    petType: '',
    categories: [],
    productList: [],
    currentPage: 1,
    pageSize: 10,
    hasMore: true,
    
    // 购物车弹窗
    showCartModal: false,
    selectedProduct: {},
    cartQuantity: 1
  },

  onLoad() {
    this.loadCategories();
    this.loadProducts();
  },

  // 加载分类
  loadCategories() {
    app.request({
      url: '/category/all',
      method: 'GET'
    }).then(res => {
      if (res.success) {
        this.setData({
          categories: res.data.records || []
        });
      }
    }).catch(err => {
      console.error('加载分类失败', err);
    });
  },

  // 加载商品
  loadProducts(isLoadMore = false) {
    if (!isLoadMore) {
      app.showLoading();
    }

    const params = {
      page: this.data.currentPage,
      size: this.data.pageSize
    };

    if (this.data.searchKeyword) {
      params.name = this.data.searchKeyword;
    }
    if (this.data.categoryId) {
      params.categoryId = this.data.categoryId;
    }
    if (this.data.petType) {
      params.petType = this.data.petType;
    }

    app.request({
      url: '/product/user/list',
      method: 'GET',
      data: params
    }).then(res => {
      if (res.success) {
        const products = (res.data.records || []).map(item => ({
          id: item.id,
          name: item.name,
          categoryName: item.categoryName || '',
          petType: item.petType || '',
          brand: item.brand || '',
          price: item.price || 0,
          image: this.getFirstImage(item.images)
        }));

        if (isLoadMore) {
          this.setData({
            productList: this.data.productList.concat(products),
            hasMore: products.length >= this.data.pageSize
          });
        } else {
          this.setData({
            productList: products,
            hasMore: products.length >= this.data.pageSize
          });
        }
      }
    }).catch(err => {
      app.showToast('加载失败，请稍后重试');
      console.error('加载商品失败', err);
    }).finally(() => {
      app.hideLoading();
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

  // 搜索输入
  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
  },

  // 搜索
  handleSearch() {
    this.setData({
      currentPage: 1
    });
    this.loadProducts();
  },

  // 按分类筛选
  filterByCategory(e) {
    const id = e.currentTarget.dataset.id;
    this.setData({
      categoryId: id,
      currentPage: 1
    });
    this.loadProducts();
  },

  // 按宠物类型筛选
  filterByPetType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({
      petType: type,
      currentPage: 1
    });
    this.loadProducts();
  },

  // 加载更多
  loadMore() {
    if (!this.data.hasMore) return;
    
    this.setData({
      currentPage: this.data.currentPage + 1
    });
    this.loadProducts(true);
  },

  // 查看详情
  viewDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/product-detail/product-detail?id=${id}`
    });
  },

  // 加入购物车
  addToCart(e) {
    const item = e.currentTarget.dataset.item;
    this.setData({
      selectedProduct: item,
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
      productId: this.data.selectedProduct.id,
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


