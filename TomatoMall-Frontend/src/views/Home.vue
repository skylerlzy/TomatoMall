<script setup lang="ts">
import { ref } from 'vue'
import Header from '../components/Header.vue'

const searchKeyword = ref('')

// 用于接收 Header 中发出的搜索关键词
const handleSearch = (keyword: string) => {
  searchKeyword.value = keyword
}
</script>

<template>
  <el-container direction="vertical">
    <!-- 搜索栏在 Home 里全局可见 -->
    <Header @search="handleSearch" />
    <router-view v-slot="{ Component }">
      <component :is="Component" :searchKeyword="searchKeyword" />
    </router-view>
  </el-container>
</template>
