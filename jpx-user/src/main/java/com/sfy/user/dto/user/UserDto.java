package com.sfy.user.dto.user;

import com.sfy.user.entity.Authority;
import com.sfy.user.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@Data
@ApiModel("用户数据模型")
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private Byte enabled;
    private Date createTime;
    private Date updateTime;
    @ApiModelProperty("店铺ID")
    private Integer shopId;
    private String shopName;

    @ApiModelProperty("用户角色")
    protected List<Role> roles;

    @ApiModelProperty("用户权限")
    protected List<Authority> permissions;

}
