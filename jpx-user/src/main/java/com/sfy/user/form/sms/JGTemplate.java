package com.sfy.user.form.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/14.
 */
@ApiModel("极光短信模版")
@Data
public class JGTemplate {

    @ApiModelProperty("模版ID")
    private int tempId;
    @ApiModelProperty("模版内容")
    private String template;
    @ApiModelProperty("模板类型，1为验证码类，2为通知类，3为营销类")
    private int type;
    @ApiModelProperty("验证码有效期，必须大于 0 且不超过 86400 ，单位为秒（当模板类型为1时必传）")
    private int ttl;
    @ApiModelProperty("备注")
    private String remark;
}
