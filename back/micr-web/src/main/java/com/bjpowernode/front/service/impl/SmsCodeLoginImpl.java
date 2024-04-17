package com.bjpowernode.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.common.constants.RedisKey;
import com.bjpowernode.common.util.HttpUtils;
import com.bjpowernode.front.config.JdwxSmsConfig;
import com.bjpowernode.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service(value = "smsCodeLoginImpl")
public class SmsCodeLoginImpl implements SmsService{
    @Resource
    private StringRedisTemplate stringRedisTemplate;
//    @Resource
//    private JdwxSmsConfig smsConfig;
    @Override
    public boolean sendSms(String phone) {
        boolean send =false;
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println("登录验证码的随机数 random="+random);
        /*String content = String.format(smsConfig.getContent(), random);
        CloseableHttpClient client = HttpClients.createDefault();
        String url = smsConfig.getUrl()+"?mobile="+phone+"&content="+content+"&appkey="+smsConfig.getAppkey();
        HttpGet get = new HttpGet(url);*/

        String host = "https://cxkjsms.market.alicloudapi.com";
        String path = "/chuangxinsms/dxjk";
        String method = "POST";
        String appcode = "1df8e3b056d54546bbb3ef71fd9c6f29";//开通服务后 买家中心-查看AppCode
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("content", "【创信】你的验证码是："+random+"，3分钟内有效！");
        querys.put("mobile", ""+phone+"");
        Map<String, String> bodys = new HashMap<String, String>();

        try {
//            CloseableHttpResponse response = client.execute(get);
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN){
                /*String text = EntityUtils.toString(response.getEntity());
                System.out.println(text);*/
                String text=    "{" +
                                "    \"ReturnStatus\": \"Success\","+
                                "        \"Message\": \"ok\","+
                                "        \"RemainPoint\": 420842,"+
                                "       \"TaskID\": 18424321,"+
                                "       \"SuccessCounts\": 1"+
                                "}";
                if (StringUtils.isNotBlank(text)){
                    JSONObject jsonObject = JSONObject.parseObject(text);
                    if ("Success".equals(jsonObject.getString("ReturnStatus"))){
                            send = true;

                            //验证码存放到redis
                            String key = RedisKey.KEY_SMS_CODE_LGOIN + phone;
                            stringRedisTemplate.boundValueOps(key).set(random,3, TimeUnit.MINUTES);

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return send;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String key = RedisKey.KEY_SMS_CODE_LGOIN + phone;
        if (stringRedisTemplate.hasKey(key)){
            String querySmsCode = stringRedisTemplate.boundValueOps(key).get();
            if (code.equals(querySmsCode)){
                return true;
            }
        }
        return false;
    }
}
