package com.bjpowernode.ajaxdemo.controller;

import com.bjpowernode.ajaxdemo.model.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {
    @GetMapping("/user/query")
    public User queryUser(){
        User user = new User(1001, "张强", 20, "男");
        return user;
    }

    @GetMapping("/user/get")
    public User queryUser2(Integer id,String name){
        User user = new User(id, name, 20, "男");
        return user;
    }
}
