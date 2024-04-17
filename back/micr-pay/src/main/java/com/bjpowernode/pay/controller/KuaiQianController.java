package com.bjpowernode.pay.controller;

import com.bjpowernode.api.model.User;
import com.bjpowernode.pay.service.KuaiQianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class KuaiQianController {
    @Resource
    private KuaiQianService kQService;

    @GetMapping("/rece/recharge")
    public String receFrontRechargeKQ(Integer uid, BigDecimal rechargeMoney, Model model){
        String view = "err";
        if (uid != null && uid > 0 && rechargeMoney !=null && rechargeMoney.doubleValue()>0){
            try {
                User user = kQService.queryUser(uid);
                if (user != null){
                    //创建快钱支付接口需要的请求参数
                    Map<String, String> data = kQService.generateFormData(uid, user.getPhone(), rechargeMoney);
                    model.addAllAttributes(data);

                    //创建充值记录
                    kQService.addRecharge(uid,rechargeMoney,data.get("orderId"));
                    //把订单号存到redis
                    kQService.addOrderIdToRedis(data.get("orderId"));
                    view = "kqForm";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return view;
    }

    @GetMapping("/rece/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request){
        System.out.println("=======接受快钱的异步通知========");
        kQService.kqNotify(request);
        return "<result>1</result><redirecturl>http://localhost:8080/</redirecturl>";
    }

    @GetMapping("/rece/query")
    @ResponseBody
    public String queryKQOrder(){
        kQService.handleQueryOrder();
        return "接受了查询的请求";
    }
}
