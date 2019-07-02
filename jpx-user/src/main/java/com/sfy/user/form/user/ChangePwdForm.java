package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/16.
 */
@Data
@ApiModel("用户修改密码请求")
public class ChangePwdForm {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("新密码")
    private String newPassword;

}
