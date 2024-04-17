package com.bjpowernode.dataservice.service;

import com.bjpowernode.api.model.BidInfo;
import com.bjpowernode.api.model.IncomeRecord;
import com.bjpowernode.api.model.ProductInfo;
import com.bjpowernode.api.service.IncomeService;
import com.bjpowernode.common.constants.YLBConstant;
import com.bjpowernode.dataservice.mapper.BidInfoMapper;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.IncomeRecordMapper;
import com.bjpowernode.dataservice.mapper.ProductInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private IncomeRecordMapper incomeMapper;
    @Resource
    private FinanceAccountMapper accountMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomePlan() {
        Date currentDate = new Date();
        Date beginTime = DateUtils.truncate(DateUtils.addDays(currentDate, -1), Calendar.DATE);
        Date endTime = DateUtils.truncate(currentDate, Calendar.DATE);
        List<ProductInfo> productInfoList = productInfoMapper.selectFullTimeProducts(beginTime,endTime);

        BigDecimal dayRate = null;
        BigDecimal cycle = null;
        BigDecimal income = null;
        Date incomeDate = null; //到期时间
        int rows = 0;
        for (ProductInfo product : productInfoList){
            dayRate = product.getRate().divide(new BigDecimal("360"), 10, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

            if (product.getProductType() == YLBConstant.PRODUCT_TYPE_XINSHOUBAO){
                cycle = new BigDecimal(product.getCycle());
                incomeDate = DateUtils.addDays(product.getProductFullTime(),(1+product.getCycle()));
            }else {
                cycle = new BigDecimal(product.getCycle() * 30);
                incomeDate = DateUtils.addDays(product.getProductFullTime(),(1+product.getCycle() * 30));
            }

             List<BidInfo> bidList = bidInfoMapper.selectByProId(product.getId());
             for (BidInfo bid : bidList){
                 income = bid.getBidMoney().multiply(cycle).multiply(dayRate);

                 IncomeRecord incomeRecord = new IncomeRecord();
                 incomeRecord.setBidId(bid.getId());
                 incomeRecord.setBidMoney(bid.getBidMoney());
                 incomeRecord.setIncomeDate(incomeDate);
                 incomeRecord.setIncomeStatus(YLBConstant.INCOME_STATUS_PLAN);
                 incomeRecord.setProdId(product.getId());
                 incomeRecord.setIncomeMoney(income);
                 incomeRecord.setUid(bid.getUid());
                 incomeMapper.insertSelective(incomeRecord);
             }

             rows = productInfoMapper.updateStatus(product.getId(), YLBConstant.PRODUCT_STATUS_PLAN);
             if (rows < 1){
                 throw new RuntimeException("生成收益计划，更新产品状态为2失败");
             }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomeBack() {
        Date expiredDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DATE);
        System.out.println("expiredDate ===== " + expiredDate);
        List<IncomeRecord> incomeRecordList= incomeMapper.selectExpiredIncome(expiredDate);

        int rows = 0;
        for (IncomeRecord ir : incomeRecordList){
            rows = accountMapper.updateAvailableMoneyByIncomeBack(ir.getUid(),ir.getBidMoney(),ir.getIncomeMoney());
            if (rows < 1){
                throw new RuntimeException("收益返还，更新账号资金失败");
            }
            ir.setIncomeStatus(YLBConstant.INCOME_STATUS_BACK);
                rows = incomeMapper.updateByPrimaryKey(ir);
                if (rows < 1){
                    throw new RuntimeException("收益返还，更新收益记录的状态失败");
                }
        }
    }
}
