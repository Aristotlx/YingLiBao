package com.bjpowernode.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.api.service.UserService;
import com.bjpowernode.common.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class RealnameServiceImpl {
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;
    public boolean handleRealname(String phone,String name,String idCard){
        boolean realame = false;
        String host = "https://dfidveri.market.alicloudapi.com";
        String path = "/verify_id_name";
        String method = "POST";
        String appcode = "1df8e3b056d54546bbb3ef71fd9c6f29";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("id_number", idCard);
        bodys.put("name", name);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String resp = EntityUtils.toString(response.getEntity());
            /*String resp = "{"+
                          "\"request_id\": \"TID8bf47ab6eda64476973cc5f5b6ebf57e\","+
                          "\"status\": \"OK\","+
                          "\"state\": 1,"+
                          "}";*/
            if (StringUtils.isNotBlank(resp)){
                JSONObject respObject = JSONObject.parseObject(resp);
                if ("1".equals(respObject.getString("state"))){
                    System.out.println("查询成功, 二要素一致");

                    //更新数据库
                    realame = userService.modifyRealname(phone,name,idCard);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realame;
    }
}
