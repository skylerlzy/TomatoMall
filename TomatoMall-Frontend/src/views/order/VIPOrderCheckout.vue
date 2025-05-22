<script setup lang="ts">
import { ref, computed } from 'vue';
import {createVIPOrder, orderCheckOut} from '../../api/order.ts';
import { ElMessage } from 'element-plus';
import { useRouter, useRoute } from "vue-router";

// 定义接口
interface VipOrderInfo {
  orderId: number;
  username: string;
  totalAmount: number;
  paymentMethod: string;
  status: string;
  createTime: string;
  vipType: string;
  period: string;
}

interface VipPlanInfo {
  type: string;
  name: string;
  price: number;
  period: string;
}

const router = useRouter();
const route = useRoute();

// 使用 computed 处理路由参数
const vipPlan = computed<VipPlanInfo>(() => ({
  type: route.query.type as string || '',
  name: route.query.name as string || '',
  price: Number(route.query.price) || 0,
  period: route.query.period as string || ''
}));

const orderId = ref<number>(0);
const paymentMethod = ref('');
const orderDetails = ref<VipOrderInfo | null>(null);
const dialogVisible = ref(true);

// 提交订单
// 修改 handleCheckout 函数
async function handleCheckout() {
  if (!paymentMethod.value) {
    ElMessage({
      message: '请选择支付方式',
      type: 'warning',
      center: true
    });
    return;
  }

  try {
    // 根据会员类型设置 isVIP 参数
    const isVIP = vipPlan.value.type === 'gold' ? 2 : vipPlan.value.type === 'silver' ? 1 : 0

    const res = await createVIPOrder(isVIP, paymentMethod.value)
    if (res.data.code === '200') {
      ElMessage({
        message: '订单创建成功，请进行支付',
        type: 'success',
        center: true
      });

      const orderData = res.data.data;
      orderId.value = orderData.orderId;
      orderDetails.value = {
        orderId: orderData.id,
        username: orderData.userName,
        totalAmount: vipPlan.value.price,
        paymentMethod: orderData.paymentMethod,
        status: orderData.status,
        createTime: orderData.createTime,
        vipType: vipPlan.value.name,
        period: vipPlan.value.period
      };
      dialogVisible.value = false;
    } else {
      ElMessage({
        message: res.data.msg || '订单创建失败',
        type: 'error',
        center: true
      });
    }
  } catch (error) {
    console.error('订单创建失败', error);
    ElMessage({
      message: '订单创建失败，请重试！',
      type: 'error',
      center: true
    });
  }
}
function goToPayment(orderId: number) {
  router.push(`/payForm/${orderId}`);
}
</script>

<template>
  <el-main>
    <div class="order-container" v-if="dialogVisible">
      <div class="order-info">
        <h2>VIP 会员订购</h2>

        <div class="vip-info">
          <h3>{{ vipPlan.name }}</h3>
          <div class="vip-details">
            <p class="vip-price">¥{{ vipPlan.price }}<span class="period">/{{ vipPlan.period }}</span></p>
          </div>
        </div>

        <!-- 支付方式选择框 -->
        <el-select v-model="paymentMethod" placeholder="选择支付方式" class="payment-select">
          <el-option label="信用卡" value="creditCard">
            <template #default>
              <img src="../../resources/payWay/creditCard.png" alt="信用卡" class="payment-icon" />
              信用卡
            </template>
          </el-option>
          <el-option label="支付宝" value="alipay">
            <template #default>
              <img src="../../resources/payWay/alipay.png" alt="支付宝" class="payment-icon" />
              支付宝
            </template>
          </el-option>
          <el-option label="微信支付" value="wechat">
            <template #default>
              <img src="../../resources/payWay/wechat.png" alt="微信支付" class="payment-icon" />
              微信支付
            </template>
          </el-option>
        </el-select>

        <!-- 总价格 -->
        <div class="total-price">
          <span>总价格: ¥{{ vipPlan.price }}</span>
        </div>

        <!-- 结算按钮 -->
        <el-button type="primary" @click="handleCheckout" class="checkout-btn">确认订购</el-button>
      </div>
    </div>

    <div class="order-container" v-if="!dialogVisible">
      <div class="order-info">
        <h2>确认订购信息</h2>

        <div class="order-details" v-if="orderDetails">
          <div class="order-time">订单创建时间: {{ orderDetails.createTime }}</div>
          <div class="vip-type">会员类型: {{ orderDetails.vipType }}</div>
          <div class="payment-method">支付方式: {{ orderDetails.paymentMethod }}</div>
          <div class="total-amount">支付金额: ¥{{ orderDetails.totalAmount }}</div>
        </div>

        <el-button type="primary" @click="goToPayment(orderId)" class="checkout-btn">立即支付</el-button>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
.order-container {
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.order-info {
  max-width: 600px;
  margin: 0 auto;
  background-color: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.order-info h2 {
  text-align: center;
  font-size: 28px;
  color: #333;
  margin-bottom: 30px;
}

.vip-info {
  text-align: center;
  padding: 20px;
  background-color: #fafafa;
  border-radius: 8px;
  margin-bottom: 24px;
}

.vip-info h3 {
  font-size: 24px;
  color: #333;
  margin-bottom: 16px;
}

.vip-price {
  font-size: 36px;
  color: #ff6402;
  font-weight: bold;
}

.period {
  font-size: 16px;
  color: #666;
}

.payment-select {
  width: 100%;
  margin: 24px 0;
}

.payment-icon {
  width: 20px;
  height: 20px;
  margin-right: 8px;
  vertical-align: middle;
}

.order-details {
  background-color: #fafafa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 24px;
}

.order-details > div {
  margin: 8px 0;
  color: #666;
}

.total-price {
  font-size: 20px;
  font-weight: 600;
  color: #ff6402;
  text-align: right;
  margin: 24px 0;
}

.checkout-btn {
  width: 100%;
  height: 48px;
  font-size: 18px;
  background-color: #ff6402;
  border: none;
}

.checkout-btn:hover {
  background-color: #e24f00;
}
</style>