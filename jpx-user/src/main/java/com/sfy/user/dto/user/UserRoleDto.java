package com.sfy.user.dto.user;

import com.sfy.user.dto.role.RoleDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/5/15.
 */
@ApiModel("用户角色")
@Data
public class UserRoleDto extends RoleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否已经选中，用于展示")
    private int checked;

}
