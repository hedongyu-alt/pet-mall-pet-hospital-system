<template>
  <div class="user-home">
    <!-- 轮播图 -->
    <div class="carousel-section">
      <el-carousel height="450px" :interval="4000">
        <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
          <div class="carousel-item" :style="{ backgroundImage: 'url(' + item.image + ')' }">
            <div class="carousel-overlay">
              <h2>{{ item.title }}</h2>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    
    <!-- 欢迎卡片 -->
    <div class="welcome-section">
      <el-card class="welcome-card">
        <div class="welcome-content">
          <div class="welcome-icon">👋</div>
          <div class="welcome-text">
            <h2>欢迎回来，{{ userInfo.nickname }}！</h2>
            <p>在伴侣宠物之家，与您的爱宠共度美好时光</p>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 商品模块 -->
    <div class="module-section">
      <div class="section-header">
        <div class="section-title">
          <h3>热门商品</h3>
          <p>精选优质宠物用品</p>
        </div>
        <el-button type="primary" plain size="small" @click="$router.push('/user/product-list')">更多</el-button>
      </div>
      <div class="cards-grid">
        <el-card class="item-card" v-for="item in productList" :key="item.id" shadow="hover" @click.native="$router.push(`/user/product-detail/${item.id}`)">
          <div class="card-image" :style="{ backgroundImage: 'url(' + getFirstImage(item.images) + ')' }"></div>
          <div class="card-content">
            <h4>{{ item.name }}</h4>
            <p class="description">{{ item.description }}</p>
            <div class="product-meta">
              <span class="category-tag">{{ item.categoryName }}</span>
              <span class="pet-type">{{ item.petType }}</span>
            </div>
            <div class="price">￥{{ item.price }}</div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 保险模块 -->
    <div class="module-section">
      <div class="section-header">
        <div class="section-title">
          <h3>宠物保险</h3>
          <p>为爱宠提供全方位保障</p>
        </div>
        <el-button type="primary" plain size="small" @click="$router.push('/user/insurance-list')">更多</el-button>
      </div>
      <div class="cards-grid">
        <el-card class="item-card insurance-card" v-for="item in insuranceList" :key="item.id" shadow="hover" @click.native="$router.push(`/user/insurance-detail/${item.id}`)">
          <div class="insurance-header">
            <div class="insurance-icon">
              <i class="el-icon-s-home"></i>
            </div>
            <div class="insurance-badge">宠物保险</div>
          </div>
          <div class="card-content">
            <h4>{{ item.name }}</h4>
            <p class="description">{{ truncateText(item.description, 50) }}</p>
            <div class="price-with-unit">
              <span class="price">￥{{ item.price }}</span>
              <span class="unit">/年</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 医院模块 -->
    <div class="module-section">
      <div class="section-header">
        <div class="section-title">
          <h3>宠物医院</h3>
          <p>专业医疗服务机构</p>
        </div>
        <el-button type="primary" plain size="small" @click="$router.push('/user/hospital-list')">更多</el-button>
      </div>
      <div class="cards-grid">
        <el-card class="item-card" v-for="item in hospitalList" :key="item.id" shadow="hover" @click.native="$router.push(`/user/hospital-detail/${item.id}`)">
          <div class="card-image" :style="{ backgroundImage: 'url(' + getFirstImage(item.images) + ')' }"></div>
          <div class="card-content">
            <h4>{{ item.name }}</h4>
            <p class="services">{{ truncateText(item.services, 40) }}</p>
            <div class="hospital-info">
              <div class="address"><i class="el-icon-location"></i> {{ truncateText(item.address, 30) }}</div>
              <div class="phone"><i class="el-icon-phone"></i> {{ item.phone }}</div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 配种模块 -->
    <div class="module-section">
      <div class="section-header">
        <div class="section-title">
          <h3>宠物配种</h3>
          <p>优质配种服务信息</p>
        </div>
        <el-button type="primary" plain size="small" @click="$router.push('/user/breeding-list')">更多</el-button>
      </div>
      <div class="cards-grid">
        <el-card class="item-card" v-for="item in breedingList" :key="item.id" shadow="hover" @click.native="$router.push(`/user/breeding-detail/${item.id}`)">
          <div class="card-image" :style="{ backgroundImage: 'url(' + getFirstImage(item.photos) + ')' }"></div>
          <div class="card-content">
            <h4>{{ item.title }}</h4>
            <div class="pet-info">
              <span>{{ item.petName }}</span>
            </div>
            <div class="tags">
              <el-tag size="small" type="success">{{ item.petType }}</el-tag>
              <el-tag size="small" type="warning">{{ item.petGender }}</el-tag>
              <el-tag size="small" type="info">{{ item.breed }}</el-tag>
            </div>
            <div class="status-tag">{{ item.status }}</div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 寄养模块 -->
    <div class="module-section">
      <div class="section-header">
        <div class="section-title">
          <h3>宠物寄养</h3>
          <p>安全可靠的寄养环境</p>
        </div>
        <el-button type="primary" plain size="small" @click="$router.push('/user/foster-list')">更多</el-button>
      </div>
      <div class="cards-grid">
        <el-card class="item-card" v-for="item in fosterList" :key="item.id" shadow="hover" @click.native="$router.push(`/user/foster-detail/${item.id}`)">
          <div class="card-image" :style="{ backgroundImage: 'url(' + getFirstImage(item.photos) + ')' }"></div>
          <div class="card-content">
            <h4>{{ item.title }}</h4>
            <div class="pet-info">
              <span>{{ item.petName }}</span>
            </div>
            <div class="tags">
              <el-tag size="small" type="success">{{ item.petType }}</el-tag>
              <el-tag size="small" type="warning">{{ item.petGender }}</el-tag>
              <el-tag size="small" type="info">{{ item.breed }}</el-tag>
            </div>
            <div class="status-tag">{{ item.status }}</div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 领养模块 -->
    <div class="module-section">
      <div class="section-header">
        <div class="section-title">
          <h3>宠物领养</h3>
          <p>给它们一个温暖的家</p>
        </div>
        <el-button type="primary" plain size="small" @click="$router.push('/user/adoption-list')">更多</el-button>
      </div>
      <div class="cards-grid">
        <el-card class="item-card" v-for="item in adoptionList" :key="item.id" shadow="hover" @click.native="$router.push(`/user/adoption-detail/${item.id}`)">
          <div class="card-image" :style="{ backgroundImage: 'url(' + getFirstImage(item.photos) + ')' }"></div>
          <div class="card-content">
            <h4>{{ item.title }}</h4>
            <div class="pet-info">
              <span>{{ item.petName }}</span>
            </div>
            <div class="tags">
              <el-tag size="small" type="success">{{ item.petType }}</el-tag>
              <el-tag size="small" type="warning">{{ item.petGender }}</el-tag>
              <el-tag size="small" type="info">{{ item.breed }}</el-tag>
            </div>
            <div class="status-tag">{{ item.status }}</div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 论坛模块 -->
    <div class="module-section">
      <div class="section-header">
        <div class="section-title">
          <h3>宠物论坛</h3>
          <p>分享养宠经验和故事</p>
        </div>
        <el-button type="primary" plain size="small" @click="$router.push('/user/forum')">更多</el-button>
      </div>
      <div class="cards-grid">
        <el-card class="item-card forum-card" v-for="item in forumList" :key="item.id" shadow="hover" @click.native="viewForumDetail(item.id)">
          <div class="card-image" :style="{ backgroundImage: 'url(' + getForumImage(item.images) + ')' }"></div>
          <div class="card-content">
            <h4>{{ item.title }}</h4>
            <p class="description">{{ truncateText(item.description || item.content, 50) }}</p>
            <div class="forum-stats">
              <span><i class="el-icon-thumb"></i> {{ item.likeCount || 0 }}</span>
              <span><i class="el-icon-chat-dot-round"></i> {{ item.commentCount || 0 }}</span>
            </div>
            <div class="author-info">
              <i class="el-icon-user"></i>
              <span>{{ item.username }}</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { getSystemConfig } from '@/api/systemConfig';
import { getUserProductList } from '@/api/product';
import { getUserInsuranceList } from '@/api/insurance';
import { getHospitalList } from '@/api/hospital';
import { getUserBreedingList } from '@/api/breeding';
import { getUserFosterList } from '@/api/foster';
import { getUserAdoptionList } from '@/api/adoption';
import { getPostList } from '@/api/forum';

export default {
  name: 'UserHome',
  data() {
    return {
      userInfo: {},
      carouselItems: [],
      productList: [],
      insuranceList: [],
      hospitalList: [],
      breedingList: [],
      fosterList: [],
      adoptionList: [],
      forumList: []
    };
  },
  created() {
    this.loadUserInfo();
    this.loadSystemConfig();
    this.loadAllModules();
  },
  methods: {
    // 加载用户信息
    loadUserInfo() {
      const userInfoStr = localStorage.getItem('userInfo');
      if (userInfoStr) {
        this.userInfo = JSON.parse(userInfoStr);
      }
    },
    
    // 加载系统配置
    loadSystemConfig() {
      getSystemConfig().then(response => {
        if (response.data.success) {
          const config = response.data.data;
          
          if (config.systemName) {
            document.title = config.systemName;
          }
          
          try {
            if (config.carouselImages) {
              const images = JSON.parse(config.carouselImages);
              if (images && images.length > 0) {
                this.carouselItems = images.map(image => ({
                  image: image,
                  title: '',
                  description: ''
                }));
              }
            }
          } catch (e) {
            console.error('解析轮播图失败', e);
          }
        }
      }).catch(error => {
        console.error('获取系统配置失败', error);
      });
    },
    
    // 加载所有模块数据
    loadAllModules() {
      // 加载商品
      getUserProductList({ page: 1, size: 4 }).then(response => {
        if (response.data.success) {
          this.productList = response.data.data.records || [];
        }
      }).catch(error => {
        console.error('获取商品列表失败', error);
      });

      // 加载保险
      getUserInsuranceList({ page: 1, size: 4 }).then(response => {
        if (response.data.success) {
          this.insuranceList = response.data.data.records || [];
        }
      }).catch(error => {
        console.error('获取保险列表失败', error);
      });

      // 加载医院
      getHospitalList({ page: 1, size: 4 }).then(response => {
        if (response.data.success) {
          this.hospitalList = response.data.data.records || [];
        }
      }).catch(error => {
        console.error('获取医院列表失败', error);
      });

      // 加载配种
      getUserBreedingList({ page: 1, size: 4 }).then(response => {
        if (response.data.success) {
          this.breedingList = response.data.data.records || [];
        }
      }).catch(error => {
        console.error('获取配种列表失败', error);
      });

      // 加载寄养
      getUserFosterList({ page: 1, size: 4 }).then(response => {
        if (response.data.success) {
          this.fosterList = response.data.data.records || [];
        }
      }).catch(error => {
        console.error('获取寄养列表失败', error);
      });

      // 加载领养
      getUserAdoptionList({ page: 1, size: 4 }).then(response => {
        if (response.data.success) {
          this.adoptionList = response.data.data.records || [];
        }
      }).catch(error => {
        console.error('获取领养列表失败', error);
      });

      // 加载论坛
      const userInfo = JSON.parse(localStorage.getItem('userInfo'));
      const userId = userInfo ? userInfo.id : null;
      getPostList({ page: 1, size: 4, userId: userId }).then(response => {
        if (response.data.code === 200) {
          this.forumList = response.data.data.list || [];
        }
      }).catch(error => {
        console.error('获取论坛列表失败', error);
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
      // 论坛的images可能是字符串或已经是数组
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
    
    // 查看论坛详情
    viewForumDetail(id) {
      this.$router.push({
        path: '/user/forum/detail',
        query: { id }
      });
    }
  }
};
</script>

<style scoped>
.user-home {
  min-height: 100vh;
}

/* 轮播图 */
.carousel-section {
  margin-bottom: 30px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.carousel-item {
  width: 100%;
  height: 450px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.carousel-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 40px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
  color: white;
}

.carousel-overlay h2 {
  font-size: 32px;
  margin: 0 0 10px 0;
  font-weight: 600;
}

.carousel-overlay p {
  font-size: 18px;
  margin: 0;
  opacity: 0.9;
}

/* 欢迎卡片 */
.welcome-section {
  margin-bottom: 30px;
}

.welcome-card {
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.welcome-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 10px;
}

.welcome-icon {
  font-size: 60px;
}

.welcome-text h2 {
  color: #2c5f2d;
  font-size: 26px;
  margin: 0 0 10px 0;
  font-weight: 600;
}

.welcome-text p {
  color: #666;
  font-size: 16px;
  margin: 0;
}

/* 模块区域 */
.module-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title h3 {
  color: #2c5f2d;
  font-size: 24px;
  margin: 0 0 5px 0;
  font-weight: 600;
}

.section-title p {
  color: #666;
  font-size: 14px;
  margin: 0;
}

/* 卡片网格 */
.cards-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.item-card {
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  overflow: hidden;
  height: 380px;
}

.item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.item-card >>> .el-card__body {
  padding: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 卡片图片 */
.card-image {
  width: 100%;
  height: 200px;
  background-size: cover;
  background-position: center;
  background-color: #f5f5f5;
  flex-shrink: 0;
}

/* 卡片内容 */
.card-content {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.card-content h4 {
  color: #333;
  font-size: 16px;
  margin: 0 0 10px 0;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex-shrink: 0;
}

.card-content .description {
  color: #666;
  font-size: 13px;
  margin: 0 0 10px 0;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 40px;
  flex-shrink: 0;
}

/* 商品相关 */
.product-meta {
  display: flex;
  gap: 8px;
  margin-bottom: auto;
  margin-top: 0;
  flex-shrink: 0;
  height: 24px;
  align-items: center;
}

.category-tag,
.pet-type {
  font-size: 12px;
  color: #666;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 4px;
  line-height: 1.5;
}

.price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: 600;
  margin-top: 10px;
  flex-shrink: 0;
}

/* 保险卡片特殊样式 */
.insurance-card {
  height: 300px;
}

.insurance-header {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100px;
  flex-shrink: 0;
}

.insurance-icon {
  width: 50px;
  height: 50px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.insurance-icon i {
  font-size: 28px;
  color: white;
}

.insurance-badge {
  background: rgba(255, 255, 255, 0.3);
  color: white;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

.insurance-card .card-content {
  height: 200px;
}

.insurance-card .description {
  margin-bottom: auto;
  height: 80px;
  -webkit-line-clamp: 3;
  line-clamp: 3;
}

.price-with-unit {
  margin-top: 10px;
  display: flex;
  align-items: baseline;
  gap: 5px;
  flex-shrink: 0;
}

.price-with-unit .price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: 600;
}

.price-with-unit .unit {
  color: #999;
  font-size: 14px;
}

/* 医院信息 */
.services {
  color: #666;
  font-size: 13px;
  margin-bottom: auto;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 40px;
  flex-shrink: 0;
}

.hospital-info {
  margin-top: 10px;
  flex-shrink: 0;
}

.hospital-info .address,
.hospital-info .phone {
  color: #666;
  font-size: 12px;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.5;
}

.hospital-info .address:last-child,
.hospital-info .phone:last-child {
  margin-bottom: 0;
}

.hospital-info i {
  color: #2c5f2d;
  margin-right: 5px;
}

/* 宠物信息（配种、寄养、领养） */
.pet-info {
  color: #606266;
  font-size: 14px;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex-shrink: 0;
}

.tags {
  display: flex;
  gap: 5px;
  margin-bottom: auto;
  margin-top: 0;
  flex-wrap: wrap;
  min-height: 24px;
  flex-shrink: 0;
}

.status-tag {
  color: #2c5f2d;
  font-size: 14px;
  font-weight: 500;
  margin-top: 10px;
  flex-shrink: 0;
}

/* 论坛卡片 */
.forum-card >>> .card-content {
  padding-bottom: 45px;
  position: relative;
}

.forum-card .description {
  margin-bottom: auto;
  flex: 1;
  min-height: 40px;
  max-height: 60px;
}

.forum-stats {
  display: flex;
  gap: 15px;
  color: #999;
  font-size: 13px;
  margin-bottom: 10px;
  margin-top: 0;
  flex-shrink: 0;
  height: 20px;
  align-items: center;
}

.forum-stats span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.forum-stats i {
  font-size: 14px;
}

.author-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  color: #999;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 10px 15px;
  border-top: 1px solid #e4e7ed;
  background: #fafafa;
  height: 45px;
  box-sizing: border-box;
}

.author-info i {
  font-size: 14px;
}

.author-info span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .cards-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 1024px) {
  .cards-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .cards-grid {
    grid-template-columns: 1fr;
  }
  
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .carousel-item {
    height: 300px;
  }
}
</style>
