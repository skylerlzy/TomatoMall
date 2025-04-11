<script setup lang="ts">
import {ElForm, ElFormItem, ElCarousel, ElCarouselItem} from "element-plus"
import {ref, computed} from 'vue'
import {router} from '../../router'
import {userInfo, userLogin} from "../../api/user.ts"

const images = ref(
    Object.keys(import.meta.glob('/src/resources/login/*.png'))
        .map(key => key.split('/').pop())
        .filter(name => name !== undefined && name !== '')
        .slice(0, 5) // 只取前5张图片
)

function getImagePath(name: string) {
  return new URL(`/src/resources/login/${name}`, import.meta.url).href
}

// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）
const username = ref('')
const password = ref('')

// 电话号码是否为空
const hasUserNameInput = computed(() => username.value !== '')
// 密码是否为空
const hasPasswordInput = computed(() => password.value !== '')
// 密码不设置特殊规则
// 登录按钮可用性
const loginDisabled = computed(() => {
  return !(hasUserNameInput.value  && hasPasswordInput.value)
})

// 登录按钮触发
function handleLogin() {
  userLogin({
    username: username.value,
    password: password.value
  }).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: "登录成功！",
        type: 'success',
        center: true,
      })
      const token = res.data.data
      sessionStorage.setItem('token', token)

      userInfo(username.value).then(res => {
        sessionStorage.setItem('name', res.data.data.name)
        sessionStorage.setItem('role', res.data.data.role)
        sessionStorage.setItem('userId', res.data.data.id)
        sessionStorage.setItem('avatar', res.data.data.avatar)
        sessionStorage.setItem('email', res.data.data.email)
        sessionStorage.setItem('username', res.data.data.username)
        sessionStorage.setItem('location', res.data.data.location)
        sessionStorage.setItem('phone', res.data.data.phone)
        sessionStorage.setItem('telephone', res.data.data.telephone)

        router.push({ path: "/allProduct" })
      })
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
      password.value = ''
    }
  })
}
</script>

<template>
  <el-header class = "custom-header" height ="20">
    <el-row :gutter="0.2">
      <el-col :span="3" class="header-icon">
      <router-link to="null" v-slot="{ navigate }" class="no-link">
        <h1 @click="navigate" class="header-text"> TomatoMall </h1>
      </router-link>
    </el-col>
    </el-row>
  </el-header>

  <el-main class="main-frame bgimage">
    <el-carousel :interval="4000" height="400px" type="card"  class="carousel">
      <el-carousel-item v-for="(image, index) in images" :key="index">
        <img :src="getImagePath(image)" alt="login" />
      </el-carousel-item>
    </el-carousel>
    <el-card class="login-card">
      <div>
        <h1>登入您的账户</h1>
        <el-form>
          <el-form-item>
            <label v-if="!hasUserNameInput" for="tel">用户昵称</label>用户昵称
            <el-input id="username" type="text" v-model="username"
                      required :class="{'error-warn-input' : (!hasUserNameInput)}"
                      placeholder="请输入用户昵称"/>
          </el-form-item>

          <el-form-item>
            <label for="password">账户密码</label>
            <el-input id="password" type="password" v-model="password"
                      required
                      placeholder="••••••••"/>
          </el-form-item>

          <span class="button-group">
              <el-button @click.prevent="handleLogin" :disabled="loginDisabled"
                         type="primary" class="login-button">登入</el-button>
              <router-link to="/register" v-slot="{navigate}">
                <el-button @click="navigate">去注册</el-button>
              </router-link>
          </span>
        </el-form>
      </div>
    </el-card>
  </el-main>
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

.header-text {
  color: #ff6402;
  font-size: x-large;
  min-width: max-content;
  margin-top: 15px;
  margin-left: 30px;
  margin-bottom: 15px;
  text-decoration: none;
}


.main-frame {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  background-image: url('/src/resources/background/background.png');
  background-size: cover;
  background-position: center;
  justify-content: space-between; /* 调整为两侧对齐 */
}

.carousel {
  width: 60%;
}

.login-card {

  width: 30%;
  margin-right:50px;
  padding: 30px;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: center;
}
.login-button {
  background-color: red;
  border-color: red;
}
</style>