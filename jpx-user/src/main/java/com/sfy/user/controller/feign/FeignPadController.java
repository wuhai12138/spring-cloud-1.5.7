package com.sfy.user.controller.feign;

import com.sfy.user.entity.User;
import com.sfy.user.form.pad.ProductBindResult;
import com.sfy.user.form.user.PadRegForm;
import com.sfy.user.form.user.PadUserLoginForm;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.user.*;
import com.sfy.user.service.auth.AuthService;
import com.sfy.user.service.user.AppUserService;
import com.sfy.user.service.user.PadUserService;
import com.sfy.user.service.user.UserService;
import com.sfy.user.utils.MessageSFY;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author 金鹏祥
 * @date 2019/5/28 11:07
 * @description
 */
@Slf4j
@RestController
@Api(description = "pad内部调用接口")
@RequestMapping("/feign")
public class FeignPadController {

    @Autowired
    private PadUserService padUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AuthService authService;

    @PostMapping("/padUser/register")
    @ApiOperation(value = "用户注册")
    public ApiResult<User> register(@RequestBody PadRegForm request) {
        return padUserService.register(request);
    }

    @PostMapping("/padUser/login")
    @ApiOperation(value = "pad用户-登录")
    public ApiResult<PadUserLoginInfo> padUserLogin(@RequestBody PadUserLoginForm form) {
        return authService.padUserLogin(form);
    }

    @GetMapping("/padUser/{code}")
    @ApiOperation(value = "根据productCode获取pad信息")
    public ApiResult<PadUserDto> findPadByCode(@PathVariable("code") String code) {
        return padUserService.getByProductCode(code);
    }

    @GetMapping("/padUser/listCodes")
    @ApiOperation(value = "获取所有pad的code")
    public ApiResult<Set<String>> listCodes() {
        return padUserService.listCodes();
    }

    @GetMapping("/appUser/bindList")
    @ApiOperation(value = "根据productCodes获取pad信息")
    public ApiResult<List<ProductBindResult>> listBinds(Set<String> productCodes) {
        return ApiResult.success(padUserService.listBinds(productCodes));
    }

    @GetMapping("/appUser/findAppUserByNames")
    @ApiOperation(value = "根据用户手机号查找用户账号")
    public ApiResult<Set<AppUserDto>> findAppUserByNames(@RequestParam("userNames") Set<String> userNames) {
        return appUserService.findAppUserInfoByNames(userNames);
    }

    @GetMapping("/appUser/findPadUserByCodes")
    @ApiOperation(value = "根据pad code查找pad账号")
    public ApiResult<Set<PadUserDto>> findPadUserByCodes(@RequestParam("productCodes") Set<String> productCodes) {
        return padUserService.findPadUserByCodes(productCodes);
    }

    @GetMapping("/auth/current")
    @ApiOperation(value = "当前登录用户信息,需要提供access token")
    public ApiResult current() {
        log.info(MessageSFY.getMessageTitle("当前登录用户信息"));
        return userService.current();
    }

}
