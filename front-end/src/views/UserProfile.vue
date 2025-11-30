<template>
  <div class="user-profile">
      <div class="profile-container">
        <el-card class="profile-card">
          <div slot="header" class="card-header">
            <div class="header-content">
              <i class="el-icon-user"></i>
              <span>个人中心</span>
            </div>
          </div>
          
          <el-form :model="form" :rules="rules" ref="profileForm" label-width="100px" class="profile-form">
            <div class="avatar-section">
              <el-form-item label="当前头像">
                <div class="avatar-wrapper">
                  <el-avatar :src="form.avatar || defaultAvatar" :size="120"></el-avatar>
                </div>
              </el-form-item>
              
              <el-form-item label="上传新头像">
                <el-upload
                  class="avatar-uploader"
                  :action="uploadAction"
                  :show-file-list="false"
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                  :before-upload="beforeUpload"
                  :headers="uploadHeaders"
                  accept="image/*">
                  <el-button size="small" type="primary" icon="el-icon-upload">选择图片上传</el-button>
                  <div slot="tip" class="el-upload__tip">支持jpg、png、gif格式，大小不超过5MB</div>
                </el-upload>
              </el-form-item>
            </div>
            
            <el-form-item label="用户名">
              <el-input v-model="form.username" disabled></el-input>
            </el-form-item>
            
            <el-form-item label="角色">
              <el-input v-model="form.role" disabled></el-input>
            </el-form-item>
            
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="请输入昵称"></el-input>
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            
            <el-form-item label="注册时间">
              <el-input v-model="form.createTime" disabled></el-input>
            </el-form-item>
            
            <el-form-item class="button-group">
              <el-button type="primary" @click="submitForm" :loading="loading">保存修改</el-button>
              <el-button @click="resetForm">重置</el-button>
              <el-button @click="goToHome">返回首页</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
  </div>
</template>

<script>
import { getUserInfo, updateUserInfo } from '@/api/user';

export default {
  name: 'UserProfile',
  data() {
    return {
      userInfo: {},
      form: {
        id: '',
        username: '',
        role: '',
        nickname: '',
        avatar: '',
        phone: '',
        email: '',
        createTime: ''
      },
      rules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      loading: false,
      uploadAction: 'http://localhost:18007/api/file/upload',
      uploadHeaders: {},
      defaultAvatar: 'https://img1.baidu.com/it/u=1666852494,3843174075&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500'
    };
  },
  created() {
    this.loadUserInfo();
  },
  methods: {
    loadUserInfo() {
      const userInfoStr = localStorage.getItem('userInfo');
      if (userInfoStr) {
        this.userInfo = JSON.parse(userInfoStr);
        
        // 从后端获取最新的用户信息
        getUserInfo(this.userInfo.id).then(response => {
          if (response.data.success) {
            const data = response.data.data;
            this.form.id = data.id;
            this.form.username = data.username;
            this.form.role = data.role;
            this.form.nickname = data.nickname;
            this.form.avatar = data.avatar || '';
            this.form.phone = data.phone || '';
            this.form.email = data.email || '';
            this.form.createTime = this.formatDate(data.createTime);
          }
        }).catch(error => {
          console.error('获取用户信息失败:', error);
          this.$notify({
            title: '错误',
            message: '获取用户信息失败',
            type: 'error',
            duration: 2000
          });
        });
      } else {
        this.$router.push('/login');
      }
    },
    submitForm() {
      this.$refs.profileForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          
          const updateData = {
            id: this.form.id,
            nickname: this.form.nickname,
            avatar: this.form.avatar,
            phone: this.form.phone,
            email: this.form.email
          };
          
          updateUserInfo(updateData).then(response => {
            this.loading = false;
            if (response.data.success) {
              // 更新本地存储的用户信息
              const updatedUser = response.data.data;
              localStorage.setItem('userInfo', JSON.stringify(updatedUser));
              this.userInfo = updatedUser;
              
              this.$notify({
                title: '成功',
                message: '个人信息更新成功',
                type: 'success',
                duration: 2000
              });
            } else {
              this.$notify({
                title: '错误',
                message: response.data.message || '更新失败',
                type: 'error',
                duration: 2000
              });
            }
          }).catch(error => {
            this.loading = false;
            console.error('更新失败:', error);
            this.$notify({
              title: '错误',
              message: '更新失败，请重试',
              type: 'error',
              duration: 2000
            });
          });
        }
      });
    },
    resetForm() {
      this.loadUserInfo();
      this.$notify({
        title: '提示',
        message: '表单已重置',
        type: 'info',
        duration: 2000
      });
    },
    goToHome() {
      this.$router.push('/user/home');
    },
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      return d.toLocaleString('zh-CN', { 
        year: 'numeric', 
        month: '2-digit', 
        day: '2-digit', 
        hour: '2-digit', 
        minute: '2-digit', 
        second: '2-digit' 
      });
    },
    beforeUpload(file) {
      // 验证文件类型
      const isImage = file.type.startsWith('image/');
      if (!isImage) {
        this.$notify({
          title: '错误',
          message: '只能上传图片文件',
          type: 'error',
          duration: 2000
        });
        return false;
      }
      
      // 验证文件大小（5MB）
      const isLt5M = file.size / 1024 / 1024 < 5;
      if (!isLt5M) {
        this.$notify({
          title: '错误',
          message: '图片大小不能超过5MB',
          type: 'error',
          duration: 2000
        });
        return false;
      }
      
      return true;
    },
    handleUploadSuccess(response, file) {
      if (response.success) {
        // 上传成功，更新头像URL并立即提交保存
        this.form.avatar = response.data;
        
        // 自动保存头像更新
        const updateData = {
          id: this.form.id,
          nickname: this.form.nickname,
          avatar: this.form.avatar,
          phone: this.form.phone,
          email: this.form.email
        };
        
        updateUserInfo(updateData).then(res => {
          if (res.data.success) {
            // 更新本地存储的用户信息
            const updatedUser = res.data.data;
            localStorage.setItem('userInfo', JSON.stringify(updatedUser));
            this.userInfo = updatedUser;
            
            this.$notify({
              title: '成功',
              message: '头像上传并保存成功',
              type: 'success',
              duration: 2000
            });
          } else {
            this.$notify({
              title: '警告',
              message: '头像上传成功，但保存失败，请手动点击保存按钮',
              type: 'warning',
              duration: 3000
            });
          }
        }).catch(error => {
          console.error('保存失败:', error);
          this.$notify({
            title: '警告',
            message: '头像上传成功，但保存失败，请手动点击保存按钮',
            type: 'warning',
            duration: 3000
          });
        });
      } else {
        this.$notify({
          title: '错误',
          message: response.message || '上传失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    handleUploadError(err, file) {
      console.error('上传失败:', err);
      this.$notify({
        title: '错误',
        message: '上传失败，请重试',
        type: 'error',
        duration: 2000
      });
    }
  }
};
</script>

<style scoped>
.profile-container {
  animation: fadeIn 0.5s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.profile-card {
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #2c5f2d;
}

.header-content i {
  font-size: 22px;
}

.profile-form {
  padding: 20px 40px;
}

.avatar-section {
  text-align: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #f9fafb;
  border-radius: 8px;
}

.avatar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.avatar-uploader {
  width: 100%;
}

.el-upload__tip {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
  line-height: 1.5;
}

.button-group {
  margin-top: 30px;
  text-align: center;
}

.button-group .el-button {
  min-width: 120px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-form {
    padding: 10px;
  }
}
</style>

