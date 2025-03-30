<script setup lang="ts">
import {router} from '../router'
import {User, SwitchButton} from "@element-plus/icons-vue"   //图标

const role = sessionStorage.getItem('role')    //登录的时候插入的

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


      <el-col :span="16">
      </el-col>

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
  background-color: #00000f;
  display: flex;
  flex-direction: column;
}

.no-link {
  text-decoration: none;
}

.role-tag {
  margin-top: 20px;
  font-size: 20px;
}

.header-text {
  color: #ff6402;
  font-size: x-large;
  min-width: max-content;
  margin-top: 15px;
  margin-left: 30px;
  margin-bottom: 15px;
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

.header-icon {
  color : #ff6402;
  display: flex;
  flex-direction: column;
  margin-top:5px;
  margin-right: 30px;
  align-items: center;
  justify-content: center;
}
</style>
