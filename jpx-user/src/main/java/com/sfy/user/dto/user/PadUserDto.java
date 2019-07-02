package com.sfy.user.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 金鹏祥
 * @date 2019/5/29 9:47
 * @description
 */
@Data
public class PadUserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String imageUrl;
    private Byte enabled;
    private Byte passwordRevised;

    @ApiModelProperty("pad code")
    private String productCode;
    @ApiModelProperty("pad信息id")
    private Long productId;
    @ApiModelProperty("pad名称")
    private String productName;
    @ApiModelProperty("pad制造商")
    private String deviceManufacturer;
    @ApiModelProperty("app 版本")
    private String appVersion;
    @ApiModelProperty("pad硬件版本")
    private String padVersion;
    @ApiModelProperty("pad头像")
    private String padIcon;
    @ApiModelProperty("是否登陆")
    private Boolean isLogin;
}
