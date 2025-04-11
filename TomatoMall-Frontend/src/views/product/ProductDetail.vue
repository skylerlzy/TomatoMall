<script setup lang="ts">
import { ref } from 'vue'
import { searchProduct,deleteProduct } from '../../api/product.ts'
import { Back } from '@element-plus/icons-vue'
import { router } from '../../router'
import {ElMessage} from "element-plus";

const role = sessionStorage.getItem('role')  // 用户身份
const id = router.currentRoute.value.params.productId
const title = ref('')
const price = ref()
const rate = ref()
const description = ref('')
const cover = ref('')
const detail = ref('')
const specifications = ref([])

  searchProduct(id).then(res => {
    title.value = res.data.data.title
    price.value = res.data.data.price
    rate.value = res.data.data.rate
    description.value = res.data.data.description
    cover.value = res.data.data.cover
    detail.value = res.data.data.detail
    specifications.value = res.data.data.specifications
  })



function toChangeProduct(id: number) {
  router.push('/changeProductInfo/' + id)
}

function toChangeStock(id: number) {
  router.push('/stockManage/' + id)
}

function deleteInfo(id: number) {
  deleteProduct(id).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '删除商品成功！',
        type: 'success',
        center: true,
      })
      // 清空表单数据
    router.push('/AllProduct')
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  }).catch(err => {
    console.error('商品删除失败', err)
    ElMessage({
      message: '商品创建失败，请重试！',
      type: 'error',
      center: true,
    })
  })
}

function toBackPage() {
  router.push('/AllProduct')
}

function manageComments() {
  router.push('/manageComments' + id)
}
</script>

<template>
  <el-container>
    <!-- 商品信息区域 -->
    <el-aside width="30%" class="page-aside">
      <div class="store-detail-main">
        <el-button @click="toBackPage()" type="primary" circle plain class="back-button">
          <el-icon><Back /></el-icon>
        </el-button>

        <el-image class="logo-image" :src="cover" fit="contain" />

        <p class="store-title">{{ title }}</p>

        <el-descriptions :column="1" border>
          <el-descriptions-item label="书名">{{ title }} </el-descriptions-item>
          <el-descriptions-item label="价格">{{ price }} 元</el-descriptions-item>
          <el-descriptions-item label="评分">{{ rate }} / 5</el-descriptions-item>
          <el-descriptions-item label="描述">{{ description }}</el-descriptions-item>
          <el-descriptions-item label="详情">{{ detail }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-aside>

    <!-- 评论区 + 管理按钮 -->
    <el-main>
      <!-- 仅 admin 可见的管理按钮 -->
      <div v-if="role === 'admin'" class="product-controls">
        <el-button type="primary" @click="toChangeProduct(id)" class="control-button">修改商品信息</el-button>
        <el-button type="warning" @click="toChangeStock(id)" class="control-button">修改库存</el-button>
        <el-button type="success" @click="manageComments" class="control-button">管理评论区</el-button>
        <el-button type="success" @click="deleteInfo(id)" class="control-button">删除商品</el-button>
      </div>

      <!-- 评论展示 -->
      <div class="comment-section">
        <h3 class="comment-title">商品评论</h3>
        <el-card v-for="i in 3" :key="i" class="comment-item">
          <div class="comment-user">用户 {{ i }}：</div>
          <div class="comment-content">这本书真的很不错，内容详实，值得推荐！</div>
        </el-card>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.page-aside {
  background-color: #fafafa;
  padding: 20px;
  border-right: 1px solid #ebeef5;
}

.back-button {
  margin-bottom: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.logo-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 12px;
  margin: 10px 0 20px 0;
}

.store-title {
  font-size: 22px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.comment-title {
  font-size: 20px;
  margin: 20px 0 10px;
  font-weight: bold;
  color: #409EFF;
}

.comment-section {
  margin-top: 20px;
}

.comment-item {
  margin-bottom: 16px;
}

.comment-user {
  font-weight: bold;
  margin-bottom: 6px;
}

.comment-content {
  color: #666;
}

.product-controls {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.control-button {
  font-size: 14px;
}
</style>
