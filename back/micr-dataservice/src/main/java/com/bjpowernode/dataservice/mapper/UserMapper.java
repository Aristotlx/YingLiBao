package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.api.model.User;
import com.bjpowernode.api.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //统计注册人数
    int selectCountUser();

    //使用手机号查询用户
    User selectByPhone(@Param("phone") String phone);

    //添加记录，获取主键值
    int insertReturnPrimaryKey(User user);

    User selectLogin(@Param("phone") String phone, @Param("loginPassword") String newPassword);

    int updateRealname(@Param("phone") String phone, @Param("name") String name, @Param("idCard") String idCard);

    UserAccountInfo selectUserAccountById(@Param("uid") Integer uid);
}
