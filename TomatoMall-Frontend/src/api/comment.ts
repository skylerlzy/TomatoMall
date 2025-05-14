import { axios } from '../util/request'
import { COMMENT_MODULE } from './_prefix'

// 评论数据类型定义
// 首先在 comment.ts 中更新 CommentInfo 接口
export interface CommentInfo {
    id?: number
    productId: number
    userId: number
    username?: string
    userAvatar?: string
    rate: number
    content: string
    createTime?: string
    son: number[]
    fatherId: number | null
}

// 获取评论列表
export const getCommentList = () => {
    return axios.get(`${COMMENT_MODULE}/getAll`).then(res => {
        return res
    })
}

// 获取单个评论详情
export const getComment = (id: number) => {
    return axios.get(`${COMMENT_MODULE}/${id}`).then(res => {
        return res
    })
}

// 添加评论
export const addComment = (comment: CommentInfo) => {
    return axios.post(
        `${COMMENT_MODULE}/add`,
        comment,
        {
            headers: { 'Content-Type': 'application/json' }
        }
    ).then(res => {
        return res
    })
}

// 更新评论
export const updateComment = (id: number, comment: CommentInfo) => {
    return axios.put(
        `${COMMENT_MODULE}/${id}`,
        comment,
        {
            headers: { 'Content-Type': 'application/json' }
        }
    ).then(res => {
        return res
    })
}

// 删除评论
export const deleteComment = (id: number) => {
    return axios.delete(`${COMMENT_MODULE}/${id}`).then(res => {
        return res
    })
}