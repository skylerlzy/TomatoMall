<script setup lang="ts">
import { ref } from 'vue'
import { ElForm, ElFormItem, ElInput, ElButton, ElTabs, ElTabPane, ElMessage, ElMessageBox, ElUpload, ElIcon } from 'element-plus'
import { createProduct } from '../../api/product.ts'
import { uploadImage } from '../../api/tool.ts'
import {Back} from '@element-plus/icons-vue'
import {router} from "../../router";

const productForm = ref({
  title: '',
  price: undefined,
  rate: undefined,
  description: '',
  cover: '',
  detail: '',
  tag: '',
  author: ''
})
type productSpecifications = {
  item : string,
  value: string,
  productId: number,
}
// 商品规格表单数据
const specForm = ref<productSpecifications[]>([])

// 对话框的显示控制
const dialogVisible = ref(true)
const activeTab = ref('basic')

// 添加商品规格
function addSpecification() {
  const newSpec: productSpecifications = {
    item: '',
    value: '',
    productId: 0 // 这会在提交时设置为商品ID
  }
  specForm.value.push(newSpec)
}

// 提交商品信息
function submitProduct() {
  const productInfo = {
      id: productForm.value.id,
      title: productForm.value.title,
      price: productForm.value.price,
      rate: productForm.value.rate,
      description: productForm.value.description,
      cover: productForm.value.cover,
      detail: productForm.value.detail,
      tag: productForm.value.tag,
      author: productForm.value.author,
      specifications: specForm.value
  }

  // 由于我们没有实际的商品ID，这里假设商品ID会在后端生成，前端暂时不设置
  // 如果需要前端生成临时ID，可以使用UUID或其他方法
  createProduct(productInfo).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '添加商品成功！',
        type: 'success',
        center: true,
      })
      // 清空表单数据
      productForm.value = {
        title: '',
        price: undefined,
        rate: undefined,
        description: '',
        cover: '',
        detail: ''
      }
      specForm.value = []
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  }).catch(err => {
    console.error('商品创建失败', err)
    ElMessage({
      message: '商品创建失败，请重试！',
      type: 'error',
      center: true,
    })
  })
}

function toBackPage(){
  router.push('/AllProduct')
}

// 切换标签页
function handleTabChange(tab: any) {
  if (tab.paneName === 'specifications' && (!productForm.value.title || !productForm.value.price || !productForm.value.rate || !productForm.value.description || !productForm.value.cover || !productForm.value.detail)) {
    ElMessageBox.alert('请先完成基本信息的填写', '提示', {
      confirmButtonText: '确定',
      type: 'warning'
    }).then(() => {
      activeTab.value = 'basic'
    })
  }
}

// 图片上传
function handleUpload(file: any) {
  productForm.value.cover = file.url
  let formData = new FormData()
  formData.append('file', file.raw)
  uploadImage(formData).then(res => {
    productForm.value.cover = res.data.data
  })
}

// 图片限制
function handleExceed() {
  ElMessage.warning('文件超出限制，请上传小于1M的文件')
}

// upload请求
function uploadHttpRequest() {
  return new XMLHttpRequest()
}
</script>

<template>
  <div class = "back">
  <el-button @click="toBackPage()" type="primary" circle plain class="back-button">
    <el-icon class = "back-icon">
      <Back />
    </el-icon>
  </el-button>
  </div>

  <el-main class="main-frame" >
    <el-form :model="productForm" label-width="120px" >
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="基本信息" name="basic" placeholder="......" >
          <el-form-item label="商品名称">
            <el-input v-model="productForm.title" placeholder="......" />
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model.number="productForm.price" placeholder="......" />
          </el-form-item>
          <el-form-item label="评分">
            <el-input v-model.number="productForm.rate" placeholder="0-10分" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="productForm.description" type="textarea" placeholder="......" />
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="productForm.author" placeholder="请输入作者姓名" />
          </el-form-item>
          <el-form-item label="商品类别">
            <el-select v-model="productForm.tag" placeholder="请选择商品类别">
              <el-option label="玄幻" value="玄幻"/>
              <el-option label="穿越" value="穿越"/>
              <el-option label="奇幻" value="奇幻"/>
              <el-option label="仙侠" value="仙侠"/>
              <el-option label="都市" value="都市"/>
              <el-option label="无限流" value="无限流"/>
              <el-option label="爽文" value="爽文"/>
              <el-option label="后宫" value="后宫"/>
            </el-select>
          </el-form-item>
          <el-form-item label="商品图片URL">
            <el-upload v-model:file="productForm.cover" :limit="1" :on-change="handleUpload" :on-exceed="handleExceed" :http-request="uploadHttpRequest" drag>
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传jpg/png文件，且不超过1MB
                </div>
              </template>
            </el-upload>
          </el-form-item>
          <el-form-item label="详细信息">
            <el-input v-model="productForm.detail" type="textarea" placeholder="......" />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="规格信息" name="specifications">
          <div v-for="(spec, index) in specForm" :key="index" class="spec-item">
            <el-form-item label="项目">
              <el-input v-model="spec.item" />
            </el-form-item>
            <el-form-item label="值">
              <el-input v-model="spec.value" />
            </el-form-item>
            <el-button type="danger" plain @click="specForm.splice(index, 1)">删除</el-button>
          </div>
          <el-button type="primary" plain @click="addSpecification()">添加规格</el-button>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitProduct()">确定</el-button>
        </span>
      </template>
    </el-form>
    <el-button type="primary" @click="submitProduct()" class="submit-button">
      提交商品信息
    </el-button>
  </el-main>
</template>
<style scoped>
.main-frame {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  padding-right: 10%;
  background-image: url('/src/resources/background/background2.png');
  background-size: cover;
  background-position: center;
}

.back {
  position: fixed;
  top: 40px; /* 从20px改为40px */
  left: 20px;
}

/* 修改表单和提交按钮的样式 */
.el-form {
  width: 500px;
  margin-top: 40px;
  margin-bottom: 20px; /* 增加底部间距 */
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.back-button {
  color: #ffffff;
  background-color: #ff6402;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 10%;
  margin-top: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.back-button:hover {
  background-color: #e65a00;
  transform: scale(1.05);
}



.el-form-item {
  margin-bottom: 22px;
}

.el-input {
  --el-input-hover-border-color: #ff6402;
  --el-input-focus-border-color: #ff6402;
}

.el-textarea {
  --el-input-hover-border-color: #ff6402;
  --el-input-focus-border-color: #ff6402;
}

.el-tabs {
  --el-tabs-header-height: 45px;
}

.el-tabs__item {
  font-size: 15px;
}

.el-tabs__item.is-active {
  color: #ff6402;
}

.el-tabs__active-bar {
  background-color: #ff6402;
}

.spec-item {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 15px;
  position: relative;
}

.spec-item .el-button {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
}

.submit-button {
  width: 450px; /* 与表单等宽 */
  height: 40px;
  background-color: #ff6402;
  border: none;
  margin-bottom: 40px; /* 增加底部间距 */
  margin-right: 40px;
  font-size: 16px;
  transition: all 0.3s;
  padding: 0; /* 移除内边距以保持宽度一致 */
}
.submit-button:hover {
  background-color: #e65a00;
  transform: scale(1.02);
}

.el-upload {
  width: 100%;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}

/* 上传按钮样式 */
:deep(.el-upload-dragger) {
  width: 100%;
  height: 120px;
  border-color: #dcdfe6;
}

:deep(.el-upload-dragger:hover) {
  border-color: #ff6402;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}
</style>
