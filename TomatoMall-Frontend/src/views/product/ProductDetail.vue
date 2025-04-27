<script setup lang="ts">
import { ref } from 'vue'
import { searchProduct,deleteProduct } from '../../api/product.ts'
import { Back } from '@element-plus/icons-vue'
import { router } from '../../router'
import {ElMessage} from "element-plus";
import { addToCart } from '../../api/cart.ts'

const role = sessionStorage.getItem('role')  // 用户身份
const id = Number(router.currentRoute.value.params.productId)
const title = ref('')
const price = ref()
const rate = ref()
const description = ref('')
const cover = ref('')
const detail = ref('')
const quantity = ref(1)

searchProduct(id).then((res)=>{
  title.value = res.data.data.title
  price.value = res.data.data.price
  rate.value = res.data.data.rate
  description.value = res.data.data.description
  cover.value = res.data.data.cover
  detail.value = res.data.data.detail
})
// 在原有的数量控制函数中添加输入框变化的处理
function handleInputNumberChange(value: number) {
  quantity.value = value
}
  // 移除原有的 toAddCartItemPage 函数，替换为直接添加到购物车的函数
  function handleAddToCart() {
    const cartItem = {
      productId: id,
      title: title.value,
      price: price.value,
      cover: cover.value,
      detail: detail.value,
      description: description.value,
      quantity: quantity.value,
    }

    addToCart(cartItem).then(res => {
      if (res.data.code === '200') {
        ElMessage({
          message: '添加商品成功！',
          type: 'success',
          center: true,
        })
      } else if (res.data.code === '400') {
        ElMessage({
          message: res.data.msg,
          type: 'error',
          center: true,
        })
      }
    }).catch(err => {
      console.error('商品添加至购物车失败', err)
      ElMessage({
        message: '商品添加至购物车失败，请重试！',
        type: 'error',
        center: true,
      })
    })
  }

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
  <div class="page-container">

      <!-- 返回按钮 -->

    <div class="main-content">
      <!-- 返回按钮 -->
      <div class="back-wrap">
        <el-button link @click="toBackPage()" class="back-button">
          <el-icon><Back /></el-icon>
          <span>返回</span>
        </el-button>
      </div>
      <!-- 商品核心信息区 -->
      <div class="product-essential">
        <!-- 左侧商品图片 -->
        <div class="preview-wrap">
          <el-image class="product-image" :src="cover" fit="contain" />
        </div>

        <!-- 右侧商品信息 -->
        <div class="product-info">
          <h1 class="product-title">{{ title }}</h1>

          <div class="product-summary">
            <div class="description-box">{{ description }}</div>
          </div>

          <div class="price-wrap">
            <div class="price-label">价格</div>
            <div class="price-box">
              <span class="price-symbol">¥</span>
              <span class="price-amount">{{ price }}</span>
            </div>
          </div>

          <div class="rating-wrap">
            <span class="rating-label">商品评分</span>
            <div class="rating-box">
              <span class="rating-score">{{ rate }}</span>
              <span class="rating-max">/5</span>
            </div>
          </div>
          <div class="action-buttons">
            <div class="quantity-control">
              <el-input-number
                  v-model="quantity"
                  :min="0"
                  style="width: 120px"
                  @change="handleInputNumberChange"
              />
            </div>
            <el-button
                type="primary"
                @click="handleAddToCart"
                class="add-cart-btn"
                :disabled="quantity === 0"
            >加入购物车</el-button>
          </div>

          <!-- 管理员控制面板 -->
          <div v-if="role === 'admin'" class="admin-panel">
            <el-button type="primary" @click="toChangeProduct(id)">编辑商品</el-button>
            <el-button type="warning" @click="toChangeStock(id)">库存管理</el-button>
            <el-button type="success" @click="manageComments">评论管理</el-button>
            <el-button type="danger" @click="deleteInfo(id)">删除商品</el-button>
          </div>
        </div>
      </div>

      <!-- 商品详情区 -->
      <div class="product-detail">
        <div class="detail-tabs">
          <div class="tab-header">
            <h3 class="active">商品详情</h3>
          </div>
          <div class="detail-content">
            <div class="detail-text">{{ detail }}</div>
          </div>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="comment-section">
        <div class="section-header">
          <h3>商品评价</h3>
        </div>
        <div class="comment-list">
          <el-card v-for="i in 3" :key="i" class="comment-item">
            <div class="comment-user">用户{{ i }}</div>
            <div class="comment-content">这本书真的很不错，内容详实，值得推荐！</div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  min-width: 990px;
  background: #f4f4f4;
}

.back-wrap {
  margin-bottom: 15px;
  padding: 8px;
  background-color: #ff6402;
  display: inline-block;
  border-radius: 4px;
}

.back-button {
  color: #fff;
  font-size: 14px;
  padding: 0;
  height: auto;
}

.back-button:hover {
  color: #fff;
  opacity: 0.8;
}


.main-content {
  width: 1190px;
  margin: 0 auto;
  padding: 20px 0;
}

.product-essential {
  display: flex;
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.preview-wrap {
  width: 400px;
  margin-right: 30px;
}

.product-image {
  width: 400px;
  height: 400px;
  border: 1px solid #f0f0f0;
}

.product-info {
  flex: 1;
  padding: 10px 20px;
}

.product-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  line-height: 1.5;
}

.product-summary {
  background: #f7f7f7;
  padding: 10px;
  margin: 10px 0;
  color: #666;
  display: inline-block;  /* 改为行内块级元素 */
  min-width: 300px;       /* 设置最小宽度 */
  border-radius: 4px;     /* 添加圆角 */
}

.price-wrap {
  margin: 20px 0;
  padding: 10px 20px;     /* 调整内边距 */
  background: #f7f7f7; /* 改为行内块级元素 */
  min-width: 200px;       /* 设置最小宽度 */
  border-radius: 4px;     /* 添加圆角 */
}

.price-label {
  color: #999;
  margin-bottom: 5px;
}

.price-box {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 16px;
  color: #e4393c;
}

.price-amount {
  font-size: 28px;
  color: #e4393c;
  font-weight: bold;
}

.action-buttons {
  margin-top: 20px;
}

.add-cart-btn {
  width: 180px;
  height: 48px;
  font-size: 16px;
  background: #e4393c;
  border-color: #e4393c;
}

.admin-panel {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 10px;
}

.product-detail {
  background: #fff;
  margin-bottom: 20px;
  border-radius: 4px;
}

.detail-tabs {
  padding: 20px;
}

.tab-header {
  height: 40px;
  line-height: 40px;
  background: #f7f7f7;
  padding: 0 20px;
  border-bottom: 1px solid #e4393c;
}

.detail-content {
  padding: 20px;
}

.comment-section {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
}

.section-header {
  border-bottom: 1px solid #e1e1e1;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.comment-item {
  border: none;
  box-shadow: none;
  border-bottom: 1px solid #f0f0f0;
}

.comment-user {
  color: #666;
  margin-bottom: 8px;
}
.quantity-control {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.comment-content {
  color: #333;
  line-height: 1.5;
}
</style>