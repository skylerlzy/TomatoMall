<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Check } from '@element-plus/icons-vue'
import {createVIPOrder} from "../../api/order.ts";

const router = useRouter()

interface VipPlan {
  id: number
  name: string
  price: number
  period: string
  benefits: string[]
  type: string
  isSubscribed?: boolean
}

const vipPlans = ref<VipPlan[]>([
  {
    id: 1,
    name: '铜牌会员',
    price: 0,
    period: '永久',
    benefits: [
      '基础商品浏览',
      '基础搜索功能',
      '标准客服支持'
    ],
    type: 'bronze',
    isSubscribed: true // 设置为已订阅
  },
  {
    id: 2,
    name: '银牌会员',
    price: 25,
    period: '每月',
    benefits: [
      '无广告浏览',
      '优先客服支持',
      '商品折扣5%',
      '专属活动参与'
    ],
    type: 'silver'
  },
  {
    id: 3,
    name: '金牌会员',
    price: 40,
    period: '每月',
    benefits: [
      '无广告浏览',
      '24小时专属客服',
      '商品折扣10%',
      '专属活动优先参与',
      'VIP专属商品'
    ],
    type: 'gold'
  }
])

const handleSubscribe = (plan: VipPlan) => {
  if (plan.isSubscribed) {
    return
  }

  router.push({
    path: '/vipOrderCheckout',
    query: {
      type: plan.type,
      name: plan.name,
      price: plan.price.toString(),
      period: plan.period
    }
  })
}
</script>
<template>
  <div class="vip-center">
    <h1>会员中心</h1>
    <div class="plans-container">
      <div v-for="plan in vipPlans"
           :key="plan.id"
           class="plan-card"
           :class="[plan.type, plan.isSubscribed ? 'subscribed' : '']">
        <div class="card-header">
          <h2>{{ plan.name }}</h2>
          <div class="price">
            <span class="amount">¥{{ plan.price }}</span>
            <span class="period">/{{ plan.period }}</span>
          </div>
        </div>
        <div class="card-body">
          <ul class="benefits">
            <li v-for="(benefit, index) in plan.benefits"
                :key="index">
              <el-icon><Check /></el-icon>
              {{ benefit }}
            </li>
          </ul>
        </div>
        <div class="card-footer">
          <el-button
              type="primary"
              class="subscribe-btn"
              :disabled="plan.isSubscribed"
              @click="handleSubscribe(plan)">
            {{ plan.isSubscribed ? '已开通' : '立即订阅' }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.vip-center {
  padding: 20px 60px;
  max-width: 1600px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 40px;
  font-size: 46px;
  font-weight: 600;
}

.plans-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 50px;
  padding: 20px 40px;
}

.plan-card {
  background: white;
  border-radius: 20px;
  padding: 50px;
  text-align: center;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: none;
  min-height: 600px;
  display: flex;
  flex-direction: column;
}

.plan-card:hover {
  transform: translateY(-15px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
}

.card-header {
  margin-bottom: 30px;
}

.plan-card h2 {
  color: #333;
  margin-bottom: 20px;
  font-size: 36px;
  font-weight: 600;
}

.price {
  margin: 25px 0;
}

.amount {
  font-size: 56px;
  font-weight: bold;
  color: #333;
}

.period {
  color: #666;
  font-size: 20px;
}

.card-body {
  flex: 1;
}

.benefits {
  list-style: none;
  padding: 0;
  margin: 0;
  text-align: left;
}

.benefits li {
  margin: 20px 0;
  color: #666;
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 20px;
}

.benefits .el-icon {
  color: #67c23a;
  font-size: 28px;
}

.card-footer {
  margin-top: auto;
  padding-top: 30px;
}

.subscribe-btn {
  width: 100%;
  height: 56px;
  border-radius: 28px;
  font-size: 20px;
}

/* 会员卡片主题色 */
.bronze .subscribe-btn {
  background-color: #cd7f32;
  border-color: #cd7f32;
}

.silver .subscribe-btn {
  background-color: #c0c0c0;
  border-color: #c0c0c0;
}

.gold .subscribe-btn {
  background-color: #ffd700;
  border-color: #ffd700;
  color: #333;
}

.bronze .subscribe-btn:hover {
  background-color: #b36a1d;
  border-color: #b36a1d;
}

.silver .subscribe-btn:hover {
  background-color: #a7a7a7;
  border-color: #a7a7a7;
}

.gold .subscribe-btn:hover {
  background-color: #e6c300;
  border-color: #e6c300;
  color: #333;
}

.plan-card.subscribed {
  border: 2px solid #67c23a;
}

.plan-card.subscribed .subscribe-btn {
  background-color: #67c23a !important;
  border-color: #67c23a !important;
  cursor: default;
  opacity: 0.8;
}

.plan-card.subscribed .subscribe-btn:hover {
  background-color: #67c23a !important;
  border-color: #67c23a !important;
  opacity: 0.8;
}
</style>