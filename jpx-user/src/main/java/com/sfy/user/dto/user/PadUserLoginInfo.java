package com.sfy.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Data
@ApiModel("pad用户登录后信息")
public class PadUserLoginInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Byte passwordRevised;
    private String productCode;

    @ApiModelProperty("登录用户 access token ")
    private String accessToken;
    @ApiModelProperty("登录用户 refresh token ")
    private String refreshToken;

}
