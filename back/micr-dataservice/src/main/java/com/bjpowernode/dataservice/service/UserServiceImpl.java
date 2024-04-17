package com.bjpowernode.dataservice.service;

import com.bjpowernode.api.model.FinanceAccount;
import com.bjpowernode.api.model.User;
import com.bjpowernode.api.pojo.UserAccountInfo;
import com.bjpowernode.api.service.UserService;
import com.bjpowernode.common.util.CommonUtil;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Value("${ylb.config.password-salt}")
    private String passwordSalt;
    @Override
    public User queryByPhone(String phone) {
        User user = null;
        if (CommonUtil.checkPhone(phone)){
            user =userMapper.selectByPhone(phone);
        }
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int userRegister(String phone, String password) {
        int result = 0;
        if (CommonUtil.checkPhone(phone) && (password != null && password.length() == 32)){
            User queryUser = userMapper.selectByPhone(phone);
            if (queryUser == null){
                String newPassword = DigestUtils.md5Hex(password + passwordSalt);

                User user = new User();
                user.setPhone(phone);
                user.setLoginPassword(newPassword);
                user.setAddTime(new Date());
                userMapper.insertReturnPrimaryKey(user);

                FinanceAccount account = new FinanceAccount();
                account.setUid(user.getId());
                account.setAvailableMoney(new BigDecimal("0"));
                financeAccountMapper.insertSelective(account);

                result = 1;
            }else {
                result = 2;
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User userLogin(String phone, String password) {
        User user = null;
        String newPassword = DigestUtils.md5Hex(password + passwordSalt);
        user = userMapper.selectLogin(phone,newPassword);
        if (user != null){
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        }
        return user;
    }

    @Override
    public boolean modifyRealname(String phone, String name, String idCard) {
        int rows =0;
        if (!StringUtils.isAnyBlank(phone,name,idCard)){
            rows = userMapper.updateRealname(phone,name,idCard);
        }
        return rows>0;
    }

    @Override
    public UserAccountInfo queryUserAllInfo(Integer uid) {
        UserAccountInfo info = null;
        if (uid != null && uid > 0){
            info = userMapper.selectUserAccountById(uid);
        }
        return info;
    }

    @Override
    public User queryById(Integer uid) {
        User user = null;
        if (uid != null && uid > 0){
            user = userMapper.selectByPrimaryKey(uid);
        }
        return user;
    }
}
