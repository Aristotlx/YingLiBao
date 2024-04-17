package com.bjpowernode.front.controller;

import com.bjpowernode.api.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

public class BaseController {

    //声明公共方法，属性
    @Resource
    protected StringRedisTemplate stringRedisTemplate;
    @DubboReference(interfaceClass = PlatBaseInfoService.class,version = "1.0")
    protected PlatBaseInfoService platBaseInfoService;

    @DubboReference(interfaceClass = ProductService.class,version = "1.0")
    protected ProductService productService;

    @DubboReference(interfaceClass = InvestService.class,version = "1.0")
    protected InvestService investService;

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;

    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    protected RechargeService rechargeService;
}
