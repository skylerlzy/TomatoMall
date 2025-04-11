<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElForm, ElFormItem, ElInput, ElButton, ElTabs, ElTabPane, ElMessage, ElMessageBox, ElUpload, ElIcon } from 'element-plus'
import { updateProduct } from '../../api/product.ts'
import { uploadImage } from '../../api/tool.ts'
import {Back, UploadFilled} from '@element-plus/icons-vue'
import {router} from "../../router";

// 商品基本信息表单数据
const productId = router.currentRoute.value.params.productId
const productForm = ref({
  id: productId,
  title: '',
  price: undefined,
  rate: undefined,
  description: '',
  cover: '',
  detail: ''
})

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
  const updateInfo = {
    id:productForm.value.id,
    title: productForm.value.title,
    price: productForm.value.price,
    rate: productForm.value.rate,
    description: productForm.value.description,
    cover: productForm.value.cover,
    detail: productForm.value.detail,
    specifications: specForm.value
  }

  // 由于我们没有实际的商品ID，这里假设商品ID会在后端生成，前端暂时不设置
  // 如果需要前端生成临时ID，可以使用UUID或其他方法
  updateProduct(updateInfo).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '修改商品信息成功！',
        type: 'success',
        center: true,
      })
      // 清空表单数据
      productForm.value = {
        id:productId,
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
  productForm.value.title = file.url
  let formData = new FormData()
  formData.append('file', file.raw)
  uploadImage(formData).then(res => {
    productForm.value.title = res.data.result
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

  <el-main class="main-frame">
    <el-form :model="productForm" label-width="120px">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="基本信息" name="basic" placeholder="......">
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
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-image: url('/src/resources/background/background.png');
  background-size: cover;
  background-position: center;
}


.back {
  margin-top: 20px;
  color: #ffffff; /* 修改文字颜色 */
  background-color: #ff6402; /* 修改背景颜色 */
  border: none; /* 去掉默认边框 */
  cursor: pointer; /* 添加鼠标悬停效果 */
  border-radius: 100%; /* 设置为圆形 */
  width: 40px; /* 调整宽度 */
  height: 40px; /* 调整高度 */
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
}
.back-button {
  color: #ffffff; /* 修改文字颜色 */
  background-color: #ff6402; /* 修改背景颜色 */
  border: none; /* 去掉默认边框 */
  cursor: pointer; /* 添加鼠标悬停效果 */
  border-radius: 100%; /* 设置为圆形 */
  width: 40px; /* 调整宽度 */
  height: 40px; /* 调整高度 */
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
}

.back-icon {
  width: 100px;
  height: 100px;
}

.back-button:hover {
  background-color: #e65a00; /* 鼠标悬停时的背景颜色 */
}

.spec-item {
  margin-bottom: 15px;
}

.el-form {
  width: 80%;
}

.el-tabs {
  width: 100%;
}

.el-tab-pane {
  width: 100%;
}

.avatar-upload {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 20px;
}

.button-group {
  color: #ff6402;
  background-color: #ff6402;
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}

.submit-button {
  border-color: white;
  color: #ffffff;
  background-color: #ff6402;
  margin-top: 20px;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}
</style>
