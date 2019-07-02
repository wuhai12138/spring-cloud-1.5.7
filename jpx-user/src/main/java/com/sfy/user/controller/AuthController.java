package com.sfy.user.controller;

import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.TokenResult;
import com.sfy.user.service.auth.AuthService;
import com.sfy.user.utils.ConstantSFY;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 金鹏祥 on 2019/5/15.
 */
@Slf4j
@RestController
@Api(description = "auth 操作")
//@RequestMapping("/auth/token")
public class AuthController {

    @Autowired
    AuthService authService;

    @RequestMapping(value = "/auth/token/getToken", method = RequestMethod.GET)
    @ApiOperation(value = "获取Token")
    public ApiResult<TokenResult> getToken() {
        return authService.getToken();
    }


    @RequestMapping(value = "/token/refreshToken", method = RequestMethod.GET)
    @ApiOperation(value = "如果Token过期， 刷新Token")
    public ApiResult<TokenResult> refreshToken(String refreshToken, String accessToken) {
        return authService.refreshToken(refreshToken,accessToken);
    }


    @RequestMapping(value = "/auth/token/removeToken", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除当前Token，相当于退出登录")
    public ApiResult removeToken() {
        if (authService.removeToken()){
            return ApiResult.success("处理成功！");
        }
        return ApiResult.error(ConstantSFY.CODE_401, ConstantSFY.MESSAGE_401);
    }

}
