package com.sfy.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/5/15.
 */
@Data
public class TokenResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="TOKEN")
    private String accessToken;
    @ApiModelProperty(value="刷新TOKEN")
    private String refreshToken;
    @ApiModelProperty(value="TOKEN有效期（单位：秒）")
    private int expiresIn;
}
