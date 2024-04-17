package com.bjpowernode.front.service;

public interface SmsService {
    boolean sendSms(String phone);

    boolean checkSmsCode(String phone,String code);
}
