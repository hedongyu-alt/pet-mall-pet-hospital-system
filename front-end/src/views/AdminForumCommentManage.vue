<template>
  <div class="admin-forum-comment-manage">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <div class="search-container">
        <div class="search-row">
          <div class="search-item">
            <label>论坛标题：</label>
            <el-input 
              v-model="searchParams.postTitle" 
              placeholder="请输入论坛标题" 
              clearable
              style="width: 250px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>评论人昵称：</label>
            <el-input 
              v-model="searchParams.commenterName" 
              placeholder="请输入评论人昵称" 
              clearable
              style="width: 250px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>评论内容：</label>
            <el-input 
              v-model="searchParams.content" 
              placeholder="请输入评论内容" 
              clearable
              style="width: 250px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table 
        :data="tableData" 
        style="width: 100%"
        :header-cell-style="{background: '#f5f7fa', color: '#606266'}">
        
        <el-table-column prop="id" label="ID" min-width="80"></el-table-column>
        
        <el-table-column label="论坛图片" min-width="120">
          <template slot-scope="scope">
            <div class="post-image" v-if="scope.row.postImages">
              <img 
                :src="getFirstImage(scope.row.postImages)" 
                @click="handlePreviewImage(scope.row.postImages)">
            </div>
            <span v-else class="no-image">无图片</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="postTitle" label="论坛标题" min-width="200"></el-table-column>
        
        <el-table-column prop="postUserNickname" label="发布人昵称" min-width="120"></el-table-column>
        
        <el-table-column prop="commentUserNickname" label="评论人昵称" min-width="120"></el-table-column>
        
        <el-table-column prop="content" label="评论内容" min-width="300" show-overflow-tooltip></el-table-column>
        
        <el-table-column prop="createTime" label="评论时间" min-width="180">
          <template slot-scope="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" min-width="250" fixed="right">
          <template slot-scope="scope">
            <el-button 
              type="primary" 
              size="mini" 
              icon="el-icon-view"
              @click="handleViewPost(scope.row.postId)">
              查看帖子
            </el-button>
            <el-button 
              type="warning" 
              size="mini" 
              icon="el-icon-edit"
              @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="mini" 
              icon="el-icon-delete"
              @click="handleDelete(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handlePageChange">
        </el-pagination>
      </div>
    </el-card>

    <!-- 帖子详情对话框 -->
    <el-dialog 
      title="论坛帖子详情" 
      :visible.sync="detailDialogVisible" 
      width="900px"
      top="5vh">
      <div class="post-detail" v-if="currentPost.id">
        <div class="detail-section">
          <h3>{{ currentPost.title }}</h3>
          <div class="post-meta">
            <el-tag size="small" type="info">发布人：{{ currentPost.username }}</el-tag>
            <el-tag size="small" type="warning" style="margin-left: 10px;">点赞：{{ currentPost.likeCount }}</el-tag>
            <el-tag size="small" type="success" style="margin-left: 10px;">评论：{{ currentPost.commentCount }}</el-tag>
            <span style="margin-left: 10px; color: #909399;">{{ formatTime(currentPost.createTime) }}</span>
          </div>
        </div>

        <div class="detail-section" v-if="currentPost.keywords">
          <label>关键词：</label>
          <el-tag 
            v-for="(keyword, index) in getKeywordArray(currentPost.keywords)" 
            :key="index"
            size="small"
            type="info"
            style="margin-right: 8px;">
            {{ keyword }}
          </el-tag>
        </div>

        <div class="detail-section" v-if="currentPost.description">
          <label>描述：</label>
          <p class="description-text">{{ currentPost.description }}</p>
        </div>

        <div class="detail-section" v-if="currentPost.images">
          <label>图片：</label>
          <div class="detail-images">
            <el-image
              v-for="(image, index) in getImageArray(currentPost.images)"
              :key="index"
              :src="image"
              :preview-src-list="getImageArray(currentPost.images)"
              fit="cover"
              class="detail-image">
            </el-image>
          </div>
        </div>

        <div class="detail-section">
          <label>正文：</label>
          <div class="content-text">
            <pre>{{ currentPost.content }}</pre>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog 
      title="图片预览" 
      :visible.sync="imagePreviewVisible" 
      width="800px"
      @close="handleClosePreview">
      <div class="image-preview-container">
        <el-image
          :src="currentPreviewImage"
          fit="contain"
          style="width: 100%; max-height: 600px;">
        </el-image>
        <div class="preview-controls" v-if="previewImages.length > 1">
          <el-button 
            icon="el-icon-arrow-left" 
            :disabled="currentImageIndex === 0"
            @click="handlePrevImage">
            上一张
          </el-button>
          <span class="image-indicator">{{ currentImageIndex + 1 }} / {{ previewImages.length }}</span>
          <el-button 
            icon="el-icon-arrow-right" 
            :disabled="currentImageIndex === previewImages.length - 1"
            @click="handleNextImage">
            下一张
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 编辑评论对话框 -->
    <el-dialog 
      title="编辑评论" 
      :visible.sync="editDialogVisible" 
      width="600px"
      :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="100px">
        <el-form-item label="评论ID">
          <span>{{ editForm.id }}</span>
        </el-form-item>
        <el-form-item label="评论人">
          <span>{{ editForm.commentUserNickname }}</span>
        </el-form-item>
        <el-form-item label="评论内容" prop="content">
          <el-input 
            v-model="editForm.content" 
            type="textarea"
            :rows="6"
            placeholder="请输入评论内容">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitEdit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAdminCommentList, adminDeleteComment, getPostById, adminUpdateComment } from '@/api/forum'

export default {
  name: 'AdminForumCommentManage',
  data() {
    return {
      searchParams: {
        postTitle: '',
        commenterName: '',
        content: ''
      },
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      detailDialogVisible: false,
      currentPost: {},
      imagePreviewVisible: false,
      previewImages: [],
      currentImageIndex: 0,
      currentPreviewImage: '',
      
      // 编辑对话框
      editDialogVisible: false,
      submitLoading: false,
      editForm: {
        id: null,
        commentUserNickname: '',
        content: ''
      },
      editRules: {
        content: [
          { required: true, message: '请输入评论内容', trigger: 'blur' },
          { min: 1, max: 500, message: '评论内容长度在1到500个字符之间', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      try {
        const params = {
          ...this.searchParams,
          page: this.currentPage,
          size: this.pageSize
        }
        
        const response = await getAdminCommentList(params)
        if (response.data.code === 200) {
          const data = response.data.data
          this.tableData = data.list
          this.total = data.total
        }
      } catch (error) {
        console.error('加载数据失败', error)
        this.$notify.error({
          title: '错误',
          message: '加载数据失败'
        })
      }
    },

    // 搜索
    handleSearch() {
      this.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchParams = {
        postTitle: '',
        commenterName: '',
        content: ''
      }
      this.currentPage = 1
      this.loadData()
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      this.loadData()
    },

    // 页码改变
    handlePageChange(page) {
      this.currentPage = page
      this.loadData()
    },

    // 查看帖子详情
    async handleViewPost(postId) {
      try {
        const response = await getPostById(postId)
        if (response.data.code === 200) {
          this.currentPost = response.data.data
          this.detailDialogVisible = true
        }
      } catch (error) {
        console.error('加载详情失败', error)
        this.$notify.error({
          title: '错误',
          message: '加载详情失败'
        })
      }
    },

    // 编辑评论
    handleEdit(row) {
      this.editForm = {
        id: row.id,
        commentUserNickname: row.commentUserNickname,
        content: row.content
      }
      this.editDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.editForm) {
          this.$refs.editForm.clearValidate()
        }
      })
    },

    // 提交编辑
    handleSubmitEdit() {
      this.$refs.editForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            const data = {
              id: this.editForm.id,
              content: this.editForm.content
            }
            
            const response = await adminUpdateComment(data)
            if (response.data.code === 200) {
              this.$notify.success({
                title: '成功',
                message: '更新成功'
              })
              this.editDialogVisible = false
              this.loadData()
            } else {
              this.$notify.error({
                title: '错误',
                message: response.data.message || '更新失败'
              })
            }
          } catch (error) {
            console.error('更新失败', error)
            this.$notify.error({
              title: '错误',
              message: '更新失败'
            })
          } finally {
            this.submitLoading = false
          }
        }
      })
    },

    // 删除评论
    handleDelete(id) {
      this.$confirm('确定要删除这条评论吗？删除后无法恢复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await adminDeleteComment(id)
          if (response.data.code === 200) {
            this.$notify.success({
              title: '成功',
              message: '删除成功'
            })
            this.loadData()
          } else {
            this.$notify.error({
              title: '错误',
              message: response.data.message || '删除失败'
            })
          }
        } catch (error) {
          console.error('删除失败', error)
          this.$notify.error({
            title: '错误',
            message: '删除失败'
          })
        }
      }).catch(() => {})
    },

    // 获取第一张图片
    getFirstImage(images) {
      if (!images) return ''
      const imageArray = images.split(',').filter(img => img.trim())
      return imageArray[0] || ''
    },

    // 获取图片数组
    getImageArray(images) {
      if (!images) return []
      return images.split(',').filter(img => img.trim())
    },

    // 获取关键词数组
    getKeywordArray(keywords) {
      if (!keywords) return []
      return keywords.split(',').filter(kw => kw.trim())
    },

    // 预览图片
    handlePreviewImage(images) {
      this.previewImages = this.getImageArray(images)
      this.currentImageIndex = 0
      this.currentPreviewImage = this.previewImages[0]
      this.imagePreviewVisible = true
    },

    // 上一张图片
    handlePrevImage() {
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--
        this.currentPreviewImage = this.previewImages[this.currentImageIndex]
      }
    },

    // 下一张图片
    handleNextImage() {
      if (this.currentImageIndex < this.previewImages.length - 1) {
        this.currentImageIndex++
        this.currentPreviewImage = this.previewImages[this.currentImageIndex]
      }
    },

    // 关闭预览
    handleClosePreview() {
      this.previewImages = []
      this.currentImageIndex = 0
      this.currentPreviewImage = ''
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.admin-forum-comment-manage {
  padding: 0;
}

.search-card {
  margin-bottom: 20px;
}

.search-container {
  padding: 10px 0;
}

.search-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 20px;
}

.search-item {
  display: flex;
  align-items: center;
}

.search-item label {
  white-space: nowrap;
  margin-right: 10px;
  font-weight: 500;
  color: #606266;
}

.table-card {
  background: white;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 帖子图片样式 */
.post-image {
  width: 80px;
  height: 80px;
  overflow: hidden;
  border-radius: 4px;
  cursor: pointer;
}

.post-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.post-image img:hover {
  transform: scale(1.1);
}

.no-image {
  color: #909399;
  font-size: 14px;
}

/* 详情对话框样式 */
.post-detail {
  max-height: 70vh;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 25px;
}

.detail-section h3 {
  font-size: 22px;
  color: #303133;
  margin: 0 0 15px 0;
}

.post-meta {
  margin-bottom: 10px;
}

.detail-section label {
  display: block;
  font-weight: 600;
  color: #606266;
  margin-bottom: 10px;
  font-size: 15px;
}

.description-text {
  color: #606266;
  line-height: 1.6;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
  margin: 0;
}

.detail-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 10px;
}

.detail-image {
  width: 100%;
  height: 150px;
  border-radius: 4px;
  cursor: pointer;
}

.content-text {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  line-height: 1.8;
}

.content-text pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: inherit;
  margin: 0;
  color: #303133;
}

/* 图片预览样式 */
.image-preview-container {
  text-align: center;
}

.preview-controls {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.image-indicator {
  font-size: 16px;
  color: #606266;
  font-weight: 500;
}
</style>

