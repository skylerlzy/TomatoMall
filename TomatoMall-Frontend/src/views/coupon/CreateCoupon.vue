<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { addCoupon, updateCoupon, CouponInfo } from '../../api/coupon'
import { getProductList, ProductInfo } from '../../api/product'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const products = ref<ProductInfo[]>([])

const formData = ref<CouponInfo>({
  isCommon: true,
  volume: 0,
  need: 0,
  deadline: '',
  number: 1,
  productIds: []
})

// 加载商品列表
const loadProducts = async () => {
  try {
    const res = await getProductList()
    if (res.data.code === '200') {
      products.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取商品列表失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    const api = addCoupon
    const res = await api(formData.value)
    if (res.data.code === '200') {
      ElMessage.success('添加成功')
      router.push('/allCoupon')
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

// 取消按钮
const handleCancel = () => {
  router.push('/allCoupon')
}

onMounted(() => {
  loadProducts()
})
</script>

<template>
  <div class="create-coupon-container">
    <div class="page-header">
      <h1>创建优惠券</h1>
    </div>
    <el-form :model="formData" label-width="100px" class="coupon-form">
      <el-form-item label="优惠券类型">
        <el-radio-group v-model="formData.isCommon">
          <el-radio :label="true">通用券</el-radio>
          <el-radio :label="false">商品券</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="优惠金额">
        <el-input-number
            v-model="formData.volume"
            :min="0"
            :precision="2"
            :step="0.01"/>
      </el-form-item>

      <el-form-item label="使用门槛">
        <el-input-number
            v-model="formData.need"
            :min="0"
            :precision="2"
            :step="0.01"/>
      </el-form-item>

      <el-form-item label="有效期至">
        <el-date-picker
            v-model="formData.deadline"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"/>
      </el-form-item>

      <el-form-item label="发放数量">
        <el-input-number
            v-model="formData.number"
            :min="1"
            :step="1"/>
      </el-form-item>

      <el-form-item
          label="可用商品"
          v-if="!formData.isCommon">
        <el-select
            v-model="formData.productIds"
            multiple
            filterable
            placeholder="选择商品"
            style="width: 100%">
          <el-option
              v-for="product in products"
              :key="product.id"
              :label="product.title"
              :value="product.id">
            <span>{{ product.title }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">
              ¥{{ product.price }}
            </span>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSubmit">创建</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.create-coupon-container {
  width: 100%;
  padding: 24px;
  box-sizing: border-box;
}

.page-header {
  max-width: 800px;
  margin: 0 auto 30px;
  padding: 0 16px;
}

.page-header h1 {
  font-size: 24px;
  color: #333;
  font-weight: 600;
  margin: 0;
}

.coupon-form {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

:deep(.el-select-dropdown__item) {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>