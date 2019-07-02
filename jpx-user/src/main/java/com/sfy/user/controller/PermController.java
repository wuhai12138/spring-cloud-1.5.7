package com.sfy.user.controller;

import com.sfy.user.entity.Authority;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.service.role.RoleService;
import com.sfy.user.service.user.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@Slf4j
@RestController
@Api(description = "系统权限管理-auth")
@RequestMapping("/auth/perm")
public class PermController {
    @Autowired
    RoleService roleService;
    @Autowired
    SysUserService sysUserService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "获取所有权限")
    public ApiResult<List<Authority>> all() {
        return ApiResult.success(roleService.getAllAuthorities());
    }

    @RequestMapping(value = "allByUserId", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "获取所有权限BY用户ID")
    public ApiResult<List<Authority>> allByUserId(Long userId) {
        return ApiResult.success(sysUserService.getUserAuths(userId));
    }


}
