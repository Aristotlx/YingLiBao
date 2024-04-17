package com.bjpowernode.front.controller;

import com.bjpowernode.common.constants.RedisKey;
import com.bjpowernode.common.enums.RCode;
import com.bjpowernode.common.util.CommonUtil;
import com.bjpowernode.front.service.SmsService;
import com.bjpowernode.front.view.RespResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "短信业务")
@RestController
@RequestMapping("/v1/sms")
public class SmsController extends BaseController{
    @Resource(name = "SmsCodeRegisterImpl")
    private SmsService smsService;

    @Resource(name = "smsCodeLoginImpl")
    private SmsService loginSmsService;

    @GetMapping("/code/register")
    public RespResult sendCodeRegister(@RequestParam String phone){
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)){
            String key = RedisKey.KEY_SMS_CODE_REG+phone;
            if (stringRedisTemplate.hasKey(key)){
                result = RespResult.ok();
                result.setRCode(RCode.SMS_CODE_CAN_USE);
            }else {
                boolean isSuccess = smsService.sendSms(phone);
                if (isSuccess){
                    result = RespResult.ok();
                }
            }

        }else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }

    @GetMapping("/code/login")
    public RespResult sendCodeLogin(@RequestParam String phone){
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)){
            String key = RedisKey.KEY_SMS_CODE_LGOIN+phone;
            if (stringRedisTemplate.hasKey(key)){
                result = RespResult.ok();
                result.setRCode(RCode.SMS_CODE_CAN_USE);
            }else {
                boolean isSuccess = loginSmsService.sendSms(phone);
                if (isSuccess){
                    result = RespResult.ok();
                }
            }

        }else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }
}
