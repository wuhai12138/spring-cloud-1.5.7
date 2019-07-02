package com.sfy.user.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sfy.boot.exception.SFYException;
import com.sfy.user.entity.SysUserInfo;
import com.sfy.user.form.user.*;
import com.sfy.user.dto.PageBean;
import com.sfy.user.dto.menu.MenuDto;
import com.sfy.user.dto.role.AuthorityDto;
import com.sfy.user.dto.user.*;
import com.sfy.user.service.auth.AuthService;
import com.sfy.user.service.user.AppUserService;
import com.sfy.user.service.user.SysUserService;
import com.sfy.user.utils.ConstantSFY;
import com.sfy.utils.tools.apiResult.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Slf4j
@RestController
@Api(description = "系统用户管理-auth")
@RequestMapping("/auth/sysUser")
public class AuthSysUserController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    AuthService authService;


    @RequestMapping(value = "/getSysUserList", method = RequestMethod.GET)
    @ApiOperation(value = "系统用户-列表")
    public ApiResult<Page<SysUserDto>> getPageList(GetSysUsersReq request){

        log.info("getSysUserList request:{}", JSON.toJSONString(request));

        return sysUserService.getPageList(request);
    }

    @RequestMapping(value = "/getSysUser", method = RequestMethod.GET)
    @ApiOperation(value = "系统用户信息")
    public ApiResult<SysUserDto> getSysUser(Long userId) {

        if (userId == null || userId == 0)
            return ApiResult.error(ConstantSFY.CODE_400, "用户Id不能为空！");
        SysUserDto sysUserDto = sysUserService.getSysUser(userId);
        if (sysUserDto == null)
            return ApiResult.error(ConstantSFY.CODE_400, "未找到用户信息");
        return ApiResult.success(sysUserDto);
    }

    @RequestMapping(value = "/setSysUser", method = RequestMethod.POST)
    @ApiOperation(value = "设置系统用户信息")
    public ApiResult<SysUserInfo> setSysUser(@RequestBody SetSysUserForm form) {
        try {
            return sysUserService.setSysUser(form);
        } catch (SFYException ex) {
            return ApiResult.error(ex.getCode(), ex.getMsg());
        } catch (Exception ex) {
            return ApiResult.error(ConstantSFY.CODE_500, ex.getMessage());
        }
    }

    @RequestMapping(value = "/addUserRole", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户角色")
    public ApiResult addUserRole(Long userId, Long roleId) {
        if (sysUserService.addUserRole(userId, roleId)){
            return ApiResult.success("添加成功！");
        }
        return ApiResult.error(ConstantSFY.CODE_400, ConstantSFY.MESSAGE_400);
    }

    @RequestMapping(value = "/setUserRoles", method = RequestMethod.POST)
    @ApiOperation(value = "设置用户角色")
    public ApiResult setUserRoles(@RequestBody SetUserRolesForm form) {
        if (sysUserService.setUserRoles(form.getUserId(), form.getRoleIds())){
            return ApiResult.success("操作成功！");
        }
        return ApiResult.error(ConstantSFY.CODE_400, ConstantSFY.MESSAGE_400);
    }

    @RequestMapping(value = "/getUserSelectedRoles", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户选中的角色信息")
    public ApiResult<List<UserRoleDto>> getUserSelectedRoles(Long userId) {
        return ApiResult.success(sysUserService.getUserSelectedRoles(userId));
    }

    @RequestMapping(value = "/getUserSelectedMenus", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户选中的菜单信息")
    public ApiResult<List<MenuDto>> getUserSelectedMenus(Long userId) {
        return ApiResult.success(sysUserService.getUserSelectedMenus(userId));
    }

    @RequestMapping(value = "/getUserMenus", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户菜单")
    public ApiResult<List<MenuDto>> getUserMenus(Long userId) {
        return ApiResult.success(sysUserService.getUserMenus(userId));
    }

    @RequestMapping(value = "/getUserSelectedAuths", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户选中的权限信息")
    public ApiResult<List<AuthorityDto>> getUserSelectedAuths(Long userId) {
        return ApiResult.success(sysUserService.getUserSelectedAuths(userId));
    }

    @Autowired
    AppUserService appUserService;

    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    @ApiOperation(value = "用户列表")
    public ApiResult<PageBean<AppUserDto>> getPageList(GetAppUsersReq request){
        return appUserService.getPageList(request);
    }

    @RequestMapping(value = "/getTests", method = RequestMethod.GET)
    @ApiOperation(value = "test用户列表")
    public ApiResult<Page<TestUserInfo>> getTests(){
        List<TestUserInfo> list = new ArrayList<>();
        TestUserInfo userInfo = new TestUserInfo();
        userInfo.setId(12);
        userInfo.setName("test user");
        list.add(userInfo);
        Page<TestUserInfo> page = new Page<>();
        page.setRecords(list);
        return ApiResult.success(page);
    }
}
