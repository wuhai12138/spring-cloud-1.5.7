package com.sfy.user.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/16.
 */
@ApiModel("用户资料")
@Data
public class SetUserProfileFrom {

    //@ApiModelProperty("用户ID")
    //private Long userId;
    @ApiModelProperty("用户编号")
    private String userCode;
    @ApiModelProperty("用户昵称")
    private String userNickname;
    @ApiModelProperty("用户头像")
    private String userIcon;
    @ApiModelProperty("用户地址")
    private String userAddress;
}
