package com.sfy.user.form.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("设置角色菜单请求")
@Data
public class SetRoleMenusForm {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("菜单ID列表")
    private List<Integer> menuIds;
}
