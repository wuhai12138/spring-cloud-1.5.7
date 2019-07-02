package com.sfy.user.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/15.
 */

@Data
public class LogoutForm {

    @ApiModelProperty(value="用户名")
    private String userName;
    @ApiModelProperty(value="access_token")
    private String accessToken;
}
