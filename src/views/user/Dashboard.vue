<script setup lang="ts">
import { computed, ref } from 'vue'
import {userInfo, userInfoUpdate} from '../../api/user.ts'
import { router } from '../../router'
import { UserFilled } from "@element-plus/icons-vue"

const role = sessionStorage.getItem("role")

//old info
const name = ref('')
const username = ref('')
const avatar = ref('')
const tel = ref('')
const location = ref('')
const password = ref('')
const email = ref('')

//new info
const newName = ref('')
const newUserName = ref('')
const newPassword = ref('')
const newAvatar = ref('')
const newTel = ref('')
const newEmail = ref('')
const newLocation = ref('')

const displayInfoCard = ref(false)
const confirmPassword = ref('')

const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
const isPasswordIdentical = computed(() => newPassword.value == confirmPassword.value)
const changeDisabled = computed(() => {
  return !(hasConfirmPasswordInput.value && isPasswordIdentical.value)
})

getUserInfo()

function getUserInfo() {
  userInfo(sessionStorage.getItem('username')).then(res => {
    name.value = res.data.data.name
    username.value = res.data.data.username
    email.value = res.data.data.email
    avatar.value = res.data.data.avatar
    tel.value = res.data.data.telephone
    location.value = res.data.data.location
  })
}

function updateInfo() {
  userInfoUpdate({
    username: newUserName.value,
    name: newName.value,
    telephone: newTel.value,
    password: password.value,
    avatar:newAvatar.value,
    email: newEmail.value,
    location: newLocation.value,
    role:"user"
  }).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        customClass: 'customMessage',
        type: 'success',
        message: '更新成功！',
      })
      getUserInfo()
    } else if (res.data.code === '400') {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}

function updatePassword() {
  userInfoUpdate({
    username: username.value,
    name: name.value,
    telephone: tel.value,
    password: newPassword.value,
    avatar:avatar.value,
    email: email.value,
    location: location.value,
    role:"user",
  }).then(res => {
    if (res.data.code === '200') {
      password.value = ''
      confirmPassword.value = ''
      ElMessageBox.alert(
          `请重新登录`,
          '修改成功',
          {
            customClass: "customDialog",
            confirmButtonText: '跳转到登录',
            type: "success",
            showClose: false,
            roundButton: true,
            center: true
          }).then(() => router.push({ path: "/login" }))
    } else if (res.data.code === '400') {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
      password.value = ''
      confirmPassword.value = ''
    }
  })
}
</script>


<template>
  <el-main class="main-container">
    <el-card class="aside-card" shadow="always">
      <div class="avatar-area">
        <el-avatar :icon="UserFilled" :size="80">
        </el-avatar>
        <span class="avatar-text"> 欢迎您，{{ name }}</span>
      </div>

      <el-divider></el-divider>

      <el-descriptions :column="1" border title="个人信息">
        <template #extra>
          <el-button type="primary" @click="displayInfoCard = displayInfoCard === false;">
            <span v-if="displayInfoCard">修改密码</span>
            <span v-else>修改个人信息</span>
          </el-button>
        </template>

        <el-descriptions-item label="昵称">
          {{ username }}
        </el-descriptions-item>

        <el-descriptions-item label="联系电话">
          {{ tel }}
        </el-descriptions-item>

        <el-descriptions-item label="邮箱">
          {{ email }}
        </el-descriptions-item>

        <el-descriptions-item label="地址">
          {{ location }}
        </el-descriptions-item>

      </el-descriptions>
    </el-card>

    <el-card v-if="displayInfoCard" class="change-card" shadow="always">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button @click="updateInfo">更新</el-button>
        </div>
      </template>

      <el-form>

        <el-form-item>
          <label for="name">姓名</label>
          <el-input type="text" id="username" v-model="newName" />
        </el-form-item>

        <el-form-item>
          <label for="username">昵称</label>
          <el-input type="text" id="username" v-model="newUserName" />
        </el-form-item>

        <el-form-item>
          <label for="phone">手机号</label>
          <el-input id="phone" v-model="tel"/>
        </el-form-item>

        <el-form-item>
          <label for="email">邮箱</label>
          <el-input id="email" v-model="email"/>
        </el-form-item>


        <el-form-item >
          <label for="location">收货地址</label>
          <el-input id="location" type="textarea" rows="4" v-model="location" placeholder="中华门"></el-input>
        </el-form-item>

      </el-form>
    </el-card>

    <el-card v-if="!displayInfoCard" class="change-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
          <el-button @click="updatePassword" :disabled="changeDisabled">
            修改
          </el-button>
        </div>
      </template>

      <el-form>
        <el-form-item>
          <label for="password">密码</label>
          <el-input type="password" id="password" v-model="newPassword" placeholder="•••••••••" required />
        </el-form-item>
        <el-form-item>
          <label v-if="!hasConfirmPasswordInput" for="confirm_password">确认密码</label>
          <label v-else-if="!isPasswordIdentical" for="confirm_password" class="error-warn">密码不一致</label>
          <label v-else for="confirm_password">确认密码</label>

          <el-input type="password" id="confirm_password" v-model="confirmPassword"
                    :class="{ 'error-warn-input': (hasConfirmPasswordInput && !isPasswordIdentical) }"
                    placeholder="•••••••••" required />
        </el-form-item>
      </el-form>

    </el-card>
  </el-main>
</template>


<style scoped>
.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.main-container {
  display: flex;
  flex-direction: row;
  padding: 15px;
  gap: 5px;
  background-image: url('/src/resources/background/background2.png');
  background-size: cover;
  background-position: center;
  justify-content: center; /* 调整为两侧对齐 */
}

.card-header {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.change-card {
  width: 66%;
}

.avatar-area {
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 30px;
}

.avatar-text {
  font-size: x-large;
  font-weight: bolder;
  padding-right: 40px;
}
</style>
