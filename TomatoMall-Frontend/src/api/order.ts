import {axios} from "../util/request.ts";
import {ORDER_MODULE} from "./_prefix.ts";
import {CART_MODULE} from "./_prefix.ts";

type orderInfo = {
    cartItemIds:string[]
    shipping_address: string,
    paymentMethod: string
}

export const orderCheckOut = (orderInfo: orderInfo) => {
    const { cartItemIds, shipping_address, paymentMethod } = orderInfo;

    return axios.post(
        `${CART_MODULE}/checkout`,
        null,  // 因为后端期望通过请求参数接收数据，而不是请求体，所以我们传递 null
        {
            params: {
                cartItemIds: cartItemIds.join(','),  // 将 cartItemIds 转换为逗号分隔的字符串
                shipping_address: shipping_address,  // 收货地址
                payment_method: paymentMethod  // 支付方式
            },
            headers: { 'Content-Type': 'application/json' }
        }
    )
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error('订单创建失败', err);
            throw err;
        });
}


// 创建 VIP 订单
export const createVIPOrder = (isVIP: number, paymentMethod: string) => {
    return axios.post(
        `${CART_MODULE}/checkoutVIP`,
        null,
        {
            params: {
                isVIP: isVIP,
                payment_method: paymentMethod
            },
            headers: { 'Content-Type': 'application/json' }
        }
    )
        .then(res => {
            return res
        })
        .catch(err => {
            console.error('VIP订单创建失败', err)
            throw err
        })
}


export const orderPay = (orderId : number) =>{
    return axios.post(`${ORDER_MODULE}/${orderId}/pay`, {headers: {'Content-Type': 'application/json'}})
        .then(res =>{
            return res;
        })
}

export const orderNotify = (orderId : number) =>{
    return axios.post(`${ORDER_MODULE}/notify`, {headers: {'Content-Type': 'application/json'}})
        .then(res =>{
            return res;
        })
}
// 获取订单状态
export const getOrderStatus = (orderId: number) => {
    return axios.get(`${ORDER_MODULE}/${orderId}/status`);
}

export const updateOrderPrice = (orderId: number, finalPrice: number, couponId: number | null) => {
    return axios.put(
        `${ORDER_MODULE}/${orderId}/price`,
        null,
        {
            params: {
                final_price: finalPrice,
                coupon_id: couponId
            },
            headers: { 'Content-Type': 'application/json' }
        }
    )
        .then(res => res)
        .catch(err => {
            console.error('更新订单价格失败', err);
            throw err;
        });
}

export const checkoutWallet = (amount: number, paymentMethod: string) => {
    return axios.post(
        `${CART_MODULE}/checkoutWallet`,
        null, // 请求体为空
        {
            params: {
                amount: amount, // 传递 Double 类型的金额
                payment_method: paymentMethod, // 支付方式
            },
            headers: { 'Content-Type': 'application/json' },
        }
    );
};