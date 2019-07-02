package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/16.
 */
@Data
@ApiModel("用户注册请求")
public class RegisterForm {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("手机验证码")
    private String code;

    /*********以下非必填数据***********/
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;

    /*********************/
    @ApiModelProperty("用户编号")
    private String userCode;
    @ApiModelProperty("用户昵称")
    private String userNickname;
    @ApiModelProperty("用户头像")
    private String userIcon;
    @ApiModelProperty("用户地址")
    private String userAddress;
    @ApiModelProperty("注册类型(ios,android,web)")
    private String registerType;
    @ApiModelProperty("注册机型(华为，小米，等)")
    private String registerMobile;
    @ApiModelProperty("机型版本号")
    private String mobileVersion;
    @ApiModelProperty("App版本")
    private String appVersion;
    @ApiModelProperty("设备ID")
    private String deviceId;
    @ApiModelProperty("用户类型 pad app sys")
    private String userType;
}
