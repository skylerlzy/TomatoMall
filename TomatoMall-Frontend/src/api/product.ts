import {axios} from '../util/request'
import {PRODUCT_MODULE} from './_prefix'
import {STOCK_MODULE} from './_prefix'

type productInfo = {
    title : string,
    price : number,
    rate : number,
    description : string,
    cover : string,
    detail : string,
    specifications : productSpecifications[],
}

type productSpecifications = {
    item : string,
    value: string,
}

type stockInfo = {
    id:number,
    productId : number,
    amount : number,
    frozen : number,
}

type updateInfo = {
    id:number,
    title?: string,
    price?: number,
    rate?: number,
    description?: string,
    cover?: string,
    detail?: string,
    specifications?: productSpecifications[],
}

export const createProduct = (productInfo: productInfo) => {
    return axios.post(`${PRODUCT_MODULE}`, productInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

export const getProductList = () =>{
    return axios.get(`${PRODUCT_MODULE}`)
        .then(res =>{
            return res;
        })
}

export const searchProduct = (id: string) =>{
    return axios.get(`${PRODUCT_MODULE}/${id}`, {headers: {'Content-Type': 'application/json'}})
    .then(res =>{
        return res;
    })
}

export const updateProduct = (updateInfo: updateInfo) =>{
    return axios.put(`${PRODUCT_MODULE}`,updateInfo,{headers: {'Content-Type': 'application/json'}})
    .then(res =>{
        return res;
    })
}

export const deleteProduct = (id: number) =>{
    return axios.delete(`${PRODUCT_MODULE}/${id}`).then(res =>{
        return res;
    })
}

export const changeStock = (id: number , stockInfo: stockInfo) =>{
    return axios.patch(`${STOCK_MODULE}/${id}` , stockInfo, {headers : {'Content-Type' :'application/json'}})
    .then(res =>{
        return res;
    })
}

export const getStocks = (id: number) =>{
    return axios.get(`${STOCK_MODULE}/${id}`).then(res =>{
        return res;
    })
}