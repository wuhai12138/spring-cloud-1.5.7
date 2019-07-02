package com.sfy.user.form.validate;

import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/14.
 */
@Data
public class SendMobileCodeForm {

    private String requestId;
    private String imgCode;
    private String mobile;
}
