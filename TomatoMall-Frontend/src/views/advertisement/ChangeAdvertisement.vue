<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Back, Plus } from '@element-plus/icons-vue'
import { router } from '../../router'
import { updateAdvertisement, getAdvertisementList } from '../../api/advertisement.ts'
import { uploadImage } from '../../api/tool.ts'

const id = Number(router.currentRoute.value.params.id)

interface Advertisement {
  id: number
  title: string
  content: string
  imgUrl: string
  productId: number
}

const advertisementForm = ref<Advertisement>({
  id: id,
  title: '',
  content: '',
  imgUrl: '',
  productId: 0
})

const getAdvertisementDetail = async () => {
  try {
    const res = await getAdvertisementList()
    const advertisement = res.data.data.find((item: Advertisement) => item.id === id)
    if (advertisement) {
      advertisementForm.value = advertisement
    }
  } catch (error) {
    ElMessage.error('获取广告详情失败')
  }
}

const submitAdvertisement = async () => {
  try {
    const params = {
      id: advertisementForm.value.id,
      title: advertisementForm.value.title,
      content: advertisementForm.value.content,
      imgUrl: advertisementForm.value.imgUrl,
      productId: advertisementForm.value.productId
    }

    const res = await updateAdvertisement(params)
    if (res.data.code === '200') {
      ElMessage.success('修改广告成功')
      router.push('/allAdvertisement')
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  } catch (error) {
    ElMessage.error('修改广告失败')
  }
}

const handleUpload = async (file: any) => {
  const formData = new FormData()
  formData.append('file', file.raw)
  try {
    const res = await uploadImage(formData)
    if (res.data.code === '200') {
      advertisementForm.value.imgUrl = res.data.data
    }
  } catch (error) {
    ElMessage.error('图片上传失败')
  }
}

const handleExceed = () => {
  ElMessage.warning('只能上传一张图片')
}

const toBackPage = () => {
  router.push('/allAdvertisement')
}

onMounted(() => {
  getAdvertisementDetail()
})
</script>

<template>
  <div class="change-ad-container">
    <el-button @click="toBackPage()" type="primary" circle plain class="back-button">
      <el-icon><Back /></el-icon>
    </el-button>

    <el-card class="form-card">
      <template #header>
        <h2>修改广告</h2>
      </template>

      <el-form :model="advertisementForm" label-width="120px" class="ad-form">
        <el-form-item label="广告标题">
          <el-input v-model="advertisementForm.title" placeholder="请输入广告标题" />
        </el-form-item>

        <el-form-item label="广告内容">
          <el-input v-model="advertisementForm.content" type="textarea" placeholder="请输入广告内容" />
        </el-form-item>

        <el-form-item label="广告图片">
          <el-upload
              class="upload-container"
              :limit="1"
              :auto-upload="false"
              :on-change="handleUpload"
              :on-exceed="handleExceed"
              drag>
            <div v-if="advertisementForm.imgUrl" class="image-preview">
              <el-image :src="advertisementForm.imgUrl" fit="cover" class="preview-image" />
            </div>
            <div v-else class="upload-placeholder">
              <el-icon><Plus /></el-icon>
              <div>点击或拖拽文件上传</div>
            </div>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitAdvertisement" class="submit-button">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.change-ad-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.back-button {
  margin-bottom: 20px;
}

.form-card {
  border-radius: 8px;
}

.ad-form {
  padding: 20px;
}

.preview-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.upload-placeholder {
  width: 200px;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  color: #8c939d;
}

.submit-button {
  width: 100%;
  margin-top: 20px;
  background-color: #ff6402;
  border-color: #ff6402;
}

.submit-button:hover {
  background-color: #e65a00;
  border-color: #e65a00;
}
</style>