package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/16.
 */
@Data
@ApiModel("密码重置请求")
public class ResetPwdForm {

    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("验证码")
    private String code;

}
