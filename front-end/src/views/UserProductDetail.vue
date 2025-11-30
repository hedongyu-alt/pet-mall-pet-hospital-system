<template>
  <div class="product-detail-container">
    <el-card shadow="hover">
      <!-- 返回按钮 -->
      <div class="back-button">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回商品列表</el-button>
      </div>

      <el-row :gutter="40" v-if="product">
        <!-- 左侧：图片展示 -->
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <div class="image-section">
            <!-- 主图展示区域（支持鼠标悬停放大） -->
            <div class="main-image-container" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
              <img 
                :src="mainImage" 
                :alt="product.name"
                class="main-image">
              
              <!-- 放大镜效果 -->
              <div 
                v-if="showMagnifier" 
                class="magnifier"
                :style="magnifierStyle">
              </div>
              
              <!-- 放大图片显示区域 -->
              <div 
                v-if="showMagnifier" 
                class="magnified-view"
                :style="magnifiedStyle">
              </div>
            </div>

            <!-- 缩略图列表 -->
            <div class="thumbnail-list" v-if="imageList.length > 1">
              <div 
                v-for="(image, index) in imageList" 
                :key="index"
                class="thumbnail-item"
                :class="{ active: mainImage === image }"
                @click="selectImage(image)">
                <img :src="image" :alt="`图片${index + 1}`">
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧：商品信息 -->
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <div class="info-section">
            <h1 class="product-title">{{ product.name }}</h1>
            
            <div class="product-meta">
              <el-tag type="success" size="medium">{{ product.categoryName }}</el-tag>
              <el-tag type="warning" size="medium">{{ product.petType }}</el-tag>
              <el-tag v-if="product.brand" type="info" size="medium">{{ product.brand }}</el-tag>
            </div>

            <div class="price-section">
              <div class="price-label">价格</div>
              <div class="price-value">
                <span class="symbol">¥</span>
                <span class="value">{{ product.price }}</span>
              </div>
            </div>

            <div class="detail-item">
              <div class="detail-label">商品描述</div>
              <div class="detail-content">{{ product.description || '暂无描述' }}</div>
            </div>

            <div class="action-buttons">
              <el-button 
                type="warning" 
                size="large" 
                icon="el-icon-shopping-cart-2"
                @click="addToCart">
                加入购物车
              </el-button>
            </div>

            <div class="extra-info">
              <div class="info-row">
                <i class="el-icon-box"></i>
                <span>正品保障</span>
              </div>
              <div class="info-row">
                <i class="el-icon-truck"></i>
                <span>默认包邮</span>
              </div>
              <div class="info-row">
                <i class="el-icon-refresh-left"></i>
                <span>7天无理由退换</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <i class="el-icon-loading"></i>
        <span>加载中...</span>
      </div>
    </el-card>

    <!-- 评价区域 -->
    <el-card shadow="hover" class="review-section" ref="reviewSection">
      <div slot="header" class="review-header">
        <h3>商品评价</h3>
        <span class="review-count">(共{{ reviewTotal }}条评价)</span>
      </div>

      <!-- 评价列表 -->
      <div v-if="!reviewLoading && reviewList.length > 0" class="review-list">
        <div 
          v-for="review in reviewList" 
          :key="review.id" 
          class="review-item">
          <div class="review-item-header">
            <div class="user-info">
              <img 
                :src="review.userAvatar || 'https://img1.baidu.com/it/u=1537776144,3012351076&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500'" 
                :alt="review.userNickname" 
                class="user-avatar">
              <div class="user-details">
                <div class="user-nickname">{{ review.userNickname || '匿名用户' }}</div>
                <div class="review-date">{{ formatDate(review.createTime) }}</div>
              </div>
            </div>
            <el-rate
              v-model="review.rating"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}分">
            </el-rate>
          </div>
          <div class="review-content">{{ review.content }}</div>
        </div>
      </div>

      <!-- 暂无评价 -->
      <div v-if="!reviewLoading && reviewList.length === 0" class="no-review">
        <i class="el-icon-chat-dot-round"></i>
        <p>暂无评价，快来成为第一个评价的用户吧~</p>
      </div>

      <!-- 加载中 -->
      <div v-if="reviewLoading" class="review-loading">
        <i class="el-icon-loading"></i>
        <span>加载评价中...</span>
      </div>

      <!-- 分页 -->
      <div v-if="reviewTotal > reviewSize" class="review-pagination">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="reviewTotal"
          :page-size="reviewSize"
          :current-page="reviewPage"
          @current-change="handleReviewPageChange">
        </el-pagination>
      </div>
    </el-card>

    <!-- 图片预览弹窗 -->
    <el-dialog
      :visible.sync="showImageDialog"
      width="80%"
      top="5vh"
      custom-class="image-dialog"
      :show-close="true"
      @close="closeImageDialog">
      <div class="dialog-image-container">
        <img :src="currentDialogImage" :alt="product ? product.name : ''">
        
        <!-- 上一张 -->
        <div class="image-nav prev" @click="prevImage" v-if="imageList.length > 1">
          <i class="el-icon-arrow-left"></i>
        </div>
        
        <!-- 下一张 -->
        <div class="image-nav next" @click="nextImage" v-if="imageList.length > 1">
          <i class="el-icon-arrow-right"></i>
        </div>
        
        <!-- 图片指示器 -->
        <div class="image-indicator" v-if="imageList.length > 1">
          {{ currentImageIndex + 1 }} / {{ imageList.length }}
        </div>
      </div>
    </el-dialog>

    <!-- 加入购物车弹窗 -->
    <el-dialog
      title="加入购物车"
      :visible.sync="cartDialogVisible"
      width="400px"
      :close-on-click-modal="false">
      <div class="cart-dialog-content" v-if="product">
        <div class="dialog-product-info">
          <img :src="mainImage" :alt="product.name">
          <div class="dialog-product-name">{{ product.name }}</div>
        </div>
        <el-form label-width="80px">
          <el-form-item label="购买数量">
            <el-input-number
              v-model="cartQuantity"
              :min="1"
              :max="999"
              controls-position="right">
            </el-input-number>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cartDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAddToCart">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserProductDetail } from '@/api/product';
import { addToCart } from '@/api/cart';
import { getProductReviewList } from '@/api/review';

export default {
  name: 'UserProductDetail',
  data() {
    return {
      product: null,
      loading: false,
      imageList: [],
      mainImage: '',
      showImageDialog: false,
      currentDialogImage: '',
      currentImageIndex: 0,
      
      // 放大镜相关
      showMagnifier: false,
      magnifierX: 0,
      magnifierY: 0,
      imageX: 0,
      imageY: 0,
      
      // 购物车相关
      cartDialogVisible: false,
      cartQuantity: 1,
      
      // 评价相关
      reviewList: [],
      reviewTotal: 0,
      reviewPage: 1,
      reviewSize: 5,
      reviewLoading: false
    };
  },
  computed: {
    magnifierStyle() {
      return {
        left: this.magnifierX + 'px',
        top: this.magnifierY + 'px'
      };
    },
    magnifiedStyle() {
      return {
        backgroundImage: `url(${this.mainImage})`,
        backgroundPosition: `${this.imageX}% ${this.imageY}%`,
        backgroundSize: '250%'
      };
    }
  },
  created() {
    this.loadProductDetail();
    this.loadReviewList();
  },
  methods: {
    // 加载商品详情
    async loadProductDetail() {
      this.loading = true;
      try {
        const id = this.$route.params.id;
        const response = await getUserProductDetail(id);
        
        if (response.data.success) {
          this.product = response.data.data;
          this.initImages();
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加载商品详情失败',
            type: 'error',
            duration: 2000
          });
          this.goBack();
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载商品详情失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
        this.goBack();
      } finally {
        this.loading = false;
      }
    },
    
    // 初始化图片列表
    initImages() {
      if (this.product.images) {
        this.imageList = this.product.images.split(',').filter(img => img.trim());
      }
      
      if (this.imageList.length === 0) {
        this.imageList = ['https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500'];
      }
      
      this.mainImage = this.imageList[0];
      this.currentDialogImage = this.mainImage;
    },
    
    // 选择图片
    selectImage(image) {
      this.mainImage = image;
      this.currentDialogImage = image;
      this.currentImageIndex = this.imageList.indexOf(image);
    },
    
    // 鼠标移动事件（放大镜）
    handleMouseMove(event) {
      const container = event.currentTarget;
      const rect = container.getBoundingClientRect();
      
      // 计算鼠标在图片中的相对位置
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      
      // 放大镜尺寸
      const magnifierSize = 150;
      
      // 计算放大镜位置（确保不超出边界）
      this.magnifierX = Math.max(0, Math.min(x - magnifierSize / 2, rect.width - magnifierSize));
      this.magnifierY = Math.max(0, Math.min(y - magnifierSize / 2, rect.height - magnifierSize));
      
      // 计算放大图片的偏移
      const scale = 2.5; // 放大倍数
      // 计算鼠标相对于容器的百分比位置
      const percentX = (x / rect.width) * 100;
      const percentY = (y / rect.height) * 100;
      
      // 根据百分比计算放大视图中的背景位置
      // 背景图片大小是250%，所以需要相应调整位置
      this.imageX = percentX;
      this.imageY = percentY;
      
      this.showMagnifier = true;
    },
    
    // 鼠标离开事件
    handleMouseLeave() {
      this.showMagnifier = false;
    },
    
    // 打开图片预览
    openImageDialog(image) {
      this.currentDialogImage = image;
      this.currentImageIndex = this.imageList.indexOf(image);
      this.showImageDialog = true;
    },
    
    // 关闭图片预览
    closeImageDialog() {
      this.showImageDialog = false;
    },
    
    // 上一张
    prevImage() {
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--;
      } else {
        this.currentImageIndex = this.imageList.length - 1;
      }
      this.currentDialogImage = this.imageList[this.currentImageIndex];
      this.mainImage = this.currentDialogImage;
    },
    
    // 下一张
    nextImage() {
      if (this.currentImageIndex < this.imageList.length - 1) {
        this.currentImageIndex++;
      } else {
        this.currentImageIndex = 0;
      }
      this.currentDialogImage = this.imageList[this.currentImageIndex];
      this.mainImage = this.currentDialogImage;
    },
    
    // 返回
    goBack() {
      this.$router.push('/user/product-list');
    },
    
    // 加入购物车
    addToCart() {
      this.cartQuantity = 1;
      this.cartDialogVisible = true;
    },
    
    // 确认加入购物车
    async confirmAddToCart() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'));
        if (!userInfo) {
          this.$notify({
            title: '提示',
            message: '请先登录',
            type: 'warning',
            duration: 2000
          });
          this.$router.push('/login');
          return;
        }
        
        const cartData = {
          userId: userInfo.id,
          productId: this.product.id,
          quantity: this.cartQuantity
        };
        
        const response = await addToCart(cartData);
        if (response.data.success) {
          this.$notify({
            title: '成功',
            message: '已加入购物车',
            type: 'success',
            duration: 2000
          });
          this.cartDialogVisible = false;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加入购物车失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加入购物车失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    // 立即购买
    buyNow() {
      this.$notify({
        title: '提示',
        message: '购买功能开发中',
        type: 'info',
        duration: 2000
      });
    },
    
    // 加载评价列表
    async loadReviewList() {
      this.reviewLoading = true;
      try {
        const productId = this.$route.params.id;
        const response = await getProductReviewList(productId, this.reviewPage, this.reviewSize);
        
        if (response.data.success) {
          this.reviewList = response.data.data.records;
          this.reviewTotal = response.data.data.total;
        }
      } catch (error) {
        console.error('加载评价失败：', error);
      } finally {
        this.reviewLoading = false;
      }
    },
    
    // 切换评价页码
    handleReviewPageChange(page) {
      this.reviewPage = page;
      this.loadReviewList();
      // 滚动到评价区域
      this.$nextTick(() => {
        const reviewSection = this.$refs.reviewSection;
        if (reviewSection) {
          reviewSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      });
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
  }
};
</script>

<style scoped>
.product-detail-container {
  min-height: calc(100vh - 130px);
  margin-bottom: 30px;
}

.back-button {
  margin-bottom: 20px;
}

/* 图片区域 */
.image-section {
  position: sticky;
  top: 90px;
  overflow: visible;
}

.main-image-container {
  position: relative;
  width: 100%;
  height: 500px;
  background: #f5f7fa;
  border-radius: 12px;
  overflow: visible;
  cursor: crosshair;
}

.main-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
}

/* 放大镜 */
.magnifier {
  position: absolute;
  width: 150px;
  height: 150px;
  border: 2px solid #2c5f2d;
  background: rgba(255, 255, 255, 0.3);
  pointer-events: none;
  z-index: 10;
}

.magnified-view {
  position: absolute;
  right: -320px;
  top: 0;
  width: 300px;
  height: 300px;
  border: 2px solid #2c5f2d;
  background-color: white;
  background-repeat: no-repeat;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  pointer-events: none;
  z-index: 20;
}

/* 缩略图列表 */
.thumbnail-list {
  display: flex;
  gap: 10px;
  margin-top: 15px;
  flex-wrap: wrap;
}

.thumbnail-item {
  width: 80px;
  height: 80px;
  border: 2px solid #dcdfe6;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.thumbnail-item:hover {
  border-color: #2c5f2d;
  transform: scale(1.05);
}

.thumbnail-item.active {
  border-color: #2c5f2d;
  box-shadow: 0 0 8px rgba(44, 95, 45, 0.3);
}

.thumbnail-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 商品信息区域 */
.info-section {
  padding: 20px 0;
}

.product-title {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 15px;
  line-height: 1.4;
}

.product-meta {
  display: flex;
  gap: 10px;
  margin-bottom: 25px;
}

/* 价格区域 */
.price-section {
  background: linear-gradient(135deg, #fff4e6 0%, #ffe7cc 100%);
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 25px;
}

.price-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.price-value {
  display: flex;
  align-items: baseline;
}

.price-value .symbol {
  font-size: 24px;
  color: #f56c6c;
  font-weight: 700;
}

.price-value .value {
  font-size: 42px;
  color: #f56c6c;
  font-weight: 700;
  margin-left: 5px;
}

/* 详情项 */
.detail-item {
  margin-bottom: 25px;
}

.detail-label {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}

.detail-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
}

.action-buttons .el-button {
  flex: 1;
}

/* 额外信息 */
.extra-info {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #606266;
}

.info-row i {
  font-size: 18px;
  color: #2c5f2d;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 0;
  gap: 15px;
}

.loading-container i {
  font-size: 40px;
  color: #2c5f2d;
}

.loading-container span {
  font-size: 16px;
  color: #909399;
}

/* 图片预览弹窗 */
.dialog-image-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 500px;
}

.dialog-image-container img {
  max-width: 100%;
  max-height: 70vh;
  object-fit: contain;
}

.image-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 50px;
  height: 50px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s;
}

.image-nav:hover {
  background: rgba(0, 0, 0, 0.7);
  transform: translateY(-50%) scale(1.1);
}

.image-nav.prev {
  left: 20px;
}

.image-nav.next {
  right: 20px;
}

.image-indicator {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 14px;
}

/* 加入购物车弹窗 */
.cart-dialog-content {
  padding: 20px 0;
}

.dialog-product-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 30px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.dialog-product-info img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.dialog-product-name {
  flex: 1;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .image-section {
    position: relative;
    top: 0;
    margin-bottom: 30px;
  }

  .magnified-view {
    display: none;
  }

  .main-image-container {
    cursor: pointer;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-buttons .el-button {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .product-title {
    font-size: 22px;
  }

  .price-value .value {
    font-size: 32px;
  }

  .main-image-container {
    height: 350px;
  }
}

/* 评价区域样式 */
.review-section {
  margin-top: 30px;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 10px;
}

.review-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #303133;
}

.review-count {
  font-size: 14px;
  color: #909399;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-item {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 12px;
  transition: all 0.3s;
}

.review-item:hover {
  background: #ecf0f5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.review-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #e4e7ed;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-nickname {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.review-date {
  font-size: 13px;
  color: #909399;
}

.review-content {
  font-size: 14px;
  line-height: 1.8;
  color: #606266;
  word-break: break-word;
}

.no-review {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  gap: 15px;
}

.no-review i {
  font-size: 60px;
  color: #dcdfe6;
}

.no-review p {
  font-size: 15px;
  color: #909399;
  margin: 0;
}

.review-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
  gap: 15px;
}

.review-loading i {
  font-size: 40px;
  color: #2c5f2d;
}

.review-loading span {
  font-size: 14px;
  color: #909399;
}

.review-pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

@media (max-width: 768px) {
  .review-item-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
}
</style>

<style>
.image-dialog {
  background: rgba(0, 0, 0, 0.95);
}

.image-dialog .el-dialog__header {
  display: none;
}

.image-dialog .el-dialog__body {
  padding: 20px;
}
</style>

