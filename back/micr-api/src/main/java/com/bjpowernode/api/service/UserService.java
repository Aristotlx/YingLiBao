package com.bjpowernode.api.service;

import com.bjpowernode.api.model.User;
import com.bjpowernode.api.pojo.UserAccountInfo;

public interface UserService {
    //根据手机号查询数据
    User queryByPhone(String phone);

    int userRegister(String phone, String password);

    User userLogin(String phone, String pword);

    boolean modifyRealname(String phone, String name, String idCard);
    UserAccountInfo queryUserAllInfo(Integer uid);

    User queryById(Integer uid);
}
