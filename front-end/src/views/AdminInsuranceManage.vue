<template>
  <div class="admin-insurance-manage">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <div class="search-container">
        <div class="search-row">
          <div class="search-item">
            <label>保险名称：</label>
            <el-input 
              v-model="searchParams.name" 
              placeholder="请输入保险名称" 
              clearable
              style="width: 200px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>价格范围：</label>
            <el-input 
              v-model.number="searchParams.minPrice" 
              placeholder="最低价格" 
              type="number"
              clearable
              style="width: 120px;">
            </el-input>
            <span style="margin: 0 5px;">-</span>
            <el-input 
              v-model.number="searchParams.maxPrice" 
              placeholder="最高价格" 
              type="number"
              clearable
              style="width: 120px;">
            </el-input>
          </div>
          
          <div class="search-actions">
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
            <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增保险</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 保险列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>保险列表</h3>
        <span class="total-count">共 {{ total }} 条数据</span>
      </div>
      
      <el-table 
        :data="insuranceList" 
        v-loading="loading"
        stripe
        style="width: 100%">
        <el-table-column prop="id" label="保险ID" min-width="100" align="center"></el-table-column>
        
        <el-table-column prop="name" label="保险名称" min-width="200">
          <template slot-scope="scope">
            <span style="font-weight: 600; color: #303133;">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="price" label="价格/年" min-width="120" align="center">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: 600;">¥{{ scope.row.price }}/年</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="coverage" label="保险范围" min-width="250">
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.coverage" placement="top">
              <span class="text-ellipsis">{{ scope.row.coverage || '暂无' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        
        <el-table-column prop="claimLimit" label="理赔限额" min-width="200">
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.claimLimit" placement="top">
              <span class="text-ellipsis">{{ scope.row.claimLimit || '暂无' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="创建时间" min-width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" min-width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" style="color: #f56c6c;" @click="handleDelete(scope.row)">删除</el-button>
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
    
    <!-- 新增/编辑/查看对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      :visible.sync="dialogVisible" 
      width="800px"
      :close-on-click-modal="false">
      <el-form :model="insuranceForm" :rules="formRules" ref="insuranceForm" label-width="120px">
        <el-form-item label="保险名称" prop="name">
          <el-input v-model="insuranceForm.name" placeholder="请输入保险名称" :disabled="dialogMode === 'view'"></el-input>
        </el-form-item>
        
        <el-form-item label="价格/年" prop="price">
          <el-input v-model.number="insuranceForm.price" placeholder="请输入价格" type="number" :disabled="dialogMode === 'view'">
            <template slot="prepend">¥</template>
            <template slot="append">元/年</template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="保险详情" prop="detail">
          <el-input 
            v-model="insuranceForm.detail" 
            type="textarea"
            :rows="4"
            placeholder="请输入保险详情" 
            :disabled="dialogMode === 'view'">
          </el-input>
        </el-form-item>
        
        <el-form-item label="保险范围" prop="coverage">
          <el-input 
            v-model="insuranceForm.coverage" 
            type="textarea"
            :rows="3"
            placeholder="请输入保险范围" 
            :disabled="dialogMode === 'view'">
          </el-input>
        </el-form-item>
        
        <el-form-item label="理赔须知" prop="claimNotice">
          <el-input 
            v-model="insuranceForm.claimNotice" 
            type="textarea"
            :rows="4"
            placeholder="请输入理赔须知" 
            :disabled="dialogMode === 'view'">
          </el-input>
        </el-form-item>
        
        <el-form-item label="理赔限额" prop="claimLimit">
          <el-input 
            v-model="insuranceForm.claimLimit" 
            type="textarea"
            :rows="2"
            placeholder="请输入理赔限额" 
            :disabled="dialogMode === 'view'">
          </el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" v-if="dialogMode !== 'view'" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getInsuranceList, getInsuranceDetail, addInsurance, updateInsurance, deleteInsurance, exportInsurances } from '@/api/insurance';

export default {
  name: 'AdminInsuranceManage',
  data() {
    return {
      // 搜索参数
      searchParams: {
        page: 1,
        size: 10,
        name: '',
        minPrice: null,
        maxPrice: null
      },
      
      // 保险列表
      insuranceList: [],
      total: 0,
      loading: false,
      
      // 对话框
      dialogVisible: false,
      dialogMode: 'add', // add, edit, view
      dialogTitle: '新增保险',
      submitLoading: false,
      
      // 保险表单
      insuranceForm: {
        id: null,
        name: '',
        detail: '',
        price: null,
        coverage: '',
        claimNotice: '',
        claimLimit: ''
      },
      
      // 表单验证规则
      formRules: {
        name: [
          { required: true, message: '请输入保险名称', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' },
          { type: 'number', message: '价格必须为数字', trigger: 'blur' }
        ]
      }
    };
  },
  
  created() {
    this.loadInsuranceList();
  },
  
  methods: {
    /**
     * 加载保险列表
     */
    async loadInsuranceList() {
      this.loading = true;
      try {
        const response = await getInsuranceList(this.searchParams);
        
        if (response.data.success) {
          const pageData = response.data.data;
          this.insuranceList = pageData.records || [];
          this.total = pageData.total || 0;
        } else {
          this.$notify({
            title: '错误',
            message: response.data.message || '加载保险列表失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载保险列表失败',
          type: 'error',
          duration: 2000
        });
      } finally {
        this.loading = false;
      }
    },
    
    /**
     * 搜索
     */
    handleSearch() {
      this.searchParams.page = 1;
      this.loadInsuranceList();
    },
    
    /**
     * 重置
     */
    handleReset() {
      this.searchParams = {
        page: 1,
        size: 10,
        name: '',
        minPrice: null,
        maxPrice: null
      };
      this.loadInsuranceList();
    },
    
    /**
     * 新增保险
     */
    handleAdd() {
      this.dialogMode = 'add';
      this.dialogTitle = '新增保险';
      this.dialogVisible = true;
      this.insuranceForm = {
        id: null,
        name: '',
        detail: '',
        price: null,
        coverage: '',
        claimNotice: '',
        claimLimit: ''
      };
      this.$nextTick(() => {
        if (this.$refs.insuranceForm) {
          this.$refs.insuranceForm.clearValidate();
        }
      });
    },
    
    /**
     * 查看保险详情
     */
    async handleView(row) {
      this.dialogMode = 'view';
      this.dialogTitle = '查看保险详情';
      this.dialogVisible = true;
      
      try {
        const response = await getInsuranceDetail(row.id);
        if (response.data.success) {
          this.insuranceForm = { ...response.data.data };
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载保险详情失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    /**
     * 编辑保险
     */
    async handleEdit(row) {
      this.dialogMode = 'edit';
      this.dialogTitle = '编辑保险';
      this.dialogVisible = true;
      
      try {
        const response = await getInsuranceDetail(row.id);
        if (response.data.success) {
          this.insuranceForm = { ...response.data.data };
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载保险详情失败',
          type: 'error',
          duration: 2000
        });
      }
      
      this.$nextTick(() => {
        if (this.$refs.insuranceForm) {
          this.$refs.insuranceForm.clearValidate();
        }
      });
    },
    
    /**
     * 删除保险
     */
    handleDelete(row) {
      this.$confirm('确定要删除该保险吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteInsurance(row.id);
          if (response.data.success) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            });
            // 删除后刷新列表
            this.loadInsuranceList();
          } else {
            this.$notify({
              title: '错误',
              message: response.data.message || '删除失败',
              type: 'error',
              duration: 2000
            });
          }
        } catch (error) {
          this.$notify({
            title: '错误',
            message: '删除失败',
            type: 'error',
            duration: 2000
          });
        }
      }).catch(() => {});
    },
    
    /**
     * 提交表单
     */
    handleSubmit() {
      this.$refs.insuranceForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true;
          try {
            let response;
            if (this.dialogMode === 'add') {
              response = await addInsurance(this.insuranceForm);
            } else {
              response = await updateInsurance(this.insuranceForm);
            }
            
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: this.dialogMode === 'add' ? '新增成功' : '更新成功',
                type: 'success',
                duration: 2000
              });
              this.dialogVisible = false;
              // 新增或编辑成功后刷新列表
              this.loadInsuranceList();
            } else {
              this.$notify({
                title: '错误',
                message: response.data.message || '操作失败',
                type: 'error',
                duration: 2000
              });
            }
          } catch (error) {
            this.$notify({
              title: '错误',
              message: '操作失败',
              type: 'error',
              duration: 2000
            });
          } finally {
            this.submitLoading = false;
          }
        }
      });
    },
    
    /**
     * 导出Excel
     */
    handleExport() {
      const params = {
        name: this.searchParams.name,
        minPrice: this.searchParams.minPrice,
        maxPrice: this.searchParams.maxPrice
      };
      
      const exportUrl = exportInsurances(params);
      window.open(exportUrl, '_blank');
      
      this.$notify({
        title: '提示',
        message: '正在导出，请稍候...',
        type: 'info',
        duration: 2000
      });
    },
    
    /**
     * 分页大小改变
     */
    handleSizeChange(size) {
      this.searchParams.size = size;
      this.searchParams.page = 1;
      this.loadInsuranceList();
    },
    
    /**
     * 当前页改变
     */
    handleCurrentChange(page) {
      this.searchParams.page = page;
      this.loadInsuranceList();
    },
    
    /**
     * 格式化日期
     */
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
    }
  }
};
</script>

<style scoped>
.admin-insurance-manage {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 40px);
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
  height: 120px;
}

.search-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.search-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-item label {
  font-weight: 500;
  color: #333;
  white-space: nowrap;
}

.search-actions {
  display: flex;
  gap: 10px;
  margin-left: auto;
}

/* 表格卡片 */
.table-card {
  border-radius: 8px;
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
  color: #333;
}

.total-count {
  color: #666;
  font-size: 14px;
}

/* 文本省略 */
.text-ellipsis {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}

/* 分页 */
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 对话框 */
.dialog-footer {
  text-align: right;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .search-row {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-actions {
    margin-left: 0;
    width: 100%;
    flex-wrap: wrap;
  }
}
</style>

