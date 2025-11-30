// pages/home/home.js
const app = getApp();

Page({
  data: {
    userNickname: '游客',
    carouselItems: [],
    productList: [],
    insuranceList: [],
    hospitalList: [],
    breedingList: [],
    fosterList: [],
    adoptionList: [],
    forumList: []
  },

  onLoad() {
    this.loadUserInfo();
    this.loadSystemConfig();
    this.loadAllModules();
  },

  onShow() {
    this.loadUserInfo();
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        userNickname: userInfo.nickname || '游客'
      });
    }
  },

  // 加载系统配置
  loadSystemConfig() {
    app.request({
      url: '/system-config/get',
      method: 'GET'
    }).then(res => {
      if (res.success && res.data) {
        const config = res.data;
        if (config.carouselImages) {
          try {
            const images = JSON.parse(config.carouselImages);
            if (images && images.length > 0) {
              this.setData({
                carouselItems: images
              });
            }
          } catch (e) {
            console.error('解析轮播图失败', e);
          }
        }
      }
    }).catch(err => {
      console.error('获取系统配置失败', err);
    });
  },

  // 加载所有模块数据
  loadAllModules() {
    this.loadProducts();
    this.loadInsurance();
    this.loadHospitals();
    this.loadBreeding();
    this.loadFoster();
    this.loadAdoption();
    this.loadForum();
  },

  // 加载商品
  loadProducts() {
    app.request({
      url: '/product/user/list',
      method: 'GET',
      data: { page: 1, size: 4 }
    }).then(res => {
      if (res.success) {
        const products = (res.data.records || []).map(item => ({
          id: item.id,
          name: item.name,
          categoryName: item.categoryName || '',
          petType: item.petType || '',
          price: item.price || 0,
          image: this.getFirstImage(item.images)
        }));
        this.setData({ productList: products });
      }
    }).catch(err => {
      console.error('获取商品列表失败', err);
    });
  },

  // 加载保险
  loadInsurance() {
    app.request({
      url: '/insurance/user/list',
      method: 'GET',
      data: { page: 1, size: 4 }
    }).then(res => {
      if (res.success) {
        const insurance = (res.data.records || []).map(item => ({
          id: item.id,
          name: item.name,
          description: this.truncateText(item.description, 50),
          price: item.price || 0
        }));
        this.setData({ insuranceList: insurance });
      }
    }).catch(err => {
      console.error('获取保险列表失败', err);
    });
  },

  // 加载医院
  loadHospitals() {
    app.request({
      url: '/hospital/list',
      method: 'GET',
      data: { page: 1, size: 4 }
    }).then(res => {
      if (res.success) {
        const hospitals = (res.data.records || []).map(item => ({
          id: item.id,
          name: item.name,
          services: this.truncateText(item.services, 40),
          address: this.truncateText(item.address, 30),
          image: this.getFirstImage(item.images)
        }));
        this.setData({ hospitalList: hospitals });
      }
    }).catch(err => {
      console.error('获取医院列表失败', err);
    });
  },

  // 加载配种
  loadBreeding() {
    app.request({
      url: '/breeding/user/list',
      method: 'GET',
      data: { page: 1, size: 4 }
    }).then(res => {
      if (res.success) {
        const breeding = (res.data.records || []).map(item => ({
          id: item.id,
          title: item.title,
          petType: item.petType || '',
          petGender: item.petGender || '',
          breed: item.breed || '',
          status: item.status || '',
          image: this.getFirstImage(item.photos)
        }));
        this.setData({ breedingList: breeding });
      }
    }).catch(err => {
      console.error('获取配种列表失败', err);
    });
  },

  // 加载寄养
  loadFoster() {
    app.request({
      url: '/foster/user/list',
      method: 'GET',
      data: { page: 1, size: 4 }
    }).then(res => {
      if (res.success) {
        const foster = (res.data.records || []).map(item => ({
          id: item.id,
          title: item.title,
          petType: item.petType || '',
          petGender: item.petGender || '',
          breed: item.breed || '',
          status: item.status || '',
          image: this.getFirstImage(item.photos)
        }));
        this.setData({ fosterList: foster });
      }
    }).catch(err => {
      console.error('获取寄养列表失败', err);
    });
  },

  // 加载领养
  loadAdoption() {
    app.request({
      url: '/adoption/user/list',
      method: 'GET',
      data: { page: 1, size: 4 }
    }).then(res => {
      if (res.success) {
        const adoption = (res.data.records || []).map(item => ({
          id: item.id,
          title: item.title,
          petType: item.petType || '',
          petGender: item.petGender || '',
          breed: item.breed || '',
          status: item.status || '',
          image: this.getFirstImage(item.photos)
        }));
        this.setData({ adoptionList: adoption });
      }
    }).catch(err => {
      console.error('获取领养列表失败', err);
    });
  },

  // 加载论坛
  loadForum() {
    const userInfo = wx.getStorageSync('userInfo');
    const userId = userInfo ? userInfo.id : null;
    
    // 构建请求参数，只有userId存在时才传递
    const requestData = { page: 1, size: 4 };
    if (userId) {
      requestData.userId = userId;
    }
    
    app.request({
      url: '/forum/posts',
      method: 'GET',
      data: requestData
    }).then(res => {
      if (res.code === 200 || res.success) {
        const forum = ((res.data && res.data.list) || res.data || []).map(item => ({
          id: item.id,
          title: item.title,
          likeCount: item.likeCount || 0,
          commentCount: item.commentCount || 0,
          username: item.username || '匿名用户',
          userAvatar: item.userAvatar || 'https://img1.baidu.com/it/u=1537776144,3012351076&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
          image: this.getForumImage(item.images)
        }));
        this.setData({ forumList: forum });
      }
    }).catch(err => {
      console.error('获取论坛列表失败', err);
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

  // 获取论坛图片
  getForumImage(images) {
    if (!images) {
      return 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
    }
    if (typeof images === 'string') {
      const imageArray = images.split(',');
      return imageArray[0] || 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
    }
    return 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
  },

  // 截断文本
  truncateText(text, length) {
    if (!text) return '';
    if (text.length <= length) return text;
    return text.substring(0, length) + '...';
  },

  // 跳转页面
  navigateTo(e) {
    const url = e.currentTarget.dataset.url;
    
    // 判断是否是tabBar页面
    const tabBarPages = [
      '/pages/home/home',
      '/pages/product-list/product-list',
      '/pages/forum-list/forum-list',
      '/pages/profile/profile'
    ];
    
    // 去掉URL参数，只取页面路径
    const pagePath = url.split('?')[0];
    
    // 如果是tabBar页面，使用switchTab，否则使用navigateTo
    if (tabBarPages.includes(pagePath)) {
      wx.switchTab({ url: pagePath });
    } else {
      wx.navigateTo({ url });
    }
  }
});


