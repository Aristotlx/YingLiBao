package com.bjpowernode.api.service;

import com.bjpowernode.api.pojo.BidInfoProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface InvestService {
    List<BidInfoProduct> queryBidListByProductId(Integer productId,Integer pageNo,Integer pageSize);

    //投资理财产品，1是成功
    int investProduct(Integer uid, Integer productId, BigDecimal money);
}
