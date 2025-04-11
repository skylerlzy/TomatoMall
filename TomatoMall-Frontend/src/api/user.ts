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