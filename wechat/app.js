// app.js
App({
  globalData: {
    userInfo: null,
    baseUrl: 'http://localhost:18007/api',
    systemName: '伴侣宠物之家',
    sessionId: null // 存储 sessionId
  },

  onLaunch() {
    // 检查登录状态
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.globalData.userInfo = userInfo;
    }
    // 从本地存储中恢复 sessionId
    const sessionId = wx.getStorageSync('sessionId');
    if (sessionId) {
      this.globalData.sessionId = sessionId;
    }
  },

  // 全局请求方法
  request(options) {
    return new Promise((resolve, reject) => {
      // 准备请求头
      const header = {
        'Content-Type': 'application/json',
        ...options.header
      };
      
      // 如果有 sessionId，添加到 Cookie 头中
      if (this.globalData.sessionId) {
        header['Cookie'] = 'JSESSIONID=' + this.globalData.sessionId;
      }
      
      wx.request({
        url: this.globalData.baseUrl + options.url,
        method: options.method || 'GET',
        data: options.data || {},
        header: header,
        success: (res) => {
          // 保存响应中的 Set-Cookie (JSESSIONID)
          if (res.header && (res.header['Set-Cookie'] || res.header['set-cookie'])) {
            const setCookie = res.header['Set-Cookie'] || res.header['set-cookie'];
            const match = setCookie.match(/JSESSIONID=([^;]+)/);
            if (match && match[1]) {
              this.globalData.sessionId = match[1];
              wx.setStorageSync('sessionId', match[1]);
            }
          }
          
          if (res.statusCode === 200) {
            resolve(res.data);
          } else {
            reject(res);
          }
        },
        fail: (err) => {
          reject(err);
        }
      });
    });
  },

  // 显示提示
  showToast(title, icon = 'none') {
    wx.showToast({
      title: title,
      icon: icon,
      duration: 2000
    });
  },

  // 显示加载
  showLoading(title = '加载中...') {
    wx.showLoading({
      title: title,
      mask: true
    });
  },

  // 隐藏加载
  hideLoading() {
    wx.hideLoading();
  }
});
