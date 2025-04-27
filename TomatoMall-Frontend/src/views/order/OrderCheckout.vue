<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getCartList } from '../../api/cart.ts';  // 获取购物车商品列表
import { orderCheckOut } from '../../api/order.ts';
import { ElMessage } from 'element-plus';
import { router } from "../../router";

type CartItemInfo = {
  cartItemId: number;
  productId: number;
  title: string;
  price: number;
  quantity: number;
};

type OrderInfo = {
  orderId: number;
  username: string;
  totalAmount: number;
  paymentMethod: string;
  status: string;
  createTime: string;
};

// 使用 ref 来定义 orderId，使其响应式
const orderId = ref<number>(0);  // 初始化为 0
const cartItemList = ref<CartItemInfo[]>([]);
const shippingAddress = ref('');
const paymentMethod = ref('');
const totalPrice = ref(0);
const orderDetails = ref<OrderInfo | null>(null); // 用来保存订单详情
const dialogVisible = ref(true); // 控制弹窗显示，初始值设置为false

onMounted(() => {
  fetchCartItems();
});

// 获取购物车商品信息
function fetchCartItems() {
  getCartList().then(res => {
    cartItemList.value = res.data.data.items;
    calculateTotalPrice();
  });
}

// 计算总价格
function calculateTotalPrice() {
  totalPrice.value = cartItemList.value.reduce((sum, item) => sum + item.price * item.quantity, 0);
}

// 提交订单
function handleCheckout() {
  if (!shippingAddress.value) {
    ElMessage({
      message: '请输入收货地址',
      type: 'warning',
      center: true
    });
    return;
  }

  if (!paymentMethod.value) {
    ElMessage({
      message: '请选择支付方式',
      type: 'warning',
      center: true
    });
    return;
  }

  const orderInfo = {
    cartItemIds: cartItemList.value.map(item => item.cartItemId.toString()),
    shipping_address: shippingAddress.value,
    paymentMethod: paymentMethod.value
  };

  orderCheckOut(orderInfo).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '订单创建成功，请进行支付',
        type: 'success',
        center: true
      });

      // 订单创建成功后，保存订单信息并显示弹窗
      const orderData = res.data.data; // 获取返回的订单数据
      orderId.value = orderData.orderId;  // 更新 orderId 为响应式
      orderDetails.value = {
        orderId: orderData.orderId,
        username: orderData.username,
        totalAmount: orderData.totalAmount,
        paymentMethod: orderData.paymentMethod,
        status: orderData.status,
        createTime: orderData.createTime
      };
      dialogVisible.value = false; // 显示弹窗
    } else {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true
      });
    }
  }).catch(err => {
    console.error('订单创建失败', err);
    ElMessage({
      message: '订单创建失败，请重试！',
      type: 'error',
      center: true
    });
  });
}

// 关闭弹窗并跳转支付
function goToPayment(orderId: number) {
  if (orderDetails.value) {
    // 使用 router.push 跳转到支付页面，订单ID传递到 URL
    router.push(`/payForm/`+ orderId);
  }
}
</script>

<template>
  <el-main>
    <div class="order-container" v-if="dialogVisible">
      <div class="order-info">
        <h2>订单详情</h2>
        <!-- 收货地址输入框 -->
        <el-input
            v-model="shippingAddress"
            placeholder="请输入收货地址"
            class="address-input"
        />
        <!-- 支付方式选择框，添加图片 -->
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
        <!-- 商品列表 -->
        <div class="cart-items">
          <div v-for="item in cartItemList" :key="item.productId" class="cart-item">
            <div class="item-title">{{ item.title }}</div>
            <div class="item-price">单价: ¥{{ item.price }}</div>
            <div class="item-quantity">数量: {{ item.quantity }}</div>
            <div class="item-total">总价: ¥{{ item.price * item.quantity }}</div>
          </div>
        </div>
        <!-- 总价格 -->
        <div class="total-price">
          <span>总价格: ¥{{ totalPrice }}</span>
        </div>
        <!-- 结算按钮 -->
        <el-button type="primary" @click="handleCheckout" class="checkout-btn">结算</el-button>
      </div>
    </div>

    <div class="order-container" v-if="!dialogVisible">
      <div class="order-info">
        <h2>确认订单</h2>
        <!-- 显示订单创建时间-->
        <div class="order-time">订单创建时间: {{ orderDetails.createTime }}</div>
        <!-- 收货地址输入框 -->
        <div class="address-input-reflect">收货地址: {{ shippingAddress }}</div>
        <!-- 支付方式 -->
        <div class="payment-method-reflect">支付方式: {{ orderDetails.paymentMethod }}</div>
        <!-- 商品列表 -->
        <div class="cart-items">
          <div v-for="item in cartItemList" :key="item.productId" class="cart-item">
            <div class="item-title">{{ item.title }}</div>
            <div class="item-price">单价: ¥{{ item.price }}</div>
            <div class="item-quantity">数量: {{ item.quantity }}</div>
            <div class="item-total">总价: ¥{{ item.price * item.quantity }}</div>
          </div>
        </div>
        <!-- 总价格 -->
        <div class="total-price">
          <span>总价格: ¥{{ totalPrice }}</span>
        </div>
        <!-- 结算按钮 -->
        <el-button type="primary" @click="goToPayment(orderId)" class="checkout-btn">结算</el-button>
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
  font-size: 24px;
  color: #ff6402;
  font-weight: 500;
  margin-bottom: 20px;
}

.address-input {
  width: 100%;
  margin-bottom: 18px;
  border-radius: 8px;
  padding: 12px;
  background-color: #fafafa;
}

.address-input:focus {
  border-color: #ff6402;
}

.payment-select {
  width: 100%;
  margin-bottom: 18px;
  border-radius: 8px;
  background-color: #fafafa;
}

.payment-icon {
  width: 20px;
  height: 20px;
  margin-right: 8px;
  vertical-align: middle;
}

.payment-method-reflect{
  width: 100%;
  margin-bottom: 18px;
  border-radius: 8px;
  background-color: #fafafa;
}

.address-input-reflect{
  width: 100%;
  margin-bottom: 18px;
  border-radius: 8px;
  background-color: #fafafa;
}

.order-time{
  width: 100%;
  margin-bottom: 18px;
  border-radius: 8px;
  background-color: #fafafa;
}


.cart-items {
  margin-bottom: 20px;
}

.cart-item {
  background-color: #fafafa;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.08);
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.item-price,
.item-quantity,
.item-total {
  font-size: 12px;
  color: #666;
}

.total-price {
  font-size: 16px;
  font-weight: 600;
  color: #ff6402;
  margin-bottom: 18px;
  text-align: right;
}

.checkout-btn {
  width: 100%;
  background-color: #ff6402;
  border: none;
  color: white;
  font-size: 16px;
  padding: 12px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.checkout-btn:hover {
  background-color: #e24f00;
}
</style>