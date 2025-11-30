<template>
  <div class="insurance-list-container">
    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-card shadow="hover">
        <div class="search-form">
          <!-- 保险名称搜索 -->
          <div class="search-item">
            <label>保险名称</label>
            <el-input 
              v-model="searchForm.name" 
              placeholder="请输入保险名称" 
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
      </el-card>
    </div>

    <!-- 保险网格展示 -->
    <div class="insurance-grid">
      <el-row :gutter="20">
        <el-col 
          v-for="insurance in insuranceList" 
          :key="insurance.id"
          :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="insurance-card" @click="viewDetail(insurance.id)">
            <div class="insurance-header">
              <div class="insurance-icon">
                <i class="el-icon-s-home"></i>
              </div>
              <div class="insurance-badge">宠物保险</div>
            </div>
            <div class="insurance-info">
              <div class="insurance-name" :title="insurance.name">{{ insurance.name }}</div>
              <div class="insurance-coverage" v-if="insurance.coverage">
                <i class="el-icon-circle-check"></i>
                <span>{{ truncateText(insurance.coverage, 50) }}</span>
              </div>
              <div class="insurance-limit" v-if="insurance.claimLimit">
                <i class="el-icon-document-checked"></i>
                <span>{{ insurance.claimLimit }}</span>
              </div>
              <div class="insurance-footer">
                <div class="insurance-price">
                  <span class="price-symbol">¥</span>
                  <span class="price-value">{{ insurance.price }}</span>
                  <span class="price-unit">/年</span>
                </div>
                <el-button 
                  type="primary" 
                  size="small" 
                  icon="el-icon-view"
                  @click.stop="viewDetail(insurance.id)">
                  查看详情
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <el-empty v-if="insuranceList.length === 0" description="暂无保险产品"></el-empty>
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
  </div>
</template>

<script>
import { getUserInsuranceList } from '@/api/insurance';

export default {
  name: 'UserInsuranceList',
  data() {
    return {
      searchForm: {
        name: '',
        minPrice: null,
        maxPrice: null
      },
      insuranceList: [],
      currentPage: 1,
      pageSize: 12,
      total: 0,
      loading: false
    };
  },
  created() {
    this.loadInsurances();
  },
  methods: {
    // 加载保险列表
    async loadInsurances() {
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
        // 价格参数：只有在有值且大于0时才传递
        if (this.searchForm.minPrice !== null && this.searchForm.minPrice !== '' && this.searchForm.minPrice > 0) {
          params.minPrice = this.searchForm.minPrice;
        }
        if (this.searchForm.maxPrice !== null && this.searchForm.maxPrice !== '' && this.searchForm.maxPrice > 0) {
          params.maxPrice = this.searchForm.maxPrice;
        }
        
        const response = await getUserInsuranceList(params);
        if (response.data.success) {
          this.insuranceList = response.data.data.records || [];
          this.total = response.data.data.total || 0;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加载保险列表失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载保险列表失败，请稍后重试',
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
      this.loadInsurances();
    },
    
    // 重置
    handleReset() {
      this.searchForm = {
        name: '',
        minPrice: null,
        maxPrice: null
      };
      this.handleSearch();
    },
    
    // 翻页
    handlePageChange(page) {
      this.currentPage = page;
      this.loadInsurances();
      // 滚动到顶部
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    
    // 截断文本
    truncateText(text, length) {
      if (!text) return '';
      if (text.length <= length) return text;
      return text.substring(0, length) + '...';
    },
    
    // 查看详情
    viewDetail(id) {
      this.$router.push(`/user/insurance-detail/${id}`);
    }
  }
};
</script>

<style scoped>
.insurance-list-container {
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

/* 保险网格 */
.insurance-grid {
  min-height: 400px;
}

.insurance-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
  height: 340px;
  display: flex;
  flex-direction: column;
}

.insurance-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

/* 保险头部 */
.insurance-header {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  padding: 25px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100px;
}

.insurance-icon {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.insurance-icon i {
  font-size: 32px;
  color: white;
}

.insurance-badge {
  background: rgba(255, 255, 255, 0.3);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

/* 保险信息 */
.insurance-info {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.insurance-name {
  font-size: 18px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.insurance-coverage,
.insurance-limit {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 13px;
  color: #606266;
  margin-bottom: 10px;
  line-height: 1.6;
}

.insurance-coverage i,
.insurance-limit i {
  font-size: 16px;
  color: #1e3a8a;
  margin-top: 2px;
  flex-shrink: 0;
}

.insurance-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
  padding-top: 15px;
  border-top: 1px solid #e4e7ed;
}

.insurance-price {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.price-symbol {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
}

.price-value {
  font-size: 28px;
  color: #f56c6c;
  font-weight: 700;
}

.price-unit {
  font-size: 14px;
  color: #909399;
  margin-left: 4px;
}

/* 分页 */
.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
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

  .insurance-card {
    height: auto;
  }

  .insurance-footer {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .insurance-footer .el-button {
    width: 100%;
  }
}
</style>

