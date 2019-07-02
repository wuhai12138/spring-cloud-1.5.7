package com.sfy.user.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sfy.user.entity.Role;
import com.sfy.user.form.role.SetRoleAuthoritiesForm;
import com.sfy.user.form.role.SetRoleMenusForm;
import com.sfy.user.form.role.GetRolesPageReq;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.role.RoleDto;
import com.sfy.user.service.role.RoleService;
import com.sfy.user.utils.ConstantSFY;
import org.springframework.util.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(description = "系统角色管理-auth")
@RequestMapping("/auth/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 获取角色
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "获取角色")
    public ApiResult<Role> get(Long id) {

        Role role = roleService.get(id);
        if (role == null){
            return ApiResult.error(ConstantSFY.CODE_204, ConstantSFY.MESSAGE_204);
        }
        return ApiResult.success(role);
    }

    /**
     * 获取角色 & 菜单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getRoleMenus", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "获取角色 菜单")
    public ApiResult<RoleDto> getRoleMenus(Long id) {

        RoleDto role = roleService.getRoleDto(id, false, true);
        if (role == null){
            return ApiResult.error(ConstantSFY.CODE_204, ConstantSFY.MESSAGE_204);
        }
        return ApiResult.success(role);
    }

    /**
     * 获取角色 & 权限
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getRolePerms", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "获取角色 权限")
    public ApiResult<RoleDto> getRolePerms(Long id) {

        RoleDto role = roleService.getRoleDto(id, true, false);
        if (role == null){
            return ApiResult.error(ConstantSFY.CODE_204, ConstantSFY.MESSAGE_204);
        }
        return ApiResult.success(role);
    }


    /**
     * 获取角色列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "获取角色列表")
    public ApiResult<Page<Role>> getPageList(GetRolesPageReq request) {

        log.info("RoleController.getPageList request = {}", JSON.toJSONString(request));
        return roleService.getPageList(request);
    }

    /**
     * 设置角色 & 菜单
     * @param request
     * @return
     */
    @RequestMapping(value = "setRoleMenus", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "设置角色 菜单")
    public ApiResult setRoleMenus(@RequestBody SetRoleMenusForm request) {

        try {
            Assert.notNull(request.getRoleId(), "roleId 不能为空");
            Assert.notEmpty(request.getMenuIds(), "menuIds 不能为空");
            if (roleService.setRoleMenus(request.getRoleId(), request.getMenuIds())) {
                return ApiResult.success("设置成功！");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_500, "设置失败！");
    }

    /**
     * 设置角色 & 权限
     * @param request
     * @return
     */
    @RequestMapping(value = "setRolePerms", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "设置角色 权限")
    public ApiResult setRolePerms(@RequestBody SetRoleAuthoritiesForm request) {

        try {
            Assert.notNull(request.getRoleId(), "roleId 不能为空");
            Assert.notEmpty(request.getAuthIds(), "authIds 不能为空");
            if (roleService.setRoleAuthorities(request.getRoleId(), request.getAuthIds())) {
                return ApiResult.success("设置成功！");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_500, "设置失败！");
    }

    /**
     * 清空角色 & 菜单
     * @param roleId
     * @return
     */
    @RequestMapping(value = "clearRoleMenus", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "清空角色 菜单")
    public ApiResult clearRoleMenus(Long roleId) {

        try {
            Assert.notNull(roleId, "roleId 不能为空");

            if (roleService.setRoleMenus(roleId, null)) {
                return ApiResult.success("操作成功！");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_500, "操作失败！");
    }

    /**
     * 清空角色 & 权限
     * @param roleId
     * @return
     */
    @RequestMapping(value = "clearRolePerms", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "清空角色 权限")
    public ApiResult clearRolePerms(Long roleId) {

        try {
            Assert.notNull(roleId, "roleId 不能为空");

            if (roleService.setRoleAuthorities(roleId, null)) {
                return ApiResult.success("操作成功！");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_500, "操作失败！");
    }
}
