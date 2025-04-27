import {axios} from '../util/request'
import {CART_MODULE} from './_prefix'

type cartItemInfo = {
    productId: number,
    title: string,
    price: number,
    description: string,
    cover : string,
    detail : string,
    quantity : number
}


//添加和修改商品信息
export const addToCart = (cartItem: cartItemInfo) => {
    return axios.post(`${CART_MODULE}`, cartItem,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

//删除单个商品Item
export const deleteCart = (cartItemId: number) => {
    return axios.delete(`${CART_MODULE}/${cartItemId}`).then(
        res => {
            return res
        }
    )
}

//获取购物车商品
export const getCartList = () => {
    return axios.get(`${CART_MODULE}`).
    then(res => {
        return res
    })
}

//更改商品购物车数量
export const updateQuantity = (cartItemId: number, quantity: number) => {
    return axios.patch(`${CART_MODULE}/${cartItemId}`,
        { quantity }, // 请求体中传递数量
        { headers: { 'Content-Type': 'application/json' } }
    ).then(res => {
        return res
    })
}

