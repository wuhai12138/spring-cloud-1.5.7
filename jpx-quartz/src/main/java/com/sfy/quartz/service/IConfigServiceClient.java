package com.sfy.quartz.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * @author 金鹏祥
 * @date 2019/5/15 10:26
 * @description
 */
@FeignClient(name = "sfy-config", path = "sfy-config", url = "${feign-url}", fallback = IConfigServiceClient.FeignClientFallback.class)
//@FeignClient(name = "config-service-v1", path = "/", url = "http://192.168.1.8:5020", configuration = MyConfiguration.class ,fallback = IConfigServiceClient.FeignClientFallback.class)
public interface IConfigServiceClient {
    @GetMapping("/voice/list/all")
    Map<String,List> allList();

    @GetMapping("/save")
    int save();

    @Component
    class FeignClientFallback implements IConfigServiceClient{

        @Override
        public Map<String, List> allList() {
            return null;
        }

        @Override
        public int save() {
            return 0;
        }

    }
}
