<script setup lang="ts">
import {ref, computed} from 'vue'
import {router} from '../../router'
import {userRegister} from "../../api/user.ts"
import {UploadFilled} from "@element-plus/icons-vue";
import {uploadImage} from '../../api/tool.ts'

// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）
const name = ref('')
const tel = ref('')
const location = ref('')
const password = ref('')
const confirmPassword = ref('')
const email = ref('')
const tags=ref('')
const avatar=ref('')
const username = ref('')
const avatarURL=ref('')

// 电话号码是否为空
const hasTelInput = computed(() => tel.value != '')
// 密码是否为空
const hasPasswordInput = computed(() => password.value != '')
// 重复密码是否为空
const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
// 地址是否为空
const hasLocationInput = computed(() => location.value != '')
// 电话号码的规则
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const telLegal = computed(() => chinaMobileRegex.test(tel.value))
// 重复密码的规则
const isPasswordIdentical = computed(() => password.value == confirmPassword.value)
// 注册按钮可用性
const registerDisabled = computed(() => {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput && hasLocationInput.value)

})

//图片上传
function handleUpload(file: any) {
  avatarURL.value = file
  let formData = new FormData()
  formData.append('file', file.raw)
  uploadImage(formData).then(res => {
    avatarURL.value = res.data.result
  })
}

//图片限制
function handleExceed(){
  ElMessage.warning('文件超出限制，请上传小于1M的文件')
}

//upload请求
function uploadHttpRequest() {
  return new XMLHttpRequest()
}

// 注册按钮触发
function handleRegister() {
  userRegister({
    username:username.value,
    name: name.value,
    telephone: tel.value,
    password: password.value,
    location: location.value,
    email:email.value,
    avatar:avatar.value,
    role:"user",
  }).then(res => {
    if (res.data.code === '200') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "注册成功！请登录账号",
        type: 'success',
        center: true,
      })
      router.push({path: "/login"})
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}
</script>


<template>
  <el-main class="main-frame bgimage">
    <el-col :span="12" />
    <el-card class="login-card" margin-right="10px" margin-left="20px">
      <div>
        <h1>创建一个新的账户</h1>

        <el-form>
          <el-row>
            <el-col :span="5">
              <el-form-item>
                <label for="name" class="name-label">昵称</label>
                <el-input id="name"
                          v-model="username"
                          placeholder="请输入昵称"/>

                <div class="avatar-upload">
                  <el-upload v-model:avatar="avatarURL" :limit="1" :on-change="handleUpload"
                             :on-exceed="handleExceed" :on-remove="handleUpload" class="upload-demo" list-type="picture"
                             :http-request="uploadHttpRequest" drag>
                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                    <el-button
                        style="margin-right: 10px"
                        size="small"
                        type="success"
                        @click="handleUpload(avatarURL)"
                    >
                      上传到服务器
                    </el-button>
                    <template #tip>
                      <div class="el-upload__tip">
                        只能上传jpg/png文件，且不超过1MB
                      </div>
                    </template>
                  </el-upload>
                </div>
              </el-form-item>

              <el-form-item>
                <label for="name" class="name-label">姓名</label>
                <el-input id="name"
                          v-model="name"
                          placeholder="请输入姓名"/>
              </el-form-item>
            </el-col>


          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item>

                <label v-if="!hasTelInput" for="tel">
                  注册手机号
                </label>
                <label v-else-if="!telLegal" for="tel" class="error-warn">
                  手机号不合法
                </label>
                <label v-else for="tel">
                  注册手机号
                </label>

                <el-input id="tel"
                          v-model="tel" :class="{'error-warn-input' :(hasTelInput && !telLegal)}"
                          placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>

            <el-col :span="2"></el-col>


            <el-form-item>
              <label for="location">
                地址
              </label>
              <el-input id="location"
                        v-model="location"
                        placeholder="请输入收货地址"/>
              </el-form-item>

          </el-row>

          <el-form-item>
            <label for="email">邮箱</label>
            <el-input type="email" id="email"
                      v-model="email"
                      placeholder="••••••••"/>
          </el-form-item>

          <el-form-item></el-form-item>

          <el-form-item>
            <label for="tag">偏好</label>
            <el-select id="tag" v-model="tags" placeholder="请选择" multiple>
              <el-option label="玄幻" value="玄幻"/>
              <el-option label="穿越" value="穿越"/>
              <el-option label="奇幻" value="奇幻"/>
              <el-option label="仙侠" value="仙侠"/>
              <el-option label="都市" value="都市"/>
              <el-option label="无限流" value="无限流"/>
              <el-option label="爽文" value="爽文"/>
              <el-option label="后宫" value="后宫"/>
            </el-select>
          </el-form-item>

          <el-form-item>
            <label for="password">密码</label>
            <el-input type="password" id="password"
                      v-model="password"
                      placeholder="••••••••"/>
          </el-form-item>

          <el-form-item>
            <label v-if="!hasConfirmPasswordInput">确认密码</label>
            <label v-else-if="!isPasswordIdentical" class="error-warn">密码不一致</label>
            <label v-else>确认密码</label>

            <el-input type="password" id="confirm-password"
                      v-model="confirmPassword"
                      :class="{'error-warn-input' :(hasConfirmPasswordInput && !isPasswordIdentical)}"
                      placeholder="••••••••"/>
          </el-form-item>

          <span class="button-group">
            <el-button @click.prevent="handleRegister"
                       :disabled="registerDisabled"
                       type="primary"
            class="register-button">
              创建账户
            </el-button>

            <router-link to="/login" v-slot="{navigate}">
              <el-button @click="navigate" class="login-button">
                去登录
              </el-button>
            </router-link>
          </span>

        </el-form>
      </div>

    </el-card>
  </el-main>

</template>


<style scoped>
.main-frame {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url('/src/resources/background/background3.png');
  background-size: cover;
  background-position: center;
}

.circle-image {
  width: 100px; /* 你可以根据需要调整大小 */
  height: 100px;
  border-radius: 50%;
  object-fit: cover; /* 确保图片完整显示并缩放 */
}

.avatar-upload {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 20px;
}

.login-card {
  width: 45%;
  padding: 20px;
  display: flex;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.button-group {
  color: #ff6402;
  bacground-color: #ff6402;
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}

.register-button {
  color: #ffffff;
  background-color: #ff6402;
}

.login-button {
  color: #ff6402;
  background-color: #ffffff;
}

</style>
