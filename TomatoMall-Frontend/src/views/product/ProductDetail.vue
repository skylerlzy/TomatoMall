<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { searchProduct, deleteProduct } from '../../api/product.ts'
import { Back,Star } from '@element-plus/icons-vue'
import { router } from '../../router'
import { ElMessage } from "element-plus"
import { addToCart } from '../../api/cart.ts'
import { getCommentList, addComment, deleteComment, updateComment, CommentInfo } from '../../api/comment.ts'
import {getUserList,addCollect, deleteCollect, } from '../../api/user.ts'

// 基础数据
const role = sessionStorage.getItem('role')
const userId = Number(sessionStorage.getItem('userId'))
const id = Number(router.currentRoute.value.params.productId)

// 商品相关

const title = ref('')
const price = ref()
const rate = ref()
const description = ref('')
const cover = ref('')
const detail = ref('')
const quantity = ref(1)
const tag = ref('')
const author = ref('')
// 评论相关
const comments = ref<CommentInfo[]>([])
const commentContent = ref('')
const replyToComment = ref<CommentInfo | null>(null)
const expandedComments = ref(new Set<number>())
const editingCommentId = ref<number | null>(null)
const editContent = ref('')


const isCollected = ref(false)

// 检查商品是否已收藏
const checkIfCollected = () => {
  const collectStr = sessionStorage.getItem('collect') || ''
  if (collectStr) {
    const collectList = collectStr.split(',').map(Number)
    isCollected.value = collectList.includes(id)
  }
}

// 收藏/取消收藏商品
const toggleCollect = async () => {
  try {
    if (isCollected.value) {
      const res = await deleteCollect(id)
      if (res.data.code === '200') {
        isCollected.value = false
        ElMessage.success('已取消收藏')

        // 更新本地存储
        let collectList = (sessionStorage.getItem('collect') || '').split(',').filter(Boolean).map(Number)
        collectList = collectList.filter(item => item !== id)
        sessionStorage.setItem('collect', collectList.join(','))
      }
    } else {
      const res = await addCollect(id)
      if (res.data.code === '200') {
        isCollected.value = true
        ElMessage.success('收藏成功')

        // 更新本地存储
        let collectList = (sessionStorage.getItem('collect') || '').split(',').filter(Boolean).map(Number)
        collectList.push(id)
        sessionStorage.setItem('collect', collectList.join(','))
      }
    }
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  }
}

// 用户信息
interface UserInfo {
  id: number
  username: string
  name: string
  avatar: string
}
const userMap = ref<Map<number, UserInfo>>(new Map())

// 获取用户列表并构建映射
const fetchUserList = async () => {
  try {
    const res = await getUserList()
    if (res.data.code === '200') {
      const users = res.data.data
      users.forEach((user: UserInfo) => {
        userMap.value.set(user.id, user)
      })
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 获取用户显示名称
const getUserDisplayName = (userId: number) => {
  const user = userMap.value.get(userId)
  return user?.username || '匿名用户'
}

// 获取用户头像
const getUserAvatar = (userId: number) => {
  const user = userMap.value.get(userId)
  return user?.avatar || ''
}

// 商品信息获取
const fetchProductInfo = async () => {
  try {
    const res = await searchProduct(String(id))
    if (res.data.code === '200') {
      const data = res.data.data
      title.value = data.title
      price.value = data.price
      rate.value = data.rate
      description.value = data.description
      cover.value = data.cover
      detail.value = data.detail
      tag.value = data.tag || '未分类' // 添加标签字段
      author.value = data.author || '未知作者' // 添加作者字段
    }
  } catch (error) {
    ElMessage.error('获取商品信息失败')
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    const res = await getCommentList()
    if (res.data.code === '200') {
      comments.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取评论失败')
  }
}

// 主评论列表
const mainComments = computed(() => {
  return comments.value.filter(c => !c.fatherId && c.productId === id)
})

// 获取回复评论
const getReplyComments = (parentId: number) => {
  return comments.value.filter(c => c.fatherId === parentId && c.productId === id)
}

const colors = ref(['#99A9BF', '#F7BA2A', '#FF9900'])

// 评论展开状态切换
const toggleComment = (commentId: number) => {
  if (expandedComments.value.has(commentId)) {
    expandedComments.value.delete(commentId)
  } else {
    expandedComments.value.add(commentId)
  }
}

// 进入编辑模式
const handleEdit = (comment: CommentInfo) => {
  editingCommentId.value = comment.id!
  editContent.value = comment.content
}

// 取消编辑
const cancelEdit = () => {
  editingCommentId.value = null
  editContent.value = ''
}

const submitEdit = async (comment: CommentInfo) => {
  if (!editContent.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }

  const updatedComment: CommentInfo = {
    productId: comment.productId,
    userId: comment.userId,
    content: editContent.value,
    rate: comment.rate,
    son: comment.son,
    fatherId: comment.fatherId
  }

  try {
    const res = await updateComment(comment.id!, updatedComment)
    if (res.data.code === '200') {
      ElMessage.success('评论修改成功')
      editingCommentId.value = null
      editContent.value = ''
      await fetchComments()
    }
  } catch (error) {
    ElMessage.error('评论修改失败')
  }
}
// 发表评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  const newComment: CommentInfo = {
    productId: id,
    userId: userId,
    content: commentContent.value,
    rate: 5,
    son: [],
    fatherId: replyToComment.value?.id || null,
    createTime: new Date().toISOString()
  }

  try {
    const res = await addComment(newComment)
    if (res.data.code === '200') {
      ElMessage.success('评论发布成功')
      commentContent.value = ''
      replyToComment.value = null
      await fetchComments()
    }
  } catch (error) {
    ElMessage.error('评论发布失败')
  }
}

// 删除评论
const handleDeleteComment = async (commentId: number) => {
  try {
    const res = await deleteComment(commentId)
    if (res.data.code === '200') {
      ElMessage.success('评论删除成功')
      await fetchComments()
    }
  } catch (error) {
    ElMessage.error('删除评论失败')
  }
}

// 回复评论
const handleReply = (comment: CommentInfo) => {
  if (!comment.fatherId) {
    replyToComment.value = comment
    commentContent.value = ''
  }
}



// 取消回复
const cancelReply = () => {
  replyToComment.value = null
  commentContent.value = ''
}

// 购物车相关
function handleInputNumberChange(value: number) {
  quantity.value = value
}

function handleAddToCart() {
  const cartItem = {
    productId: id,
    title: title.value,
    price: price.value,
    cover: cover.value,
    detail: detail.value,
    description: description.value,
    quantity: quantity.value,
  }

  addToCart(cartItem).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '添加商品成功！',
        type: 'success',
        center: true,
      })
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  }).catch(err => {
    console.error('商品添加至购物车失败', err)
    ElMessage({
      message: '商品添加至购物车失败，请重试！',
      type: 'error',
      center: true,
    })
  })
}

// 商品管理相关
function toChangeProduct(id: number) {
  router.push('/changeProductInfo/' + id)
}

function toChangeStock(id: number) {
  router.push('/stockManage/' + id)
}

function deleteInfo(id: number) {
  deleteProduct(id).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '删除商品成功！',
        type: 'success',
        center: true,
      })
      router.push('/AllProduct')
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  }).catch(err => {
    console.error('商品删除失败', err)
    ElMessage({
      message: '商品删除失败，请重试！',
      type: 'error',
      center: true,
    })
  })
}

// 页面导航
function toBackPage() {
  router.push('/AllProduct')
}

function manageComments() {
  router.push('/manageComments' + id)
}

// 格式化日期
const formatDate = (date: string | undefined) => {
  if (!date) return ''
  const d = new Date(date)
  const now = new Date()
  const diff = now.getTime() - d.getTime()

  if (diff < 1000 * 60) {
    return '刚刚'
  } else if (diff < 1000 * 60 * 60) {
    return `${Math.floor(diff / (1000 * 60))}分钟前`
  } else if (diff < 1000 * 60 * 60 * 24) {
    return `${Math.floor(diff / (1000 * 60 * 60))}小时前`
  } else {
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
  }
}

onMounted(async () => {
  checkIfCollected()
  await fetchUserList()
  await Promise.all([
    fetchProductInfo(),
    fetchComments()
  ])
})
</script>

<template>
  <div class="page-container">
    <div class="main-content">
      <!-- 返回按钮 -->
      <div class="back-wrap">
        <el-button link @click="toBackPage()" class="back-button">
          <el-icon><Back /></el-icon>
          <span>返回</span>
        </el-button>
      </div>

      <!-- 商品核心信息区 -->
      <div class="product-essential">
        <!-- 左侧商品图片 -->
        <div class="preview-wrap">
          <el-image class="product-image" :src="cover" fit="contain" />
        </div>

        <!-- 右侧商品信息 -->
        <div class="product-info">
          <h1 class="product-title">{{ title }}</h1>
          <div class="collect-icon-wrapper" @click="toggleCollect">
            <el-icon :class="['collect-icon', isCollected ? 'collected' : '']">
              <Star />
            </el-icon>
          </div>
          <div class="product-summary">
            <div class="description-box">{{ description }}</div>
          </div>
            <!-- 添加收藏按钮到右上角 -->
          <div class="price-wrap">
            <div class="price-label">价格</div>
            <div class="price-box">
              <span class="price-symbol">¥</span>
              <span class="price-amount">{{ price }}</span>
            </div>
          </div>

          <div class="author-tag-wrap">
            <div class="info-item">
              <span class="info-label">作者：</span>
              <span class="info-value">{{ author }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">分类：</span>
              <span class="info-value tag-value">{{ tag }}</span>
            </div>
          </div>

          <div class="demo-rate-block">
            <span class="demonstration"></span>
            <el-rate v-model="rate" :colors="colors" />
          </div>

          <div class="action-buttons">
            <div class="quantity-control">
              <el-input-number
                  v-model="quantity"
                  :min="0"
                  style="width: 120px"
                  @change="handleInputNumberChange"
              />
            </div>
            <el-button
                type="primary"
                @click="handleAddToCart"
                class="add-cart-btn"
                :disabled="quantity === 0"
            >加入购物车</el-button>
          </div>

          <!-- 管理员控制面板 -->
          <div v-if="role === 'admin'" class="admin-panel">
            <el-button type="primary" @click="toChangeProduct(id)">编辑商品</el-button>
            <el-button type="warning" @click="toChangeStock(id)">库存管理</el-button>
            <el-button type="success" @click="manageComments">评论管理</el-button>
            <el-button type="danger" @click="deleteInfo(id)">删除商品</el-button>
          </div>
        </div>
      </div>

      <!-- 商品详情区 -->
      <div class="product-detail">
        <div class="detail-tabs">
          <div class="tab-header">
            <h3 class="active">商品详情</h3>
          </div>
          <div class="detail-content">
            <div class="detail-text">{{ detail }}</div>
          </div>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="comment-section">
        <div class="section-header">
          <h3>商品评价</h3>
        </div>

        <!-- 评论输入框 -->
        <div class="comment-input">
          <el-input
              v-model="commentContent"
              type="textarea"
              :rows="3"
              :placeholder="replyToComment ? `回复 ${getUserDisplayName(replyToComment.userId)} 的评论` : '写下你的评论'"
          />
          <div class="comment-actions">
            <el-button
                v-if="replyToComment"
                text
                @click="cancelReply"
            >取消回复</el-button>
            <el-button
                type="primary"
                @click="submitComment"
            >发表评论</el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <template v-if="mainComments.length">
            <el-card
                v-for="comment in mainComments"
                :key="comment.id"
                class="comment-item"
                shadow="hover"
            >
              <div class="comment-main">
                <div class="comment-user-info">
                  <el-avatar
                      :size="40"
                      :src="getUserAvatar(comment.userId)"
                  />
                  <div class="user-meta">
                    <div class="username">
                      {{ getUserDisplayName(comment.userId) }}
                    </div>
                    <div class="comment-time">{{ formatDate(comment.createTime) }}</div>
                  </div>
                </div>

                <!-- 评论内容区域 -->
                <template v-if="editingCommentId === comment.id">
                  <div class="comment-edit">
                    <el-input
                        v-model="editContent"
                        type="textarea"
                        :rows="3"
                        :maxlength="500"
                        show-word-limit
                    />
                    <div class="edit-actions">
                      <el-button text @click="cancelEdit">取消</el-button>
                      <el-button type="primary" @click="submitEdit(comment)">保存</el-button>
                    </div>
                  </div>
                </template>
                <template v-else>
                  <div class="comment-content">{{ comment.content }}</div>
                </template>

                <!-- 评论操作区 -->
                <div class="comment-footer" v-if="editingCommentId !== comment.id">
                  <el-button
                      text
                      type="primary"
                      @click="handleReply(comment)"
                  >回复</el-button>
                  <template v-if="comment.userId === userId">
                    <el-button
                        text
                        type="primary"
                        @click="handleEdit(comment)"
                    >编辑</el-button>
                  </template>
                  <el-button
                      v-if="comment.userId === userId || role === 'admin'"
                      text
                      type="danger"
                      @click="handleDeleteComment(comment.id!)"
                  >删除</el-button>
                </div>

                <!-- 回复评论列表 -->
                <div v-if="getReplyComments(comment.id!).length > 0">
                  <el-collapse>
                    <el-collapse-item>
                      <template #title>
                        <span style="color: #409EFF">
                          {{ getReplyComments(comment.id!).length }}条回复
                        </span>
                      </template>
                      <div class="reply-list">
                        <div
                            v-for="reply in getReplyComments(comment.id!)"
                            :key="reply.id"
                            class="reply-item"
                        >
                          <div class="reply-user-info">
                            <el-avatar
                                :size="32"
                                :src="getUserAvatar(reply.userId)"
                            />
                            <div class="user-meta">
                              <div class="username">
                                {{ getUserDisplayName(reply.userId) }}
                              </div>
                              <div class="comment-time">{{ formatDate(reply.createTime) }}</div>
                            </div>
                          </div>

                          <!-- 回复内容区域 -->
                          <template v-if="editingCommentId === reply.id">
                            <div class="reply-edit">
                              <el-input
                                  v-model="editContent"
                                  type="textarea"
                                  :rows="2"
                                  :maxlength="500"
                                  show-word-limit
                              />
                              <div class="edit-actions">
                                <el-button text @click="cancelEdit">取消</el-button>
                                <el-button type="primary" @click="submitEdit(reply)">保存</el-button>
                              </div>
                            </div>
                          </template>
                          <template v-else>
                            <div class="reply-content">{{ reply.content }}</div>
                          </template>

                          <!-- 回复操作区 -->
                          <div class="reply-actions" v-if="editingCommentId !== reply.id">
                            <template v-if="reply.userId === userId">
                              <el-button
                                  text
                                  type="primary"
                                  @click="handleEdit(reply)"
                              >编辑</el-button>
                            </template>
                            <el-button
                                v-if="reply.userId === userId || role === 'admin'"
                                text
                                type="danger"
                                @click="handleDeleteComment(reply.id!)"
                            >删除</el-button>
                          </div>
                        </div>
                      </div>
                    </el-collapse-item>
                  </el-collapse>
                </div>
              </div>
            </el-card>
          </template>
          <el-empty v-else description="暂无评论" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 基础布局样式 */
.page-container {
  min-width: 990px;
  background: #f4f4f4;
  min-height: 100vh;
}

.main-content {
  width: 1190px;
  margin: 0 auto;
  padding: 20px 0;
}

/* 返回按钮样式 */
.back-wrap {
  margin-bottom: 15px;
  padding: 8px;
  background-color: #ff6402;
  display: inline-block;
  border-radius: 4px;
}

.back-button {
  color: #fff;
  font-size: 14px;
  padding: 0;
  height: auto;
}

.back-button:hover {
  color: #fff;
  opacity: 0.8;
}

/* 商品基本信息区域 */
.product-essential {
  display: flex;
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.preview-wrap {
  width: 400px;
  margin-right: 30px;
}

.product-image {
  width: 400px;
  height: 400px;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
}

/* 商品信息样式 */
.product-info {
  flex: 1;
  padding: 10px 20px;
}

.product-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
  line-height: 1.5;
}

.product-summary {
  background: #f7f7f7;
  padding: 15px;
  margin: 15px 0;
  color: #666;
  border-radius: 8px;
  line-height: 1.6;
}

/* 价格区域样式 */
.price-wrap {
  margin: 20px 0;
  padding: 15px 20px;
  background: #fff8f8;
  border-radius: 8px;
  border: 1px solid #ffe4e4;
}

.price-label {
  color: #999;
  margin-bottom: 8px;
  font-size: 14px;
}

.price-box {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 18px;
  color: #ff4d4f;
  margin-right: 4px;
}

.price-amount {
  font-size: 32px;
  color: #ff4d4f;
  font-weight: bold;
  line-height: 1;
}

/* 操作按钮区域 */
.action-buttons {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.add-cart-btn {
  width: 180px;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
  background: #ff4d4f;
  border-color: #ff4d4f;
  border-radius: 24px;
  transition: all 0.3s ease;
}

.add-cart-btn:hover {
  background: #ff7875;
  border-color: #ff7875;
}

/* 管理员面板 */
.admin-panel {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 12px;
}

/* 商品详情区域 */
.product-detail {
  background: #fff;
  margin-bottom: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.detail-tabs {
  padding: 20px;
}

.tab-header {
  height: 48px;
  line-height: 48px;
  background: #fafafa;
  padding: 0 24px;
  border-bottom: 2px solid #ff4d4f;
  border-radius: 8px 8px 0 0;
}

.detail-content {
  padding: 24px;
  line-height: 1.8;
  color: #333;
}

/* 评论区域样式 */
.comment-section {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.section-header {
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 16px;
  margin-bottom: 24px;
}

.section-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 评论输入框 */
.comment-input {
  margin-bottom: 24px;
  background: #fafafa;
  padding: 16px;
  border-radius: 8px;
}

.comment-actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 评论列表样式 */
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  border: none;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.comment-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.comment-main {
  padding: 20px;
}

/* 评论用户信息 */
.comment-user-info {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.user-meta {
  margin-left: 12px;
}

.username {
  font-size: 15px;
  font-weight: 500;
  color: #333;
  line-height: 1.4;
}

.comment-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

/* 评论内容 */
.comment-content {
  color: #333;
  line-height: 1.6;
  font-size: 14px;
  margin: 12px 0;
  padding-left: 52px;
}

/* 评论底部操作区 */
.comment-footer {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f5f5f5;
  padding-left: 52px;
}

/* 回复列表样式 */
.reply-list {
  margin: 16px 0 0 52px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.reply-item {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  background: #fff;
  margin-bottom: 8px;
  border-radius: 6px;
}

.reply-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.reply-user-info {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.reply-content {
  margin: 8px 0 8px 44px;
  color: #333;
  font-size: 14px;
  line-height: 1.6;
}

.reply-actions {
  margin-left: 44px;
  margin-top: 8px;
  display: flex;
  gap: 12px;
}

/* Element Plus 组件样式覆盖 */
:deep(.el-button--text) {
  padding: 0;
  height: auto;
  font-size: 13px;
}

:deep(.el-button--text:hover) {
  opacity: 0.8;
}

:deep(.el-avatar) {
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.el-card) {
  border: 1px solid #e8e8e8;
  transition: all 0.3s ease;
}

:deep(.el-card:hover) {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

:deep(.el-collapse-transition) {
  transition: all 0.3s ease-in-out;
}

.demo-rate-block {
  display: inline-block;
  width: 49%;
  box-sizing: border-box;
}
.demo-rate-block:last-child {
  border-right: none;
}
/* 收藏按钮样式 */
.collect-icon-wrapper {
  position: absolute;
  top: 15px;
  right: 15px;
  cursor: pointer;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  z-index: 10;
}

.collect-icon-wrapper:hover {
  transform: scale(1.1);
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.collect-icon {
  font-size: 20px;
  color: #999;
  transition: all 0.3s ease;
}

.collect-icon.collected {
  color: #ff4d4f;
  transform: scale(1.1);
}

/* 确保 product-info 是相对定位，这样收藏按钮可以相对它绝对定位 */
.product-info {
  position: relative;
  flex: 1;
  padding: 10px 20px;
}
</style>