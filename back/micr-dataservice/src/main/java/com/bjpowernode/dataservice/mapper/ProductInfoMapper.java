package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);

    BigDecimal selectAvgRate();

    List<ProductInfo> selectByTypeLimit(@Param("ptype") Integer ptype,
                                        @Param("offset") Integer offset,
                                        @Param("rows") Integer rows);

    Integer selectCountByType(@Param("ptype") Integer pType);

    int updateLeftProductMoney(@Param("id") Integer productId, @Param("money") BigDecimal money);

    int updateSelled(@Param("id") Integer productId);

    List<ProductInfo> selectFullTimeProducts(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updateStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);
}
