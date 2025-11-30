<template>
  <div class="product-list-container">
    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-card shadow="hover">
        <div class="search-form">
          <!-- 商品名称搜索 -->
          <div class="search-item">
            <label>商品名称</label>
            <el-input 
              v-model="searchForm.name" 
              placeholder="请输入商品名称" 
              clearable
              @clear="handleSearch">
            </el-input>
          </div>

          <!-- 品牌搜索 -->
          <div class="search-item">
            <label>品牌</label>
            <el-input 
              v-model="searchForm.brand" 
              placeholder="请输入品牌" 
              clearable
              @clear="handleSearch">
            </el-input>
          </div>

          <!-- 价格范围 -->
          <div class="search-item">
            <label>价格范围</label>
            <div class="price-range">
              <el-input-number 
                v-model="searchForm.minPrice" 
                :min="0" 
                :precision="2"
                placeholder="最低价"
                controls-position="right">
              </el-input-number>
              <span style="margin: 0 8px;">-</span>
              <el-input-number 
                v-model="searchForm.maxPrice" 
                :min="0" 
                :precision="2"
                placeholder="最高价"
                controls-position="right">
              </el-input-number>
            </div>
          </div>

          <!-- 搜索按钮 -->
          <div class="search-buttons">
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
          </div>
        </div>

        <!-- 筛选按钮区域 -->
        <div class="filter-section">
          <div class="filter-group">
            <label>商品分类：</label>
            <div class="filter-buttons">
              <div 
                class="filter-btn" 
                :class="{ active: searchForm.categoryId === '' }"
                @click="filterByCategory('')">
                全部
              </div>
              <div 
                v-for="category in categories" 
                :key="category.id"
                class="filter-btn"
                :class="{ active: searchForm.categoryId === category.id }"
                @click="filterByCategory(category.id)">
                {{ category.name }}
              </div>
            </div>
          </div>

          <div class="filter-group">
            <label>宠物类型：</label>
            <div class="filter-buttons">
              <div 
                class="filter-btn" 
                :class="{ active: searchForm.petType === '' }"
                @click="filterByPetType('')">
                全部
              </div>
              <div 
                class="filter-btn"
                :class="{ active: searchForm.petType === '狗' }"
                @click="filterByPetType('狗')">
                狗
              </div>
              <div 
                class="filter-btn"
                :class="{ active: searchForm.petType === '猫' }"
                @click="filterByPetType('猫')">
                猫
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 商品网格展示 -->
    <div class="product-grid">
      <el-row :gutter="20">
        <el-col 
          v-for="product in productList" 
          :key="product.id"
          :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="product-card" @click="viewDetail(product.id)">
            <div class="product-image">
              <img :src="getFirstImage(product.images)" :alt="product.name">
              <div class="product-overlay">
                <el-button type="primary" size="small" icon="el-icon-view">查看详情</el-button>
              </div>
            </div>
            <div class="product-info">
              <div class="product-name" :title="product.name">{{ product.name }}</div>
              <div class="product-category">{{ product.categoryName }} · {{ product.petType }}</div>
              <div class="product-brand" v-if="product.brand">品牌：{{ product.brand }}</div>
              <div class="product-footer">
                <div class="product-price">
                  <span class="price-symbol">¥</span>
                  <span class="price-value">{{ product.price }}</span>
                </div>
                <el-button 
                  type="warning" 
                  size="small" 
                  icon="el-icon-shopping-cart-2"
                  @click.stop="addToCart(product)">
                  加入购物车
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <el-empty v-if="productList.length === 0" description="暂无商品"></el-empty>
    </div>

    <!-- 分页 -->
    <div class="pagination-section" v-if="total > 0">
      <el-pagination
        background
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <!-- 加入购物车弹窗 -->
    <el-dialog
      title="加入购物车"
      :visible.sync="cartDialogVisible"
      width="400px"
      :close-on-click-modal="false">
      <div class="cart-dialog-content">
        <div class="dialog-product-info">
          <img :src="selectedProduct.image" :alt="selectedProduct.name">
          <div class="dialog-product-name">{{ selectedProduct.name }}</div>
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
import { getUserProductList } from '@/api/product';
import { getAllCategories } from '@/api/category';
import { addToCart } from '@/api/cart';

export default {
  name: 'UserProductList',
  data() {
    return {
      searchForm: {
        name: '',
        brand: '',
        categoryId: '',
        petType: '',
        minPrice: null,
        maxPrice: null
      },
      productList: [],
      categories: [],
      currentPage: 1,
      pageSize: 12,
      total: 0,
      loading: false,
      cartDialogVisible: false,
      cartQuantity: 1,
      selectedProduct: {
        id: null,
        name: '',
        image: ''
      }
    };
  },
  created() {
    this.loadCategories();
    this.loadProducts();
  },
  methods: {
    // 加载商品分类
    async loadCategories() {
      try {
        const response = await getAllCategories();
        if (response.data.success) {
          this.categories = response.data.data.records || [];
        }
      } catch (error) {
        console.error('加载分类失败：', error);
      }
    },
    
    // 加载商品列表
    async loadProducts() {
      this.loading = true;
      try {
        // 构建参数，过滤掉空字符串和null
        const params = {
          page: this.currentPage,
          size: this.pageSize
        };
        
        // 只添加有值的参数
        if (this.searchForm.name) {
          params.name = this.searchForm.name;
        }
        if (this.searchForm.brand) {
          params.brand = this.searchForm.brand;
        }
        if (this.searchForm.categoryId) {
          params.categoryId = this.searchForm.categoryId;
        }
        if (this.searchForm.petType) {
          params.petType = this.searchForm.petType;
        }
        // 价格参数：只有在有值且大于0时才传递
        if (this.searchForm.minPrice !== null && this.searchForm.minPrice !== '' && this.searchForm.minPrice > 0) {
          params.minPrice = this.searchForm.minPrice;
        }
        if (this.searchForm.maxPrice !== null && this.searchForm.maxPrice !== '' && this.searchForm.maxPrice > 0) {
          params.maxPrice = this.searchForm.maxPrice;
        }
        
        const response = await getUserProductList(params);
        if (response.data.success) {
          this.productList = response.data.data.records || [];
          this.total = response.data.data.total || 0;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加载商品失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载商品失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 搜索
    handleSearch() {
      this.currentPage = 1;
      this.loadProducts();
    },
    
    // 重置
    handleReset() {
      this.searchForm = {
        name: '',
        brand: '',
        categoryId: '',
        petType: '',
        minPrice: null,
        maxPrice: null
      };
      this.handleSearch();
    },
    
    // 按分类筛选
    filterByCategory(categoryId) {
      this.searchForm.categoryId = categoryId;
      this.handleSearch();
    },
    
    // 按宠物类型筛选
    filterByPetType(petType) {
      this.searchForm.petType = petType;
      this.handleSearch();
    },
    
    // 翻页
    handlePageChange(page) {
      this.currentPage = page;
      this.loadProducts();
      // 滚动到顶部
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    
    // 获取第一张图片
    getFirstImage(images) {
      if (!images) {
        return 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
      }
      const imageArray = images.split(',');
      return imageArray[0] || 'https://img1.baidu.com/it/u=2531584226,2194794184&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
    },
    
    // 查看详情
    viewDetail(id) {
      this.$router.push(`/user/product-detail/${id}`);
    },
    
    // 加入购物车
    addToCart(product) {
      // 设置选中的商品信息
      this.selectedProduct = {
        id: product.id,
        name: product.name,
        image: this.getFirstImage(product.images)
      };
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
          productId: this.selectedProduct.id,
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
    }
  }
};
</script>

<style scoped>
.product-list-container {
  min-height: calc(100vh - 130px);
  padding-bottom: 20px;
}

/* 搜索筛选区域 */
.search-section {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: flex-end;
}

.search-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.search-item label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.search-item .el-input {
  width: 200px;
}

.price-range {
  display: flex;
  align-items: center;
}

.price-range .el-input-number {
  width: 130px;
}

.search-buttons {
  display: flex;
  gap: 10px;
}

/* 筛选按钮区域 */
.filter-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.filter-group {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.filter-group:last-child {
  margin-bottom: 0;
}

.filter-group > label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  min-width: 90px;
}

.filter-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-btn {
  padding: 8px 20px;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s;
  background: white;
}

.filter-btn:hover {
  color: #2c5f2d;
  border-color: #2c5f2d;
  background: #f0f9f0;
}

.filter-btn.active {
  color: white;
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  border-color: #2c5f2d;
  font-weight: 600;
}

/* 商品网格 */
.product-grid {
  min-height: 400px;
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
  height: 420px;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.product-image {
  position: relative;
  width: 100%;
  height: 260px;
  overflow: hidden;
  background: #f5f7fa;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-card:hover .product-image img {
  transform: scale(1.1);
}

.product-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.product-info {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-category {
  font-size: 13px;
  color: #909399;
  margin-bottom: 5px;
}

.product-brand {
  font-size: 13px;
  color: #606266;
  margin-bottom: 10px;
}

.product-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
  padding-top: 10px;
  border-top: 1px solid #e4e7ed;
}

.product-price {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
}

.price-value {
  font-size: 24px;
  color: #f56c6c;
  font-weight: 700;
  margin-left: 2px;
}

/* 分页 */
.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
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
@media (max-width: 768px) {
  .search-form {
    flex-direction: column;
    align-items: stretch;
  }

  .search-item .el-input,
  .price-range .el-input-number {
    width: 100%;
  }

  .filter-group {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .filter-group > label {
    min-width: auto;
  }

  .product-card {
    height: auto;
  }
}
</style>

