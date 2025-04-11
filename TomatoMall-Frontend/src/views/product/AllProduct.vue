<script setup lang="ts">
import {ref,computed,defineProps} from 'vue'
import {router} from '../../router'
import {getProductList} from "../../api/product.ts";

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


//获取所有商品


// 点击创建商店按钮，跳转到创建商店界面
function toCreateProductPage() {
  router.push("/createProduct")
}

// 点击商店卡片，跳转到对应的商店界面
function toProductDetailPage(productId: number) {
  router.push("/productDetail/" + productId)
}
</script>

<template>
  <div class="head">
    <img :src="header" alt="" class="product-header" v-if="header"/>
  </div>

  <el-main>
    <div v-if="role === 'admin'">
      <el-button class="create-product-button" type="primary" plain @click="toCreateProductPage()">
        添加商品
      </el-button>
    </div>
    <div>
      <el-scrollbar max-height="750px" always>
        <div class="product-item-list">
          <div v-for="product in filteredProducts" :key="product.id" class="product-card"
               @click="toProductDetailPage(product.id)">
            <img :src="product.cover" alt="" class="product-logoUrl" v-if="product.cover"/>
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
</template>

<style scoped>
/* 这里可以添加一些样式来美化产品列表 */
.product-item-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.product-card {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  width: 300px;
  cursor: pointer;
  transition: transform 0.2s;
}

.product-card:hover {
  transform: scale(1.05);
}

.product-cover {
  width: 100%;
  height: auto;
  border-radius: 4px;
}

.product-title {
  font-size: 18px;
  margin: 10px 0;
}

.product-description, .product-price, .product-rate {
  font-size: 14px;
  color: #606266;
  margin: 5px 0;
}

.product-specifications {
  margin-top: 10px;
}

.product-specifications p {
  margin: 5px 0;
}
</style>
