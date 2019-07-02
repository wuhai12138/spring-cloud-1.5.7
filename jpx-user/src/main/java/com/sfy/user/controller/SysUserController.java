package com.sfy.user.controller;


import com.sfy.user.form.user.*;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.user.SysUserLoginInfo;
import com.sfy.user.service.auth.AuthService;
import com.sfy.user.service.user.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Slf4j
@RestController
@Api(description = "系统用户管理")
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    AuthService authService;


    @RequestMapping(value = "sysUserLogin", method = RequestMethod.POST)
    @ApiOperation(value = "系统用户-登录")
    public ApiResult<SysUserLoginInfo> sysUserLogin(@RequestBody SysUserLoginForm form){
        return authService.sysUserLogin(form);
    }

}
