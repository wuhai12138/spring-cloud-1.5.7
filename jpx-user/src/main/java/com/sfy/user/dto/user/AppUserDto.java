package com.sfy.user.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Data
@ApiModel("app 用户信息")
public class AppUserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    //private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String imageUrl;
    private Byte enabled;
    private Byte passwordRevised;

    /********** APP User Info **************/
    private String userCode;
    private String userNickname;
    private String userIcon;
    private String userAddress;
    private String registerType;
    private String registerMobile;
    private String mobileVersion;
    private String appVersion;
    private Date registerTime;
    private String isLogin;
    private Date loginTime;
    private String deviceId;
    private Date createTime;
    private Date updateTime;
}