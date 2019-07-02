package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("系统用户登录")
public class SysUserLoginForm {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("请求Id")
    private String requestId;
    @ApiModelProperty("请求Id对应的验证码")
    private String imageCode;
}
