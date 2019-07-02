package com.sfy.user.controller;

import com.sfy.user.entity.User;
import com.sfy.user.form.user.*;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.user.AppUserLoginInfo;
import com.sfy.user.service.auth.AuthService;
import com.sfy.user.service.user.UserService;
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
@Api(description = "用户管理")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户登录")
    public ApiResult<AppUserLoginInfo> login(@RequestBody LoginForm request) {
        return authService.login(request);
    }



    @RequestMapping(value = "verCodeLogin", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "验证码登录")
    public ApiResult<AppUserLoginInfo> verCodeLogin(@RequestBody VerCodeLoginForm loginForm) {
        try {
            return authService.verCodeLogin(loginForm);
        } catch (Exception ex) {
            return ApiResult.error(401, "验证码登录失败！");
        }
    }

    @RequestMapping(value = "registerByMobile", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    public ApiResult<User> registerByMobile(@RequestBody MobileRegForm request) {

        return userService.registerByMobile(request);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    public ApiResult<User> register(@RequestBody RegisterForm request) {

        return userService.register(request);
    }

    @RequestMapping(value = "findAccount", method = RequestMethod.POST)
    @ApiOperation(value = "根据用户手机号查找用户账号")
    public ApiResult findAccount(@RequestBody FindAccountForm request) {

        return userService.findAccount(request);
    }

    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public ApiResult<User> resetPwd(@RequestBody ResetPwdForm request) {

        return userService.resetPwd(request);
    }

    @RequestMapping(value = "storeAccessToken", method = RequestMethod.POST)
    @ApiOperation(value = "重置用户权限，重新；拉取用户权限到oauth")
    public void storeAccessToken(String userName) {
        authService.storeAccessToken(userName);
    }


}