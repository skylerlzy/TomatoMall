<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { checkoutWallet } from '../../api/order.ts'
import { router } from '../../router'

// 钱包数据
const balance = ref(0) // 用户余额
const rechargeAmount = ref(100) // 默认充值金额
const paymentMethod = ref('Alipay') // 默认支付方式
const transactions = ref([]) // 交易历史记录
const isLoading = ref(false)

// 支付方式选项
const paymentOptions = [
  { label: '支付宝', value: 'Alipay' },
  { label: '微信支付', value: 'WechatPay' },
  { label: '银联', value: 'UnionPay' }
]

// 预设充值金额选项
const presetAmounts = [
  { value: 50, label: '¥50' },
  { value: 100, label: '¥100' },
  { value: 200, label: '¥200' },
  { value: 500, label: '¥500' },
  { value: 1000, label: '¥1000' }
]

// 自定义充值金额是否可用
const customAmountActive = computed(() => {
  return !presetAmounts.some(item => item.value === rechargeAmount.value)
})

// 获取钱包信息
const fetchWalletInfo = async () => {
  // 模拟从后端获取钱包数据
  // 实际项目中应该调用后端API
  setTimeout(() => {
    balance.value = 520.50 // 模拟余额
    transactions.value = [
      { id: 1, type: '充值', amount: 200, date: '2023-11-15 14:30:22', status: '成功' },
      { id: 2, type: '消费', amount: -99, date: '2023-11-12 09:15:36', status: '成功' },
      { id: 3, type: '充值', amount: 500, date: '2023-11-01 18:22:10', status: '成功' },
      { id: 4, type: '消费', amount: -120.5, date: '2023-10-28 11:05:43', status: '成功' }
    ]
  }, 500)
}

// 选择预设金额
const selectPresetAmount = (amount: number) => {
  rechargeAmount.value = amount
}

// 处理充值
const handleRecharge = async () => {
  if (rechargeAmount.value <= 0) {
    ElMessage.warning('充值金额必须大于0')
    return
  }

  isLoading.value = true

  try {
    // 调用充值API创建订单
    const res = await checkoutWallet(rechargeAmount.value, paymentMethod.value)

    // 处理响应
    if (res.data.code === '200') {
      const orderId = res.data.data.orderId;
      ElMessage.success('充值请求已提交，即将跳转到支付页面')

      // 延迟跳转到支付页面
      setTimeout(() => {
        router.push(`/pay/${orderId}`)
      }, 1000)
    } else {
      ElMessage.error(res.data.msg || '充值请求失败')
    }
  } catch (error) {
    ElMessage.error('充值请求失败，请稍后再试')
    console.error('充值失败', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchWalletInfo()
})
</script>

<template>
  <div class="wallet-center">
    <div class="wallet-header">
      <div class="balance-card">
        <div class="card-header">
          <h2>我的余额</h2>
          <el-button type="primary" size="small" round @click="fetchWalletInfo">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
        <div class="balance-amount">
          <span class="currency">¥</span>
          <span class="amount">{{ balance.toFixed(2) }}</span>
        </div>
        <!-- 添加钱包充值按钮 -->
        <el-button type="primary" class="recharge-btn" @click="handleRecharge">
          充值钱包
        </el-button>
      </div>
    </div>

    <div class="wallet-content">
      <!-- 充值区域 -->
      <div class="recharge-section">
        <h3>余额充值</h3>
        <div class="preset-amounts">
          <el-radio-group v-model="rechargeAmount">
            <el-radio-button v-for="item in presetAmounts" :key="item.value" :label="item.value">
              {{ item.label }}
            </el-radio-button>
          </el-radio-group>
        </div>

        <div class="custom-amount">
          <el-input-number
              v-model="rechargeAmount"
              :min="1"
              :precision="2"
              :step="10"
              placeholder="其他金额"
              class="custom-amount-input"
          />
        </div>

        <div class="payment-method">
          <h4>支付方式</h4>
          <el-radio-group v-model="paymentMethod">
            <el-radio v-for="item in paymentOptions" :key="item.value" :label="item.value">
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </div>

        <div class="recharge-action">
          <el-button
              type="primary"
              size="large"
              @click="handleRecharge"
              :loading="isLoading"
              :disabled="rechargeAmount <= 0">
            立即充值
          </el-button>
        </div>
      </div>

      <!-- 交易记录 -->
      <div class="transactions-section">
        <h3>交易记录</h3>
        <el-table :data="transactions" style="width: 100%">
          <el-table-column prop="type" label="类型" width="100" />
          <el-table-column prop="amount" label="金额" width="120">
            <template #default="scope">
              <span :class="scope.row.amount > 0 ? 'income' : 'expense'">
                {{ scope.row.amount > 0 ? '+' : '' }}{{ scope.row.amount }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="date" label="交易时间" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '成功' ? 'success' : 'danger'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wallet-center {
  padding: 30px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.wallet-header {
  margin-bottom: 30px;
}

.balance-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 30px;
  position: relative;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.balance-amount {
  font-size: 48px;
  color: #ff4d4f;
  font-weight: 700;
  margin-bottom: 20px;
}

.currency {
  font-size: 28px;
  margin-right: 5px;
}

.recharge-btn {
  background-color: #ff6402;
  border-color: #ff6402;
  width: 120px;
  height: 40px;
  font-size: 16px;
}

.recharge-btn:hover {
  background-color: #e65a00;
  border-color: #e65a00;
}

.wallet-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.recharge-section, .transactions-section {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 30px;
}

h3 {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 20px;
  color: #333;
}

h4 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 16px;
  color: #333;
}

.preset-amounts {
  margin-bottom: 20px;
}

.custom-amount {
  margin-bottom: 20px;
}

.custom-amount-input {
  width: 100%;
}

.payment-method {
  margin-bottom: 30px;
}

.recharge-action {
  text-align: center;
}

.recharge-action .el-button {
  width: 60%;
  background-color: #ff6402;
  border-color: #ff6402;
}

.recharge-action .el-button:hover {
  background-color: #e65a00;
  border-color: #e65a00;
}

.income {
  color: #52c41a;
  font-weight: 500;
}

.expense {
  color: #f5222d;
  font-weight: 500;
}

/* 响应式布局 */
@media (max-width: 992px) {
  .wallet-content {
    grid-template-columns: 1fr;
  }
}

/* 让表格行可点击的样式 */
:deep(.el-table tr) {
  cursor: pointer;
}

:deep(.el-table tr:hover > td) {
  background-color: #f9f0f0 !important;
}

/* 自定义表格样式 */
:deep(.el-table th) {
  font-weight: 600;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table--enable-row-hover .el-table__body tr:hover > td.el-table__cell) {
  background-color: #fff2f2;
}

.rate-value {
  font-weight: 600;
  color: #ff9900;
  font-size: 15px;
}

.rate-display {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>