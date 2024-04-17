<template>
  <div>
    <Header></Header>
    <div class="login-content">
    <div class="login-flex">
        <div class="login-left"></div>
        <!---->
        <div class="login-box">
            <h3 class="login-title">实名认证</h3>
            <form action="" id="renZ_Submit">
                <div class="alert-input">
                    <input type="text" class="form-border user-name" name="username" v-model="name" @blur="checkName" placeholder="请输入您的真实姓名">
                    <div class="err">{{nameErr}}</div>
                    <p class="prompt_name"></p>
                    <input type="text" class="form-border user-sfz" name="sfz" v-model='idCard' @blur="checkIdCard" placeholder="请输入15位或18位身份证号">
                    <div class="err">{{idCardErr}}</div>
                    <p class="prompt_sfz"></p>
                    <input type="text" class="form-border user-num" name="mobile" v-bind:value="phone" placeholder="请输入11位手机号" readonly>
                    <p class="prompt_num"></p>
                    <!-- <div class="form-yzm form-border">
                        <input class="yzm-write" type="text" v-model="code" @blur="checkCode" placeholder="输入短信验证码"> 
                        <input class="yzm-send" type="text" value="获取验证码" disabled="disabled" id="yzmBtn" readonly="readonly" >
                    </div>
                    <div class="err">{{codeErr}}</div>
                    <p class="prompt_yan"></p> -->
                </div>
                <div class="alert-input-agree">
                    <input type="checkbox" v-model="agree">我已阅读并同意<a href="javascript:;" target="_blank">《动力金融网注册服务协议》</a>
                </div>
                <div class="alert-input-btn">
                    <input type="button" @click="requestRealname" class="login-submit" value="认证">
                </div>
            </form>
            <div class="login-skip">
                暂不认证？ <a href="javascript:;" @click="goLink('/page/user/usercenter')">跳过</a>
            </div>
        </div>

    </div>
</div>
    <Footer></Footer>
  </div>
</template>

<script>
import Header from '@/components/common/Header'
import Footer from "@/components/common/Footer"
import {doPostJson} from '@/api/httpRequest'
import layx from "vue-layx"
export default {
    name:"RealNameView",
    components:{
        Header,
        Footer
    },
    data() {
        return {
            phone:'',
            name:'',
            nameErr:'',
            idCard:'',
            idCardErr:'',
            // code:'',
            // codeErr:'',
            agree:false
        }
    },
    mounted() {
        let userinfo = window.localStorage.getItem("userinfo")
        if(userinfo){
            this.phone = JSON.parse(userinfo).phone
        }
    },
    methods: {
        goLink(url,params){
        this.$router.push({
            path:url,
            query:params
        })
        },
        checkName(){
            if(this.name == ''){
                this.nameErr = '必须输入姓名'
            }else if(this.name.length < 2){
                this.nameErr = '姓名不正确'
            }else if(!/^[\u4e00-\u9fa5]{0,}$/.test(this.name)){
                this.nameErr = '姓名必须是中文'
            }else{
                this.nameErr=''
            }
        },
        checkIdCard(){
            if(this.idCard == ''){
                this.idCardErr = '请输入身份证号'
            }else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(this.idCard)){
                this.idCardErr = '身份证号格式不正确'
            }else{
                this.idCardErr = ''
            }
        },
        // checkCode(){
        //     if(this.code == '' || this.code == undefined){
        //         this.codeErr = '必须输入验证码'
        //     }else if(this.code.length != 4){
        //         this.codeErr = '验证码是4位的'
        //     }else {
        //         this.codeErr = ''
        //     }
        // },
        requestRealname(){
            if(this.agree == false){
                 layx.msg('请阅读实名认证协议',{dialogIcon:'warn',position:'ct'})
                 return
            }
            this.checkName()
            this.checkIdCard()
            // this.checkCode()
            if(//this.codeErr == '' && 
            this.nameErr == '' && this.idCardErr == ''){
                let param = {
                    name:this.name,
                    phone:this.phone,
                    idCard:this.idCard,
                    // code:this.code
                }
                doPostJson('/v1/user/realname',param).then(resp=>{
                    if(resp && resp.data.code == 1000){
                        this.$router.push({
                            path:'/page/user/usercenter'
                        })
                    }
                })
            }
        }
    },
}
</script>

<style scoped>
 .err{
    color: red;
    font-size: 18px;
 }
</style>