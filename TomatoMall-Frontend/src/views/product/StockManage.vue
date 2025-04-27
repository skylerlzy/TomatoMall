<script setup lang="ts">
import { stockInfo, getStocks, changeStock } from "../../api/product.ts";
import { ref } from "vue";
import { router } from "../../router";
import {ElButton, ElIcon, ElMessage} from 'element-plus';
import {Back} from "@element-plus/icons-vue";  // Assuming ElMessage is used for success messages

const productId = router.currentRoute.value.params.productId
const id = ref();
const amount = ref();
const frozen = ref();

const newAmount = ref();
const newFrozen = ref();

getStocks(Number(productId)).then((res) => {
  id.value = res.data.data.id;
  amount.value = res.data.data.amount;
  frozen.value = res.data.data.frozen;
});

function toBackPage(productId: number) {
  router.push('/productDetail/' + productId);
}

function updateStocks(productId: number) {
  changeStock(productId, {
    id: id.value,
    amount: newAmount.value,
    frozen: newFrozen.value,
    productId: productId
  }).then((res) => {
    if (res.data.code === '200') {
      ElMessage.success({
        message: '修改成功',
        type: 'success',
        center: true,
      });
      getStocks(Number(productId)).then((res) => {
        id.value = res.data.id;
        amount.value = res.data.data.amount;
        frozen.value = res.data.data.frozen;
      });
    } else {
      ElMessage.error({
        message: '修改失败',
        type: 'error',
        center: true,
      });
    }
  });
}
</script>

<template>
  <div class = "back">
    <el-button @click="toBackPage(Number(productId))" type="primary" circle plain class="back-button">
      <el-icon class = "back-icon">
        <Back />
      </el-icon>
    </el-button>
  </div>
  <el-card class="stock-card" shadow="hover">
    <h2>库存管理</h2>

    <el-row :gutter="20" class="stock-row">
      <el-col :span="12">
        <div class="stock-info">
          <p><strong>当前库存数量:</strong> {{ amount }}</p>
          <p><strong>当前冻结数量:</strong> {{ frozen }}</p>
        </div>
      </el-col>
    </el-row>

    <el-form label-position="top" class="stock-form">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="修改库存数量">
            <el-input-number v-model="newAmount" :min="0" :step="1" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="修改冻结数量">
            <el-input-number v-model="newFrozen" :min="0" :step="1" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item>
        <el-button type="primary" @click="updateStocks(productId)">提交修改</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<style scoped>

.back {
  margin-top: 20px;
  color: #ffffff; /* 修改文字颜色 */
  background-color: #ff6402; /* 修改背景颜色 */
  border: none; /* 去掉默认边框 */
  cursor: pointer; /* 添加鼠标悬停效果 */
  border-radius: 100%; /* 设置为圆形 */
  width: 40px; /* 调整宽度 */
  height: 40px; /* 调整高度 */
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
}
.back-button {
  color: #ffffff; /* 修改文字颜色 */
  background-color: #ff6402; /* 修改背景颜色 */
  border: none; /* 去掉默认边框 */
  cursor: pointer; /* 添加鼠标悬停效果 */
  border-radius: 100%; /* 设置为圆形 */
  width: 40px; /* 调整宽度 */
  height: 40px; /* 调整高度 */
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
}

.back-icon {
  width: 100px;
  height: 100px;
}

.back-button:hover {
  background-color: #e65a00; /* 鼠标悬停时的背景颜色 */
}
.stock-card {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
}

.stock-info {
  font-size: 16px;
  line-height: 1.8;
}

.stock-form {
  margin-top: 20px;
}

.stock-row {
  margin-bottom: 20px;
}
</style>
