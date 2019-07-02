package com.sfy.quartz.service;

import com.sfy.utils.constant.ConstantSFY;
import com.sfy.utils.tools.apiResult.ApiResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sfy-mqtt", path = "sfy-mqtt", url = "${feign-url}",fallback = IMQTTServiceClient.FeignClientFallback.class)
public interface IMQTTServiceClient {

    /**
     * @Description 查询字典实体
     * @Param [code]
     * @Author jpx
     * @Version  1.0
     * @Return com.sfy.product.dto.ApiResult<com.sfy.product.entity.config.SysDict>
     * @Exception
     * @Date 2018/12/28
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sendMQTT")
    ApiResult<String> sendMqtt(@RequestParam("topic") String topic, @RequestParam("sendData") String sendData);

    @Component
    class FeignClientFallback implements IMQTTServiceClient {
        @Override
        public ApiResult<String> sendMqtt(String topic, String sendData) {
            return ApiResult.error(1006, "feign断开链接", null);
        }
    }
}
