import axios from 'axios'
axios.defaults.withCredentials = true

const service = axios.create()

function hasToken(){
    return !(sessionStorage.getItem('token') == '')
}

service.interceptors.request.use(
    config => {
        if(hasToken()) {
            config.headers['token'] = sessionStorage.getItem('token')
        }
        return config
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
)

service.interceptors.response.use(
    response =>{
        if(response.status === 200){
            return response;
        }else{
            return Promise.reject();
        }
    },error =>{
        console.log(error);
        return Promise.reject();
}
)


export {
    service as axios
}