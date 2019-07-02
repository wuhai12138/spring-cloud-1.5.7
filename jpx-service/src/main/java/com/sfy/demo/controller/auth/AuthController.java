package com.sfy.demo.controller.auth;

import com.sfy.utils.constant.ConstantSFY;
import com.sfy.utils.entity.MessageResult;
import com.sfy.utils.tools.log.MessageSFY;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
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
@Api(description = "令牌服务")
@RequestMapping(value = "/auth", produces = APPLICATION_JSON_UTF8_VALUE)
public class AuthController {
//    @GetMapping("/current")
//    public Principal user(Principal principal) {
//        return principal;
//    }
//
//    @GetMapping("/query")
//    @PreAuthorize("hasAnyAuthority('query')")
//    public String query() {
//        return "具有query权限";
//    }
//
//    @ApiOperation(value = "计算+", notes = "加法")
//    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
//    @ResponseBody
//    public UserResult test(){
//        UserResult userResult = new UserResult();
//        userResult.setValue("3333333");
//        System.out.println(db1Service.findUsers().size());
//        System.out.println(db1Service.findTestss().size());
//        db1Service.save();
//
//        return userResult;
//    }
//
//    @ApiOperation(value="登录", notes="登录",httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//    //@PostMapping("/generatorToken")
//    @RequestMapping(value="/postUser")
//    @ResponseBody
//    public UserResult postUser(@ApiParam("请求体") @RequestBody UserForm userForm){
//        UserResult userResult = new UserResult();
//        userResult.setValue("1111");
//
//        return userResult;
//    }

//    @ApiOperation(value="注册", notes="注册",httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiResponses(value = {
//            @ApiResponse(code = ConstantSFY.STATUS_401, message = ConstantSFY.MESSAGE_401),
//            @ApiResponse(code = ConstantSFY.STATUS_200, message = ConstantSFY.MESSAGE_200),
//            @ApiResponse(code = ConstantSFY.STATUS_regist_001_4001, message = ConstantSFY.MESSAGE_regist_001_4001)
//    })
//    @RequestMapping(value="/v1/register_001")
//    @ResponseBody
//    @Transactional
//    public BooleanResultData register_001(@ApiParam("请求体") @RequestBody UserRegisterForm userRegisterForm){
//        MessageSFY.getMessageTitle("注册");
//        return userService.register_001(userRegisterForm);
//    }
//
//    @ApiOperation(value="退出", notes="退出",httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiResponses(value = {
//            @ApiResponse(code = ConstantSFY.STATUS_401, message = ConstantSFY.MESSAGE_401),
//            @ApiResponse(code = ConstantSFY.STATUS_200, message = ConstantSFY.MESSAGE_200),
//            @ApiResponse(code = ConstantSFY.STATUS_500, message = ConstantSFY.MESSAGE_500)
//    })
//    @RequestMapping(method = RequestMethod.DELETE, value = "/v1/exit_001")
//    @ApiImplicitParams(
//            @ApiImplicitParam(name="accessToken", value = "令牌", dataType = "String", paramType = "query")
//    )
//    @ResponseBody
//    public BooleanResultData exit_001(String accessToken) {
//        MessageSFY.getMessageTitle("退出");
//        return userService.exit_001(accessToken);
//    }


    @ApiResponses(value = {
            @ApiResponse(code = ConstantSFY.CODE_401, message = ConstantSFY.MESSAGE_401),
            @ApiResponse(code = ConstantSFY.CODE_500, message = ConstantSFY.MESSAGE_500)
    })
    @ApiImplicitParams(
            @ApiImplicitParam(name="name", value = "name", dataType = "String", paramType = "query")
    )
    @ApiOperation(value="测试GFT", notes="测试TOKEN", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.GET, value = "/v1/auth/testGet")
    @ResponseBody
    public MessageResult testGet(String name) {
        log.info(MessageSFY.getMessageTitle("测试GFT"));
        MessageResult messageResult = new MessageResult();
        messageResult.setMessage(name);
//        db1Service.findDevice();
//        db1Service.findSysdict();
//        db1Service.save();
        return messageResult;
    }


    @ApiResponses(value = {
            @ApiResponse(code = ConstantSFY.CODE_401, message = ConstantSFY.MESSAGE_401)
    })
    @ApiOperation(value="测试POST", notes="测试POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.POST, value="/v1/auth/testPost")
    @ResponseBody
    public MessageResult testPost(@ApiParam("请求体") @RequestBody MessageResult messageResult){
        return messageResult;
    }
}
