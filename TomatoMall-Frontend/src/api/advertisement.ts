import {axios} from '../util/request'
import {ADVERTISEMENT_MODULE} from './_prefix'

type advertisementInfo = {
    productId: number,
    title: string,
    content: string,
    imgUrl: string,
}

type updateInfo = {
    productId: number,
    title: string,
    content: string,
    imgUrl: string,
}

export const createAdvertisement = (advertisementInfo: advertisementInfo) => {
    return axios.post(`${ADVERTISEMENT_MODULE}`, advertisementInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

export const getAdvertisementList = () =>{
    return axios.get(`${ADVERTISEMENT_MODULE}`)
        .then(res =>{
            return res;
        })
}

export const updateAdvertisement = (updateInfo: updateInfo) =>{
    return axios.put(`${ADVERTISEMENT_MODULE}`,updateInfo,{headers: {'Content-Type': 'application/json'}})
        .then(res =>{
            return res;
        })
}

export const deleteAdvertisement = (id: number) =>{
    return axios.delete(`${ADVERTISEMENT_MODULE}/${id}`).then(res =>{
        return res;
    })
}
