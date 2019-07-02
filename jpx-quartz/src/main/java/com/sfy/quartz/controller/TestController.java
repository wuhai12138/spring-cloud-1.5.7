package com.sfy.quartz.controller;

import com.sfy.quartz.service.impl.TxServiceImpl;
//import com.sfy.quartz.service.DB1Service
import com.sfy.quartz.utils.ConstantSFY;
import com.sfy.utils.entity.MessageResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Created by SuperS on 2017/9/25.
 *
 * @author SuperS
 */
@Slf4j
@RestController
@Api(description = "测试服务；可不输令牌验证")
@RequestMapping(value = "/test", produces = APPLICATION_JSON_UTF8_VALUE)
public class TestController {
    @Autowired
    private TxServiceImpl txService;

    @ApiResponses(value = {
            @ApiResponse(code = ConstantSFY.CODE_401, message = ConstantSFY.MESSAGE_401),
            @ApiResponse(code = ConstantSFY.CODE_500, message = ConstantSFY.MESSAGE_500)
    })
    @ApiImplicitParams(
            @ApiImplicitParam(name="name", value = "name", dataType = "String", paramType = "query")
    )
    @ApiOperation(value="测试GFT", notes="测试TOKEN", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.GET, value = "/v1/testGet")
    @ResponseBody
    public MessageResult testGet(String name) {
//        txService.testSave();
        MessageResult messageResult = new MessageResult();
        messageResult.setMessage(name);
        return messageResult;
    }


    @ApiResponses(value = {
            @ApiResponse(code = ConstantSFY.CODE_401, message = ConstantSFY.MESSAGE_401)
    })
    @ApiOperation(value="测试POST", notes="测试POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.POST, value="/v1/testPost")
    @ResponseBody
    public MessageResult testPost(@ApiParam("请求体") @RequestBody MessageResult messageResult){
        return messageResult;
    }
}
