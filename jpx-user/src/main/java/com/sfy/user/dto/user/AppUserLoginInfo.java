package com.sfy.user.dto.user;

import com.sfy.user.form.pad.ProductBindResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/20.
 */
@Data
public class AppUserLoginInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Byte passwordRevised;
    private String username;

    @ApiModelProperty("登录用户 access token ")
    private String accessToken;
    @ApiModelProperty("登录用户 refresh token ")
    private String refreshToken;

    @ApiModelProperty(value="关联设备ID")
    private List<ProductBindResult> deviceList;

    private String userNickname;
    private String userIcon;
    private String userAddress;
    private String registerType;
    private String registerMobile;
    private String deviceId;
    private Date createTime;
    private Date updateTime;

}
