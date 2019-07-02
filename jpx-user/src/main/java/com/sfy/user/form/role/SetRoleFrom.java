package com.sfy.user.form.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("设置角色请求")
@Data
public class SetRoleFrom {

    private Long id;
    private String name;
    private String value;

}
