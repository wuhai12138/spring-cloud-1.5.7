package com.sfy.user.controller;

import com.alibaba.fastjson.JSON;
import com.sfy.user.entity.User;
import com.sfy.user.form.FirstChangePwdForm;
import com.sfy.user.form.user.*;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.user.UserDtoInfo;
import com.sfy.user.dto.user.UserSimpleInfo;
import com.sfy.user.service.user.AppUserService;
import com.sfy.user.service.user.UserService;
import com.sfy.user.utils.ConstantSFY;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@Slf4j
@RestController
@Api(description = "用户管理-auth")
@RequestMapping("/auth/user")
public class AuthUserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/current", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "当前登录用户信息,需要提供access token")
    public ApiResult<UserDtoInfo> current() {

        return userService.current();
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "当前登录用户信息,需要提供access token")
    public UserSimpleInfo getUserInfo() {

        UserSimpleInfo simpleInfo = userService.getUserSimpleInfo();
        log.info("getUserInfo: {}", JSON.toJSONString(simpleInfo));
        return simpleInfo;
    }

    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码请求")
    public ApiResult<User> changePwd(@RequestBody ChangePwdForm request) {

        return userService.changePwd(request);
    }

    @RequestMapping(value = "/setPwd", method = RequestMethod.POST)
    @ApiOperation(value = "首次设置密码")
    public ApiResult<User> setPwd(@RequestBody FirstChangePwdForm request) {
        return userService.setPwd(request);
    }


    @Autowired
    AppUserService appUserService;

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    @ApiOperation(value = "设置用户资料")
    public ApiResult setUserProfile(@RequestBody SetUserProfileFrom profile) {

        return appUserService.setUserProfile(profile);
    }

    @RequestMapping(value = "/setEnabled", method = RequestMethod.POST)
    @ApiOperation(value = "设置用户是否启用")
    public ApiResult setEnabled(Long userId, Integer enabled) {

        if (userId == null || userId == 0)
            return ApiResult.error(ConstantSFY.CODE_400, "userId 参数错误！");
        if (enabled == null || (enabled != 1 && enabled != 0))
            return ApiResult.error(ConstantSFY.CODE_400, "enabled 参数错误！");

        if (userService.setEnabled(userId, enabled))
            return ApiResult.success("设置成功，请刷新用户权限！");
        return ApiResult.error(ConstantSFY.CODE_400, "用户是否启用设置失败！");
    }
}