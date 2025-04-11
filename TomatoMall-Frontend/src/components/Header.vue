<script setup lang="ts">
import {ref} from 'vue'
import {router} from '../router'
import {User, SwitchButton,Search,Position} from "@element-plus/icons-vue"   //图标

const role = sessionStorage.getItem('role')    //登录的时候插入的
const searchTerm = ref('')

const emit = defineEmits(['search'])
const emitSearch = () => {
  emit('search', searchTerm.value)
}

//退出登录
function logout() {
  ElMessageBox.confirm(
      '是否要退出登录？',
      '提示',
      {
        customClass: "customDialog",
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: "warning",
        showClose: false,
        roundButton: true,
        center: true
      }
  ).then(() => {
    sessionStorage.setItem('token', '')
    router.push({path: "/login"})
  })
}

</script>

<template>
  <el-header class="custom-header" height="20">
    <el-row :gutter="0.2">
      <el-col :span="3" class="header-icon">
        <router-link to="/allStore" v-slot="{ navigate }" class="no-link">
          <h1 @click="navigate" class="header-text"> TomatoMall </h1>
        </router-link>
      </el-col>

      <el-col :span="6" >

      </el-col>

      <div class="search-box">
        <el-input
            v-model="searchTerm"
            placeholder="请输入书名搜索"
            @input="emitSearch"
            clearable
            class="search-input"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #suffix>
            <el-icon><Position /></el-icon>
          </template>
        </el-input>
      </div>

      <el-col :span="6"/>

      <el-col :span="1" class="header-icon">
        <router-link to="/dashboard" v-slot="{ navigate }">
          <el-icon @click="navigate" :size="35" color="#ff6402">
            <User/>
          </el-icon>
        </router-link>
      </el-col>

      <el-col :span="1" class="header-icon">
        <a @click="logout">
          <el-icon :size="35" color="#ff6402">
            <SwitchButton/>
          </el-icon>
        </a>
      </el-col>
    </el-row>
  </el-header>
</template>

<style scoped>
.custom-header {
  background-color: #ffffff;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  display: flex;
  flex-direction: column;
  border-bottom: 2px solid #ffffff; /* 添加边框 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影*/
}

.no-link {
  text-decoration: none;
}

.role-tag {
  margin-top: 20px;
  font-size: 20px;
}

.product-text {
  color: #ff6402;
  display: flex;
}

.header-text {
  color: #ff6402;
  font-size: x-large;
  min-width: max-content;
  margin-top: 15px;
  margin-left: 30px;
  margin-bottom: 15px;
  text-decoration: none;
}

.header-icon {
  color : #ff6402;
  display: flex;
  flex-direction: column;
  margin-top:5px;
  margin-right: 30px;
  align-items: center;
  justify-content: center;
}

.search-box {
  margin-top: 20px;
  margin-left: 40px;
  width: 300px; /* 你可以根据需求进一步缩小或扩大 */
}

.search-input :deep(.el-input__inner) {
  height: 40px;
  font-size: 16px;
  padding-left: 35px;
  padding-right: 35px; /* 为右侧图标预留空间 */
}
</style>
