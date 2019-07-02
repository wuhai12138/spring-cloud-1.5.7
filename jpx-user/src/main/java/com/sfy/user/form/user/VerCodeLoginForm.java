package com.sfy.user.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VerCodeLoginForm {

    @ApiModelProperty(value="手机号")
    private String phone;
    @ApiModelProperty(value="验证码")
    private String verCode;
    /*@ApiModelProperty(value="密码")
    private String password;*/
    @ApiModelProperty(value="注册类型(ios,android,web)")
    private String registerType;
    @ApiModelProperty(value="注册机型(华为，小米，等) 可以是原厂字符串，记录")
    private String registerMobile;
    @ApiModelProperty(value="终端版本")
    private String mobileVersion;
    @ApiModelProperty(value="程序系统版本")
    private String appVersion;
    @ApiModelProperty(value="是否请求验证码(1:是,0:否，并且直接注册操作)")
    private String isVerCode;
    @ApiModelProperty(value="设备ID")
    private String deviceId;
}