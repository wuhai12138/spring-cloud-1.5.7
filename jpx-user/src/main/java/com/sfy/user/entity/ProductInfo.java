package com.sfy.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * pad产品表
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_product_info")
@ApiModel(value="ProductInfo对象", description="pad产品表")
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "pad id")
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    @ApiModelProperty(value = "账号id")
    private Long userId;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品代码")
    private String productCode;

    @ApiModelProperty(value = "mac地址")
    private String productMac;

    @ApiModelProperty(value = "产品cpuId")
    private String productCpuId;

    @ApiModelProperty(value = "生产厂商")
    private String deviceManufacturer;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "app版本")
    private String appVersion;

    @ApiModelProperty(value = "机型版本号")
    private String padVersion;

    @ApiModelProperty(value = "pad头像")
    private String padIcon;

    @ApiModelProperty(value = "是否登陆")
    private Boolean isLogin;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;


}
