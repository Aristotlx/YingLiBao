package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.api.model.BidInfo;
import com.bjpowernode.api.pojo.BidInfoProduct;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {


    List<BidInfoProduct> selectByProductId(@Param("productId") Integer productId, @Param("offset") int offset, @Param("rows") Integer rows);

    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    BigDecimal selectSumBidMoney();

    List<BidInfo> selectByProId(@Param("productId") Integer productId);
}
