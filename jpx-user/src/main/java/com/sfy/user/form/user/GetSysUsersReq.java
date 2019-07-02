package com.sfy.user.form.user;

import com.sfy.user.form.PageReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Data
@ApiModel("获取系统用户")
public class GetSysUsersReq extends PageReq implements Serializable{

    private static final long serialVersionUID = 1L;

    private String sysUserName;
    private String accountNumber;
    private Integer shopId;
}
