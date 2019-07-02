package com.sfy.user.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/5/20.
 */
@Data
public class BindingProductResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="关系ID（需要得到后更新到用户信息中）")
    String relaId;
    @ApiModelProperty(value="设备id")
    String productId;
    @ApiModelProperty(value="设备名称")
    String productName;
    @ApiModelProperty(value="设备代码")
    String productCode;
    @ApiModelProperty(value="设备别名")
    String productAlias;
}
