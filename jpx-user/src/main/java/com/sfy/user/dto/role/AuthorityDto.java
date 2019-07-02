package com.sfy.user.dto.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("角色权限数据模型")
@Data
public class AuthorityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String value;
    private int checked;
    private Date createTime;
    private Date updateTime;

}
