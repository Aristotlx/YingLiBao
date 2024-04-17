package com.bjpowernode.dataservice.service;

import com.bjpowernode.api.model.RechargeRecord;
import com.bjpowernode.api.service.RechargeService;
import com.bjpowernode.common.constants.YLBConstant;
import com.bjpowernode.common.util.CommonUtil;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {
    @Resource
    private RechargeRecordMapper rechargeMapper;
    @Resource
    private FinanceAccountMapper accountMapper;
    @Override
    public List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<RechargeRecord> records = new ArrayList<>();
        if (uid != null && uid > 0){
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo -1)*pageSize;
            records = rechargeMapper.selectByUid(uid,offset,pageSize);
        }
        return records;
    }

    @Override
    public int addRechargeRecord(RechargeRecord record) {
        return rechargeMapper.insertSelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int handleKQNotify(String orderId, String payAmount, String payResult) {
        int result = 0;//订单不存在
        int rows = 0;
        //查询订单
        RechargeRecord record = rechargeMapper.selectByRechargeNo(orderId);
        if (record != null){
            if (record != null){
                if (record.getRechargeStatus() == YLBConstant.RECHARGE_STATUS_PROCESSING){
                    String fen = record.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                    if (fen.equals(payAmount)){
                        //充值成功
                        if ("10".equals(payResult)){
                            rows = accountMapper.updateAvailableMoneyByRecharge(record.getUid(),record.getRechargeMoney());
                            if (rows < 1 ){
                                throw new RuntimeException("更新充值资金账号失败");
                            }
                                rows = rechargeMapper.updateStatus(record.getId(),YLBConstant.RECHARGE_STATUS_SUCCESS);
                            if (rows < 1 ){
                                throw new RuntimeException("更新充值状态失败");
                            }
                            result = 1;//成功
                        }else {
                            //充值失败
                            rows = rechargeMapper.updateStatus(record.getId(),YLBConstant.RECHARGE_STATUS_FAIL);
                            if (rows < 1 ){
                                throw new RuntimeException("更新充值状态失败");
                            }
                            result = 2;//失败
                        }
                    }else {
                        result = 4;//金额不一样
                    }
                }else {
                    result = 3;//订单已经处理过
                }
            }
        }
        return result;
    }
}
