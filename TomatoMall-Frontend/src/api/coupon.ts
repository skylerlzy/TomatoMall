import { axios } from '../util/request'
import { COUPON_MODULE } from './_prefix'

// 优惠券数据类型定义
export interface CouponInfo {
    id?: number
    userId?: number
    isCommon: boolean
    volume: number
    need: number
    deadline: string
    number: number
    productIds: number[]
}

// 获取优惠券列表
export const getCouponList = () => {
    return axios.get(`${COUPON_MODULE}/getList`).then(res => {
        return res
    })
}

// 获取单个优惠券详情
export const getCoupon = (id: number) => {
    return axios.get(`${COUPON_MODULE}/${id}`).then(res => {
        return res
    })
}

// 添加优惠券
export const addCoupon = (coupon: CouponInfo) => {
    return axios.post(
        `${COUPON_MODULE}/add`,
        coupon,
        {
            headers: { 'Content-Type': 'application/json' }
        }
    ).then(res => {
        return res
    })
}

// 更新优惠券
export const updateCoupon = (coupon: CouponInfo) => {
    return axios.put(
        `${COUPON_MODULE}/update`,
        coupon,
        {
            headers: { 'Content-Type': 'application/json' }
        }
    ).then(res => {
        return res
    })
}

// 删除优惠券
export const deleteCoupon = (id: number) => {
    return axios.delete(`${COUPON_MODULE}/${id}`).then(res => {
        return res
    })
}