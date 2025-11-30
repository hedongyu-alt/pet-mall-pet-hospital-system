<template>
  <div class="policy-list-container">
    <div class="page-header">
      <h2>我的保单</h2>
      <p>查看您购买的所有保险产品</p>
    </div>

    <!-- 保单列表 -->
    <div class="policy-grid">
      <el-row :gutter="20">
        <el-col 
          v-for="policy in policyList" 
          :key="policy.id"
          :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
          <div class="policy-card">
            <div class="policy-header">
              <div class="insurance-icon">
                <i class="el-icon-s-home"></i>
              </div>
              <el-tag 
                :type="policy.status === '生效中' ? 'success' : 'info'"
                size="medium">
                {{ policy.status }}
              </el-tag>
            </div>
            <div class="policy-body">
              <div class="insurance-name" :title="policy.insuranceName">{{ policy.insuranceName }}</div>
              <div class="policy-info-row">
                <span class="label">订单编号：</span>
                <span class="value">{{ policy.orderNo }}</span>
              </div>
              <div class="policy-info-row">
                <span class="label">宠物信息：</span>
                <span class="value">{{ policy.petName }} / {{ policy.petType }} / {{ policy.petAge }}岁</span>
              </div>
              <div class="policy-info-row">
                <span class="label">保险期限：</span>
                <span class="value">{{ formatDate(policy.startDate) }} 至 {{ formatDate(policy.endDate) }}</span>
              </div>
              <div class="policy-price">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ policy.price }}</span>
                <span class="price-unit">/年</span>
              </div>
            </div>
            <div class="policy-footer">
              <el-button 
                type="primary" 
                size="small" 
                icon="el-icon-document-add"
                :disabled="policy.status !== '生效中'"
                @click="showClaimDialog(policy)">
                申请理赔
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <el-empty v-if="policyList.length === 0" description="暂无保单"></el-empty>
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

    <!-- 理赔申请弹窗 -->
    <el-dialog
      title="申请理赔"
      :visible.sync="claimDialogVisible"
      width="700px"
      :close-on-click-modal="false">
      <div class="claim-dialog-content" v-if="selectedPolicy">
        <div class="policy-summary">
          <div class="summary-item">
            <span class="summary-label">保险名称：</span>
            <span class="summary-value">{{ selectedPolicy.insuranceName }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">宠物信息：</span>
            <span class="summary-value">{{ selectedPolicy.petName }} / {{ selectedPolicy.petType }}</span>
          </div>
        </div>

        <el-form :model="claimForm" :rules="claimRules" ref="claimForm" label-width="100px">
          <el-form-item label="申请原因" prop="reason">
            <el-input 
              v-model="claimForm.reason" 
              placeholder="请输入申请原因"
              maxlength="100"
              show-word-limit>
            </el-input>
          </el-form-item>
          <el-form-item label="情况描述" prop="description">
            <el-input 
              type="textarea" 
              v-model="claimForm.description" 
              placeholder="请详细描述事情经过和当前情况"
              :rows="5"
              maxlength="500"
              show-word-limit>
            </el-input>
          </el-form-item>
          <el-form-item label="佐证图片" prop="evidenceImages">
            <el-upload
              action="http://localhost:18007/api/file/upload"
              list-type="picture-card"
              :on-success="handleImageSuccess"
              :on-remove="handleImageRemove"
              :before-upload="beforeImageUpload"
              :limit="5"
              :file-list="imageFileList">
              <i class="el-icon-plus"></i>
              <div slot="tip" class="el-upload__tip">最多上传5张图片，单张图片不超过5MB</div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitClaim">提交申请</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserOrders } from '@/api/insuranceOrder';
import { submitClaim } from '@/api/insuranceClaim';

export default {
  name: 'UserPolicyList',
  data() {
    return {
      policyList: [],
      currentPage: 1,
      pageSize: 12,
      total: 0,
      loading: false,
      
      // 理赔申请相关
      claimDialogVisible: false,
      selectedPolicy: null,
      claimForm: {
        reason: '',
        description: '',
        evidenceImages: []
      },
      claimRules: {
        reason: [
          { required: true, message: '请输入申请原因', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入情况描述', trigger: 'blur' }
        ]
      },
      imageFileList: []
    };
  },
  created() {
    this.loadPolicyList();
  },
  methods: {
    // 加载保单列表
    async loadPolicyList() {
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
        
        const response = await getUserOrders(params);
        if (response.data.success) {
          this.policyList = response.data.data.records || [];
          this.total = response.data.data.total || 0;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加载保单列表失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载保单列表失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 翻页
    handlePageChange(page) {
      this.currentPage = page;
      this.loadPolicyList();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    
    // 显示理赔弹窗
    showClaimDialog(policy) {
      this.selectedPolicy = policy;
      this.claimForm = {
        reason: '',
        description: '',
        evidenceImages: []
      };
      this.imageFileList = [];
      
      if (this.$refs.claimForm) {
        this.$refs.claimForm.clearValidate();
      }
      
      this.claimDialogVisible = true;
    },
    
    // 图片上传成功
    handleImageSuccess(response, file, fileList) {
      if (response.success) {
        this.claimForm.evidenceImages.push(response.data);
      } else {
        this.$notify({
          title: '失败',
          message: '图片上传失败',
          type: 'error',
          duration: 2000
        });
        // 移除失败的文件
        const index = fileList.findIndex(item => item.uid === file.uid);
        if (index !== -1) {
          fileList.splice(index, 1);
        }
      }
    },
    
    // 图片移除
    handleImageRemove(file, fileList) {
      // 如果文件上传成功，从evidenceImages中移除对应的URL
      if (file.response && file.response.success) {
        const url = file.response.data;
        const index = this.claimForm.evidenceImages.indexOf(url);
        if (index !== -1) {
          this.claimForm.evidenceImages.splice(index, 1);
        }
      }
    },
    
    // 图片上传前校验
    beforeImageUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!isImage) {
        this.$notify({
          title: '提示',
          message: '只能上传图片文件',
          type: 'warning',
          duration: 2000
        });
        return false;
      }
      if (!isLt5M) {
        this.$notify({
          title: '提示',
          message: '图片大小不能超过5MB',
          type: 'warning',
          duration: 2000
        });
        return false;
      }
      return true;
    },
    
    // 提交理赔申请
    submitClaim() {
      this.$refs.claimForm.validate(async (valid) => {
        if (valid) {
          try {
            const userInfo = JSON.parse(localStorage.getItem('userInfo'));
            
            const claimData = {
              userId: userInfo.id,
              orderId: this.selectedPolicy.id,
              reason: this.claimForm.reason,
              description: this.claimForm.description,
              evidenceImages: this.claimForm.evidenceImages.join(',')
            };
            
            const response = await submitClaim(claimData);
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: '理赔申请已提交',
                type: 'success',
                duration: 2000
              });
              this.claimDialogVisible = false;
            } else {
              this.$notify({
                title: '失败',
                message: response.data.message || '提交失败',
                type: 'error',
                duration: 2000
              });
            }
          } catch (error) {
            this.$notify({
              title: '错误',
              message: '提交失败，请稍后重试',
              type: 'error',
              duration: 2000
            });
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.policy-list-container {
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

/* 保单网格 */
.policy-grid {
  min-height: 400px;
}

.policy-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
  height: 380px;
  display: flex;
  flex-direction: column;
}

.policy-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

/* 保单头部 */
.policy-header {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}

.insurance-icon {
  width: 50px;
  height: 50px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.insurance-icon i {
  font-size: 28px;
  color: white;
}

/* 保单主体 */
.policy-body {
  padding: 20px;
  flex: 1;
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

.policy-info-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
  font-size: 13px;
  line-height: 1.6;
}

.policy-info-row .label {
  color: #909399;
  min-width: 80px;
  flex-shrink: 0;
}

.policy-info-row .value {
  color: #606266;
  flex: 1;
  word-break: break-word;
}

.policy-price {
  display: flex;
  align-items: baseline;
  gap: 2px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #e4e7ed;
}

.price-symbol {
  font-size: 16px;
  color: #f56c6c;
  font-weight: 600;
}

.price-value {
  font-size: 24px;
  color: #f56c6c;
  font-weight: 700;
}

.price-unit {
  font-size: 14px;
  color: #909399;
  margin-left: 2px;
}

/* 保单底部 */
.policy-footer {
  padding: 15px 20px;
  border-top: 1px solid #e4e7ed;
  display: flex;
  justify-content: flex-end;
}

.policy-footer .el-button {
  width: 100%;
}

/* 分页 */
.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

/* 理赔弹窗 */
.claim-dialog-content {
  padding: 10px 0;
}

.policy-summary {
  background: #f0f9ff;
  padding: 15px 20px;
  border-radius: 8px;
  margin-bottom: 25px;
  border-left: 4px solid #1e3a8a;
}

.summary-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
}

.summary-item:last-child {
  margin-bottom: 0;
}

.summary-label {
  color: #606266;
  font-weight: 500;
  min-width: 90px;
}

.summary-value {
  color: #303133;
  font-weight: 600;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header h2 {
    font-size: 24px;
  }

  .policy-card {
    height: auto;
  }

  .policy-info-row {
    flex-direction: column;
  }

  .policy-info-row .label {
    margin-bottom: 4px;
  }
}
</style>

