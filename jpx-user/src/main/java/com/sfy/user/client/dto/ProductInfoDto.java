package com.sfy.user.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 金鹏祥
 * @date 2019/5/28 11:13
 * @description
 */
@Data
public class ProductInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("pad id")
    private Long userId;
    @ApiModelProperty("pad code")
    private String productCode;
    @ApiModelProperty("pad信息id")
    private Long productId;
    @ApiModelProperty("pad名称")
    private String productName;
    @ApiModelProperty("pad制造商")
    private String deviceManufacturer;
    @ApiModelProperty("app 版本")
    private String appVersion;
    @ApiModelProperty("pad硬件版本")
    private String padVersion;
    @ApiModelProperty("pad头像")
    private String padIcon;
    @ApiModelProperty("是否登陆")
    private Boolean isLogin;
}
