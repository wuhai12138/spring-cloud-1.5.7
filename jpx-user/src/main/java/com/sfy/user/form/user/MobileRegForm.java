package com.sfy.user.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/17.
 * 用户简单注册
 */
@Data
public class MobileRegForm {

    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("手机验证码")
    private String code;

    @ApiModelProperty(value="用户类型 ios/android/web")
    private String registerType;
}
