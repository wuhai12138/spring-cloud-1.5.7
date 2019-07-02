package com.sfy.user.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/5/15.
 */
@Data
public class LoginForm implements Serializable {
    @ApiModelProperty(value="用户名")
    private String userName;
    @ApiModelProperty(value="密码")
    private String password;
}
