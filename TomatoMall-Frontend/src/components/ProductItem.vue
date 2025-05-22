<script setup lang="ts">
import { ref } from "vue"
import { searchProduct } from '../api/product.ts'

const props = defineProps({
  productId: {
    type: string,
    required: true
  }
  }
)

const id = ref()
const title = ref('')
const rate = ref()
const price = ref()
const description = ref('')
const cover = ref('')
const detail = ref('')
const specifications = ref([])

initInfo(props.productId)

function initInfo(productId : string) {
  searchProduct(productId).then(res => {
    id.value = res.data.id
    title.value = res.data.title
    rate.value = res.data.rate
    price.value = res.data.price
    description.value = res.data.description
    cover.value = res.data.cover
    detail.value = res.data.detail
    specifications.value = res.data.specifications
  })
}

</script>

<template>
  <el-card class="product-item-card" :body-style="{ padding: '0px' }" shadow="hover">
    <el-image class="logo-image" :src="cover" />

    <div style="padding: 14px">
      <h1> {{ title }} </h1>
      <el-descriptions :column="1">
        <el-descriptions-item style="font-size: 10px" label="书名">
          {{ title }}书名
        </el-descriptions-item>
        <el-descriptions-item style="font-size: 10px" label="价格">
          {{ price }} 单价
        </el-descriptions-item>
        <el-descriptions-item style="font-size: 10px" label="评分">
          {{ rate }}评分
        </el-descriptions-item>
        <el-descriptions-item style="font-size: 10px" label="简述">
          {{ description }}
          </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>

</template>

<style scoped>

</style>