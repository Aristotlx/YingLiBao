package com.bjpowernode.dataservice.service;

import com.bjpowernode.api.model.BidInfo;
import com.bjpowernode.api.model.FinanceAccount;
import com.bjpowernode.api.model.ProductInfo;
import com.bjpowernode.api.pojo.BidInfoProduct;
import com.bjpowernode.api.service.InvestService;
import com.bjpowernode.common.constants.YLBConstant;
import com.bjpowernode.common.util.CommonUtil;
import com.bjpowernode.dataservice.mapper.BidInfoMapper;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@DubboService(interfaceClass = InvestService.class,version = "1.0")
public class InvestServiceImpl implements InvestService {

    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private FinanceAccountMapper accountMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Override
    public List<BidInfoProduct> queryBidListByProductId(Integer productId, Integer pageNo, Integer pageSize) {
        List<BidInfoProduct> bidList = new ArrayList<>();
        if(productId != null && productId >0){
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo -1) * pageSize;
            bidList = bidInfoMapper.selectByProductId(productId,offset,pageSize);
        }
        return bidList;
    }

    //投资理财产品，1是成功
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int investProduct(Integer uid, Integer productId, BigDecimal money) {
        int result = 0;//默认，参数不正确
        int rows = 0;
        if ((uid != null && uid > 0)
                && (productId != null && productId > 0)
                && (money != null && money.intValue()%100 == 0 && money.intValue()>=100)){
            ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
            if (productInfo != null && productInfo.getProductStatus() == YLBConstant.PRODUCT_STATUS_SELLING) {
                if (CommonUtil.ge(productInfo.getLeftProductMoney(), money) &&
                        CommonUtil.ge(money, productInfo.getBidMinLimit()) &&
                        CommonUtil.ge(productInfo.getBidMaxLimit(), money)) {
                    FinanceAccount account = accountMapper.selectByUidForUpdate(uid);
                    if (account != null){
                        if (CommonUtil.ge(account.getAvailableMoney(),money)){
                            rows = accountMapper.updateAvailableMoneyByInvest(uid,money);
                            if (rows < 1){
                                throw new RuntimeException("更新账号资金失败");
                            }
                            rows = productInfoMapper.updateLeftProductMoney(productId,money);
                            if (rows < 1){
                                throw new RuntimeException("更新产品剩余可投资金额失败");
                            }
                            BidInfo bidInfo = new BidInfo();
                            bidInfo.setBidMoney(money);
                            bidInfo.setBidStatus(YLBConstant.INVEST_STATUS_SUCC);
                            bidInfo.setBidTime(new Date());
                            bidInfo.setProdId(productId);
                            bidInfo.setUid(uid);
                            bidInfoMapper.insertSelective(bidInfo);

                            if(productInfo.getLeftProductMoney().compareTo(new BigDecimal("0")) == 0){
                                rows = productInfoMapper.updateSelled(productId);
                                if (rows < 1){
                                    throw new RuntimeException("更新产品满标状态失败");
                                }
                            }
                                    result = 1; //投资成功
                        }else {
                            result =3;//资金不足
                        }
                    }else {
                        result =2;//资金账号不存在
                    }
                }

            }else {
                result =4;//理财产品不存在或已满标
            }

        }
        return result;
    }
}
