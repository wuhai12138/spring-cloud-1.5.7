package com.sfy.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/16.
 */
@Data
@ApiModel("用户首次无密码修改密码请求")
public class FirstChangePwdForm {

    @ApiModelProperty("用户名")
    private String username;
//    @ApiModelProperty("密码")
//    private String password;
    @ApiModelProperty("新密码")
    private String newPassword;

}
