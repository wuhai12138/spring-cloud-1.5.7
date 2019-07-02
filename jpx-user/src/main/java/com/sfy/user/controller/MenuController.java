package com.sfy.user.controller;

import com.sfy.user.entity.SysMenu;
import com.sfy.user.form.menu.SetMenuForm;
import com.sfy.user.form.menu.SetMenusHideForm;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.menu.MenuDto;
import com.sfy.user.service.meun.MenuService;
import com.sfy.user.utils.ConstantSFY;
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
 * Created by 金鹏祥 on 2019/5/13.
 */
@Slf4j
@RestController
@Api(description = "系统菜单管理-auth")
@RequestMapping("/auth/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "获取所有菜单信息")
    public ApiResult<List<MenuDto>> all(Byte hide) {

        List<SysMenu> sysMenus = menuService.getAll(hide);
        if (sysMenus == null || sysMenus.size() == 0) {
            return ApiResult.success(new ArrayList<>());
        }
        return ApiResult.success(menuService.builderByList(sysMenus));
    }

    @RequestMapping(value = "set", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "设置菜单信息")
    public ApiResult<SysMenu> set(@RequestBody SetMenuForm request) {
        SysMenu sysMenu = menuService.set(request);
        if (sysMenu == null) {
            return ApiResult.error(ConstantSFY.CODE_400, ConstantSFY.MESSAGE_400);
        }
        return ApiResult.success(sysMenu);
    }

    @RequestMapping(value = "batchHide", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "批量隐藏菜单信息")
    public ApiResult batchHide(@RequestBody SetMenusHideForm request) {
        try {
            if (menuService.batchHide(request)) {
                return ApiResult.success("批量设置成功！");
            }
            return ApiResult.error(ConstantSFY.CODE_400, "批量设置失败！");
        } catch (Exception ex) {
            return ApiResult.error(ConstantSFY.CODE_500, ex.getMessage());
        }
    }
}