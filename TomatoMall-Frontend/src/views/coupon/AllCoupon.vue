<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { getCouponList, deleteCoupon, CouponInfo } from '../../api/coupon'
import { ElMessage, ElMessageBox } from 'element-plus'
import { router } from "../../router";

const coupons = ref<CouponInfo[]>([])
const role = sessionStorage.getItem('role')
const isAdmin = computed(() => role === 'admin')

const loadCoupons = async () => {
  try {
    const res = await getCouponList()
    if (res.data.code === '200') {
      coupons.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取优惠券列表失败')
  }
}

const toCreateCouponPage = () => {
  if (!isAdmin.value) {
    ElMessage.warning('只有管理员可以添加优惠券')
    return
  }
  router.push('/createCoupon')
}

const handleDelete = (id: number) => {
  if (!isAdmin.value) {
    ElMessage.warning('只有管理员可以删除优惠券')
    return
  }

  ElMessageBox.confirm('确定要删除这张优惠券吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteCoupon(id)
      if (res.data.code === '200') {
        ElMessage.success('删除成功')
        loadCoupons()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  loadCoupons()
})
</script>

<template>
  <div class="coupon-container">
    <div class="page-header">
      <div class="header-content">
        <h1>优惠券列表</h1>
        <el-button
            v-if="isAdmin"
            type="primary"
            class="create-coupon-button"
            @click="toCreateCouponPage">
          添加优惠券
        </el-button>
      </div>
    </div>
    <div class="coupon-list">
      <div v-for="coupon in coupons"
           :key="coupon.id"
           class="coupon-item">
        <div class="coupon-content">
          <div class="coupon-value">
            <span class="symbol">¥</span>
            <span class="amount">{{ coupon.volume }}</span>
          </div>
          <div class="coupon-info">
            <div class="condition">满{{ coupon.need }}可用</div>
            <div class="validity">有效期至：{{ coupon.deadline }}</div>
            <div class="type">
              {{ coupon.isCommon ? '通用券' : '指定商品可用' }}
            </div>
            <div class="count">剩余：{{ coupon.number }}张</div>
          </div>
        </div>
        <div class="coupon-actions">
          <el-button
              v-if="isAdmin"
              type="danger"
              size="small"
              @click="handleDelete(coupon.id!)">
            删除
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.create-coupon-button {
  height: 40px;
  padding: 0 25px;
  background: #ff6402;
  border: none;
  color: white;
  font-size: 14px;
  border-radius: 4px;
  transition: all 0.3s;
}

.create-coupon-button:hover {
  background: #e65a00;
  transform: scale(1.02);
}
.coupon-container {
  width: 100%;
  min-height: calc(100vh - 64px); /* 减去头部高度 */
  padding: 24px;
  box-sizing: border-box;
  overflow-x: hidden;
}

.page-header {
  max-width: 1200px;
  margin: 0 auto 30px;
  padding: 0 16px;
}

.page-header h1 {
  font-size: 24px;
  color: #333;
  font-weight: 600;
  margin: 0;
}

.coupon-list {
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 24px;
  background: #f5f7fa;
  border-radius: 12px;
  box-sizing: border-box;
}

.coupon-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  background: white;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e4e7ed;
}

.coupon-content {
  display: flex;
  align-items: center;
  gap: 20px;
  flex: 1;
}

.coupon-value {
  color: #ff6402;
  min-width: 100px;
  text-align: center;
}

.symbol {
  font-size: 16px;
}

.amount {
  font-size: 32px;
  font-weight: bold;
}

.coupon-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
  flex: 1;
}

.condition {
  font-size: 16px;
  font-weight: 500;
}

.validity, .type, .count {
  font-size: 14px;
  color: #666;
}

.coupon-actions {
  padding-left: 20px;
  flex-shrink: 0;
}

@media screen and (max-width: 768px) {
  .coupon-list {
    padding: 16px;
  }

  .coupon-item {
    padding: 12px;
  }

  .coupon-value {
    min-width: 80px;
  }

  .amount {
    font-size: 24px;
  }
}
</style>