package com.sfy.user.form.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("设置角色权限请求")
@Data
public class SetRoleAuthoritiesForm {
    @ApiModelProperty("角色ID")
    private Long roleId;
    @ApiModelProperty("权限ID集合")
    private List<Long> authIds;
}
