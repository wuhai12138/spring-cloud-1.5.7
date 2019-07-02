package com.sfy.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_info")
@ApiModel(value="UserInfo对象", description="用户信息")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    @ApiModelProperty(value = "用户编号")
    private String userCode;

    @ApiModelProperty(value = "用户昵称")
    private String userNickname;

    @ApiModelProperty(value = "用户地址")
    private String userAddress;

    @ApiModelProperty(value = "注册类型(ios,android,web)")
    private String registerType;

    @ApiModelProperty(value = "注册机型(华为，小米，等)")
    private String registerMobile;

    @ApiModelProperty(value = "机型版本号")
    private String mobileVersion;

    private String appVersion;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @TableLogic
    private String isDel;

    private String infoJson;

    private String userIcon;

    private String isLogin;

    private Date loginTime;

    private String deviceId;

    private Date createTime;

    private Date updateTime;


}
