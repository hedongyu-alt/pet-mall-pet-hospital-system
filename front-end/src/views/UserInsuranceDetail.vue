<template>
  <div class="insurance-detail-container">
    <el-card shadow="hover">
      <!-- 返回按钮 -->
      <div class="back-button">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回保险列表</el-button>
      </div>

      <el-row :gutter="40" v-if="insurance">
        <!-- 左侧：图标展示 -->
        <el-col :xs="24" :sm="24" :md="10" :lg="10">
          <div class="icon-section">
            <div class="insurance-icon-container">
              <div class="insurance-large-icon">
                <i class="el-icon-s-home"></i>
              </div>
              <div class="insurance-badge">宠物保险产品</div>
            </div>
            
            <!-- 价格展示 -->
            <div class="price-section">
              <div class="price-label">保险费用</div>
              <div class="price-value">
                <span class="symbol">¥</span>
                <span class="value">{{ insurance.price }}</span>
                <span class="unit">/年</span>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button 
                type="primary" 
                size="large" 
                icon="el-icon-shopping-cart-2"
                @click="showPurchaseDialog">
                购买保险
              </el-button>
            </div>

            <!-- 额外信息 -->
            <div class="extra-info">
              <div class="info-row">
                <i class="el-icon-circle-check"></i>
                <span>正规保险公司承保</span>
              </div>
              <div class="info-row">
                <i class="el-icon-phone-outline"></i>
                <span>24小时客服热线</span>
              </div>
              <div class="info-row">
                <i class="el-icon-document-checked"></i>
                <span>快速理赔服务</span>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧：保险信息 -->
        <el-col :xs="24" :sm="24" :md="14" :lg="14">
          <div class="info-section">
            <h1 class="insurance-title">{{ insurance.name }}</h1>
            
            <!-- 保险详情 -->
            <div class="detail-item">
              <div class="detail-label">
                <i class="el-icon-document"></i>
                <span>保险详情</span>
              </div>
              <div class="detail-content">{{ insurance.detail || '暂无详情' }}</div>
            </div>

            <!-- 保险范围 -->
            <div class="detail-item">
              <div class="detail-label">
                <i class="el-icon-circle-check"></i>
                <span>保险范围</span>
              </div>
              <div class="detail-content coverage-content">
                <div 
                  v-for="(item, index) in getCoverageList(insurance.coverage)" 
                  :key="index"
                  class="coverage-item">
                  <i class="el-icon-success"></i>
                  <span>{{ item }}</span>
                </div>
              </div>
            </div>

            <!-- 理赔限额 -->
            <div class="detail-item">
              <div class="detail-label">
                <i class="el-icon-wallet"></i>
                <span>理赔限额</span>
              </div>
              <div class="detail-content highlight-content">{{ insurance.claimLimit || '暂无信息' }}</div>
            </div>

            <!-- 理赔须知 -->
            <div class="detail-item">
              <div class="detail-label">
                <i class="el-icon-warning-outline"></i>
                <span>理赔须知</span>
              </div>
              <div class="detail-content notice-content">{{ insurance.claimNotice || '暂无须知' }}</div>
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

    <!-- 购买保险弹窗 -->
    <el-dialog
      title="购买保险"
      :visible.sync="purchaseDialogVisible"
      width="600px"
      :close-on-click-modal="false">
      <el-form :model="purchaseForm" :rules="purchaseRules" ref="purchaseForm" label-width="120px">
        <div class="form-section">
          <div class="section-title">宠物信息</div>
          <el-form-item label="宠物名称" prop="petName">
            <el-input v-model="purchaseForm.petName" placeholder="请输入宠物名称"></el-input>
          </el-form-item>
          <el-form-item label="宠物类型" prop="petType">
            <div class="filter-buttons">
              <div 
                class="filter-btn" 
                :class="{ active: purchaseForm.petType === '狗' }"
                @click="purchaseForm.petType = '狗'">
                狗
              </div>
              <div 
                class="filter-btn"
                :class="{ active: purchaseForm.petType === '猫' }"
                @click="purchaseForm.petType = '猫'">
                猫
              </div>
            </div>
          </el-form-item>
          <el-form-item label="宠物年龄" prop="petAge">
            <el-input-number 
              v-model="purchaseForm.petAge" 
              :min="0" 
              :max="30"
              controls-position="right"
              placeholder="请输入宠物年龄">
            </el-input-number>
            <span style="margin-left: 10px; color: #909399;">岁</span>
          </el-form-item>
          <el-form-item label="宠物品种" prop="petBreed">
            <el-input v-model="purchaseForm.petBreed" placeholder="请输入宠物品种（选填）"></el-input>
          </el-form-item>
        </div>

        <div class="form-section">
          <div class="section-title">收款信息</div>
          <el-form-item label="收款人姓名" prop="payeeName">
            <el-input v-model="purchaseForm.payeeName" placeholder="请输入收款人姓名"></el-input>
          </el-form-item>
          <el-form-item label="收款人电话" prop="payeePhone">
            <el-input v-model="purchaseForm.payeePhone" placeholder="请输入收款人电话"></el-input>
          </el-form-item>
          <el-form-item label="收款账号" prop="payeeAccount">
            <el-input v-model="purchaseForm.payeeAccount" placeholder="请输入收款账号"></el-input>
          </el-form-item>
        </div>

        <div class="form-section">
          <div class="section-title">保险生效信息</div>
          <el-form-item label="生效日期" prop="startDate">
            <el-date-picker
              v-model="purchaseForm.startDate"
              type="date"
              placeholder="选择生效日期"
              value-format="yyyy-MM-dd"
              :picker-options="datePickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="保险费用">
            <div class="price-display">
              <span class="price-symbol">¥</span>
              <span class="price-value">{{ insurance ? insurance.price : 0 }}</span>
              <span class="price-unit">/年</span>
            </div>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="purchaseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPurchase">确认购买</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserInsuranceDetail } from '@/api/insurance';
import { purchaseInsurance } from '@/api/insuranceOrder';

export default {
  name: 'UserInsuranceDetail',
  data() {
    return {
      insurance: null,
      loading: false,
      purchaseDialogVisible: false,
      purchaseForm: {
        petName: '',
        petType: '',
        petAge: null,
        petBreed: '',
        payeeName: '',
        payeePhone: '',
        payeeAccount: '',
        startDate: ''
      },
      purchaseRules: {
        petName: [
          { required: true, message: '请输入宠物名称', trigger: 'blur' }
        ],
        petType: [
          { required: true, message: '请选择宠物类型', trigger: 'change' }
        ],
        petAge: [
          { required: true, message: '请输入宠物年龄', trigger: 'blur' }
        ],
        payeeName: [
          { required: true, message: '请输入收款人姓名', trigger: 'blur' }
        ],
        payeePhone: [
          { required: true, message: '请输入收款人电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        payeeAccount: [
          { required: true, message: '请输入收款账号', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ]
      },
      datePickerOptions: {
        disabledDate(time) {
          // 禁用今天之前的日期
          return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        }
      }
    };
  },
  created() {
    this.loadInsuranceDetail();
  },
  methods: {
    // 加载保险详情
    async loadInsuranceDetail() {
      this.loading = true;
      try {
        const id = this.$route.params.id;
        const response = await getUserInsuranceDetail(id);
        
        if (response.data.success) {
          this.insurance = response.data.data;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加载保险详情失败',
            type: 'error',
            duration: 2000
          });
          this.goBack();
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载保险详情失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
        this.goBack();
      } finally {
        this.loading = false;
      }
    },
    
    // 获取保险范围列表
    getCoverageList(coverage) {
      if (!coverage) return [];
      // 按中文分隔符分割（、或，）
      return coverage.split(/[、，,]/).filter(item => item.trim());
    },
    
    // 返回
    goBack() {
      this.$router.push('/user/insurance-list');
    },
    
    // 显示购买弹窗
    showPurchaseDialog() {
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
      
      // 重置表单
      this.purchaseForm = {
        petName: '',
        petType: '',
        petAge: null,
        petBreed: '',
        payeeName: '',
        payeePhone: '',
        payeeAccount: '',
        startDate: ''
      };
      
      // 如果有ref，清除验证
      if (this.$refs.purchaseForm) {
        this.$refs.purchaseForm.clearValidate();
      }
      
      this.purchaseDialogVisible = true;
    },
    
    // 确认购买
    confirmPurchase() {
      this.$refs.purchaseForm.validate(async (valid) => {
        if (valid) {
          try {
            const userInfo = JSON.parse(localStorage.getItem('userInfo'));
            
            const purchaseData = {
              userId: userInfo.id,
              insuranceId: this.insurance.id,
              petName: this.purchaseForm.petName,
              petType: this.purchaseForm.petType,
              petAge: this.purchaseForm.petAge,
              petBreed: this.purchaseForm.petBreed,
              payeeName: this.purchaseForm.payeeName,
              payeePhone: this.purchaseForm.payeePhone,
              payeeAccount: this.purchaseForm.payeeAccount,
              startDate: this.purchaseForm.startDate
            };
            
            const response = await purchaseInsurance(purchaseData);
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: '购买成功',
                type: 'success',
                duration: 2000
              });
              this.purchaseDialogVisible = false;
              // 跳转到我的保单页面
              this.$router.push('/user/policy-list');
            } else {
              this.$notify({
                title: '失败',
                message: response.data.message || '购买失败',
                type: 'error',
                duration: 2000
              });
            }
          } catch (error) {
            this.$notify({
              title: '错误',
              message: '购买失败，请稍后重试',
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
.insurance-detail-container {
  min-height: calc(100vh - 130px);
  margin-bottom: 30px;
}

.back-button {
  margin-bottom: 20px;
}

/* 图标区域 */
.icon-section {
  position: sticky;
  top: 90px;
}

.insurance-icon-container {
  position: relative;
  width: 100%;
  padding: 60px 20px;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.insurance-large-icon {
  width: 150px;
  height: 150px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  transition: all 0.3s;
}

.insurance-large-icon:hover {
  transform: scale(1.05);
}

.insurance-large-icon i {
  font-size: 80px;
  color: white;
}

.insurance-badge {
  background: rgba(255, 255, 255, 0.3);
  color: white;
  padding: 10px 20px;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

/* 价格区域 */
.price-section {
  background: linear-gradient(135deg, #fff4e6 0%, #ffe7cc 100%);
  padding: 25px;
  border-radius: 12px;
  margin-bottom: 20px;
  text-align: center;
}

.price-label {
  font-size: 15px;
  color: #606266;
  margin-bottom: 10px;
  font-weight: 500;
}

.price-value {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 3px;
}

.price-value .symbol {
  font-size: 28px;
  color: #f56c6c;
  font-weight: 700;
}

.price-value .value {
  font-size: 48px;
  color: #f56c6c;
  font-weight: 700;
}

.price-value .unit {
  font-size: 18px;
  color: #909399;
  margin-left: 5px;
}

/* 操作按钮 */
.action-buttons {
  width: 100%;
  margin-bottom: 20px;
}

.action-buttons .el-button {
  width: 100%;
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
  color: #1e3a8a;
}

/* 保险信息区域 */
.info-section {
  padding: 20px 0;
}

.insurance-title {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 30px;
  line-height: 1.4;
  border-bottom: 3px solid #1e3a8a;
  padding-bottom: 15px;
}

/* 详情项 */
.detail-item {
  margin-bottom: 30px;
  border-radius: 12px;
  border: 1px solid #e4e7ed;
  padding: 20px;
  transition: all 0.3s;
}

.detail-item:hover {
  border-color: #1e3a8a;
  box-shadow: 0 4px 12px rgba(30, 58, 138, 0.1);
}

.detail-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e4e7ed;
}

.detail-label i {
  font-size: 18px;
  color: #1e3a8a;
}

.detail-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
  word-break: break-word;
}

/* 保险范围内容 */
.coverage-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.coverage-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px;
  background: #f0f9ff;
  border-radius: 8px;
  transition: all 0.3s;
}

.coverage-item:hover {
  background: #dbeafe;
  transform: translateX(5px);
}

.coverage-item i {
  font-size: 16px;
  color: #1e3a8a;
  margin-top: 2px;
  flex-shrink: 0;
}

.coverage-item span {
  flex: 1;
  font-size: 14px;
  color: #303133;
}

/* 高亮内容 */
.highlight-content {
  background: #fff4e6;
  padding: 15px;
  border-radius: 8px;
  font-weight: 600;
  color: #f56c6c;
  font-size: 15px;
}

/* 须知内容 */
.notice-content {
  background: #fef3f3;
  padding: 15px;
  border-radius: 8px;
  border-left: 4px solid #f56c6c;
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
  color: #1e3a8a;
}

.loading-container span {
  font-size: 16px;
  color: #909399;
}

/* 购买弹窗样式 */
.form-section {
  margin-bottom: 25px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.form-section:last-of-type {
  margin-bottom: 0;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #1e3a8a;
}

.filter-buttons {
  display: flex;
  gap: 10px;
}

.filter-btn {
  padding: 8px 24px;
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
  background: #1e3a8a;
  border-color: #1e3a8a;
  font-weight: 600;
}

.price-display {
  display: flex;
  align-items: baseline;
  gap: 3px;
}

.price-display .price-symbol {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
}

.price-display .price-value {
  font-size: 28px;
  color: #f56c6c;
  font-weight: 700;
}

.price-display .price-unit {
  font-size: 14px;
  color: #909399;
  margin-left: 4px;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .icon-section {
    position: relative;
    top: 0;
    margin-bottom: 30px;
  }

  .insurance-title {
    font-size: 26px;
  }
}

@media (max-width: 768px) {
  .insurance-title {
    font-size: 22px;
  }

  .price-value .value {
    font-size: 36px;
  }

  .insurance-large-icon {
    width: 120px;
    height: 120px;
  }

  .insurance-large-icon i {
    font-size: 60px;
  }
}
</style>

