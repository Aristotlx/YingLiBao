<template>
    <!--头部-->
      <div class="public-head">
         <div class="public-head-nav">
          <div class="public-head-left">
            <h1 class="public-head-logo"><a href="javascript:;">
                <img src="@/assets/image/logo.png" alt="">
            </a></h1>
            <ul class="public-head-list">
                <li><a href="javascript:void(0)" @click="goLink('/')">主页</a></li>
                <li class="public-head-hover">
                    <a href="javascript:void(0);">我要投资</a>
                    <!--二级导航-->
                    <div class="two-title">
                        <a href="javascript:void(0)" @click="goLink('/page/product/list',{ptype:1})">优先类产品</a>
                        <a href="javascript:void(0)" @click="goLink('/page/product/list',{ptype:2})">散标类产品</a>
                    </div>
                </li>
                <li><a href="javascript:void(0)">借款人信息</a></li>
                <li><a href="javascript:void(0)">信息披露</a></li>
                <li><a href="javascript:void(0)">安全计划</a></li>
            </ul>
        </div>
        <div class="public-head-right" v-if="logined">
            <a href="javascript:void(0)" style="color:red" @click="goLink('/page/user/realname')">实名认证</a>
            <a href="javascript:void(0)" style="color:red" @click="goLink('/page/user/usercenter')">用户中心</a>
        </div>
        <div class="public-head-right" v-else>
            <a href="javascript:void(0)" style="color:blue" @click="goLink('/page/user/login')">登录</a>
            <a href="javascript:void(0)" style="color:blue" @click="goLink('/page/user/register')">注册</a>
        </div>

    </div>
</div>
<!--end-->

</template>

<script>
export default {
name:"MyHeader",
data() {
    return {
        logined:false
    }
},
mounted() {
    if(window.localStorage.getItem("userinfo")){
        this.logined = true
    }
},
methods: {
    goLink(url,params){
        this.$router.push({
            path:url,
            query:params
        })
    },
    
},
}
</script>

<style scoped>

</style>