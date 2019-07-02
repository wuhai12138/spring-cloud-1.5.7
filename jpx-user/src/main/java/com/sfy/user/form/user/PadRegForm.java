package com.sfy.user.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 金鹏祥
 * @date 2019/5/28 9:30
 * @description
 */
@Data
public class PadRegForm {
    @ApiModelProperty("小方元设备码")
    private String productCode;
    @ApiModelProperty("小方元名称")
    private String productName;
    @ApiModelProperty("小方元mac地址")
    private String productMac;
    @ApiModelProperty("小方元mcpuId")
    private String productCpuId;
}
