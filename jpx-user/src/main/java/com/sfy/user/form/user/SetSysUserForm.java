package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("设置系统用户")
public class SetSysUserForm {

    @ApiModelProperty("用户的id，唯一标识，注册时不需要")
    private Long userId;
    @ApiModelProperty("系统用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("账号")
    private String accountNumber;
    @ApiModelProperty("激活邮箱")
    private String email;
    @ApiModelProperty("头像")
    private String imageUrl;
    @ApiModelProperty("店铺ID")
    private Integer shopId;
    @ApiModelProperty("店铺名称")
    private String shopName;
}
