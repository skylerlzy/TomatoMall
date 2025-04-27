<script setup lang="ts">
import {ref,computed,defineProps,onMounted} from 'vue'
import {router} from '../../router'
import {getProductList} from "../../api/product.ts";
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

const role = sessionStorage.getItem("role")
const productList = ref<productInfo[]>([])

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

// 点击商店卡片，跳转到对应的商店界面
function toProductDetailPage(productId: number) {
  router.push("/productDetail/" + productId)
}
//广告商品跳转函数
function toAdvertisementProductPage(productId: number) {
  router.push("/productDetail/" + productId)
}

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
    <div class="ad-container left-ad">
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
    <div>
      <el-scrollbar >
        <div class="product-item-list">
          <div v-for="product in filteredProducts" :key="product.id" class="product-card"
               @click="toProductDetailPage(product.id)">
            <el-image :src="product.cover" class="product-logoUrl" v-if="product.cover"/>
            <h2 class="product-title">{{ product.title }}</h2>
            <p class="product-description">{{ product.description }}</p>
            <p class="product-price">价格: ¥{{ product.price }}</p>
            <p class="product-rate">评分: {{ product.rate }}</p>
            <div class="product-specifications">
              <p v-for="spec in product.specifications" :key="spec.id">
                {{ spec.item }}: {{ spec.value }}
              </p>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>
  </el-main>
  </div>
  <!-- 右侧广告 -->
    <div class="ad-container right-ad">
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

.el-main {
  width: 100%;
  max-width: 1200px; /* 限制主内容区域宽度 */
}

.product-item-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center; /* 使产品卡片居中对齐 */
}

.product-card {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  width: 250px;
  cursor: pointer;
  transition: transform 0.2s;
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

.ad-container:hover {
  transform: scale(1.02);
}

.ad-image {
  width: 80%;
  height: 200px;
  border-radius: 4px;
}

.ad-content {
  padding: 10px 0;
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
</style>
