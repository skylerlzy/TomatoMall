<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getCartList } from '../../api/cart.ts';
import { orderCheckOut, updateOrderPrice } from '../../api/order.ts';
import { getCouponList, CouponInfo } from '../../api/coupon.ts';
import { userInfo,userInfoUpdate } from '../../api/user.ts';
import { ElMessage } from 'element-plus';
import { router } from "../../router";

type CartItemInfo = {
  cartItemId: number;
  productId: number;
  title: string;
  price: number;
  quantity: number;
};
type CouponInfo = {
  id: number;
  volume: number;
  need: number;
  isCommon: boolean;
  productIds: number[];
  deadline: string;
  number: number;
};
type OrderInfo = {
  orderId: number;
  username: string;
  totalAmount: number;
  paymentMethod: string;
  status: string;
  createTime: string;
};

const orderId = ref<number>(0);
const cartItemList = ref<CartItemInfo[]>([]);
const shippingAddress = ref('');
const paymentMethod = ref('');
const totalPrice = ref(0);
const originalPrice = ref(0);
const coupons = ref<CouponInfo[]>([]);
const selectedCoupon = ref<CouponInfo | null>(null);
const userBalance = ref(0); // 用户余额
const userPoints = ref(0); // 用户积分
const pointsToUse = ref(0); // 使用的积分
const maxPointsDeduction = 500; // 最大积分抵扣金额

// 获取用户信息
const loadUserInfo = async () => {
  const username = sessionStorage.getItem('username');
  if (username) {
    try {
      const res = await userInfo(username);
      if (res.data.code === '200') {
        userBalance.value = res.data.data.wallet;
        userPoints.value = res.data.data.point;
      } else {
        ElMessage.error('获取用户信息失败');
      }
    } catch (error) {
      console.error('获取用户信息失败', error);
      ElMessage.error('获取用户信息失败，请稍后再试');
    }
  }
};

const fetchCartItems = () => {
  getCartList().then(res => {
    cartItemList.value = res.data.data.items;
    calculateTotalPrice();
    loadCoupons();
  });
};
const loadCoupons = async () => {
  try {
    const res = await getCouponList();
    if (res.data.code === '200') {
      coupons.value = res.data.data.filter((coupon: CouponInfo) => {
        const meetsPriceCondition = coupon.need <= originalPrice.value;
        const isValidForProducts = coupon.isCommon ||
            cartItemList.value.some(item => coupon.productIds.includes(item.productId));
        return meetsPriceCondition && isValidForProducts && coupon.number > 0;
      });
    }
  } catch (error) {
    ElMessage.error('获取优惠券列表失败');
  }
};

const calculateTotalPrice = () => {
  originalPrice.value = cartItemList.value.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
  );


  let discount = 0;
  if (selectedCoupon.value && originalPrice.value >= selectedCoupon.value.need) {
    discount += selectedCoupon.value.volume;
  }
  const pointsDeduction = Math.min(pointsToUse.value / 10, maxPointsDeduction);
  totalPrice.value = originalPrice.value - discount - pointsDeduction;
};
// 处理积分变化
const handlePointsChange = (value: number) => {
  pointsToUse.value = Math.min(value, userPoints.value, maxPointsDeduction * 10);
  calculateTotalPrice();
};
const handleCouponSelect = (coupon: CouponInfo | null) => {
  selectedCoupon.value = coupon;
  calculateTotalPrice();
};

const clearCouponSelection = () => {
  selectedCoupon.value = null;
  calculateTotalPrice();
};

// 提交订单
const handleCheckout = async () => {
  if (!shippingAddress.value) {
    ElMessage.warning('请输入收货地址');
    return;
  }

  if (!paymentMethod.value) {
    ElMessage.warning('请选择支付方式');
    return;
  }

  const pointsDeduction = Math.min(pointsToUse.value / 10, maxPointsDeduction);

  if (paymentMethod.value === 'wallet') {
    if (userBalance.value >= totalPrice.value) {
      // 直接扣除余额并完成支付
      userBalance.value -= totalPrice.value;
      userPoints.value -= pointsToUse.value;

      await userInfoUpdate({
        username: sessionStorage.getItem('username')!,
        wallet: userBalance.value,
        point: userPoints.value,
      });

      ElMessage.success('支付成功！');
      router.push('/allCartItem');
      return;
    } else {
      ElMessage.error('余额不足，请选择其他支付方式');
      return;
    }
  }

  // 普通支付流程
  const orderInfo = {
    cartItemIds: cartItemList.value.map(item => item.cartItemId.toString()),
    shipping_address: shippingAddress.value,
    paymentMethod: paymentMethod.value,
  };

  orderCheckOut(orderInfo).then(async res => {
    if (res.data.code === '200') {
      const orderId = res.data.data.orderId;

      if (selectedCoupon.value || pointsToUse.value > 0) {
        await updateOrderPrice(orderId, totalPrice.value, selectedCoupon.value?.id || null);
      }

      userPoints.value -= pointsToUse.value;
      await userInfoUpdate({
        username: sessionStorage.getItem('username')!,
        point: userPoints.value,
      });

      router.push(`/payForm/${orderId}`);
    } else {
      ElMessage.error(res.data.msg);
    }
  }).catch(err => {
    console.error('订单创建失败', err);
    ElMessage.error('订单创建失败，请重试！');
  });
};

onMounted(() => {
  fetchCartItems();
  loadUserInfo();
});

</script>

<template>
  <div class="checkout-page">
    <div class="checkout-container">

      <div class="checkout-card">
        <h2 class="card-title">确认订单信息</h2>

        <el-form class="checkout-form">
          <el-form-item label="收货地址" class="form-item">
            <el-input
                v-model="shippingAddress"
                placeholder="请输入详细收货地址"
                size="large"
            />
          </el-form-item>

          <el-form-item label="支付方式" class="form-item">
            <el-select v-model="paymentMethod" placeholder="请选择支付方式" class="payment-select" size="large">
              <el-option label="信用卡" value="creditCard">
                <div class="payment-option">
                  <img src="../../resources/payWay/creditCard.png" alt="信用卡" />
                  <span>信用卡</span>
                </div>
              </el-option>
              <el-option label="支付宝" value="alipay">
                <div class="payment-option">
                  <img src="../../resources/payWay/alipay.png" alt="支付宝" />
                  <span>支付宝</span>
                </div>
              </el-option>
              <el-option label="微信支付" value="wechat">
                <div class="payment-option">
                  <img src="../../resources/payWay/wechat.png" alt="微信支付" />
                  <span>微信支付</span>
                </div>
              </el-option>
              <el-option label="钱包支付" value="wallet">
                <div class="payment-option">
                  <img src="../../resources/payWay/wechat.png" alt="钱包支付" />
                  <span>钱包支付</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class="user-info">
          <p>钱包余额：<span class="balance">¥{{ userBalance.toFixed(2) }}</span></p>
          <p>积分余额：<span class="points">{{ userPoints }}</span></p>
        </div>
        <div class="order-items">
          <h3>商品清单</h3>
          <div class="item-list">
            <div v-for="item in cartItemList" :key="item.productId" class="order-item">
              <div class="item-info">
                <span class="item-title">{{ item.title }}</span>
                <span class="item-quantity">x{{ item.quantity }}</span>
              </div>
              <div class="item-price">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
            </div>
          </div>
        </div>
        <div class="points-section">
          <h3>积分抵扣</h3>
          <p>当前积分：{{ userPoints }}，10积分可抵扣1元，最多可抵扣500元</p>
          <el-input-number
              v-model="pointsToUse"
              :min="0"
              :max="userPoints"
              :step="10"
              @change="handlePointsChange"
          />
        </div>
        <div class="coupon-section">
          <div class="coupon-header">
            <h3>{{ selectedCoupon ? '已选择优惠券' : '选择优惠券' }}</h3>
            <el-button
                v-if="selectedCoupon"
                type="text"
                class="clear-btn"
                @click="clearCouponSelection"
            >
              取消选择
            </el-button>
          </div>
          <div class="coupon-list">
            <el-radio-group v-model="selectedCoupon" @change="handleCouponSelect">
              <el-radio
                  v-for="coupon in coupons"
                  :key="coupon.id"
                  :label="coupon"
                  class="coupon-item"
              >
                <div class="coupon-card">
                  <div class="coupon-left">
                    <div class="amount-section">
                      <span class="symbol">¥</span>
                      <span class="amount">{{ coupon.volume }}</span>
                    </div>
                    <div class="divider"></div>
                  </div>
                  <div class="coupon-right">
                    <div class="coupon-condition">满{{ coupon.need }}可用</div>
                    <div class="coupon-type">
                      {{ coupon.isCommon ? '全场通用' : '部分商品可用' }}
                    </div>
                    <div class="coupon-validity">有效期至：{{ coupon.deadline }}</div>
                    <div class="coupon-count">剩余: {{ coupon.number }}张</div>
                  </div>
                </div>
              </el-radio>
            </el-radio-group>
          </div>
          <div v-if="coupons.length === 0" class="no-coupon">
            暂无可用优惠券
          </div>
        </div>

        <div class="order-summary">
          <div class="price-detail">
            <p>商品总价：<span>¥{{ originalPrice.toFixed(2) }}</span></p>
            <p v-if="selectedCoupon">优惠券抵扣：<span>-¥{{ selectedCoupon.volume.toFixed(2) }}</span></p>
            <p>积分抵扣：<span>-¥{{ (pointsToUse / 10).toFixed(2) }}</span></p>
          </div>
          <div class="final-amount">
            <p>应付金额：<span class="final-price">¥{{ totalPrice.toFixed(2) }}</span></p>
          </div>
        </div>

        <el-button type="primary" class="submit-btn" @click="handleCheckout">
          提交订单
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.checkout-page {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.checkout-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 0 16px;
}

.checkout-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 32px 24px;
  margin-top: 20px;
}

.card-title {
  font-size: 20px;
  color: #333;
  margin-bottom: 32px;
  text-align: center;
  font-weight: 600;
}
.user-info {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
.balance {
  font-weight: bold;
  color: #ff6402;
}

.points {
  font-weight: bold;
  color: #52c41a;
}

.user-info p {
  margin: 0;
  font-size: 14px;
  color: #333;
}
.checkout-form {
  margin-bottom: 32px;
}

.form-item {
  margin-bottom: 24px;
}

.form-item :deep(.el-input),
.form-item :deep(.el-select) {
  width: 100%;
  max-width: 400px;
}

.payment-select {
  width: 100%;
}

.payment-option {
  display: flex;
  align-items: center;
  gap: 12px;
}

.payment-option img {
  width: 20px;
  height: 20px;
}

.order-items {
  margin-bottom: 32px;
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
}

.order-items h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 16px;
  font-weight: 500;
}

.item-list {
  border-top: 1px solid #eee;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #eee;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.item-title {
  font-size: 14px;
  color: #333;
}

.item-quantity {
  color: #666;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 4px;
}

.item-price {
  font-size: 15px;
  color: #ff6402;
  font-weight: 500;
}

.coupon-section {
  margin: 32px 0;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.coupon-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #fcfcfc;
  border-bottom: 1px solid #f0f0f0;
}

.coupon-header h3 {
  font-size: 16px;
  margin: 0;
  font-weight: 500;
  color: #333;
}

.clear-btn {
  color: #ff6402;
  padding: 0;
  font-size: 14px;
}

.clear-btn:hover {
  color: #e65a00;
  opacity: 0.8;
}

.coupon-list {
  padding: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.coupon-item {
  height: auto;
  margin: 0;
  padding: 0;
  width: 100%;
}

.coupon-card {
  position: relative;
  display: flex;
  height: 90px;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
}

.coupon-card:hover {
  border-color: #ff6402;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 100, 2, 0.1);
}

.coupon-left {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 120px;
  background: #fff5f0;
  padding: 0 20px;
}

.amount-section {
  text-align: center;
  color: #ff6402;
}

.symbol {
  font-size: 16px;
  margin-right: 2px;
}

.amount {
  font-size: 32px;
  font-weight: bold;
  line-height: 1;
}

.divider {
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background: repeating-linear-gradient(
      to bottom,
      transparent 0,
      transparent 6px,
      #e4e7ed 6px,
      #e4e7ed 12px
  );
}

.coupon-right {
  flex: 1;
  padding: 12px 16px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coupon-condition {
  font-size: 15px;
  color: #333;
  font-weight: 500;
  margin-bottom: 4px;
}

.coupon-type {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.coupon-validity {
  font-size: 12px;
  color: #999;
}

.coupon-count {
  font-size: 12px;
  color: #ff6402;
  margin-top: 2px;
}

.no-coupon {
  text-align: center;
  color: #999;
  padding: 32px 0;
  font-size: 14px;
}

/* 选中状态样式 */
.el-radio.is-checked .coupon-card {
  border-color: #ff6402;
  background: #fff5f0;
}

.el-radio.is-checked .coupon-left {
  background: #ff6402;
}

.el-radio.is-checked .amount-section {
  color: #fff;
}

.el-radio.is-checked .divider {
  background: repeating-linear-gradient(
      to bottom,
      transparent 0,
      transparent 6px,
      rgba(255, 255, 255, 0.3) 6px,
      rgba(255, 255, 255, 0.3) 12px
  );
}

.order-summary {
  margin-top: 32px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.price-detail {
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.price-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: #666;
}

.price-item:last-child {
  margin-bottom: 0;
}

.final-amount {
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
}

.discount {
  color: #ff6402;
  font-weight: 500;
}

.final-price {
  color: #ff6402;
  font-size: 24px;
  font-weight: bold;
}

.submit-btn {
  width: 100%;
  height: 44px;
  background-color: #ff6402;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  margin-top: 32px;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background-color: #e65a00;
}

@media screen and (max-width: 480px) {
  .checkout-container {
    padding: 0 12px;
  }

  .checkout-card {
    padding: 24px 16px;
  }

  .coupon-list {
    padding: 12px;
    gap: 12px;
  }

  .coupon-card {
    height: 80px;
  }

  .coupon-left {
    width: 100px;
    padding: 0 12px;
  }

  .amount {
    font-size: 24px;
  }

  .coupon-right {
    padding: 8px 12px;
  }

  .coupon-condition {
    font-size: 14px;
  }

  .price-detail,
  .final-amount {
    padding: 12px 16px;
  }

  .final-price {
    font-size: 20px;
  }

  .submit-btn {
    margin-top: 24px;
  }
}
</style>