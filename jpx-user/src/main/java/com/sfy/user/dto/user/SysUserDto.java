package com.sfy.user.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Data
@ApiModel("系统用户信息")
public class SysUserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    //private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private Byte enabled;
    private Date createTime;
    private Date updateTime;

    private String isLogin;
    private Date loginTime;
    private Integer shopId;
    private String shopName;

    private String sysUsername;
    private String accountNumber;
}
