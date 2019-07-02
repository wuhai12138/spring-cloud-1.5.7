package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("pad用户登录")
public class PadUserLoginForm {

    @ApiModelProperty("用户名")
    private String productCode;
    @ApiModelProperty("cpuId")
    private String cpuId;
    @ApiModelProperty("mac")
    private String productMac;
}
