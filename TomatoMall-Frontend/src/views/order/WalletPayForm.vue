<script setup lang="ts">
import { ref, onMounted, nextTick, onBeforeUnmount } from 'vue';
import { router } from '../../router';
import { ElMessage } from 'element-plus';
import { checkoutWallet, getOrderStatus } from "../../api/order";

const orderId = Number(router.currentRoute.value.params.orderId);
const paymentForm = ref<string>('');
const isPaymentFormVisible = ref(false);
const paymentDetails = ref({
  orderId: 0,
  totalAmount: 0,
  paymentMethod: '钱包支付'
});

let pollingTimer: number | null = null;
const pollingInterval = 3000;

const clearPollingTimer = () => {
  if (pollingTimer) {
    window.clearInterval(pollingTimer);
    pollingTimer = null;
  }
};

const fetchPaymentForm = async () => {
  try {
    const res = await checkoutWallet(paymentDetails.value.totalAmount, paymentDetails.value.paymentMethod);
    if (res.data.code === '200') {
      const { paymentForm: form, totalAmount, paymentMethod, orderId } = res.data.data;
      paymentForm.value = form;
      paymentDetails.value = { orderId, totalAmount, paymentMethod }; // 确保数据正确赋值
      isPaymentFormVisible.value = true;

      await nextTick();
      renderPaymentForm();
      startPollingOrderStatus();
    } else {
      ElMessage.error('获取支付表单失败');
    }
  } catch (error) {
    console.error('支付请求失败', error);
    ElMessage.error('支付请求失败，请稍后再试');
  }
};

const renderPaymentForm = () => {
  // 创建一个容器显示支付表单
  const container = document.createElement('div');
  container.innerHTML = paymentForm.value;
  document.body.appendChild(container);

  // 自动提交表单到新窗口
  const form = container.querySelector('form');
  if (form) {
    form.target = '_blank';
    form.submit();
  }
};

const startPollingOrderStatus = () => {
  clearPollingTimer();

  pollingTimer = window.setInterval(async () => {
    try {
      const res = await getOrderStatus(orderId);
      if (res.data.code === '200') {
        const status = res.data.data.status;

        if (status === 'PAID') {
          clearPollingTimer();
          ElMessage.success('支付成功！');
          router.push('/walletCenter');
        } else if (status === 'FAILED') {
          clearPollingTimer();
          ElMessage.error('支付失败，请重试');
        }
      }
    } catch (error) {
      console.error('查询订单状态失败', error);
    }
  }, pollingInterval);
};

onMounted(() => {
  fetchPaymentForm();
});

onBeforeUnmount(() => {
  clearPollingTimer();
});
</script>

<template>
  <div class="wallet-payment-container">
    <div v-if="isPaymentFormVisible" class="payment-info">
      <h2>钱包支付</h2>
      <p>订单号：{{ paymentDetails.orderId }}</p>
      <p>支付金额：￥{{ paymentDetails.totalAmount.toFixed(2) }}</p> <!-- 确保金额显示正确 -->
      <p>支付方式：{{ paymentDetails.paymentMethod }}</p> <!-- 确保支付方式显示正确 -->
    </div>


    <div class="payment-tips">
      <p>支付完成后请不要关闭页面，系统会自动处理</p>
    </div>
  </div>
</template>

<style scoped>
.wallet-payment-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.payment-info {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 4px;
}

.payment-tips {
  text-align: center;
  color: #666;
  margin-top: 20px;
}
</style>