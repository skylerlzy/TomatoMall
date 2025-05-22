<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { User, SwitchButton, Search, Position, ShoppingCart ,Star,Sell ,Notification,Coin,Postcard,Wallet} from "@element-plus/icons-vue"
import { userInfo } from '../api/user.ts'
import WalletCenter from "../views/user/WalletCenter.vue";

const router = useRouter()
const searchTerm = ref('')
const telephone = ref('')
const username = sessionStorage.getItem('username') || ''
const drawerVisible = ref(false)
const userAvatar = ref('')
const role = sessionStorage.getItem('role') || ''

userInfo(username).then((res) => {
  telephone.value = res.data.data.telephone
  userAvatar.value = res.data.data.avatar
})

const emit = defineEmits(['search'])
const emitSearch = () => {
  emit('search', searchTerm.value)
}

const logout = () => {
  ElMessageBox.confirm('是否要退出登录？', '提示', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
    showClose: false,
    roundButton: true,
    center: true
  }).then(() => {
    window.sessionStorage.setItem('token', '')
    router.push('/login')
  })
}

const toDashboard = () => {
  router.push('/dashboard')
}

// 点击创建商店按钮，跳转到创建商店界面
function toCreateProductPage() {
  router.push("/createProduct")
}
//创建广告按钮
function toAllAdvertisementPage() {
  router.push("/allAdvertisement")
}

//开通VIP按钮
function toVipCenter() {
     router.push("/vipCenter")
}

function toAllCoupon() {
  router.push("/allCoupon")
}

</script>

<template>
  <div class="header-container">
    <div class="header-left">
      <router-link to="/allProduct" class="logo-link">
        <h1 class="logo-text">TomatoMall</h1>
      </router-link>
    </div>

    <div class="header-center">
      <el-col :span="20" />
      <div class="search-box">
        <el-input
            v-model="searchTerm"
            placeholder="搜索"
            @input="emitSearch"
            clearable
            class="search-input">
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #suffix>
            <el-icon><Position /></el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <div class="header-right">
      <router-link to="/allCartItem" class="cart-icon">
        <el-icon :size="24" color="#ff6402">
          <ShoppingCart />
        </el-icon>
      </router-link>

      <div class="avatar-wrapper">
        <el-avatar
            :size="35"
            :src="userAvatar"
            @click="drawerVisible = true"
            class="user-avatar" />
      </div>
    </div>

    <el-drawer
        v-model="drawerVisible"
        direction="rtl"
        size="300px"
        :with-header="false">
      <div class="drawer-content">
        <div class="user-info">
          <el-avatar
              :src="userAvatar"
              class="user-avatar-large" />
          <div class="user-details">
            <span class="user-name">{{ username }}</span>
            <span class="user-phone">{{ telephone }}</span>
          </div>
        </div>

        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="toDashboard"
              text>
            <el-icon><User /></el-icon>
            修改个人信息
          </el-button>
        </div>
        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="router.push('/collection')"
              text>
            <el-icon><Star /></el-icon>
            <span>我的收藏</span>
          </el-button>
        </div>
        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="router.push('/walletCenter')"
              text>
            <el-icon><Wallet /></el-icon>
            <span>我的钱包</span>
          </el-button>
        </div>
        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="toAllCoupon"
              text>
            <el-icon><Postcard /></el-icon>
            优惠券
          </el-button>
        </div>

        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="toVipCenter"
              text>
            <el-icon><Coin /></el-icon>
            VIP中心
          </el-button>
        </div>
        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="logout"
              text>
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-button>
        </div>
        <div v-if="role === 'admin'">
        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="toCreateProductPage()"
              text>
            <el-icon><Sell /></el-icon>
            添加商品
          </el-button>
        </div>
        <div class="drawer-menu">
          <el-button
              class="menu-item"
              @click="toAllAdvertisementPage"
              text>
            <el-icon><Notification /></el-icon>
            广告管理
          </el-button>
        </div>
      </div>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
.header-container {
  height: 60px;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.header-left {
  flex: 0 0 180px; /* 固定宽度 */
}

.logo-text {
  color: #ff6402;
  margin: 0;
  font-size: 20px; /* 减小字体大小 */
}

.logo-link {
  text-decoration: none;
}

.logo-text {
  color: #ff6402;
  margin: 0;
  font-size: 24px;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 0 20px;
}

.search-box {
  width: 100%;
  max-width: 350px;  /* 将搜索框最大宽度从 500px 改为 350px */
}

.search-input {
  width: 70%;
}
.header-right {
  flex: 1;
  max-width: 200px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 20px;
}

.cart-icon {
  text-decoration: none;
  display: flex;
  align-items: center;
}

.avatar-wrapper {
  cursor: pointer;
}

.user-avatar {
  transition: transform 0.2s;
}

.user-avatar:hover {
  transform: scale(1.1);
}


.user-info {
  margin-top: 10px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.user-avatar-large {
  width: 60px;
  height: 60px;
  border-radius: 50%;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.user-phone {
  font-size: 14px;
  color: #909399;
}

.menu-item {
  margin-right: 20px;
  display: flex;
  align-items: center;
  width: 100%;
  padding: 20px;
  justify-content: flex-start;
  gap: 20px;
}

.drawer-content {
  padding: 0;
  display: flex;
  flex-direction: column;
  margin-top: -10px;
}

.menu-item {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 24px 16px;
  margin: 0;
  justify-content: flex-start;
  gap: 16px;
}

.drawer-menu {
  width: 80%;
  margin: 8px auto;
  border-bottom: 1px solid #ebeef5;
  padding: 8px 0;
}

.menu-item .el-icon {
  font-size: 18px;
  color: #ff6402;
  margin-left: 12px;
}

.user-info {
  padding: 20px 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.menu-item:hover {
  background-color: #f5f7fa;
}


.menu-item .el-icon {
  font-size: 18px;
  color: #ff6402;
  margin-left: 8px;
}

:deep(.el-drawer__body) {
  padding: 0;
}

:deep(.el-drawer) {
  border-radius: 16px 0 0 16px;
  overflow: hidden;
}

:deep(.el-button.is-text) {
  padding: 0;
}
</style>