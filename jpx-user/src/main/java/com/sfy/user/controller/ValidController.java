package com.sfy.user.controller;

import com.sfy.user.form.validate.SendMobileCodeForm;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.service.validate.ValidateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 金鹏祥 on 2019/5/14.
 */
@Slf4j
@RestController
@Api(description = "验证渠道")
@RequestMapping("/valid")
public class ValidController {

    @Autowired
    ValidateService validateService;
    /**
     * 请求图形验证码
     *
     * @throws IOException
     */
    @RequestMapping(value = "imageCode", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "图形验证码")
    public void image(String requestId, HttpServletResponse response) throws IOException {

        validateService.image(requestId, response);
    }

    /**
     * 发送一个短信验证码，验证码有效时间为180秒，
     * 每申请发送一次短信验证码，就需要提供一次不同的imgCode，从而防止接口被刷
     */
    @RequestMapping(value = "mobileCode", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "发送一个短信验证码")
    public ApiResult mobile(SendMobileCodeForm request) {
        return validateService.mobile(request);
    }
}