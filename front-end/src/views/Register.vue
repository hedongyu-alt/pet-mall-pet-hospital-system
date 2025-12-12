<template>
  <div class="register-container">
    <div class="register-background">
      <div class="pet-animation">
        <div class="paw-print paw1"></div>
        <div class="paw-print paw2"></div>
        <div class="paw-print paw3"></div>
        <div class="paw-print paw4"></div>
      </div>
    </div>
    
    <div class="register-box">
      <div class="register-header">
        <h2>加入伴侣宠物之家</h2>
        <p class="subtitle">注册成为普通用户，开启爱宠之旅</p>
      </div>
      
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" class="register-form">
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            prefix-icon="el-icon-user"
            placeholder="请输入用户名（4-20位）"
            clearable
            @blur="checkUsernameAvailable"
          ></el-input>
          <div v-if="usernameChecked && usernameExists" class="error-tip">用户名已存在</div>
          <div v-if="usernameChecked && !usernameExists" class="success-tip">用户名可用</div>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            prefix-icon="el-icon-lock"
            placeholder="请输入密码（6-20位）"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            prefix-icon="el-icon-lock"
            placeholder="请再次输入密码"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            prefix-icon="el-icon-edit"
            placeholder="请输入昵称"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="phone">
          <el-input
            v-model="registerForm.phone"
            prefix-icon="el-icon-phone"
            placeholder="请输入手机号（可选）"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            prefix-icon="el-icon-message"
            placeholder="请输入邮箱（可选）"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="captcha">
          <div class="captcha-container">
            <el-input
              v-model="registerForm.captcha"
              prefix-icon="el-icon-picture"
              placeholder="请输入验证码"
              clearable
              maxlength="4"
              style="width: 60%;"
            ></el-input>
            <div class="captcha-image" @click="refreshCaptcha">
              <img :src="captchaUrl" alt="验证码" />
              <div class="refresh-hint">点击刷新</div>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="handleRegister"
            class="register-button"
          >
            {{ loading ? '注册中...' : '注 册' }}
          </el-button>
        </el-form-item>
        
        <div class="login-link">
          已有账号？
          <span @click="goToLogin">立即登录</span>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { register, checkUsername } from '@/api/user';
import { generateCaptcha, verifyCaptcha } from '@/utils/captcha';

export default {
  name: 'Register',
  data() {
    // 验证确认密码
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        phone: '',
        email: '',
        captcha: ''
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 4, max: 20, message: '用户名长度为4-20位', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
        ],
        captcha: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      },
      loading: false,
      usernameChecked: false,
      usernameExists: false,
      captchaUrl: '', // 验证码图片的base64数据
      captchaText: '' // 正确的验证码文本
    };
  },
  mounted() {
    this.refreshCaptcha();
  },
  methods: {
    /**
     * 刷新验证码
     * 前端生成新的验证码图片和文本
     */
    refreshCaptcha() {
      const captcha = generateCaptcha();
      this.captchaUrl = captcha.image;
      this.captchaText = captcha.text;
      console.log('验证码已生成:', captcha.text); // 调试用,生产环境应删除
    },
    
    /**
     * 检查用户名是否可用
     */
    checkUsernameAvailable() {
      if (this.registerForm.username && this.registerForm.username.length >= 4) {
        checkUsername(this.registerForm.username)
          .then(response => {
            this.usernameChecked = true;
            this.usernameExists = response.data.exists;
          })
          .catch(error => {
            console.error('检查用户名错误:', error);
          });
      } else {
        this.usernameChecked = false;
        this.usernameExists = false;
      }
    },
    
    /**
     * 处理注册逻辑
     */
    handleRegister() {
      // 检查用户名是否已存在
      if (this.usernameExists) {
        this.$notify({
          title: '错误',
          message: '用户名已存在，请更换',
          type: 'error',
          duration: 3000
        });
        return;
      }
      
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          // 前端验证验证码
          if (!verifyCaptcha(this.registerForm.captcha, this.captchaText)) {
            this.$notify({
              title: '错误',
              message: '验证码错误，请重新输入',
              type: 'error',
              duration: 3000
            });
            // 验证码错误，刷新验证码
            this.refreshCaptcha();
            this.registerForm.captcha = '';
            return;
          }
          
          // 验证码正确，继续注册流程
          this.loading = true;
          
          // 准备注册数据(不再传递验证码)
          const registerData = {
            username: this.registerForm.username,
            password: this.registerForm.password,
            nickname: this.registerForm.nickname,
            phone: this.registerForm.phone || null,
            email: this.registerForm.email || null
          };
          
          register(registerData)
            .then(response => {
              this.loading = false;
              const result = response.data;
              
              if (result.success) {
                this.$notify({
                  title: '成功',
                  message: result.message + '，即将跳转到登录页面',
                  type: 'success',
                  duration: 2000
                });
                
                // 跳转到登录页面
                setTimeout(() => {
                  this.$router.push('/login');
                }, 2000);
              } else {
                // 注册失败，刷新验证码
                this.refreshCaptcha();
                this.registerForm.captcha = '';
                this.$notify({
                  title: '错误',
                  message: result.message,
                  type: 'error',
                  duration: 3000
                });
              }
            })
            .catch(error => {
              this.loading = false;
              this.refreshCaptcha();
              this.registerForm.captcha = '';
              this.$notify({
                title: '错误',
                message: '注册请求失败，请检查网络连接',
                type: 'error',
                duration: 3000
              });
              console.error('注册错误:', error);
            });
        }
      });
    },
    
    /**
     * 跳转到登录页面
     */
    goToLogin() {
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.register-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  overflow: hidden;
  padding: 30px 0;
}

.register-background {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

.pet-animation {
  width: 100%;
  height: 100%;
  position: relative;
}

.paw-print {
  position: absolute;
  width: 40px;
  height: 40px;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="rgba(255,255,255,0.1)"><path d="M12 2c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm6 8c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm-12 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm6 6c-2.2 0-4 1.8-4 4h8c0-2.2-1.8-4-4-4z"/></svg>');
  background-size: contain;
  animation: pawAnimation 20s infinite;
  opacity: 0;
}

.paw1 {
  top: 10%;
  left: 20%;
  animation-delay: 0s;
}

.paw2 {
  top: 30%;
  right: 15%;
  animation-delay: 5s;
}

.paw3 {
  bottom: 20%;
  left: 15%;
  animation-delay: 10s;
}

.paw4 {
  bottom: 10%;
  right: 20%;
  animation-delay: 15s;
}

@keyframes pawAnimation {
  0%, 100% {
    opacity: 0;
    transform: scale(0.5) rotate(0deg);
  }
  50% {
    opacity: 1;
    transform: scale(1) rotate(360deg);
  }
}

.register-box {
  position: relative;
  width: 480px;
  background: white;
  border-radius: 16px;
  padding: 40px 50px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  animation: slideIn 0.6s ease-out;
  z-index: 1;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.pet-icon {
  font-size: 50px;
  margin-bottom: 10px;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.register-header h2 {
  color: #2c5f2d;
  font-size: 26px;
  font-weight: 600;
  margin: 10px 0;
}

.subtitle {
  color: #97bc62;
  font-size: 14px;
  margin: 5px 0 0 0;
}

.register-form {
  margin-top: 20px;
}

.register-form >>> .el-form-item {
  margin-bottom: 20px;
}

.register-form >>> .el-input__inner {
  height: 45px;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s;
}

.register-form >>> .el-input__inner:focus {
  border-color: #97bc62;
  box-shadow: 0 0 0 2px rgba(151, 188, 98, 0.1);
}

.error-tip {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
}

.success-tip {
  color: #67c23a;
  font-size: 12px;
  margin-top: 5px;
}

.register-button {
  width: 100%;
  height: 45px;
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s;
}

.register-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(44, 95, 45, 0.3);
}

.login-link {
  text-align: center;
  color: #666;
  font-size: 14px;
  margin-top: 10px;
}

.login-link span {
  color: #2c5f2d;
  cursor: pointer;
  font-weight: 600;
  transition: color 0.3s;
}

.login-link span:hover {
  color: #97bc62;
  text-decoration: underline;
}

.captcha-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.captcha-image {
  width: 38%;
  height: 45px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  position: relative;
  border: 1px solid #e0e0e0;
  transition: all 0.3s;
}

.captcha-image:hover {
  border-color: #97bc62;
  box-shadow: 0 0 0 2px rgba(151, 188, 98, 0.1);
}

.captcha-image img {
  width: 100%;
  height: 100%;
  display: block;
  object-fit: cover;
}

.refresh-hint {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  font-size: 10px;
  text-align: center;
  padding: 2px 0;
  opacity: 0;
  transition: opacity 0.3s;
}

.captcha-image:hover .refresh-hint {
  opacity: 1;
}
</style>

