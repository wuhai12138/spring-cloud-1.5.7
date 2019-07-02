package com.sfy.user.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 金鹏祥 on 2019/6/12.
 */
@Data
@ApiModel("用户数据模型")
public class UserSimpleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private Byte enabled;
    private Integer shopId;
    private String shopName;
    private Date createTime;
    private Date updateTime;
}
