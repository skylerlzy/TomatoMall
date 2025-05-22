<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getCartList, updateQuantity } from '../../api/cart.ts';
import { router } from "../../router";
import { deleteCart } from "../../api/cart.ts";
import { ElMessage } from "element-plus";

type CartItemInfo = {
  cartItemId: number;
  productId: number;
  title: string;
  price: number;
  description: string;
  cover: string;
  detail: string;
  quantity: number;
};

const cartItemList = ref<CartItemInfo[]>([]);

onMounted(() => {
  fetchCartItems();
});

function fetchCartItems() {
  getCartList().then(res => {
    cartItemList.value = res.data.data.items;
  });
}

function toProductDetailPage(productId: number) {
  router.push(`/productDetail/` + productId);
}

function deleteCartItem(cartItemId: number) {
  deleteCart(cartItemId).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: '删除商品成功！',
        type: 'success',
        center: true,
      });
      fetchCartItems();
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      });
    }
  }).catch(err => {
    console.error('商品删除失败', err);
    ElMessage({
      message: '商品删除失败，请重试！',
      type: 'error',
      center: true,
    });
  });
}

function updateCartItemQuantity(cartItemId: number, quantity: number) {
  updateQuantity(cartItemId, quantity).then(res => {
    if (res.data.code === '200') {
      fetchCartItems();
    } else {
    }
  }).catch(err => {
    console.error('更新商品数量失败', err);
  });
}

function increaseQuantity(cartItem: CartItemInfo) {
  cartItem.quantity++;
  updateCartItemQuantity(cartItem.cartItemId, cartItem.quantity);
}

function decreaseQuantity(cartItem: CartItemInfo) {
  if (cartItem.quantity > 1) {
    cartItem.quantity--;
    updateCartItemQuantity(cartItem.cartItemId, cartItem.quantity);
  }
}

function orderCheckout(){
    router.push('/orderCheckOut');
}

</script>

<template>
  <el-main>
    <div class="cart-container">
      <div class="cart-scrollbar" max-height="450px" always>
        <div class="cart-item-list">
          <div v-for="cartItem in cartItemList" :key="cartItem.productId" class="cart-card" @click="toProductDetailPage(cartItem.productId)">
            <div class="cart-item-left">
              <img :src="cartItem.cover" alt="" class="cartItem-logoUrl" />
            </div>
            <div class="cart-item-right">
              <h2 class="cartItem-title">{{ cartItem.title }}</h2>
              <p class="cartItem-description">商品详情: {{ cartItem.description }}</p>
              <p class="cartItem-price">单价: ¥{{ cartItem.price }}</p>
              <p class="cartItem-amount">总价: ¥{{ cartItem.price * cartItem.quantity }}</p>
              <div class="quantity-control">
                <button @click.stop="decreaseQuantity(cartItem)" class="quantity-btn">-</button>
                <span class="quantity-display">{{ cartItem.quantity }}</span>
                <button @click.stop="increaseQuantity(cartItem)" class="quantity-btn">+</button>
              </div>
              <button class="remove-btn" @click.stop="deleteCartItem(cartItem.cartItemId)">删除</button>
            </div>
          </div>
        </div>
        <el-button type="primary" @click="orderCheckout()" class="order-button">购物车商品结算</el-button>
      </div>
    </div>
  </el-main>

</template>

<style scoped>
.cart-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  display: flex;
  justify-content: center;  /* Ensure scrollbar is centered horizontally */
}

.cart-scrollbar {
  width: 80%;  /* Set width of the scrollbar */
  max-height: 450px;  /* Limit the height of the scrollbar */
}

.cart-item-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.cart-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 20px;
  position: relative;
  transition: transform 0.2s, box-shadow 0.3s;
}

.cart-card:hover {
  transform: scale(1.02);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.cart-item-left {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 120px;
}

.cartItem-logoUrl {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.cartItem-price {
  margin-top: 8px;
  font-size: 14px;
  font-weight: bold;
  color: #ff6402;
}

.cart-item-right {
  flex: 1;
  padding-left: 20px;
  text-align: left;
}

.cartItem-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.cartItem-description {
  font-size: 14px;
  color: #777;
  margin: 10px 0;
}

.cartItem-amount {
  font-size: 16px;
  font-weight: bold;
  color: #ff6402;
  margin-bottom: 10px;
}

.remove-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #3ea87a;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.remove-btn:hover {
  background-color: #fd0000;
}

@media (max-width: 768px) {
  .cart-card {
    flex-direction: column;
    align-items: center;
  }

  .cart-item-left {
    margin-bottom: 15px;
  }

  .cart-item-right {
    text-align: center;
    padding-left: 0;
  }
}.cart-container {
   padding: 20px;
   background-color: #fff;
   border-radius: 10px;
   display: flex;
   justify-content: center;  /* Ensure scrollbar is centered horizontally */
 }

.cart-scrollbar {
  width: 80%;  /* Set width of the scrollbar */
  max-height: 450px;  /* Limit the height of the scrollbar */
}

.cart-item-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-button {
  border-color: #3ea87a;
  background-color: #3ea87a;
  margin-top: 20px;
  margin-left: 45%;
}

.cart-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 20px;
  position: relative;
  transition: transform 0.2s, box-shadow 0.3s;
}

.cart-card:hover {
  transform: scale(1.02);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.cart-item-left {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 120px;
}

.cartItem-logoUrl {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.cartItem-price {
  margin-top: 8px;
  font-size: 14px;
  font-weight: bold;
  color: #ff6402;
}

.cart-item-right {
  flex: 1;
  padding-left: 20px;
  text-align: left;
}

.cartItem-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.cartItem-description {
  font-size: 14px;
  color: #777;
  margin: 10px 0;
}

.cartItem-amount {
  font-size: 16px;
  font-weight: bold;
  color: #ff6402;
  margin-bottom: 10px;
}

.remove-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #3ea87a;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.remove-btn:hover {
  background-color: #fd0000;
}

.quantity-control {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 10px;
}

.quantity-btn {
  width: 30px;
  height: 30px;
  font-size: 18px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 50%;
  margin: 0 5px;
  cursor: pointer;
}

.quantity-btn:hover {
  background-color: #d43f3a;
}

.quantity-display {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

@media (max-width: 768px) {
  .cart-card {
    flex-direction: column;
    align-items: center;
  }

  .cart-item-left {
    margin-bottom: 15px;
  }

  .cart-item-right {
    text-align: center;
    padding-left: 0;
  }
}
</style>