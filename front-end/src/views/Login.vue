<template>
  <div class="login-container">
    <div class="login-background">
      <div class="pet-animation">
        <div class="paw-print paw1"></div>
        <div class="paw-print paw2"></div>
        <div class="paw-print paw3"></div>
        <div class="paw-print paw4"></div>
      </div>
    </div>
    
    <div class="login-box">
      <div class="login-header">
        <h2>伴侣宠物之家系统</h2>
        <p class="subtitle">爱宠生活，从这里开始</p>
      </div>
      
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="el-icon-user"
            placeholder="请输入用户名"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            prefix-icon="el-icon-lock"
            placeholder="请输入密码"
            clearable
            @keyup.enter.native="handleLogin"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="captcha">
          <div class="captcha-container">
            <el-input
              v-model="loginForm.captcha"
              prefix-icon="el-icon-picture"
              placeholder="请输入验证码"
              clearable
              maxlength="4"
              @keyup.enter.native="handleLogin"
              style="width: 60%;"
            ></el-input>
            <div class="captcha-image" @click="refreshCaptcha">
              <img :src="captchaUrl" alt="验证码" />
              <div class="refresh-hint">点击刷新</div>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item prop="role">
          <div class="role-selector">
            <div
              class="role-option"
              :class="{ active: loginForm.role === '普通用户' }"
              @click="loginForm.role = '普通用户'"
            >
              <i class="el-icon-user"></i>
              <span>普通用户</span>
            </div>
            <div
              class="role-option"
              :class="{ active: loginForm.role === '管理员' }"
              @click="loginForm.role = '管理员'"
            >
              <i class="el-icon-s-custom"></i>
              <span>管理员</span>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="handleLogin"
            class="login-button"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
        
        <div class="register-link">
          还没有账号？
          <span @click="goToRegister">立即注册</span>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/user';
import { generateCaptcha, verifyCaptcha } from '@/utils/captcha';

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        captcha: '',
        role: '普通用户'
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        captcha: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择登录身份', trigger: 'change' }
        ]
      },
      loading: false,
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
     * 处理登录逻辑
     */
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // 前端验证验证码
          if (!verifyCaptcha(this.loginForm.captcha, this.captchaText)) {
            this.$notify({
              title: '错误',
              message: '验证码错误，请重新输入',
              type: 'error',
              duration: 3000
            });
            // 验证码错误，刷新验证码
            this.refreshCaptcha();
            this.loginForm.captcha = '';
            return;
          }
          
          // 验证码正确，继续登录流程
          this.loading = true;
          
          // 登录时不再传递验证码参数
          login(this.loginForm.username, this.loginForm.password, this.loginForm.role)
            .then(response => {
              this.loading = false;
              const result = response.data;
              
              if (result.success) {
                // 保存用户信息到本地存储
                localStorage.setItem('userInfo', JSON.stringify(result.data));
                
                this.$notify({
                  title: '成功',
                  message: result.message,
                  type: 'success',
                  duration: 2000
                });
                
                // 根据角色跳转不同页面
                setTimeout(() => {
                  if (result.data.role === '管理员') {
                    this.$router.push('/admin/home');
                  } else {
                    this.$router.push('/user/home');
                  }
                }, 500);
              } else {
                // 登录失败，刷新验证码
                this.refreshCaptcha();
                this.loginForm.captcha = '';
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
              this.loginForm.captcha = '';
              this.$notify({
                title: '错误',
                message: '登录请求失败，请检查网络连接',
                type: 'error',
                duration: 3000
              });
              console.error('登录错误:', error);
            });
        }
      });
    },
    
    /**
     * 跳转到注册页面
     */
    goToRegister() {
      this.$router.push('/register');
    }
  }
};
</script>

<style scoped>
.login-container {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  overflow: hidden;
}

.login-background {
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

.login-box {
  position: relative;
  width: 420px;
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

.login-header {
  text-align: center;
  margin-bottom: 35px;
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

.login-header h2 {
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

.login-form {
  margin-top: 20px;
}

.login-form >>> .el-input__inner {
  height: 45px;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s;
}

.login-form >>> .el-input__inner:focus {
  border-color: #97bc62;
  box-shadow: 0 0 0 2px rgba(151, 188, 98, 0.1);
}

.role-selector {
  display: flex;
  gap: 15px;
  width: 100%;
}

.role-option {
  flex: 1;
  height: 70px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s;
  background: white;
}

.role-option:hover {
  border-color: #97bc62;
  background: #f8faf5;
}

.role-option.active {
  border-color: #2c5f2d;
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  color: white;
}

.role-option i {
  font-size: 24px;
}

.role-option span {
  font-size: 14px;
  font-weight: 500;
}

.login-button {
  width: 100%;
  height: 45px;
  background: linear-gradient(135deg, #2c5f2d 0%, #97bc62 100%);
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(44, 95, 45, 0.3);
}

.register-link {
  text-align: center;
  color: #666;
  font-size: 14px;
  margin-top: 10px;
}

.register-link span {
  color: #2c5f2d;
  cursor: pointer;
  font-weight: 600;
  transition: color 0.3s;
}

.register-link span:hover {
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

