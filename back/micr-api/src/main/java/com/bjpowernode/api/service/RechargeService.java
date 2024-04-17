package com.bjpowernode.api.service;

import com.bjpowernode.api.model.RechargeRecord;

import java.util.List;

public interface RechargeService {

    List<RechargeRecord> queryByUid(Integer uid,Integer pageNo,Integer pageSize);

    int addRechargeRecord(RechargeRecord record);

    int handleKQNotify(String orderId, String payAmount, String payResult);
}
