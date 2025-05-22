<script setup lang="ts">
import {ref,computed,defineProps,onMounted} from 'vue'
import {router} from '../../router'
import {getProductList,getSalesList,getRateList} from "../../api/product.ts";
import { Close } from '@element-plus/icons-vue'
import { getAdvertisementList } from "../../api/advertisement.ts"

type productSpecifications = {
  id?: number,
  item: string,
  value: string,
  productId: number,
}

type productInfo = {
  id: number,
  title: string,
  price: number,
  rate: number,
  description: string,
  cover: string,
  detail: string,
  specifications: productSpecifications[],
}


const salesRanking = ref<productInfo[]>([])
const rateRanking = ref<productInfo[]>([])
const role = sessionStorage.getItem("role")
const productList = ref<productInfo[]>([])
const isVIP = sessionStorage.getItem("isVIP") === "true"

const props = defineProps({
  searchKeyword: {
    type: String,
    default: ''
  }
})

getProductList().then(res => {
  productList.value = res.data.data
})


const filteredProducts = computed(() => {
  if (!props.searchKeyword) return productList.value
  return productList.value.filter(p =>
      p.title.includes(props.searchKeyword)
  )
})

const header = ref ("src/resources/book/header.png")

const recommendedProducts = computed(() => {
  return productList.value.filter(product => product.price < 700)
})
// 点击商店卡片，跳转到对应的商店界面
function toProductDetailPage(productId: number) {
  router.push("/productDetail/" + productId)
}
//广告商品跳转函数
function toAdvertisementProductPage(productId: number) {
  router.push("/productDetail/" + productId)
}


// 获取排行榜数据的函数
const fetchRankingData = async () => {
  try {
    const [salesRes, rateRes] = await Promise.all([
      getSalesList(),
      getRateList()
    ])

    if (salesRes.data.code === '200') {
      salesRanking.value = salesRes.data.data
    }

    if (rateRes.data.code === '200') {
      rateRanking.value = rateRes.data.data
    }
  } catch (error) {
    console.error('获取排行榜数据失败', error)
  }
}

// 在组件挂载时获取数据
onMounted(() => {
  fetchRankingData()
  // 其他已有的 onMounted 逻辑保持不变...
})

// 表格列定义
const salesColumns = [
  { prop: 'rank', label: '排名', width: '80' },
  { prop: 'title', label: '图书名称' },
  { prop: 'sales', label: '销量', width: '100' }
]

const rateColumns = [
  { prop: 'rank', label: '排名', width: '80' },
  { prop: 'title', label: '图书名称' },
  { prop: 'rate', label: '评分', width: '100' }
]

// 添加序号
const salesRankingWithRank = computed(() => {
  return salesRanking.value.map((item, index) => ({
    ...item,
    rank: index + 1
  }))
})

const rateRankingWithRank = computed(() => {
  return rateRanking.value.map((item, index) => ({
    ...item,
    rank: index + 1
  }))
})

type Advertisement = {
  productId: number
  title: string
  content: string
  imgUrl: string
}

const advertisements = ref<Advertisement[]>([])
const leftAd = ref<Advertisement>()
const rightAd = ref<Advertisement>()

const showLeftAd = ref(true)
const showRightAd = ref(true)

const hideAd = (position: 'left' | 'right') => {
  if (position === 'left') {
    showLeftAd.value = false
  } else {
    showRightAd.value = false
  }
}
// 获取广告并随机选择两条不同的广告
const initAdvertisements = async () => {
  const res = await getAdvertisementList()
  if (res.data.code === '200') {
    advertisements.value = res.data.data
    if (advertisements.value.length >= 2) {
      const indices = new Set<number>()
      while (indices.size < 2) {
        indices.add(Math.floor(Math.random() * advertisements.value.length))
      }
      const [index1, index2] = Array.from(indices)
      leftAd.value = advertisements.value[index1]
      rightAd.value = advertisements.value[index2]
    }
  }
}

// 在组件挂载时获取广告
onMounted(() => {
  initAdvertisements()
})


</script>

<template>
  <div class="page-container">
  <!-- 左侧广告 -->
    <div class="ad-container left-ad" v-if="!isVIP">
      <div v-show="leftAd && showLeftAd" class="ad-content">
        <el-icon class="close-icon" @click.stop="hideAd('left')"><Close /></el-icon>
        <div class="ad-image-wrapper" @click="toAdvertisementProductPage(leftAd?.productId)">
          <el-image :src="leftAd?.imgUrl" class="ad-image" fit="cover"/>
        </div>
      </div>
    </div>
  <div class = "all-product-page">
  <div class="head">
    <img :src="header" alt="" class="product-header" v-if="header"/>
  </div>

  <el-main>
    <div class="recommended-section">
      <h2 class="section-title">今日推荐图书</h2>
      <div class="recommended-container">
        <el-carousel :interval="3000" type="card" height="300px" >
          <el-carousel-item v-for="product in recommendedProducts" :key="product.id">
            <div class="carousel-card" @click="toProductDetailPage(product.id)">
              <el-image :src="product.cover" class="product-logoUrl" v-if="product.cover"/>
              <h2 class="product-title">{{ product.title }}</h2>
              <p class="product-price">价格: ¥{{ product.price }}</p>
              <el-rate
                  v-model="product.rate"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}"
              />
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <!-- 在今日推荐和所有商品之间添加排行榜部分 -->
    <div class="ranking-section">
      <h2 class="section-title">排行榜</h2>
      <div class="ranking-container">
        <!-- 销量排行榜 -->
        <div class="ranking-table">
          <h3 class="ranking-title">销量榜</h3>
          <el-table
              :data="salesRankingWithRank"
              style="width: 100%"
              :header-cell-style="{ background: '#f8f8f8', color: '#303133' }"
              highlight-current-row
              @row-click="(row) => toProductDetailPage(row.id)"
          >
            <el-table-column
                prop="rank"
                label="排名"
                width="80"
                align="center"
            >
              <template #default="scope">
                <div
                    class="rank-number"
                    :class="{
                  'top-rank': scope.row.rank <= 3,
                  'rank-1': scope.row.rank === 1,
                  'rank-2': scope.row.rank === 2,
                  'rank-3': scope.row.rank === 3
                }"
                >
                  {{ scope.row.rank }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="图书名称">
              <template #default="scope">
                <div class="book-title-cell">
                  <el-image
                      :src="scope.row.cover"
                      fit="cover"
                      class="rank-cover"
                  />
                  <span class="rank-title">{{ scope.row.title }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="sales" label="销量" width="100" align="center" />
          </el-table>
        </div>

        <!-- 评分排行榜 -->
        <div class="ranking-table">
          <h3 class="ranking-title">评分榜</h3>
          <el-table
              :data="rateRankingWithRank"
              style="width: 100%"
              :header-cell-style="{ background: '#f8f8f8', color: '#303133' }"
              highlight-current-row
              @row-click="(row) => toProductDetailPage(row.id)"
          >
            <el-table-column
                prop="rank"
                label="排名"
                width="80"
                align="center"
            >
              <template #default="scope">
                <div
                    class="rank-number"
                    :class="{
                  'top-rank': scope.row.rank <= 3,
                  'rank-1': scope.row.rank === 1,
                  'rank-2': scope.row.rank === 2,
                  'rank-3': scope.row.rank === 3
                }"
                >
                  {{ scope.row.rank }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="图书名称">
              <template #default="scope">
                <div class="book-title-cell">
                  <el-image
                      :src="scope.row.cover"
                      fit="cover"
                      class="rank-cover"
                  />
                  <span class="rank-title">{{ scope.row.title }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="rate" label="评分" width="100" align="center">
              <template #default="scope">
                <div class="rate-value">
                  {{ scope.row.rate.toFixed(1) }}
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <div class="all-products-section">
      <h2 class="section-title">所有商品</h2>
      <div>
      <el-scrollbar >
        <div class="product-item-list">
          <div v-for="product in filteredProducts" :key="product.id" class="product-card"
               @click="toProductDetailPage(product.id)">
            <el-image :src="product.cover" class="product-logoUrl" v-if="product.cover"/>
            <h2 class="product-title">{{ product.title }}</h2>
            <p class="product-description" font-size="20px">{{ product.description }}</p>
            <p class="product-price">价格: ¥{{ product.price }}</p>
            <el-rate
                v-model=product.rate
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
            />
            <div class="product-specifications">
              <p v-for="spec in product.specifications" :key="spec.id">
                {{ spec.item }}: {{ spec.value }}
              </p>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>
    </div>
  </el-main>
  </div>
  <!-- 右侧广告 -->
    <div class="ad-container right-ad" v-if="!isVIP">
      <div v-show="rightAd && showRightAd" class="ad-content">
        <el-icon class="close-icon" @click.stop="hideAd('right')"><Close /></el-icon>
        <div class="ad-image-wrapper" @click="toAdvertisementProductPage(rightAd?.productId)">
          <el-image :src="rightAd?.imgUrl" class="ad-image" fit="cover"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
:deep(body) {
  background-color: #f5f5f5;
}
/*allProduct*/
.all-product-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 1600px; /* 设置最大宽度 */
  margin-top:2px;
  padding: 0 45px; /* 两侧预留空间 */
}

.head {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.product-header {
  width: 100%;
  max-width: 1200px; /* 限制头部图片宽度 */
  height: auto;
}
.recommended-section {
  margin-bottom: 40px;
  width: 100%;
}

.section-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #ff4d4f;
}

.recommended-list {
  display: flex;
  gap: 15px;
  overflow-x: auto;
  padding: 10px 0;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}

.recommended-list::-webkit-scrollbar {
  display: none; /* Chrome, Safari and Opera */
}

.recommended-section {
  margin-bottom: 40px;
  width: 100%;
}

.recommended-container {
  width: 100%;
  padding: 10px 0;
}

.carousel-card {
  margin-left:80px;
  width: 60%;
  height: 100%;
  background-color: white;
  border: 1px solid #e0e0e0;  /* 添加更明显的边框 */
  border-radius: 8px;  /* 增加圆角 */
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);  /* 增强阴影效果 */
  padding: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;  /* 平滑过渡效果 */
}

.carousel-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);  /* 悬停时更强的阴影 */
  border-color: #ff4d4f;  /* 悬停时边框颜色变化 */
}

.carousel-card .product-logoUrl {
  width: 30%;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.carousel-card .product-title {
  font-size: 16px;
  margin: 8px 0;
  line-height: 1.3;
  height: 42px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  text-align: center;
}

.carousel-card .product-price {
  font-size: 14px;
  margin: 5px 0;
  color: #ff4d4f;
  font-weight: bold;
}

/* 修改Element Plus走马灯样式 */
:deep(.el-carousel__item) {
  border-radius: 4px;
}

:deep(.el-carousel__arrow) {
  background-color: rgba(255, 77, 79, 0.7);
}

:deep(.el-carousel__arrow:hover) {
  background-color: rgba(255, 77, 79, 0.9);
}

:deep(.el-carousel__indicators .el-carousel__button) {
  background-color: #ff4d4f;
}

.el-main {
  width: 100%;
  max-width: 1200px; /* 限制主内容区域宽度 */
}
/* 调整商品图片大小 */
.product-logoUrl {
  width: 100%;
  height: 180px; /* 减小图片高度 */
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.product-item-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center; /* 使产品卡片居中对齐 */
}

.product-card {
  height: 350px;
  border: 1px solid #e0e0e0;  /* 更明显的边框 */
  border-radius: 8px;  /* 增加圆角 */
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);  /* 增强阴影效果 */
  padding: 20px;
  width: 200px;
  cursor: pointer;
  transition: all 0.3s ease;  /* 平滑过渡效果 */
  background-color: white;  /* 确保背景为白色 */
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);  /* 悬停时更强的阴影 */
  border-color: #ff4d4f;  /* 悬停时边框颜色变化 */
}

/* 调整标题样式 */
.product-title {
  font-size: 16px;
  margin: 8px 0;
  line-height: 1.3;
  height: 42px; /* 限制标题高度为两行 */
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 调整描述文字样式 */
.product-description {
  font-size: 13px;
  color: #666;
  margin: 8px 0;
  height: 38px; /* 限制描述高度 */
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 调整价格和评分样式 */
.product-price, .product-rate {
  font-size: 14px;
  margin: 5px 0;
}

/* 调整规格信息样式 */
.product-specifications {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.product-specifications p {
  margin: 3px 0;
}

.create-product-button {
  margin-bottom: 20px;
}
.page-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  gap: 10px; /* 添加间距 */
}

.ad-container {
  width: 200px;
  flex-shrink: 0; /* 防止广告容器被压缩 */
  position: sticky;
  top: 20px;
  height: 600px; /* 固定高度 */
}

.ad-content {
  width: 100%;
  height: 100%;
}

.ad-image-wrapper {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.ad-image {
  width: 100%;
  height: 100%;
  border-radius: 4px;
}

.close-icon {
  position: absolute;
  top: 8px;
  right: 8px;
  z-index: 1;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  padding: 4px;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.close-icon:hover {
  background-color: rgba(255, 255, 255, 0.9);
  color: #333;
  transform: scale(1.1);
}

.ad-container {
  background-color: #f5f5f5; /* 与全局背景色一致 */
}

.ad-image {
  width: 80%;
  height: 200px;
  border-radius: 4px;
}

.ad-content {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 10px;
}

.ad-content h3 {
  margin: 0 0 8px;
  font-size: 16px;
  color: #333;
}

.ad-content p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.all-product-page {
  flex: 1;
  min-width: 0; /* 防止内容溢出 */
  margin: 0;
}

.ranking-section {
  margin: 30px 0 40px;
  width: 100%;
}

.ranking-container {
  display: flex;
  gap: 20px;
  width: 100%;
  margin-top: 20px;
}

.ranking-table {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 15px;
  transition: transform 0.3s;
}

.ranking-table:hover {
  transform: translateY(-5px);
}

.ranking-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #ff4d4f;
}

.rank-number {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #f0f0f0;
  color: #666;
  font-weight: bold;
}

.top-rank {
  color: white;
}

.rank-1 {
  background-color: #ff4d4f;
}

.rank-2 {
  background-color: #ff7e82;
}

.rank-3 {
  background-color: #ffb199;
}

.book-title-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.rank-cover {
  width: 40px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.rank-title {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.rate-value {
  font-weight: 600;
  color: #ff9900;
  font-size: 15px;
}
.rate-display {
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 让表格行可点击的样式 */
:deep(.el-table tr) {
  cursor: pointer;
}

:deep(.el-table tr:hover > td) {
  background-color: #f9f0f0 !important;
}

/* 自定义表格样式 */
:deep(.el-table th) {
  font-weight: 600;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table--enable-row-hover .el-table__body tr:hover > td.el-table__cell) {
  background-color: #fff2f2;
}
</style>
