<template>
  <div>
  <Header></Header>
  <div class="content clearfix">
    <div class="detail-left">
        <div class="detail-left-title">{{product.productName}}（{{product.productNo}}期）</div>
        <ul class="detail-left-number">
            <li>
                <span>历史年化收益率</span>
                <p><b>{{product.rate}}</b>%</p>
                <span>历史年化收益率</span>
            </li>
            <li>
                <span>募集金额（元）</span>
                <p><b>{{product.productMoney}}</b>元</p>
                <span v-if="product.leftProductMoney">募集中&nbsp;&nbsp;剩余募集金额{{product.leftProductMoney}}元</span>
                <span v-else>已满标</span>
            </li>
            <li>
                <span>投资周期</span>
                <p v-if="product.productType == 0"><b>{{product.cycle}}</b>天</p>
                <p v-else><b>{{product.cycle}}</b>个月</p>
            </li>

        </ul>
        <div class="detail-left-way">
            <span>收益获取方式</span>
            <span>收益返还：<i>到期还本付息</i></span>
        </div>
        <!--投资记录-->
        <div class="datail-record">
            <h2 class="datail-record-title">投资记录</h2>
            <div class="datail-record-list">
                <table align="center" width="880" border="0" cellspacing="0" cellpadding="0">
                    <colgroup>
                        <col style="width: 72px" />
                        <col style="width: 203px" />
                        <col style="width: 251px" />
                        <col style="width: 354px" />
                    </colgroup>
                    <thead class="datail_thead">
                        <tr>
                            <th>序号</th>
                            <th>投资人</th>
                            <th>投资金额（元）</th>
                            <th>投资时间</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(bid,ind) in bidList" :key="bid.id">
                            <td>{{ind+1}}</td>
                            <td class="datail-record-phone">{{bid.phone}}</td>
                            <td>{{bid.bidMoney}}</td>
                            <td>{{bid.bidTime}}</td>
                        </tr> 
                    </tbody>
                </table>
            </div>
        </div>

    </div>
    <!--右侧-->
    <div class="detail-right">
        <div class="detail-right-title">立即投资</div>
        <div class="detail-right-mode">
            <h3 class="detail-right-mode-title">收益方式</h3>
            <p class="detail-right-mode-p"><span>到期还本付息</span></p>
            <h3 class="detail-right-mode-title">我的账户可用</h3>
            <div class="detail-right-mode-rmb" v-if="logined == false">
                <p>资金（元）：******</p>
                <a href="javascript:void(0)" style="color:blue" @click="goLink('/page/user/login')">请登录</a>
            </div>
            <div class="detail-right-mode-rmb" v-else>
                <p>资金（元）：{{accountMoney}}</p>
            </div>
            <h3 class="detail-right-mode-title">预计利息收入 {{income}}（元）</h3>
            <form action="" id="number_submit">
                <p>请在下方输入投资金额</p>
                <input type="text" placeholder="请输入日投资金额，应为100元整倍数" v-model="investMoney" @blur="checkInvestMoney" class="number-money" >
                <div class="err">{{investMoneyErr}}</div>
                <input type="button" value="立即投资" @click="investProduct" class="submit-btn">
            </form>
        </div>
    </div>
  </div>
<Footer></Footer>
</div>
</template>

<script>
import Header from "@/components/common/Header";
import Footer from "@/components/common/Footer";
import { doGet,doPost } from '@/api/httpRequest';
import layx from "vue-layx"
export default {
    name:"ProductDetail",
    components:{
      Header,
      Footer
    },
    data(){
      return{
        product:{
                id:0,
                productName:"",
                rate:0.0,
                cycle:0,
                releaseTime:0,
                productType:0,
                productNo:"",
                productMoney:0,
                leftProductMoney:0,
                bidMinLimit:0,
                bidMaxLimit:0,
                productStatus:0,
                productFullTime:"",
                productDesc:"",
        },
        bidList:[{
            id:0,
            phone:"",
            bidTime:"",
            bidMoney:0.00
        }],
        logined:false,
        accountMoney:0.0,
        investMoney:100,
        investMoneyErr:'',
        income:""
      }
    },
    mounted(){
      if(window.localStorage.getItem("userinfo")){
        this.logined = true;
      }
      this.initPage()
    },
    methods: {
    goLink(url,params){
        this.$router.push({
            path:url,
            query:params
        })
    },
    initPage(){
     let productId = this.$route.query.productId;
      doGet('/v1/product/info',{productId:productId}).then(resp=>{
        if(resp){
          this.product = resp.data.data
          this.bidList = resp.data.list
        }
      })

      doGet('/v1/user/usercenter').then(resp=>{
        if(resp && resp.data.code == 1000){
            this.accountMoney = resp.data.data.money;
        }
      })   
    },
    checkInvestMoney(){
        if(isNaN(this.investMoney)){
            this.investMoneyErr='请输入正确的金额'
        }else if(parseInt(this.investMoney) < 100){
            this.investMoneyErr='最小投资100元'
        }else if(parseFloat(this.investMoney) % 100 !=0){
            this.investMoneyErr='投资金额是100的整数倍'
        }else{
            this.investMoneyErr=''
            let dayRate = this.product.rate / 365 /100
            let incomeMoney = 0.0
            if(this.product.productType == 0){
                incomeMoney = this.investMoney * this.product.cycle * dayRate
            }else{
                incomeMoney = this.investMoney * (this.product.cycle * 30) * dayRate
            }
                this.income = incomeMoney.toFixed(2)
        }
    },
    investProduct(){
        let userinfo = JSON.parse(window.localStorage.getItem("userinfo"))
        if(userinfo){
            if(userinfo.name != ''){
                this.checkInvestMoney()
                if(this.investMoneyErr == ''){
                    doPost('/v1/invest/product',{productId:this.product.id,money:this.investMoney})
                        .then(resp=>{
                            if(resp && resp.data.code == 1000){
                                this.initPage()
                            }
                        })
                }
            }else{
                layx.msg('投资前需实名认证',{dialogIcon:'warn',position:'ct'})
            }
        }else{
            layx.msg('请先登录',{dialogIcon:'warn',position:'ct'})
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