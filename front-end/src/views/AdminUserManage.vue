<template>
  <div class="admin-user-manage">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <div class="search-container">
        <div class="search-row">
          <div class="search-item">
            <label>昵称：</label>
            <el-input 
              v-model="searchParams.nickname" 
              placeholder="请输入昵称" 
              clearable
              style="width: 200px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>手机号：</label>
            <el-input 
              v-model="searchParams.phone" 
              placeholder="请输入手机号" 
              clearable
              style="width: 200px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>身份：</label>
            <div class="filter-buttons">
              <el-button 
                :type="searchParams.role === '' ? 'primary' : ''" 
                size="small"
                @click="searchParams.role = ''">
                全部
              </el-button>
              <el-button 
                :type="searchParams.role === '管理员' ? 'primary' : ''" 
                size="small"
                @click="searchParams.role = '管理员'">
                管理员
              </el-button>
              <el-button 
                :type="searchParams.role === '普通用户' ? 'primary' : ''" 
                size="small"
                @click="searchParams.role = '普通用户'">
                普通用户
              </el-button>
            </div>
          </div>
          
          <div class="search-actions">
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
            <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 用户列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>用户列表</h3>
        <span class="total-count">共 {{ total }} 条数据</span>
      </div>
      
      <el-table 
        :data="userList" 
        v-loading="loading"
        stripe
        style="width: 100%">
        <el-table-column prop="id" label="用户ID" min-width="80" align="center"></el-table-column>
        <el-table-column prop="avatar" label="头像" min-width="80" align="center">
          <template slot-scope="scope">
            <el-avatar :src="scope.row.avatar" :size="40"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120"></el-table-column>
        <el-table-column prop="nickname" label="昵称" min-width="120"></el-table-column>
        <el-table-column prop="role" label="身份" min-width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role === '管理员' ? 'danger' : 'primary'" size="small">
              {{ scope.row.role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" min-width="120"></el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="150"></el-table-column>
        <el-table-column prop="status" label="状态" min-width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '正常' ? 'success' : 'info'" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              type="text" 
              :icon="scope.row.status === '正常' ? 'el-icon-lock' : 'el-icon-unlock'"
              @click="handleToggleStatus(scope.row)">
              {{ scope.row.status === '正常' ? '禁用' : '启用' }}
            </el-button>
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
    
    <!-- 新增/编辑对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      :visible.sync="dialogVisible" 
      width="600px"
      :close-on-click-modal="false">
      <el-form :model="userForm" :rules="formRules" ref="userForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="dialogMode === 'view'"></el-input>
        </el-form-item>
        
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname" placeholder="请输入昵称" :disabled="dialogMode === 'view'"></el-input>
        </el-form-item>
        
        <el-form-item label="初始密码" v-if="dialogMode === 'add'">
          <el-input value="123456" disabled placeholder="默认密码：123456"></el-input>
          <span style="color: #ff9800; font-size: 12px;">
            <i class="el-icon-info"></i> 新用户默认密码为 123456，用户登录后可自行修改
          </span>
        </el-form-item>
        
        <el-form-item label="身份" prop="role">
          <div class="filter-buttons">
            <el-button 
              :type="userForm.role === '管理员' ? 'danger' : ''" 
              size="small"
              :disabled="dialogMode === 'view'"
              @click="userForm.role = '管理员'">
              管理员
            </el-button>
            <el-button 
              :type="userForm.role === '普通用户' ? 'primary' : ''" 
              size="small"
              :disabled="dialogMode === 'view'"
              @click="userForm.role = '普通用户'">
              普通用户
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="头像" prop="avatar">
          <div class="avatar-upload">
            <div class="avatar-preview">
              <el-avatar 
                :key="avatarKey"
                :src="userForm.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" 
                :size="100">
              </el-avatar>
              <div class="avatar-tips" v-if="dialogMode !== 'view'">
                <span>建议尺寸：500x500</span>
                <span>支持jpg、png格式</span>
                <span>大小不超过2MB</span>
              </div>
            </div>
            <div class="upload-buttons" v-if="dialogMode !== 'view'">
              <el-upload
                action="http://localhost:18007/api/file/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :on-error="handleAvatarError"
                :before-upload="beforeAvatarUpload"
                accept="image/*">
                <el-button size="medium" type="primary" icon="el-icon-upload" :loading="uploading">
                  {{ uploading ? '上传中...' : '点击上传头像' }}
                </el-button>
              </el-upload>
              <div v-if="userForm.avatar" class="current-avatar-url">
                <span style="color: #67c23a; font-size: 12px;">
                  <i class="el-icon-success"></i> 已设置头像
                </span>
              </div>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" :disabled="dialogMode === 'view'"></el-input>
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" :disabled="dialogMode === 'view'"></el-input>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <div class="filter-buttons">
            <el-button 
              :type="userForm.status === '正常' ? 'success' : ''" 
              size="small"
              :disabled="dialogMode === 'view'"
              @click="userForm.status = '正常'">
              正常
            </el-button>
            <el-button 
              :type="userForm.status === '禁用' ? 'info' : ''" 
              size="small"
              :disabled="dialogMode === 'view'"
              @click="userForm.status = '禁用'">
              禁用
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" v-if="dialogMode !== 'view'">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserList, getUserDetail, addUser, updateUser, deleteUser, toggleUserStatus, exportUsers } from '@/api/adminUser';

export default {
  name: 'AdminUserManage',
  data() {
    return {
      // 搜索参数
      searchParams: {
        page: 1,
        size: 10,
        nickname: '',
        phone: '',
        role: ''
      },
      
      // 用户列表
      userList: [],
      total: 0,
      loading: false,
      
      // 对话框
      dialogVisible: false,
      dialogMode: 'add', // add, edit, view
      dialogTitle: '新增用户',
      
      // 用户表单
      userForm: {
        id: null,
        username: '',
        nickname: '',
        role: '普通用户',
        avatar: '',
        phone: '',
        email: '',
        status: '正常'
      },
      
      // 上传状态
      uploading: false,
      avatarKey: 0,
      
      // 表单验证规则
      formRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择身份', trigger: 'change' }
        ]
      }
    };
  },
  
  created() {
    this.loadUserList();
  },
  
  methods: {
    /**
     * 加载用户列表
     */
    async loadUserList() {
      this.loading = true;
      try {
        const response = await getUserList(this.searchParams);
        
        if (response.data.success) {
          const pageData = response.data.data;
          this.userList = pageData.records || [];
          this.total = pageData.total || 0;
        } else {
          this.$notify({
            title: '错误',
            message: response.data.message || '加载用户列表失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载用户列表失败',
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
      this.loadUserList();
    },
    
    /**
     * 重置
     */
    handleReset() {
      this.searchParams = {
        page: 1,
        size: 10,
        nickname: '',
        phone: '',
        role: ''
      };
      this.loadUserList();
    },
    
    /**
     * 新增用户
     */
    handleAdd() {
      this.dialogMode = 'add';
      this.dialogTitle = '新增用户';
      this.dialogVisible = true;
      this.uploading = false;
      this.avatarKey = Date.now();
      this.userForm = {
        id: null,
        username: '',
        nickname: '',
        role: '普通用户',
        avatar: '',
        phone: '',
        email: '',
        status: '正常'
      };
      this.$nextTick(() => {
        if (this.$refs.userForm) {
          this.$refs.userForm.clearValidate();
        }
      });
    },
    
    /**
     * 查看用户详情
     */
    async handleView(row) {
      this.dialogMode = 'view';
      this.dialogTitle = '查看用户详情';
      this.dialogVisible = true;
      
      try {
        const response = await getUserDetail(row.id);
        if (response.data.success) {
          this.userForm = { ...response.data.data, password: '' };
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载用户详情失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    /**
     * 编辑用户
     */
    async handleEdit(row) {
      this.dialogMode = 'edit';
      this.dialogTitle = '编辑用户';
      this.dialogVisible = true;
      this.uploading = false;
      this.avatarKey = Date.now();
      
      try {
        const response = await getUserDetail(row.id);
        if (response.data.success) {
          // 编辑时不包含密码字段
          const userData = response.data.data;
          this.userForm = {
            id: userData.id,
            username: userData.username,
            nickname: userData.nickname,
            role: userData.role,
            avatar: userData.avatar,
            phone: userData.phone,
            email: userData.email,
            status: userData.status
          };
          console.log('编辑用户，当前头像:', this.userForm.avatar);
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载用户详情失败',
          type: 'error',
          duration: 2000
        });
      }
      
      this.$nextTick(() => {
        if (this.$refs.userForm) {
          this.$refs.userForm.clearValidate();
        }
      });
    },
    
    /**
     * 删除用户
     */
    handleDelete(row) {
      this.$confirm('确定要删除该用户吗？删除后将无法恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteUser(row.id);
          if (response.data.success) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            });
            
            // 刷新列表
            await this.loadUserList();
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
     * 切换用户状态
     */
    handleToggleStatus(row) {
      const newStatus = row.status === '正常' ? '禁用' : '正常';
      const action = newStatus === '禁用' ? '禁用' : '启用';
      const tips = newStatus === '禁用' ? '禁用后该用户将无法登录系统。' : '启用后该用户可以正常登录系统。';
      
      this.$confirm(`确定要${action}该用户吗？${tips}`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await toggleUserStatus({
            id: row.id,
            status: newStatus
          });
          if (response.data.success) {
            this.$notify({
              title: '成功',
              message: `${action}成功`,
              type: 'success',
              duration: 2000
            });
            
            // 刷新列表
            await this.loadUserList();
          } else {
            this.$notify({
              title: '错误',
              message: response.data.message || `${action}失败`,
              type: 'error',
              duration: 2000
            });
          }
        } catch (error) {
          this.$notify({
            title: '错误',
            message: `${action}失败`,
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
      this.$refs.userForm.validate(async (valid) => {
        if (valid) {
          try {
            let response;
            if (this.dialogMode === 'add') {
              response = await addUser(this.userForm);
            } else {
              response = await updateUser(this.userForm);
            }
            
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: this.dialogMode === 'add' ? '新增成功，默认密码为 123456' : '更新成功',
                type: 'success',
                duration: 3000
              });
              
              // 关闭对话框
              this.dialogVisible = false;
              
              // 等待对话框关闭动画完成后再刷新列表
              this.$nextTick(() => {
                this.loadUserList();
              });
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
          }
        }
      });
    },
    
    /**
     * 导出Excel
     */
    handleExport() {
      const params = {
        nickname: this.searchParams.nickname,
        phone: this.searchParams.phone,
        role: this.searchParams.role
      };
      
      const exportUrl = exportUsers(params);
      window.open(exportUrl, '_blank');
      
      this.$notify({
        title: '提示',
        message: '正在导出，请稍候...',
        type: 'info',
        duration: 2000
      });
    },
    
    /**
     * 头像上传成功
     */
    handleAvatarSuccess(response, file) {
      this.uploading = false;
      
      console.log('上传响应:', response);
      
      if (response && response.success) {
        // 上传成功后立即更新头像URL，实现即时回显
        console.log('头像URL:', response.data);
        
        // 使用Vue.set确保响应式更新
        this.$set(this.userForm, 'avatar', response.data);
        
        // 强制刷新avatar组件
        this.avatarKey = Date.now();
        
        this.$notify({
          title: '成功',
          message: '头像上传成功',
          type: 'success',
          duration: 2000
        });
      } else {
        this.$notify({
          title: '错误',
          message: (response && response.message) || '头像上传失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    /**
     * 头像上传失败
     */
    handleAvatarError(err, file) {
      this.uploading = false;
      console.error('上传失败:', err);
      this.$notify({
        title: '错误',
        message: '头像上传失败，请重试',
        type: 'error',
        duration: 2000
      });
    },
    
    /**
     * 上传前校验
     */
    beforeAvatarUpload(file) {
      console.log('准备上传文件:', file.name, '类型:', file.type, '大小:', file.size);
      
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
          message: '图片大小不能超过 2MB',
          type: 'error',
          duration: 2000
        });
        return false;
      }
      
      this.uploading = true;
      return true;
    },
    
    /**
     * 分页大小改变
     */
    handleSizeChange(size) {
      this.searchParams.size = size;
      this.searchParams.page = 1;
      this.loadUserList();
    },
    
    /**
     * 当前页改变
     */
    handleCurrentChange(page) {
      this.searchParams.page = page;
      this.loadUserList();
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
.admin-user-manage {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 40px);
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
  height: 140px;
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

.filter-buttons {
  display: flex;
  gap: 8px;
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

/* 分页 */
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 头像上传 */
.avatar-upload {
  display: flex;
  gap: 30px;
  align-items: center;
}

.avatar-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.avatar-tips {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 12px;
  color: #999;
  text-align: center;
}

.upload-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.current-avatar-url {
  margin-top: 5px;
  padding: 5px 10px;
  background: #f0f9ff;
  border-radius: 4px;
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

