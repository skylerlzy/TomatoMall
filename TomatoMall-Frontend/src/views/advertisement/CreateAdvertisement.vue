<script setup lang="ts">
import { ref, watch } from 'vue'
import {getProductList, searchProduct} from '../../api/product.ts'
import { createAdvertisement,getAdvertisementList,updateAdvertisement,deleteAdvertisement } from '../../api/advertisement.ts'
import { Back, Position, Search } from '@element-plus/icons-vue'
import { router } from "../../router"
import {uploadImage} from '../../api/tool.ts'
import {ElButton, ElFormItem, ElUpload} from "element-plus";

const searchTerm = ref('')
const searchResults = ref([])
const loading = ref(false)
type productInfo = {
  id : number,
  title : string,
  price : number,
  rate : number,
  description : string,
  cover : string,
  detail : string,
}

const productList = ref<productInfo[]>([])


getProductList().then(res => {
  productList.value = res.data.data
})
// 广告表单数据
const advertisementForm = ref({
  productId: 0,
  title: '',
  content: '',
  imgUrl: '',
})

const findProductIdByTitle = (title: string): number | null => {
  let product = productList.value.find(p => p.title.includes(title))
  if (product) {
    product.cover = ''
  }
  return product ? product.id : null
}

// 搜索商品
const handleSearch = async () => {
  if (!searchTerm.value) return

  loading.value = true
  try {
    const productId = findProductIdByTitle(searchTerm.value)
    if (!productId) {
      ElMessage({
        message: '未找到相关商品',
        type: 'warning'
      })
      return
    }

    const res = await searchProduct(String(productId))
    if (res.data.code === '200') {
      const product = res.data.data
      advertisementForm.value = {
        productId: product.id,
        title: product.title,
        content: product.description,
        imgUrl: product.cover
      }
    }
  } catch (err) {
    ElMessage({
      message: '搜索失败，请重试',
      type: 'error'
    })
  } finally {
    loading.value = false
  }
}

// 监听搜索输入
watch(searchTerm, (newVal) => {
  if (newVal) {
    handleSearch()
  }
})

// 提交广告信息
const submitAdvertisement = () => {
  if (!advertisementForm.value.productId) {
    ElMessage({
      message: '请先搜索并选择商品',
      type: 'warning'
    })
    return
  }

  createAdvertisement(advertisementForm.value).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '添加广告成功！',
        type: 'success',
      })
      router.push('/AllProduct')
    }
  })
}
// 图片上传
function handleUpload(file: any) {
  advertisementForm.value.imgUrl = file.url
  let formData = new FormData()
  formData.append('file', file.raw)
  uploadImage(formData).then(res => {
    advertisementForm.value.imgUrl = res.data.data
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
  <div class="create-ad-container">
    <div class="header">
      <el-button @click="router.push('/AllProduct')"
                 type="primary"
                 circle
                 class="back-button">
        <el-icon><Back /></el-icon>
      </el-button>
      <h2 class="page-title">创建商品广告</h2>
    </div>

    <div class="search-section">
      <el-input
          v-model="searchTerm"
          placeholder="输入书名搜索商品"
          :loading="loading"
          clearable
          class="search-input">
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <el-card class="form-card">
      <el-form :model="advertisementForm"
               label-width="120px"
               class="ad-form">
        <el-form-item label="商品ID">
          <el-input v-model="advertisementForm.productId" disabled />
        </el-form-item>

        <el-form-item label="广告标题">
          <el-input v-model="advertisementForm.title" />
        </el-form-item>

        <el-form-item label="广告内容">
          <el-input v-model="advertisementForm.content"
                    type="textarea"
                    rows="4" />
        </el-form-item>

        <el-form-item label="商品图片URL">
          <el-upload v-model:file="advertisementForm.imgUrl" :limit="1" :on-change="handleUpload" :on-exceed="handleExceed" :http-request="uploadHttpRequest" drag>
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传jpg/png文件，且不超过1MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary"
                     @click="submitAdvertisement"
                     :disabled="!advertisementForm.productId"
                     class="submit-button">
            提交广告
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.create-ad-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 0 20px;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.page-title {
  margin-left: 20px;
  color: #303133;
}

.search-section {
  margin-bottom: 30px;
}

.search-input {
  width: 100%;
}

.form-card {
  background-color: #fff;
  border-radius: 8px;
}

.ad-form {
  padding: 20px;
}

.preview-image {
  width: 200px;
  height: 200px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}

.empty-image {
  width: 200px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #909399;
}

.submit-button {
  width: 100%;
  margin-top: 20px;
  background-color: #ff6402;
  border-color: #ff6402;
}

.submit-button:hover {
  background-color: #e65a00;
  border-color: #e65a00;
}

@media (max-width: 768px) {
  .create-ad-container {
    padding: 0 10px;
  }

  .preview-image,
  .empty-image {
    width: 150px;
    height: 150px;
  }
}
</style>