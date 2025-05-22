<script setup lang="ts">
import {ref,computed,defineProps} from 'vue'
import {router} from '../../router'
import {getAdvertisementList} from "../../api/advertisement.ts";
import {Back} from "@element-plus/icons-vue";

type advertisementInfo = {
  id: number,
  productId: number,
  title: string,
  content: string,
  imgUrl: string,
}

const role = sessionStorage.getItem("role")
const advertisementList = ref<advertisementInfo[]>([])

const props = defineProps({
  searchKeyword: {
    type: String,
    default: ''
  }
})

getAdvertisementList().then(res => {
  advertisementList.value = res.data.data
})


// 点击创建商店按钮，跳转到创建商店界面
function toCreateProductPage() {
  router.push("/createProduct")
}

//创建广告按钮
function toCreateAdvertisementPage() {
  router.push("/createAdvertisement")
}

//广告商品跳转函数
function toAdvertisementDetailPage(id: number) {
  router.push("/advertisementDetail/" + id)
}

// 返回按钮
function toBackPage() {
  router.push('/AllProduct')
}

</script>

<template>
  <div class = "all-advertisement-page">
    <el-button @click="toBackPage()" type="primary" circle plain class="back-button">
      <el-icon><Back /></el-icon>
    </el-button>
    <el-main>
      <div v-if="role === 'admin'">
        <el-button class="create-product-button" type="primary" plain @click="toCreateAdvertisementPage()">
          添加广告
        </el-button>
      </div>

      <div>
        <el-scrollbar max-height="750px" always>
          <div class="product-item-list">
            <div v-for="advertisement in advertisementList" :key="advertisement.id" class="product-card"
                 @click="toAdvertisementDetailPage(advertisement.id)">
              <el-image :src="advertisement.imgUrl" class="product-logoUrl" v-if="advertisement.imgUrl"/>
              <h2 class="product-title">{{ advertisement.title }}</h2>
              <p class="product-description">{{ advertisement.content }}</p>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </el-main>
  </div>
</template>

<style scoped>
/* 调整主容器布局 */
.all-advertisement-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 1600px;
  margin: 0 auto;
  padding: 30px 45px;
  background: #f4f4f4;
  min-height: 100vh;
}

/* 调整主内容区布局 */
.el-main {
  width: 100%;
  max-width: 1200px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.back-button {
  position: fixed;
  left: 20px;
  top: 60px; /* 从40px改为60px */
  width: 40px;
  height: 40px;
  background: #ff6402;
  border: none;
  color: white;
  transition: all 0.3s;
  z-index: 1000;
}

.back-button:hover {
  background: #e65a00;
  transform: scale(1.05);
}

/* 添加广告按钮 - 居中对齐 */
.create-product-button {
  position: relative;
  left: 50%;
  transform: translateX(-50%);
  margin-bottom: 20px;
  height: 40px;
  padding: 0 25px;
  background: #ff6402;
  border: none;
  color: white;
  font-size: 14px;
  border-radius: 4px;
  transition: all 0.3s;
}

.create-product-button:hover {
  background: #e65a00;
  transform: translateX(-50%) scale(1.02);
}

/* 广告列表 */
.product-item-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  padding: 0 1px;
}

.product-card {
  background: white;
  padding: 12px;
  border-radius: 2px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #eee;
}

.product-card:hover {
  box-shadow: 0 3px 8px rgba(0,0,0,0.08);
  transform: translateY(-1px);
}

.product-logoUrl {
  width: 100%;
  height: 200px;
  object-fit: contain;
}

.product-title {
  padding: 10px 0;
  margin: 0;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  border-bottom: 1px solid #f0f0f0;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-description {
  padding: 10px 0;
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.5;
  height: 54px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

/* 滚动条样式 */
:deep(.el-scrollbar__wrap) {
  margin-bottom: 0 !important;
}

:deep(.el-scrollbar__bar) {
  opacity: 0.3;
}

:deep(.el-scrollbar__bar:hover) {
  opacity: 0.8;
}
</style>
