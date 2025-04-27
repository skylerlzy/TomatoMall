<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Back } from '@element-plus/icons-vue'
import { router } from '../../router'
import { deleteAdvertisement, getAdvertisementList } from "../../api/advertisement.ts"
import {deleteProduct} from "../../api/product.ts";
import {ElMessage} from "element-plus";

const role = sessionStorage.getItem('role')
const id = Number(router.currentRoute.value.params.id)
const advertisementInfo = ref({
  id: 0,
  productId: 0,
  title: '',
  content: '',
  imgUrl: '',
})

// 获取广告详情
const getAdvertisementDetail = async () => {
  try {
    const res = await getAdvertisementList()
    const advertisement = res.data.data.find(item => item.id === id)
    if (advertisement) {
      advertisementInfo.value = advertisement
    }
  } catch (error) {
    ElMessage.error('获取广告详情失败')
  }
}

onMounted(() => {
  getAdvertisementDetail()
})

function deleteInfo(id: number) {
  deleteAdvertisement(id).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '删除广告成功！',
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
    console.error('广告删除失败', err)
    ElMessage({
      message: '广告创建失败，请重试！',
      type: 'error',
      center: true,
    })
  })
}

function toChangeProduct(id: number) {
  router.push('/changeAdvertisement/' + id)
}

function toBackPage() {
  router.push('/allAdvertisement')
}
</script>

<template>
  <el-container>
    <el-aside width="30%" class="page-aside">
      <div class="store-detail-main">
        <el-button @click="toBackPage()" type="primary" circle plain class="back-button">
          <el-icon><Back /></el-icon>
        </el-button>

        <el-image class="logo-image" :src="advertisementInfo.imgUrl" fit="contain" />

        <p class="store-title">{{ advertisementInfo.title }}</p>

        <el-descriptions :column="1" border>
          <el-descriptions-item label="标题">{{ advertisementInfo.title }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ advertisementInfo.content }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-aside>

    <el-main>
      <div v-if="role === 'admin'" class="product-controls">
        <el-button type="primary" @click="toChangeProduct(id)" class="control-button">
          修改广告信息
        </el-button>
        <el-button type="danger" @click="deleteInfo(id)" class="control-button">
          删除广告
        </el-button>
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

.add-button {
  border-color: #3ea87a;
  background-color: #3ea87a;
  margin-top: 20px;
  margin-left: 170px;
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
