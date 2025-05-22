<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCollect } from '../../api/user.ts'
import { ElMessage } from 'element-plus'
import { router } from '../../router'

type productInfo = {
  id: number,
  title: string,
  price: number,
  rate: number,
  description: string,
  cover: string,
  detail: string,
  tag: string,
  author: string,
}

const collectList = ref<productInfo[]>([])
const loading = ref(true)

// 获取收藏商品列表
const fetchCollectList = async () => {
  try {
    loading.value = true
    const res = await getCollect()
    if (res.data.code === '200') {
      collectList.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取收藏列表失败')
    console.error('获取收藏列表失败', error)
  } finally {
    loading.value = false
  }
}

// 前往商品详情页
const toProductDetail = (productId: number) => {
  router.push(`/productDetail/${productId}`)
}

onMounted(() => {
  fetchCollectList()
})
</script>

<template>
  <div class="collection-container">
    <div class="collection-header">
      <h1>我的收藏</h1>
      <p>收藏夹中共有 {{ collectList.length }} 件商品</p>
    </div>

    <el-divider />

    <div class="collection-content">
      <el-empty
          v-if="!loading && collectList.length === 0"
          description="暂无收藏商品"
      />

      <el-skeleton
          v-else-if="loading"
          :rows="3"
          animated
          :count="4"
      />

      <div v-else class="product-grid">
        <div
            v-for="product in collectList"
            :key="product.id"
            class="product-card"
            @click="toProductDetail(product.id)"
        >
          <el-image
              :src="product.cover"
              class="product-cover"
              fit="cover"
          />
          <h3 class="product-title">{{ product.title }}</h3>
          <p class="product-desc">{{ product.description }}</p>
          <div class="product-footer">
            <p class="product-price">¥{{ product.price }}</p>
            <el-rate
                v-model="product.rate"
                disabled
                text-color="#ff9900"
            />
          </div>
          <div class="product-tags">
            <span class="tag-author">{{ product.author }}</span>
            <span class="tag-category">{{ product.tag }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.collection-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.collection-header {
  text-align: center;
  margin-bottom: 20px;
}

.collection-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.collection-header p {
  color: #666;
  font-size: 16px;
}

.collection-content {
  min-height: 400px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.product-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  padding: 20px;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: white;
  height: 380px;
  width: 260px; /* 固定卡片宽度 */
}

.product-cover {
  margin-left : 35px;
  width:70%;
  height: 200px; /* 调整图片高度 */
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
  border-color: #ff4d4f;
}

.product-title {
  font-size: 16px;
  margin: 8px 0;
  line-height: 1.3;
  height: 42px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-description {
  font-size: 13px;
  color: #666;
  margin: 8px 0;
  height: 38px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-desc {
  font-size: 13px;
  color: #666;
  margin: 8px 0;
  height: 38px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.product-price {
  font-size: 16px;
  color: #ff4d4f;
  font-weight: bold;
  margin: 8px 0;
}

.product-tags {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  font-size: 13px;
}

.tag-author, .tag-category {
  color: #666;
  padding: 2px 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

@media (max-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
}
</style>