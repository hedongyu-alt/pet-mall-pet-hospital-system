<template>
  <div class="admin-system-setting">
    <!-- 系统名称设置 -->
    <el-card class="setting-card" shadow="hover">
      <div slot="header" class="card-header">
        <i class="el-icon-s-tools"></i>
        <span>系统名称设置</span>
      </div>
      <div class="setting-content">
        <el-form :model="configForm" label-width="120px">
          <el-form-item label="系统名称">
            <el-input 
              v-model="configForm.systemName" 
              placeholder="请输入系统名称"
              maxlength="50"
              show-word-limit
            ></el-input>
            <div class="form-tip">系统名称会显示在页面标题和浏览器标签中</div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleUpdateSystemName">
              <i class="el-icon-check"></i>
              保存系统名称
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 轮播图设置 -->
    <el-card class="setting-card" shadow="hover">
      <div slot="header" class="card-header">
        <i class="el-icon-picture"></i>
        <span>用户端轮播图设置</span>
      </div>
      <div class="setting-content">
        <div class="carousel-tip">
          <i class="el-icon-info"></i>
          建议上传尺寸为 1280x800 的图片，最多支持 5 张轮播图
        </div>
        
        <div class="carousel-list">
          <div 
            v-for="(image, index) in carouselImages" 
            :key="index" 
            class="carousel-item"
          >
            <div class="image-wrapper">
              <img :src="image" alt="轮播图">
              <div class="image-actions">
                <el-button 
                  type="danger" 
                  size="mini" 
                  icon="el-icon-delete" 
                  @click="handleDeleteCarousel(index)"
                  circle
                ></el-button>
              </div>
            </div>
            <div class="image-index">图片 {{ index + 1 }}</div>
          </div>
          
          <div 
            v-if="carouselImages.length < 5" 
            class="carousel-item add-item"
            @click="handleAddCarousel"
          >
            <div class="add-wrapper">
              <i class="el-icon-plus"></i>
              <span>添加轮播图</span>
            </div>
          </div>
        </div>

        <el-button type="primary" @click="handleSaveCarousel" style="margin-top: 20px;">
          <i class="el-icon-check"></i>
          保存轮播图设置
        </el-button>
      </div>
    </el-card>

    <!-- 修改管理员密码 -->
    <el-card class="setting-card" shadow="hover">
      <div slot="header" class="card-header">
        <i class="el-icon-lock"></i>
        <span>修改管理员密码</span>
      </div>
      <div class="setting-content">
        <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="120px">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input 
              v-model="passwordForm.oldPassword" 
              type="password" 
              placeholder="请输入旧密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input 
              v-model="passwordForm.newPassword" 
              type="password" 
              placeholder="请输入新密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="confirmPassword">
            <el-input 
              v-model="passwordForm.confirmPassword" 
              type="password" 
              placeholder="请再次输入新密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleUpdatePassword">
              <i class="el-icon-check"></i>
              修改密码
            </el-button>
            <el-button @click="handleResetPasswordForm">
              <i class="el-icon-refresh-left"></i>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 图片上传对话框 -->
    <el-dialog 
      title="上传轮播图" 
      :visible.sync="uploadDialogVisible" 
      width="500px"
      :close-on-click-modal="false"
    >
      <el-upload
        class="upload-demo"
        drag
        action="http://localhost:18007/api/file/upload"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :show-file-list="false"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过2MB</div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import { getSystemConfig, updateSystemConfig, updateAdminPassword } from '@/api/systemConfig';

export default {
  name: 'AdminSystemSetting',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'));
      } else if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

    return {
      configForm: {
        systemName: ''
      },
      carouselImages: [],
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      uploadDialogVisible: false,
      userInfo: {}
    };
  },
  created() {
    this.loadUserInfo();
    this.loadSystemConfig();
  },
  methods: {
    loadUserInfo() {
      const userInfoStr = localStorage.getItem('userInfo');
      if (userInfoStr) {
        this.userInfo = JSON.parse(userInfoStr);
      }
    },
    loadSystemConfig() {
      getSystemConfig().then(response => {
        if (response.data.success) {
          const config = response.data.data;
          this.configForm.systemName = config.systemName;
          
          // 解析轮播图
          try {
            if (config.carouselImages) {
              this.carouselImages = JSON.parse(config.carouselImages);
            } else {
              this.carouselImages = [];
            }
          } catch (e) {
            this.carouselImages = [];
          }
        }
      }).catch(error => {
        this.$notify({
          title: '错误',
          message: '获取系统配置失败',
          type: 'error',
          duration: 2000
        });
      });
    },
    handleUpdateSystemName() {
      if (!this.configForm.systemName || this.configForm.systemName.trim() === '') {
        this.$notify({
          title: '提示',
          message: '系统名称不能为空',
          type: 'warning',
          duration: 2000
        });
        return;
      }

      const data = {
        systemName: this.configForm.systemName,
        carouselImages: JSON.stringify(this.carouselImages)
      };

      updateSystemConfig(data).then(response => {
        if (response.data.success) {
          this.$notify({
            title: '成功',
            message: '系统名称更新成功',
            type: 'success',
            duration: 2000
          });
          
          // 更新浏览器标题
          document.title = this.configForm.systemName;
        } else {
          this.$notify({
            title: '错误',
            message: response.data.message || '系统名称更新失败',
            type: 'error',
            duration: 2000
          });
        }
      }).catch(error => {
        this.$notify({
          title: '错误',
          message: '系统名称更新失败',
          type: 'error',
          duration: 2000
        });
      });
    },
    handleAddCarousel() {
      this.uploadDialogVisible = true;
    },
    handleDeleteCarousel(index) {
      this.$confirm('确定要删除这张轮播图吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.carouselImages.splice(index, 1);
        this.$notify({
          title: '成功',
          message: '删除成功，请点击保存按钮保存更改',
          type: 'success',
          duration: 2000
        });
      }).catch(() => {});
    },
    handleSaveCarousel() {
      const data = {
        systemName: this.configForm.systemName,
        carouselImages: JSON.stringify(this.carouselImages)
      };

      updateSystemConfig(data).then(response => {
        if (response.data.success) {
          this.$notify({
            title: '成功',
            message: '轮播图设置保存成功',
            type: 'success',
            duration: 2000
          });
        } else {
          this.$notify({
            title: '错误',
            message: response.data.message || '轮播图设置保存失败',
            type: 'error',
            duration: 2000
          });
        }
      }).catch(error => {
        this.$notify({
          title: '错误',
          message: '轮播图设置保存失败',
          type: 'error',
          duration: 2000
        });
      });
    },
    beforeUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$notify({
          title: '错误',
          message: '只能上传JPG/PNG格式的图片',
          type: 'error',
          duration: 2000
        });
      }
      if (!isLt2M) {
        this.$notify({
          title: '错误',
          message: '图片大小不能超过2MB',
          type: 'error',
          duration: 2000
        });
      }
      return isImage && isLt2M;
    },
    handleUploadSuccess(response, file, fileList) {
      if (response.success) {
        this.carouselImages.push(response.data);
        this.uploadDialogVisible = false;
        this.$notify({
          title: '成功',
          message: '图片上传成功，请点击保存按钮保存更改',
          type: 'success',
          duration: 2000
        });
      } else {
        this.$notify({
          title: '错误',
          message: response.message || '图片上传失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    handleUploadError(err, file, fileList) {
      this.$notify({
        title: '错误',
        message: '图片上传失败',
        type: 'error',
        duration: 2000
      });
    },
    handleUpdatePassword() {
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          const data = {
            adminId: this.userInfo.id.toString(),
            oldPassword: this.passwordForm.oldPassword,
            newPassword: this.passwordForm.newPassword
          };

          updateAdminPassword(data).then(response => {
            if (response.data.success) {
              this.$notify({
                title: '成功',
                message: '密码修改成功，请重新登录',
                type: 'success',
                duration: 2000
              });
              
              // 清空表单
              this.handleResetPasswordForm();
              
              // 延迟跳转到登录页
              setTimeout(() => {
                localStorage.removeItem('userInfo');
                this.$router.push('/login');
              }, 2000);
            } else {
              this.$notify({
                title: '错误',
                message: response.data.message || '密码修改失败',
                type: 'error',
                duration: 2000
              });
            }
          }).catch(error => {
            this.$notify({
              title: '错误',
              message: '密码修改失败',
              type: 'error',
              duration: 2000
            });
          });
        } else {
          return false;
        }
      });
    },
    handleResetPasswordForm() {
      this.$refs.passwordForm.resetFields();
    }
  }
};
</script>

<style scoped>
.admin-system-setting {
  padding: 20px;
}

.setting-card {
  margin-bottom: 30px;
  border-radius: 8px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #2c5f2d;
}

.card-header i {
  font-size: 20px;
}

.setting-content {
  padding: 20px 0;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.carousel-tip {
  background: #f0f9ff;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  padding: 12px;
  color: #1e40af;
  font-size: 14px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.carousel-tip i {
  font-size: 16px;
}

.carousel-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.carousel-item {
  position: relative;
}

.image-wrapper {
  position: relative;
  width: 100%;
  height: 150px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid #e5e7eb;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 10px;
  right: 10px;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-wrapper:hover .image-actions {
  opacity: 1;
}

.image-index {
  text-align: center;
  margin-top: 8px;
  font-size: 14px;
  color: #666;
}

.add-item {
  cursor: pointer;
}

.add-wrapper {
  width: 100%;
  height: 150px;
  border: 2px dashed #d1d5db;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #9ca3af;
  transition: all 0.3s;
}

.add-wrapper:hover {
  border-color: #2c5f2d;
  color: #2c5f2d;
}

.add-wrapper i {
  font-size: 36px;
}

.add-wrapper span {
  font-size: 14px;
}

.upload-demo {
  text-align: center;
}

.el-upload__tip {
  margin-top: 10px;
}

/* 表单样式 */
.el-form {
  max-width: 500px;
}

.el-form-item {
  margin-bottom: 22px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .carousel-list {
    grid-template-columns: 1fr;
  }
}
</style>

