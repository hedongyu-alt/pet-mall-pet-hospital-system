<template>
  <div class="admin-claim-manage">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <div class="search-container">
        <div class="search-row">
          <div class="search-item">
            <label>申请原因：</label>
            <el-input 
              v-model="searchParams.reason" 
              placeholder="请输入申请原因" 
              clearable
              style="width: 200px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>审核状态：</label>
            <div class="filter-buttons">
              <el-button 
                :type="searchParams.status === '' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = ''">
                全部
              </el-button>
              <el-button 
                :type="searchParams.status === '待审核' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = '待审核'">
                待审核
              </el-button>
              <el-button 
                :type="searchParams.status === '审核通过' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = '审核通过'">
                审核通过
              </el-button>
              <el-button 
                :type="searchParams.status === '审核拒绝' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = '审核拒绝'">
                审核拒绝
              </el-button>
              <el-button 
                :type="searchParams.status === '已打款' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = '已打款'">
                已打款
              </el-button>
            </div>
          </div>
          
          <div class="search-item">
            <label>时间范围：</label>
            <el-date-picker
              v-model="dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 380px;">
            </el-date-picker>
          </div>
          
          <div class="search-actions">
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 理赔列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>理赔申请列表</h3>
        <span class="total-count">共 {{ total }} 条数据</span>
      </div>
      
      <el-table 
        :data="claimList" 
        v-loading="loading"
        stripe
        style="width: 100%">
        <el-table-column prop="id" label="理赔ID" min-width="80" align="center"></el-table-column>
        
        <el-table-column prop="claimNo" label="理赔编号" min-width="180">
          <template slot-scope="scope">
            <span style="font-weight: 600; color: #303133;">{{ scope.row.claimNo }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="insuranceName" label="保险名称" min-width="200" show-overflow-tooltip></el-table-column>
        
        <el-table-column prop="petName" label="宠物名称" min-width="100" align="center"></el-table-column>
        
        <el-table-column prop="reason" label="申请原因" min-width="180" show-overflow-tooltip></el-table-column>
        
        <el-table-column label="佐证图片" min-width="150" align="center">
          <template slot-scope="scope">
            <div class="image-stack" v-if="getImageList(scope.row.evidenceImages).length > 0" @click="handleViewImages(scope.row)">
              <img 
                v-for="(img, index) in getImageList(scope.row.evidenceImages).slice(0, 3)" 
                :key="index"
                :src="img" 
                :style="{ zIndex: 3 - index, left: index * 8 + 'px' }"
                class="stack-image">
              <span v-if="getImageList(scope.row.evidenceImages).length > 3" class="image-count">+{{ getImageList(scope.row.evidenceImages).length - 3 }}</span>
            </div>
            <span v-else style="color: #999;">暂无图片</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="claimAmount" label="理赔金额" min-width="110" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.claimAmount" style="color: #f56c6c; font-weight: 600;">¥{{ scope.row.claimAmount }}</span>
            <span v-else style="color: #909399;">未定</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="审核状态" min-width="100" align="center">
          <template slot-scope="scope">
            <el-tag 
              :type="getStatusType(scope.row.status)" 
              size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="申请时间" min-width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" min-width="340" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleViewDetail(scope.row)">查看详情</el-button>
            <el-button 
              type="text" 
              icon="el-icon-edit" 
              style="color: #E6A23C;"
              @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button 
              v-if="scope.row.status === '待审核'"
              type="text" 
              icon="el-icon-check" 
              style="color: #67c23a;"
              @click="handleApprove(scope.row)">
              审核通过
            </el-button>
            <el-button 
              v-if="scope.row.status === '待审核'"
              type="text" 
              icon="el-icon-close" 
              style="color: #f56c6c;" 
              @click="handleReject(scope.row)">
              审核拒绝
            </el-button>
            <el-button 
              v-if="scope.row.status === '审核通过'"
              type="text" 
              icon="el-icon-s-finance" 
              style="color: #409eff;"
              @click="handlePay(scope.row)">
              打款
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="searchParams.page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="searchParams.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
    
    <!-- 理赔详情对话框 -->
    <el-dialog 
      title="理赔申请详情" 
      :visible.sync="detailDialogVisible" 
      width="900px"
      :close-on-click-modal="false">
      <div v-if="currentClaim" class="claim-detail">
        <!-- 理赔基本信息 -->
        <div class="detail-section">
          <h4 class="section-title">理赔信息</h4>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">理赔编号：</span>
                <span class="info-value">{{ currentClaim.claimNo }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">审核状态：</span>
                <el-tag :type="getStatusType(currentClaim.status)" size="small">{{ currentClaim.status }}</el-tag>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">保险名称：</span>
                <span class="info-value">{{ currentClaim.insuranceName }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">宠物名称：</span>
                <span class="info-value">{{ currentClaim.petName }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">申请原因：</span>
                <span class="info-value">{{ currentClaim.reason }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">理赔金额：</span>
                <span class="info-value" style="color: #f56c6c; font-weight: 600;">
                  {{ currentClaim.claimAmount ? '¥' + currentClaim.claimAmount : '未定' }}
                </span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <div class="info-item">
                <span class="info-label">情况描述：</span>
                <div class="info-value description-box">{{ currentClaim.description }}</div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="currentClaim.rejectReason">
            <el-col :span="24">
              <div class="info-item">
                <span class="info-label">拒绝原因：</span>
                <div class="info-value reject-box">{{ currentClaim.rejectReason }}</div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="currentClaim.adminRemark">
            <el-col :span="24">
              <div class="info-item">
                <span class="info-label">管理员备注：</span>
                <div class="info-value">{{ currentClaim.adminRemark }}</div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">申请时间：</span>
                <span class="info-value">{{ formatDate(currentClaim.createTime) }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
        
        <!-- 佐证图片 -->
        <div class="detail-section" v-if="currentClaim.evidenceImages">
          <h4 class="section-title">佐证图片</h4>
          <div class="evidence-images">
            <el-image
              v-for="(image, index) in getImageList(currentClaim.evidenceImages)"
              :key="index"
              :src="image"
              :preview-src-list="getImageList(currentClaim.evidenceImages)"
              fit="cover"
              class="evidence-image">
            </el-image>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    
    <!-- 审核通过对话框 -->
    <el-dialog 
      title="审核通过" 
      :visible.sync="approveDialogVisible" 
      width="600px"
      :close-on-click-modal="false">
      <el-form :model="approveForm" :rules="approveRules" ref="approveForm" label-width="120px">
        <el-form-item label="理赔编号">
          <span style="color: #606266;">{{ approveForm.claimNo }}</span>
        </el-form-item>
        <el-form-item label="理赔金额" prop="claimAmount">
          <el-input 
            v-model="approveForm.claimAmount" 
            placeholder="请输入理赔金额"
            type="number"
            style="width: 100%;">
            <template slot="prepend">¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="管理员备注" prop="adminRemark">
          <el-input 
            v-model="approveForm.adminRemark" 
            type="textarea"
            :rows="4"
            placeholder="请输入管理员备注"
            style="width: 100%;">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApprove" :loading="approveLoading">确定</el-button>
      </span>
    </el-dialog>
    
    <!-- 审核拒绝对话框 -->
    <el-dialog 
      title="审核拒绝" 
      :visible.sync="rejectDialogVisible" 
      width="600px"
      :close-on-click-modal="false">
      <el-form :model="rejectForm" :rules="rejectRules" ref="rejectForm" label-width="120px">
        <el-form-item label="理赔编号">
          <span style="color: #606266;">{{ rejectForm.claimNo }}</span>
        </el-form-item>
        <el-form-item label="拒绝原因" prop="rejectReason">
          <el-input 
            v-model="rejectForm.rejectReason" 
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因"
            style="width: 100%;">
          </el-input>
        </el-form-item>
        <el-form-item label="管理员备注" prop="adminRemark">
          <el-input 
            v-model="rejectForm.adminRemark" 
            type="textarea"
            :rows="4"
            placeholder="请输入管理员备注"
            style="width: 100%;">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmReject" :loading="rejectLoading">确定</el-button>
      </span>
    </el-dialog>
    
    <!-- 图片查看对话框 -->
    <el-dialog 
      title="佐证图片" 
      :visible.sync="imageDialogVisible" 
      width="800px"
      :close-on-click-modal="false">
      <div v-if="currentImages.length > 0" class="image-viewer">
        <el-carousel :autoplay="false" arrow="always" height="500px">
          <el-carousel-item v-for="(image, index) in currentImages" :key="index">
            <img :src="image" alt="佐证图片" class="carousel-image">
          </el-carousel-item>
        </el-carousel>
      </div>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog 
      title="编辑理赔申请" 
      :visible.sync="editDialogVisible" 
      width="900px"
      :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="120px">
        <el-form-item label="理赔编号">
          <span>{{ editForm.claimNo }}</span>
        </el-form-item>
        
        <el-form-item label="申请原因" prop="reason">
          <el-input v-model="editForm.reason" placeholder="请输入申请原因"></el-input>
        </el-form-item>
        
        <el-form-item label="情况描述" prop="description">
          <el-input 
            v-model="editForm.description" 
            type="textarea"
            :rows="4"
            placeholder="请输入情况描述">
          </el-input>
        </el-form-item>
        
        <el-form-item label="佐证图片">
          <div class="upload-container">
            <div class="image-list">
              <div 
                v-for="(img, index) in editForm.imageList" 
                :key="index" 
                class="image-item">
                <img :src="img" alt="佐证图片">
                <div class="image-overlay">
                  <i class="el-icon-zoom-in" @click="handlePreviewEditImage(index)"></i>
                  <i class="el-icon-delete" @click="handleRemoveEditImage(index)"></i>
                </div>
              </div>
              
              <el-upload
                v-if="editForm.imageList.length < 9"
                class="image-uploader"
                action=""
                :show-file-list="false"
                :before-upload="handleBeforeUpload"
                :http-request="handleUploadEditImage">
                <i class="el-icon-plus upload-icon"></i>
                <div class="upload-text">上传图片</div>
              </el-upload>
            </div>
            <div class="upload-tip">最多可上传9张图片，支持jpg、png格式，单张图片不超过2MB</div>
          </div>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitEdit" :loading="submitEditLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAdminClaims, getAdminClaimDetail, approveClaim, rejectClaim, payClaim, adminUpdateClaim } from '@/api/insuranceClaim';
import { uploadFile } from '@/api/file';

export default {
  name: 'AdminClaimManage',
  data() {
    return {
      claimList: [],
      total: 0,
      loading: false,
      searchParams: {
        page: 1,
        size: 10,
        status: '',
        reason: ''
      },
      dateRange: [],
      
      // 详情对话框
      detailDialogVisible: false,
      currentClaim: null,
      
      // 审核通过对话框
      approveDialogVisible: false,
      approveLoading: false,
      approveForm: {
        claimId: null,
        claimNo: '',
        claimAmount: '',
        adminRemark: ''
      },
      approveRules: {
        claimAmount: [
          { required: true, message: '请输入理赔金额', trigger: 'blur' },
          { pattern: /^[0-9]+(\.[0-9]{1,2})?$/, message: '请输入有效的金额', trigger: 'blur' }
        ],
        adminRemark: [
          { required: true, message: '请输入管理员备注', trigger: 'blur' }
        ]
      },
      
      // 审核拒绝对话框
      rejectDialogVisible: false,
      rejectLoading: false,
      rejectForm: {
        claimId: null,
        claimNo: '',
        rejectReason: '',
        adminRemark: ''
      },
      rejectRules: {
        rejectReason: [
          { required: true, message: '请输入拒绝原因', trigger: 'blur' }
        ],
        adminRemark: [
          { required: true, message: '请输入管理员备注', trigger: 'blur' }
        ]
      },
      
      // 图片查看
      imageDialogVisible: false,
      currentImages: [],
      
      // 编辑对话框
      editDialogVisible: false,
      submitEditLoading: false,
      editForm: {
        id: null,
        claimNo: '',
        reason: '',
        description: '',
        imageList: []
      },
      editFormRules: {
        reason: [
          { required: true, message: '请输入申请原因', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入情况描述', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.loadClaimList();
  },
  methods: {
    // 加载理赔列表
    async loadClaimList() {
      this.loading = true;
      try {
        const params = {
          page: this.searchParams.page,
          size: this.searchParams.size
        };
        
        // 添加筛选条件
        if (this.searchParams.status) {
          params.status = this.searchParams.status;
        }
        if (this.searchParams.reason) {
          params.reason = this.searchParams.reason;
        }
        if (this.dateRange && this.dateRange.length === 2) {
          params.startTime = this.dateRange[0];
          params.endTime = this.dateRange[1];
        }
        
        const response = await getAdminClaims(params);
        if (response.data.success) {
          this.claimList = response.data.data.records || [];
          this.total = response.data.data.total || 0;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '加载理赔列表失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载理赔列表失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 搜索
    handleSearch() {
      this.searchParams.page = 1;
      this.loadClaimList();
    },
    
    // 重置
    handleReset() {
      this.searchParams = {
        page: 1,
        size: 10,
        status: '',
        reason: ''
      };
      this.dateRange = [];
      this.loadClaimList();
    },
    
    // 导出Excel
    handleExport() {
      // 获取当前列表数据
      if (this.claimList.length === 0) {
        this.$notify({
          title: '提示',
          message: '暂无数据可导出',
          type: 'warning',
          duration: 2000
        });
        return;
      }
      
      // 构建Excel数据
      const headers = ['理赔编号', '保险名称', '宠物名称', '申请原因', '理赔金额', '审核状态', '申请时间'];
      const data = this.claimList.map(claim => [
        claim.claimNo,
        claim.insuranceName,
        claim.petName,
        claim.reason,
        claim.claimAmount ? '¥' + claim.claimAmount : '未定',
        claim.status,
        this.formatDate(claim.createTime)
      ]);
      
      // 生成CSV内容
      let csvContent = '\uFEFF'; // UTF-8 BOM
      csvContent += headers.join(',') + '\n';
      data.forEach(row => {
        csvContent += row.map(cell => `"${cell}"`).join(',') + '\n';
      });
      
      // 下载文件
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      const url = URL.createObjectURL(blob);
      link.setAttribute('href', url);
      link.setAttribute('download', `理赔申请列表_${new Date().getTime()}.csv`);
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      
      this.$notify({
        title: '成功',
        message: '导出成功',
        type: 'success',
        duration: 2000
      });
    },
    
    // 查看详情
    async handleViewDetail(claim) {
      try {
        const response = await getAdminClaimDetail(claim.id);
        if (response.data.success) {
          this.currentClaim = response.data.data;
          this.detailDialogVisible = true;
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '获取详情失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '获取详情失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    // 编辑理赔申请
    async handleEdit(claim) {
      try {
        const response = await getAdminClaimDetail(claim.id);
        if (response.data.success) {
          const claimData = response.data.data;
          this.editForm = {
            id: claimData.id,
            claimNo: claimData.claimNo,
            reason: claimData.reason,
            description: claimData.description,
            imageList: this.getImageList(claimData.evidenceImages)
          };
          this.editDialogVisible = true;
          this.$nextTick(() => {
            if (this.$refs.editForm) {
              this.$refs.editForm.clearValidate();
            }
          });
        } else {
          this.$notify({
            title: '失败',
            message: response.data.message || '获取详情失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '获取详情失败，请稍后重试',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    // 上传前校验
    handleBeforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt2M = file.size / 1024 / 1024 < 2;
      
      if (!isImage) {
        this.$notify({
          title: '错误',
          message: '只能上传图片文件',
          type: 'error',
          duration: 2000
        });
        return false;
      }
      
      if (!isLt2M) {
        this.$notify({
          title: '错误',
          message: '图片大小不能超过2MB',
          type: 'error',
          duration: 2000
        });
        return false;
      }
      
      return true;
    },
    
    // 上传编辑图片
    async handleUploadEditImage(params) {
      try {
        const response = await uploadFile(params.file);
        if (response.data.success) {
          const imageUrl = response.data.data;
          this.editForm.imageList.push(imageUrl);
          this.$notify({
            title: '成功',
            message: '图片上传成功',
            type: 'success',
            duration: 2000
          });
        } else {
          this.$notify({
            title: '错误',
            message: response.data.message || '图片上传失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        console.error(error);
        this.$notify({
          title: '错误',
          message: '图片上传失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    // 删除编辑图片
    handleRemoveEditImage(index) {
      this.editForm.imageList.splice(index, 1);
    },
    
    // 预览编辑图片
    handlePreviewEditImage(index) {
      this.currentImages = [...this.editForm.imageList];
      this.imageDialogVisible = true;
    },
    
    // 提交编辑
    handleSubmitEdit() {
      this.$refs.editForm.validate(async (valid) => {
        if (valid) {
          this.submitEditLoading = true;
          try {
            const submitData = {
              id: this.editForm.id,
              reason: this.editForm.reason,
              description: this.editForm.description,
              evidenceImages: this.editForm.imageList.join(',')
            };
            
            const response = await adminUpdateClaim(submitData);
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              });
              this.editDialogVisible = false;
              this.loadClaimList();
            } else {
              this.$notify({
                title: '失败',
                message: response.data.message || '更新失败',
                type: 'error',
                duration: 2000
              });
            }
          } catch (error) {
            console.error(error);
            this.$notify({
              title: '错误',
              message: '更新失败，请稍后重试',
              type: 'error',
              duration: 2000
            });
          } finally {
            this.submitEditLoading = false;
          }
        }
      });
    },
    
    // 审核通过
    handleApprove(claim) {
      this.approveForm = {
        claimId: claim.id,
        claimNo: claim.claimNo,
        claimAmount: '',
        adminRemark: ''
      };
      this.approveDialogVisible = true;
      this.$nextTick(() => {
        if (this.$refs.approveForm) {
          this.$refs.approveForm.clearValidate();
        }
      });
    },
    
    // 确认审核通过
    confirmApprove() {
      this.$refs.approveForm.validate(async (valid) => {
        if (valid) {
          this.approveLoading = true;
          try {
            const data = {
              claimId: this.approveForm.claimId,
              claimAmount: this.approveForm.claimAmount,
              adminRemark: this.approveForm.adminRemark
            };
            
            const response = await approveClaim(data);
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: '审核通过成功',
                type: 'success',
                duration: 2000
              });
              this.approveDialogVisible = false;
              this.loadClaimList();
            } else {
              this.$notify({
                title: '失败',
                message: response.data.message || '审核通过失败',
                type: 'error',
                duration: 2000
              });
            }
          } catch (error) {
            this.$notify({
              title: '错误',
              message: '审核通过失败，请稍后重试',
              type: 'error',
              duration: 2000
            });
          } finally {
            this.approveLoading = false;
          }
        }
      });
    },
    
    // 审核拒绝
    handleReject(claim) {
      this.rejectForm = {
        claimId: claim.id,
        claimNo: claim.claimNo,
        rejectReason: '',
        adminRemark: ''
      };
      this.rejectDialogVisible = true;
      this.$nextTick(() => {
        if (this.$refs.rejectForm) {
          this.$refs.rejectForm.clearValidate();
        }
      });
    },
    
    // 确认审核拒绝
    confirmReject() {
      this.$refs.rejectForm.validate(async (valid) => {
        if (valid) {
          this.rejectLoading = true;
          try {
            const data = {
              claimId: this.rejectForm.claimId,
              rejectReason: this.rejectForm.rejectReason,
              adminRemark: this.rejectForm.adminRemark
            };
            
            const response = await rejectClaim(data);
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: '审核拒绝成功',
                type: 'success',
                duration: 2000
              });
              this.rejectDialogVisible = false;
              this.loadClaimList();
            } else {
              this.$notify({
                title: '失败',
                message: response.data.message || '审核拒绝失败',
                type: 'error',
                duration: 2000
              });
            }
          } catch (error) {
            this.$notify({
              title: '错误',
              message: '审核拒绝失败，请稍后重试',
              type: 'error',
              duration: 2000
            });
          } finally {
            this.rejectLoading = false;
          }
        }
      });
    },
    
    // 打款
    handlePay(claim) {
      this.$confirm(`确定要对理赔编号 ${claim.claimNo} 进行打款操作吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const data = {
            claimId: claim.id
          };
          
          const response = await payClaim(data);
          if (response.data.success) {
            this.$notify({
              title: '成功',
              message: '打款成功',
              type: 'success',
              duration: 2000
            });
            this.loadClaimList();
          } else {
            this.$notify({
              title: '失败',
              message: response.data.message || '打款失败',
              type: 'error',
              duration: 2000
            });
          }
        } catch (error) {
          this.$notify({
            title: '错误',
            message: '打款失败，请稍后重试',
            type: 'error',
            duration: 2000
          });
        }
      }).catch(() => {
        // 用户取消
      });
    },
    
    // 查看图片
    handleViewImages(claim) {
      if (claim.evidenceImages) {
        this.currentImages = claim.evidenceImages.split(',').filter(img => img.trim());
        this.imageDialogVisible = true;
      }
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.searchParams.size = val;
      this.searchParams.page = 1;
      this.loadClaimList();
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.searchParams.page = val;
      this.loadClaimList();
    },
    
    // 获取图片列表
    getImageList(images) {
      if (!images) return [];
      return images.split(',').filter(img => img.trim());
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
      const second = String(d.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
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
.admin-claim-manage {
  padding: 20px;
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
}

.search-container {
  width: 100%;
}

.search-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: flex-start;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-item label {
  white-space: nowrap;
  font-weight: 500;
  color: #606266;
  min-width: 80px;
}

.filter-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.search-actions {
  display: flex;
  gap: 10px;
  margin-left: auto;
}

/* 表格卡片 */
.table-card {
  margin-top: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.table-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.total-count {
  color: #909399;
  font-size: 14px;
}

/* 图片层叠 */
.image-stack {
  position: relative;
  width: 80px;
  height: 60px;
  margin: 0 auto;
  cursor: pointer;
}

.stack-image {
  position: absolute;
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s;
}

.stack-image:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
}

.image-count {
  position: absolute;
  right: 0;
  bottom: 5px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
  z-index: 10;
}

/* 分页 */
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 详情对话框 */
.claim-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e4e7ed;
}

.info-item {
  margin-bottom: 15px;
  display: flex;
  align-items: flex-start;
}

.info-label {
  font-weight: 500;
  color: #606266;
  min-width: 100px;
  flex-shrink: 0;
}

.info-value {
  color: #303133;
  flex: 1;
}

.description-box {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  line-height: 1.8;
  white-space: pre-wrap;
  word-break: break-word;
}

.reject-box {
  background: #fef3f3;
  padding: 15px;
  border-radius: 6px;
  border-left: 4px solid #f56c6c;
  color: #f56c6c;
  line-height: 1.8;
}

/* 佐证图片 */
.evidence-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
}

.evidence-image {
  width: 100%;
  height: 150px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.evidence-image:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* 图片查看器 */
.image-viewer {
  width: 100%;
  height: 500px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

/* 上传容器 */
.upload-container {
  width: 100%;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 10px;
}

.image-item {
  position: relative;
  width: 120px;
  height: 120px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-item:hover .image-overlay {
  opacity: 1;
}

.image-overlay i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.image-overlay i:hover {
  transform: scale(1.2);
}

.image-uploader {
  width: 120px;
  height: 120px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  transition: border-color 0.3s;
}

.image-uploader:hover {
  border-color: #409eff;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.upload-text {
  margin-top: 8px;
  color: #8c939d;
  font-size: 12px;
}

.upload-tip {
  color: #999;
  font-size: 12px;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .search-row {
    flex-direction: column;
  }
  
  .search-actions {
    margin-left: 0;
  }
}
</style>

