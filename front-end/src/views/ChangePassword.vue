<template>
  <div class="change-password">
      <div class="password-container">
        <el-card class="password-card">
          <div slot="header" class="card-header">
            <div class="header-content">
              <i class="el-icon-lock"></i>
              <span>修改密码</span>
            </div>
          </div>
          
          <el-form :model="form" :rules="rules" ref="passwordForm" label-width="120px" class="password-form">
            <el-alert
              title="温馨提示"
              type="info"
              description="为了您的账号安全，请定期修改密码。密码长度建议在6-20位之间。"
              :closable="false"
              show-icon
              style="margin-bottom: 30px;">
            </el-alert>
            
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input 
                v-model="form.oldPassword" 
                type="password" 
                placeholder="请输入旧密码"
                show-password>
              </el-input>
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input 
                v-model="form.newPassword" 
                type="password" 
                placeholder="请输入新密码（6-20位）"
                show-password>
              </el-input>
            </el-form-item>
            
            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input 
                v-model="form.confirmPassword" 
                type="password" 
                placeholder="请再次输入新密码"
                show-password>
              </el-input>
            </el-form-item>
            
            <el-form-item class="button-group">
              <el-button type="primary" @click="submitForm" :loading="loading">确认修改</el-button>
              <el-button @click="resetForm">重置</el-button>
              <el-button @click="goToHome">返回首页</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <!-- 安全提示卡片 -->
        <el-card class="tips-card">
          <div slot="header" class="card-header">
            <div class="header-content">
              <i class="el-icon-warning"></i>
              <span>安全提示</span>
            </div>
          </div>
          
          <div class="tips-content">
            <div class="tip-item">
              <i class="el-icon-check"></i>
              <span>密码长度建议在6-20位之间</span>
            </div>
            <div class="tip-item">
              <i class="el-icon-check"></i>
              <span>建议使用字母、数字、符号的组合</span>
            </div>
            <div class="tip-item">
              <i class="el-icon-check"></i>
              <span>不要使用过于简单的密码</span>
            </div>
            <div class="tip-item">
              <i class="el-icon-check"></i>
              <span>定期更换密码，保护账号安全</span>
            </div>
          </div>
        </el-card>
      </div>
  </div>
</template>

<script>
import { changePassword } from '@/api/user';

export default {
  name: 'ChangePassword',
  data() {
    const defaultAvatar = 'https://img1.baidu.com/it/u=1666852494,3843174075&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500';
    
    const validateNewPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else if (value.length < 6 || value.length > 20) {
        callback(new Error('密码长度应在6-20位之间'));
      } else if (value === this.form.oldPassword) {
        callback(new Error('新密码不能与旧密码相同'));
      } else {
        if (this.form.confirmPassword !== '') {
          this.$refs.passwordForm.validateField('confirmPassword');
        }
        callback();
      }
    };
    
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'));
      } else if (value !== this.form.newPassword) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    
    return {
      userInfo: {},
      defaultAvatar: defaultAvatar,
      form: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      rules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, validator: validateNewPassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      loading: false
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
      } else {
        this.$router.push('/login');
      }
    },
    submitForm() {
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          
          changePassword(
            this.userInfo.id.toString(),
            this.form.oldPassword,
            this.form.newPassword
          ).then(response => {
            this.loading = false;
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: '密码修改成功，请重新登录',
                type: 'success',
                duration: 2000
              });
              
              // 延迟跳转到登录页
              setTimeout(() => {
                localStorage.removeItem('userInfo');
                this.$router.push('/login');
              }, 2000);
            } else {
              this.$notify({
                title: '错误',
                message: response.data.message || '修改失败',
                type: 'error',
                duration: 2000
              });
            }
          }).catch(error => {
            this.loading = false;
            console.error('修改密码失败:', error);
            this.$notify({
              title: '错误',
              message: '修改失败，请重试',
              type: 'error',
              duration: 2000
            });
          });
        }
      });
    },
    resetForm() {
      this.$refs.passwordForm.resetFields();
      this.$notify({
        title: '提示',
        message: '表单已重置',
        type: 'info',
        duration: 2000
      });
    },
    goToHome() {
      this.$router.push('/user/home');
    }
  }
};
</script>

<style scoped>
.password-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
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

.password-card {
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.tips-card {
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

.password-form {
  padding: 20px 40px;
}

.button-group {
  margin-top: 30px;
  text-align: center;
}

.button-group .el-button {
  min-width: 120px;
}

.tips-content {
  padding: 10px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  color: #666;
  font-size: 14px;
}

.tip-item i {
  color: #2c5f2d;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .password-form {
    padding: 10px;
  }
}
</style>

