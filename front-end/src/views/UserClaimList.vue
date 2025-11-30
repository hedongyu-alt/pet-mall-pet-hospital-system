<template>
  <div class="claim-list-container">
    <div class="page-header">
      <h2>申保记录</h2>
      <p>查看您的所有理赔申请记录</p>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-card shadow="hover">
        <div class="filter-section">
          <label>申请状态：</label>
          <div class="filter-buttons">
            <div 
              class="filter-btn" 
              :class="{ active: searchStatus === '' }"
              @click="filterByStatus('')">
              全部
            </div>
            <div 
              class="filter-btn"
              :class="{ active: searchStatus === '待审核' }"
              @click="filterByStatus('待审核')">
              待审核
            </div>
            <div 
              class="filter-btn"
              :class="{ active: searchStatus === '审核通过' }"
              @click="filterByStatus('审核通过')">
              审核通过
            </div>
            <div 
              class="filter-btn"
              :class="{ active: searchStatus === '审核拒绝' }"
              @click="filterByStatus('审核拒绝')">
              审核拒绝
            </div>
            <div 
              class="filter-btn"
              :class="{ active: searchStatus === '已打款' }"
              @click="filterByStatus('已打款')">
              已打款
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 申保记录列表 -->
    <div class="claim-list">
      <el-card 
        v-for="claim in claimList" 
        :key="claim.id"
        shadow="hover"
        class="claim-item">
        <div class="claim-header">
          <div class="claim-info">
            <div class="claim-no">理赔编号：{{ claim.claimNo }}</div>
            <el-tag 
              :type="getStatusType(claim.status)"
              size="medium">
              {{ claim.status }}
            </el-tag>
          </div>
          <div class="claim-time">{{ formatDate(claim.createTime) }}</div>
        </div>

        <el-row :gutter="30">
          <el-col :xs="24" :sm="24" :md="16" :lg="16">
            <div class="claim-detail">
              <div class="detail-row">
                <span class="label">保险名称：</span>
                <span class="value">{{ claim.insuranceName }}</span>
              </div>
              <div class="detail-row">
                <span class="label">宠物信息：</span>
                <span class="value">{{ claim.petName }}</span>
              </div>
              <div class="detail-row">
                <span class="label">申请原因：</span>
                <span class="value">{{ claim.reason }}</span>
              </div>
              <div class="detail-row">
                <span class="label">情况描述：</span>
                <span class="value description-text">{{ claim.description }}</span>
              </div>
              
              <!-- 理赔金额 -->
              <div class="detail-row" v-if="claim.claimAmount">
                <span class="label">理赔金额：</span>
                <span class="value amount">¥{{ claim.claimAmount }}</span>
              </div>
              
              <!-- 拒绝原因 -->
              <div class="detail-row reject-row" v-if="claim.status === '审核拒绝' && claim.rejectReason">
                <span class="label">拒绝原因：</span>
                <span class="value">{{ claim.rejectReason }}</span>
              </div>
              
              <!-- 管理员备注 -->
              <div class="detail-row" v-if="claim.adminRemark">
                <span class="label">备注信息：</span>
                <span class="value">{{ claim.adminRemark }}</span>
              </div>
            </div>
          </el-col>

          <el-col :xs="24" :sm="24" :md="8" :lg="8">
            <div class="evidence-section" v-if="claim.evidenceImages">
              <div class="evidence-label">佐证图片：</div>
              <div class="evidence-images">
                <el-image
                  v-for="(image, index) in getImageList(claim.evidenceImages)"
                  :key="index"
                  :src="image"
                  :preview-src-list="getImageList(claim.evidenceImages)"
                  fit="cover"
                  class="evidence-image">
                </el-image>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 空状态 -->
      <el-empty v-if="claimList.length === 0" description="暂无申保记录"></el-empty>
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
import { getUserClaims } from '@/api/insuranceClaim';

export default {
  name: 'UserClaimList',
  data() {
    return {
      claimList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      searchStatus: ''
    };
  },
  created() {
    this.loadClaimList();
  },
  methods: {
    // 加载申保记录列表
    async loadClaimList() {
      this.loading = true;
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
        
        const params = {
          userId: userInfo.id,
          page: this.currentPage,
          size: this.pageSize
        };
        
        // 如果有状态筛选
        if (this.searchStatus) {
          params.status = this.searchStatus;
        }
        
        const response = await getUserClaims(params);
        if (response.data.success) {
          this.claimList = response.data.data.records || [];
          this.total = response.data.data.total || 0;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加载申保记录失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载申保记录失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 按状态筛选
    filterByStatus(status) {
      this.searchStatus = status;
      this.currentPage = 1;
      this.loadClaimList();
    },
    
    // 翻页
    handlePageChange(page) {
      this.currentPage = page;
      this.loadClaimList();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hour = String(d.getHours()).padStart(2, '0');
      const minute = String(d.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hour}:${minute}`;
    },
    
    // 获取图片列表
    getImageList(images) {
      if (!images) return [];
      return images.split(',').filter(img => img.trim());
    },
    
    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        '待审核': 'warning',
        '审核通过': 'success',
        '审核拒绝': 'danger',
        '已打款': 'success'
      };
      return typeMap[status] || 'info';
    }
  }
};
</script>

<style scoped>
.claim-list-container {
  min-height: calc(100vh - 130px);
  padding-bottom: 20px;
}

/* 页面标题 */
.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.page-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 15px;
  color: #909399;
}

/* 搜索筛选区域 */
.search-section {
  margin-bottom: 20px;
}

.filter-section {
  display: flex;
  align-items: center;
}

.filter-section > label {
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
  color: #1e3a8a;
  border-color: #1e3a8a;
  background: #f0f9ff;
}

.filter-btn.active {
  color: white;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  border-color: #1e3a8a;
  font-weight: 600;
}

/* 申保记录列表 */
.claim-list {
  min-height: 400px;
}

.claim-item {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.claim-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

/* 申保头部 */
.claim-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  margin-bottom: 20px;
  border-bottom: 2px solid #e4e7ed;
}

.claim-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.claim-no {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.claim-time {
  font-size: 14px;
  color: #909399;
}

/* 申保详情 */
.claim-detail {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-row {
  display: flex;
  align-items: flex-start;
  font-size: 14px;
  line-height: 1.8;
}

.detail-row .label {
  color: #606266;
  font-weight: 500;
  min-width: 90px;
  flex-shrink: 0;
}

.detail-row .value {
  color: #303133;
  flex: 1;
  word-break: break-word;
}

.detail-row .value.description-text {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 6px;
  line-height: 1.8;
}

.detail-row .value.amount {
  font-size: 20px;
  font-weight: 700;
  color: #f56c6c;
}

.detail-row.reject-row {
  background: #fef3f3;
  padding: 10px;
  border-radius: 6px;
  border-left: 4px solid #f56c6c;
}

.detail-row.reject-row .value {
  color: #f56c6c;
}

/* 佐证图片 */
.evidence-section {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.evidence-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  margin-bottom: 10px;
}

.evidence-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 10px;
  flex: 1;
}

.evidence-image {
  width: 100%;
  height: 100px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.evidence-image:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 分页 */
.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header h2 {
    font-size: 24px;
  }

  .filter-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .filter-section > label {
    min-width: auto;
  }

  .claim-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .claim-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .detail-row {
    flex-direction: column;
  }

  .detail-row .label {
    margin-bottom: 4px;
  }

  .evidence-images {
    grid-template-columns: repeat(3, 1fr);
  }
}
</style>

