package com.sfy.user.dto.user;

import com.sfy.user.dto.menu.MenuDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Data
@ApiModel("系统用户登录后信息")
public class SysUserLoginInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Byte passwordRevised;
    private String sysUsername;
    private String accountNumber;

    @ApiModelProperty("登录用户 access token ")
    private String accessToken;
    @ApiModelProperty("登录用户 refresh token ")
    private String refreshToken;

    @ApiModelProperty("登录用户菜单")
    private List<MenuDto> menus;
}
