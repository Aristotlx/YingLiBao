import axios from "axios";
import qs from 'qs'
import layx from "vue-layx"

axios.defaults.baseURL = "http://localhost:8000/api"
axios.defaults.timeout = 50000

function doGet(url,params){
    return axios({
        url : url,
        method : 'get',
        params : params
    })
}

function doPostJson(url,params){
    return axios({
        url:url,
        method:'post',
        data:params
    })
}

function doPost(url,params){
    let requestData = qs.stringify(params)
    return axios.post(url,requestData)
}

axios.interceptors.request.use(function(config){
    let storageToken = window.localStorage.getItem("token")
    let userinfo = window.localStorage.getItem("userinfo")
    if(storageToken && userinfo){
        if(config.url == '/v1/user/realname' || config.url == '/v1/user/usercenter' ||
            config.url == '/v1/recharge/records' || config.url == '/v1/invest/product'){
            config.headers['Authorization'] = 'Bearer ' + storageToken
            config.headers['uid'] = JSON.parse(userinfo).uid
        }
    }
    return config
},function(err){
    console.log("请求错误"+err)
})

//应答拦截器，同意对错误处理
axios.interceptors.response.use(function(resp){
    if(resp && resp.data.code > 1000){
        let code = resp.data.code
        if(code == 3000){
            //token无效，重新登陆
            window.location.href='/page/user/login'
        }else{
            layx.msg(resp.data.msg,{dialogIcon:'warn',position:'ct'})
        }
    }
    return resp
},function(err){
    console.log("应答拦截器错误"+err)
    window.location.href("/")
})

export {doGet,doPost,doPostJson}