package com.sfy.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.sfy.user.entity.ProductInfo;
import com.sfy.user.form.pad.ProductBindResult;
import com.sfy.user.dto.user.PadUserDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * pad产品表 Mapper 接口
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-06-03
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {
    /**
     * 根据productCode查找pad
     * @param productCode
     * @return
     */
    PadUserDto findPadByProductCode(@Param("productCode") String productCode);

    /**
     * 获取绑定结果列表
     * @param productCodes
     * @return
     */
    List<ProductBindResult> listBinds(@Param("productCodes") Set<String> productCodes);

    /**
     * 根据userId查询
     * @param userIds
     * @return
     */
    List<PadUserDto> queryList(@Param("userIds") Set<Long> userIds);

    /**
     * 查询pad code
     * @param wrapper
     * @return
     */
    @Select("select product_code from t_product_info ${ew.customSqlSegment}")
    Set<String> listProductCodes(@Param(Constants.WRAPPER) Wrapper wrapper);
}
