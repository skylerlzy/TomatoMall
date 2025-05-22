import {axios} from '../util/request.ts';
import {USER_MODULE} from './_prefix';

type LoginInfo = {
    username: string
    password: string
}

type RegisterInfo = {
    username: string
    name: string
    telephone: string
    password: string
    avatar:string
    email: string
    location: string
    role:string
    likeTag : string
}

type UpdateInfo = {
    username?: string
    name?: string
    telephone?: string
    password?: string
    avatar?:string
    email?: string
    location?: string
    role?:string
    isVip?: boolean
}

export const userLogin = (loginInfo: LoginInfo) => {
    return axios.post(`${USER_MODULE}/login`,  loginInfo)
        .then(res => {
            return res
        })
}

export const userInfo = (username:string) => {
    return axios.get(`${USER_MODULE}/${username}`).then((res) => {
        return res
    })
}

// 用户注册
export const userRegister = (registerInfo: RegisterInfo) => {
    return axios.post(`${USER_MODULE}`, registerInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 更新用户信息
export const userInfoUpdate = (updateInfo: UpdateInfo) => {
    return axios.put(`${USER_MODULE}`, updateInfo, {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 更新用户信息
export const getUserList = () => {
    return axios.get(`${USER_MODULE}/list`, {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 添加收藏商品
export const addCollect = (productId: number) => {
    return axios.post(`${USER_MODULE}/addCollect?productId=${productId}`)
        .then(res => {
            return res
        })
}

// 删除收藏商品
export const deleteCollect = (productId: number) => {
    return axios.delete(`${USER_MODULE}/deleteCollect?productId=${productId}`)
        .then(res => {
            return res
        })
}

// 获取用户收藏商品列表
export const getCollect = () => {
    return axios.get(`${USER_MODULE}/getCollect`)
        .then(res => {
            return res
        })
}