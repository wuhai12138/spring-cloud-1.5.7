package com.sfy.user.form.user;

import com.sfy.user.form.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/16.
 */
@ApiModel("用户列表检索条件")
@Data
public class GetUsersReq extends PageReq {

    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("Email")
    private String email;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("开始注册时间")
    private Long beginTime;
    @ApiModelProperty("到结束注册时间")
    private Long endTime;
}
