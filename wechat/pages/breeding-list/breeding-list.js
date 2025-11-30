// pages/breeding-list/breeding-list.js
Page({
  data: {},
  
  onLoad() {},
  
  openLink1() {
    wx.setClipboardData({
      data: 'https://www.notmaker.com/detail/ead6dd2ab313460a92388a4316e84f4d',
      success: () => {
        wx.showToast({
          title: '链接已复制，请在浏览器中打开',
          icon: 'none'
        });
      }
    });
  },
  
  openLink2() {
    wx.setClipboardData({
      data: 'https://www.notmaker.com/detail/84d80241117c422db9bc614fa9dbfad6',
      success: () => {
        wx.showToast({
          title: '链接已复制，请在浏览器中打开',
          icon: 'none'
        });
      }
    });
  }
});
