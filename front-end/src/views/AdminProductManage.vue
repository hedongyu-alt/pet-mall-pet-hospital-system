<template>
  <div class="admin-product-manage">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <div class="search-container">
        <div class="search-row">
          <div class="search-item">
            <label>商品名称：</label>
            <el-input 
              v-model="searchParams.name" 
              placeholder="请输入商品名称" 
              clearable
              style="width: 200px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>品牌：</label>
            <el-input 
              v-model="searchParams.brand" 
              placeholder="请输入品牌" 
              clearable
              style="width: 200px;">
            </el-input>
          </div>
          
          <div class="search-item">
            <label>商品分类：</label>
            <div class="filter-buttons">
              <el-button 
                :type="searchParams.categoryId === null ? 'primary' : ''" 
                size="small"
                @click="searchParams.categoryId = null">
                全部
              </el-button>
              <el-button 
                v-for="category in categoryList" 
                :key="category.id"
                :type="searchParams.categoryId === category.id ? 'primary' : ''" 
                size="small"
                @click="searchParams.categoryId = category.id">
                {{ category.name }}
              </el-button>
            </div>
          </div>
        </div>
        
        <div class="search-row">
          <div class="search-item">
            <label>宠物类型：</label>
            <div class="filter-buttons">
              <el-button 
                :type="searchParams.petType === '' ? 'primary' : ''" 
                size="small"
                @click="searchParams.petType = ''">
                全部
              </el-button>
              <el-button 
                :type="searchParams.petType === '狗' ? 'primary' : ''" 
                size="small"
                @click="searchParams.petType = '狗'">
                狗
              </el-button>
              <el-button 
                :type="searchParams.petType === '猫' ? 'primary' : ''" 
                size="small"
                @click="searchParams.petType = '猫'">
                猫
              </el-button>
            </div>
          </div>
          
          <div class="search-item">
            <label>状态：</label>
            <div class="filter-buttons">
              <el-button 
                :type="searchParams.status === '' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = ''">
                全部
              </el-button>
              <el-button 
                :type="searchParams.status === '上架' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = '上架'">
                上架
              </el-button>
              <el-button 
                :type="searchParams.status === '下架' ? 'primary' : ''" 
                size="small"
                @click="searchParams.status = '下架'">
                下架
              </el-button>
            </div>
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
            <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增商品</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 商品列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>商品列表</h3>
        <span class="total-count">共 {{ total }} 条数据</span>
      </div>
      
      <el-table 
        :data="productList" 
        v-loading="loading"
        stripe
        style="width: 100%">
        <el-table-column prop="id" label="商品ID" min-width="100" align="center"></el-table-column>
        
        <el-table-column label="商品图片" min-width="150" align="center">
          <template slot-scope="scope">
            <div class="image-stack" v-if="getImageList(scope.row.images).length > 0" @click="handleViewImages(scope.row)">
              <img 
                v-for="(img, index) in getImageList(scope.row.images).slice(0, 3)" 
                :key="index"
                :src="img" 
                :style="{ zIndex: 3 - index, left: index * 8 + 'px' }"
                class="stack-image">
              <span v-if="getImageList(scope.row.images).length > 3" class="image-count">+{{ getImageList(scope.row.images).length - 3 }}</span>
            </div>
            <span v-else style="color: #999;">暂无图片</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="name" label="商品名称" min-width="200">
          <template slot-scope="scope">
            <span style="font-weight: 600; color: #303133;">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="商品分类" min-width="120">
          <template slot-scope="scope">
            <el-tag type="info" size="small">{{ getCategoryName(scope.row.categoryId) }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="brand" label="品牌" min-width="120">
          <template slot-scope="scope">
            <span style="color: #606266;">{{ scope.row.brand || '暂无' }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="petType" label="适用宠物" min-width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.petType === '狗' ? 'warning' : 'success'" size="small">{{ scope.row.petType }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="price" label="价格" min-width="100" align="center">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: 600;">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="状态" min-width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '上架' ? 'success' : 'info'" size="small">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="创建时间" min-width="180">
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
              :icon="scope.row.status === '上架' ? 'el-icon-download' : 'el-icon-upload2'" 
              @click="handleToggleStatus(scope.row)">
              {{ scope.row.status === '上架' ? '下架' : '上架' }}
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
      width="800px"
      :close-on-click-modal="false">
      <el-form :model="productForm" :rules="formRules" ref="productForm" label-width="120px">
        <el-form-item label="商品图片" prop="images">
          <div class="upload-container">
            <div class="image-list">
              <div 
                v-for="(img, index) in productForm.imageList" 
                :key="index" 
                class="image-item">
                <img :src="img" alt="商品图片">
                <div class="image-overlay">
                  <i class="el-icon-zoom-in" @click="handlePreviewImage(index)"></i>
                  <i class="el-icon-delete" @click="handleRemoveImage(index)" v-if="dialogMode !== 'view'"></i>
                </div>
              </div>
              
              <el-upload
                v-if="productForm.imageList.length < 5 && dialogMode !== 'view'"
                class="image-uploader"
                action=""
                :show-file-list="false"
                :before-upload="handleBeforeUpload"
                :http-request="handleUploadImage">
                <i class="el-icon-plus upload-icon"></i>
                <div class="upload-text">上传图片</div>
              </el-upload>
            </div>
            <div class="upload-tip">最多可上传5张图片，支持jpg、png格式，单张图片不超过2MB</div>
          </div>
        </el-form-item>
        
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productForm.name" placeholder="请输入商品名称" :disabled="dialogMode === 'view'"></el-input>
        </el-form-item>
        
        <el-form-item label="商品分类" prop="categoryId">
          <div class="filter-buttons" v-if="dialogMode === 'view'">
            <el-tag type="info">{{ getCategoryName(productForm.categoryId) }}</el-tag>
          </div>
          <div class="filter-buttons" v-else>
            <el-button 
              v-for="category in categoryList" 
              :key="category.id"
              :type="productForm.categoryId === category.id ? 'primary' : ''" 
              size="small"
              @click="productForm.categoryId = category.id">
              {{ category.name }}
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="productForm.brand" placeholder="请输入品牌" :disabled="dialogMode === 'view'"></el-input>
        </el-form-item>
        
        <el-form-item label="适用宠物类型" prop="petType">
          <div class="filter-buttons" v-if="dialogMode === 'view'">
            <el-tag :type="productForm.petType === '狗' ? 'warning' : 'success'">{{ productForm.petType }}</el-tag>
          </div>
          <div class="filter-buttons" v-else>
            <el-button 
              :type="productForm.petType === '狗' ? 'primary' : ''" 
              size="small"
              @click="productForm.petType = '狗'">
              狗
            </el-button>
            <el-button 
              :type="productForm.petType === '猫' ? 'primary' : ''" 
              size="small"
              @click="productForm.petType = '猫'">
              猫
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="价格" prop="price">
          <el-input v-model.number="productForm.price" placeholder="请输入价格" type="number" :disabled="dialogMode === 'view'">
            <template slot="prepend">¥</template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <div class="filter-buttons" v-if="dialogMode === 'view'">
            <el-tag :type="productForm.status === '上架' ? 'success' : 'info'">{{ productForm.status }}</el-tag>
          </div>
          <div class="filter-buttons" v-else>
            <el-button 
              :type="productForm.status === '上架' ? 'primary' : ''" 
              size="small"
              @click="productForm.status = '上架'">
              上架
            </el-button>
            <el-button 
              :type="productForm.status === '下架' ? 'primary' : ''" 
              size="small"
              @click="productForm.status = '下架'">
              下架
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="商品描述" prop="description">
          <el-input 
            v-model="productForm.description" 
            type="textarea"
            :rows="4"
            placeholder="请输入商品描述" 
            :disabled="dialogMode === 'view'">
          </el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" v-if="dialogMode !== 'view'" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
    
    <!-- 图片预览对话框 -->
    <el-dialog 
      title="图片预览" 
      :visible.sync="imagePreviewVisible" 
      width="800px"
      :close-on-click-modal="true">
      <div class="image-preview-container">
        <div class="preview-main">
          <i class="el-icon-arrow-left preview-arrow" @click="handlePrevImage" v-if="previewImages.length > 1"></i>
          <img :src="previewImages[currentImageIndex]" class="preview-image">
          <i class="el-icon-arrow-right preview-arrow" @click="handleNextImage" v-if="previewImages.length > 1"></i>
        </div>
        <div class="preview-footer">
          <span>{{ currentImageIndex + 1 }} / {{ previewImages.length }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getProductList, getProductDetail, addProduct, updateProduct, deleteProduct, updateProductStatus, exportProducts } from '@/api/product';
import { getCategoryList } from '@/api/category';
import { uploadFile } from '@/api/file';

export default {
  name: 'AdminProductManage',
  data() {
    return {
      // 搜索参数
      searchParams: {
        page: 1,
        size: 10,
        name: '',
        brand: '',
        categoryId: null,
        petType: '',
        status: '',
        minPrice: null,
        maxPrice: null
      },
      
      // 商品列表
      productList: [],
      total: 0,
      loading: false,
      
      // 分类列表
      categoryList: [],
      
      // 对话框
      dialogVisible: false,
      dialogMode: 'add', // add, edit, view
      dialogTitle: '新增商品',
      submitLoading: false,
      
      // 商品表单
      productForm: {
        id: null,
        images: '',
        imageList: [],
        name: '',
        categoryId: null,
        description: '',
        brand: '',
        petType: '狗',
        price: null,
        status: '上架'
      },
      
      // 表单验证规则
      formRules: {
        name: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择商品分类', trigger: 'change' }
        ],
        petType: [
          { required: true, message: '请选择适用宠物类型', trigger: 'change' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' },
          { type: 'number', message: '价格必须为数字', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      },
      
      // 图片预览
      imagePreviewVisible: false,
      previewImages: [],
      currentImageIndex: 0
    };
  },
  
  created() {
    this.loadCategoryList();
    this.loadProductList();
  },
  
  methods: {
    /**
     * 加载分类列表
     */
    async loadCategoryList() {
      try {
        const response = await getCategoryList({ page: 1, size: 1000 });
        if (response.data.success) {
          this.categoryList = response.data.data.records || [];
        }
      } catch (error) {
        console.error('加载分类列表失败', error);
      }
    },
    
    /**
     * 加载商品列表
     */
    async loadProductList() {
      this.loading = true;
      try {
        const response = await getProductList(this.searchParams);
        
        if (response.data.success) {
          const pageData = response.data.data;
          this.productList = pageData.records || [];
          this.total = pageData.total || 0;
        } else {
          this.$notify({
            title: '错误',
            message: response.data.message || '加载商品列表失败',
            type: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载商品列表失败',
          type: 'error',
          duration: 2000
        });
      } finally {
        this.loading = false;
      }
    },
    
    /**
     * 获取图片列表
     */
    getImageList(images) {
      if (!images) return [];
      return images.split(',').filter(img => img.trim());
    },
    
    /**
     * 获取分类名称
     */
    getCategoryName(categoryId) {
      const category = this.categoryList.find(c => c.id === categoryId);
      return category ? category.name : '未知分类';
    },
    
    /**
     * 查看图片
     */
    handleViewImages(row) {
      this.previewImages = this.getImageList(row.images);
      if (this.previewImages.length > 0) {
        this.currentImageIndex = 0;
        this.imagePreviewVisible = true;
      }
    },
    
    /**
     * 预览图片（表单中）
     */
    handlePreviewImage(index) {
      this.previewImages = [...this.productForm.imageList];
      this.currentImageIndex = index;
      this.imagePreviewVisible = true;
    },
    
    /**
     * 上一张图片
     */
    handlePrevImage() {
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--;
      } else {
        this.currentImageIndex = this.previewImages.length - 1;
      }
    },
    
    /**
     * 下一张图片
     */
    handleNextImage() {
      if (this.currentImageIndex < this.previewImages.length - 1) {
        this.currentImageIndex++;
      } else {
        this.currentImageIndex = 0;
      }
    },
    
    /**
     * 上传前校验
     */
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
    
    /**
     * 上传图片
     */
    async handleUploadImage(params) {
      try {
        const response = await uploadFile(params.file);
        if (response.data.success) {
          const imageUrl = response.data.data;
          this.productForm.imageList.push(imageUrl);
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
        this.$notify({
          title: '错误',
          message: '图片上传失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    /**
     * 删除图片
     */
    handleRemoveImage(index) {
      this.productForm.imageList.splice(index, 1);
    },
    
    /**
     * 搜索
     */
    handleSearch() {
      this.searchParams.page = 1;
      this.loadProductList();
    },
    
    /**
     * 重置
     */
    handleReset() {
      this.searchParams = {
        page: 1,
        size: 10,
        name: '',
        brand: '',
        categoryId: null,
        petType: '',
        status: '',
        minPrice: null,
        maxPrice: null
      };
      this.loadProductList();
    },
    
    /**
     * 新增商品
     */
    handleAdd() {
      this.dialogMode = 'add';
      this.dialogTitle = '新增商品';
      this.dialogVisible = true;
      this.productForm = {
        id: null,
        images: '',
        imageList: [],
        name: '',
        categoryId: null,
        description: '',
        brand: '',
        petType: '狗',
        price: null,
        status: '上架'
      };
      this.$nextTick(() => {
        if (this.$refs.productForm) {
          this.$refs.productForm.clearValidate();
        }
      });
    },
    
    /**
     * 查看商品详情
     */
    async handleView(row) {
      this.dialogMode = 'view';
      this.dialogTitle = '查看商品详情';
      this.dialogVisible = true;
      
      try {
        const response = await getProductDetail(row.id);
        if (response.data.success) {
          const productData = response.data.data;
          this.productForm = {
            ...productData,
            imageList: this.getImageList(productData.images)
          };
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载商品详情失败',
          type: 'error',
          duration: 2000
        });
      }
    },
    
    /**
     * 编辑商品
     */
    async handleEdit(row) {
      this.dialogMode = 'edit';
      this.dialogTitle = '编辑商品';
      this.dialogVisible = true;
      
      try {
        const response = await getProductDetail(row.id);
        if (response.data.success) {
          const productData = response.data.data;
          this.productForm = {
            id: productData.id,
            images: productData.images,
            imageList: this.getImageList(productData.images),
            name: productData.name,
            categoryId: productData.categoryId,
            description: productData.description,
            brand: productData.brand,
            petType: productData.petType,
            price: productData.price,
            status: productData.status
          };
        }
      } catch (error) {
        this.$notify({
          title: '错误',
          message: '加载商品详情失败',
          type: 'error',
          duration: 2000
        });
      }
      
      this.$nextTick(() => {
        if (this.$refs.productForm) {
          this.$refs.productForm.clearValidate();
        }
      });
    },
    
    /**
     * 删除商品
     */
    handleDelete(row) {
      this.$confirm('确定要删除该商品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteProduct(row.id);
          if (response.data.success) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            });
            // 删除后刷新列表
            this.loadProductList();
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
     * 切换商品状态
     */
    handleToggleStatus(row) {
      const newStatus = row.status === '上架' ? '下架' : '上架';
      this.$confirm(`确定要将该商品${newStatus}吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await updateProductStatus(row.id, newStatus);
          if (response.data.success) {
            this.$notify({
              title: '成功',
              message: `${newStatus}成功`,
              type: 'success',
              duration: 2000
            });
            // 切换状态后刷新列表
            this.loadProductList();
          } else {
            this.$notify({
              title: '错误',
              message: response.data.message || `${newStatus}失败`,
              type: 'error',
              duration: 2000
            });
          }
        } catch (error) {
          this.$notify({
            title: '错误',
            message: `${newStatus}失败`,
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
      this.$refs.productForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true;
          try {
            // 将图片列表转换为逗号分隔的字符串
            const submitData = {
              ...this.productForm,
              images: this.productForm.imageList.join(',')
            };
            delete submitData.imageList;
            
            let response;
            if (this.dialogMode === 'add') {
              response = await addProduct(submitData);
            } else {
              response = await updateProduct(submitData);
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
              this.loadProductList();
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
        brand: this.searchParams.brand,
        categoryId: this.searchParams.categoryId,
        petType: this.searchParams.petType,
        status: this.searchParams.status,
        minPrice: this.searchParams.minPrice,
        maxPrice: this.searchParams.maxPrice
      };
      
      const exportUrl = exportProducts(params);
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
      this.loadProductList();
    },
    
    /**
     * 当前页改变
     */
    handleCurrentChange(page) {
      this.searchParams.page = page;
      this.loadProductList();
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
.admin-product-manage {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 40px);
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
  height: 240px;
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
  flex-wrap: wrap;
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

/* 图片层叠展示 */
.image-stack {
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto;
  cursor: pointer;
}

.stack-image {
  position: absolute;
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s;
}

.stack-image:hover {
  transform: translateY(-5px);
}

.image-count {
  position: absolute;
  bottom: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
  z-index: 10;
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

/* 图片预览 */
.image-preview-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.preview-main {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 500px;
}

.preview-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.preview-arrow {
  position: absolute;
  font-size: 30px;
  color: #409eff;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.9);
  padding: 10px;
  border-radius: 50%;
  transition: all 0.3s;
}

.preview-arrow:hover {
  background: #409eff;
  color: #fff;
}

.preview-arrow.el-icon-arrow-left {
  left: 20px;
}

.preview-arrow.el-icon-arrow-right {
  right: 20px;
}

.preview-footer {
  margin-top: 20px;
  color: #666;
  font-size: 14px;
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

