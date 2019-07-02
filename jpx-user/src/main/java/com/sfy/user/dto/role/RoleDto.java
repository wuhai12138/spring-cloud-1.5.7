package com.sfy.user.dto.role;

import com.sfy.user.dto.menu.MenuDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("角色数据模型")
@Data
public class RoleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String value;
    private Date createTime;
    private Date updateTime;

    @ApiModelProperty("角色权限")
    private List<AuthorityDto> authorities;

    @ApiModelProperty("角色菜单")
    private List<MenuDto> menus;
}
