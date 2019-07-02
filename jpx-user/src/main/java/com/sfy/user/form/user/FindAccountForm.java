package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/16.
 */
@Data
@ApiModel("根据手机号查找用户账号")
public class FindAccountForm {

    private String requestId;
    private String imgCode;
    private String mobile;
}
