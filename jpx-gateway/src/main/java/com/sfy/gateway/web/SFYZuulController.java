package com.sfy.gateway.web;

//import com.sfy.gateway.event.RefreshRouteService;
import com.sfy.gateway.result.ApiResult;
import com.sfy.gateway.utils.ConstantSFY;
import com.sfy.gateway.utils.MessageSFY;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class SFYZuulController {

//    @Autowired
//    RefreshRouteService refreshRouteService;

//    @RequestMapping("/refreshRoute")
//    public String refreshRoute(){
//        refreshRouteService.refreshRoute();
//        return "refreshRoute";
//    }

    @Autowired
    ZuulHandlerMapping zuulHandlerMapping;

    @RequestMapping("/watchNowRoute")
    public String watchNowRoute(){
        //可以用debug模式看里面具体是什么
        Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
        return "watchNowRoute";
    }

    @RequestMapping(value = "/errorLogin")
    @ResponseBody
    public ApiResult errorLogin(int code){
        log.info(MessageSFY.getMessageTitle(code + ""));
        if(ConstantSFY.CODE_4023 == code){
            return ApiResult.error(ConstantSFY.CODE_4023, ConstantSFY.MESSAGE_4023, null);
        }if(ConstantSFY.CODE_403 == code){
            return ApiResult.error(ConstantSFY.CODE_403, ConstantSFY.MESSAGE_403, null);
        }if(ConstantSFY.CODE_4012 == code){
            return ApiResult.error(ConstantSFY.CODE_4012, ConstantSFY.MESSAGE_4012, null);
        }if(ConstantSFY.CODE_4013 == code){
            return ApiResult.error(ConstantSFY.CODE_4013, ConstantSFY.MESSAGE_4013, null);
        }if(ConstantSFY.CODE_4008 == code){
            return ApiResult.error(ConstantSFY.CODE_4008, ConstantSFY.MESSAGE_4008, null);
        }else{
            return ApiResult.error(ConstantSFY.CODE_4011, ConstantSFY.MESSAGE_4011, null);
        }
    }

}
