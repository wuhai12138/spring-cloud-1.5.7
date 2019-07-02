package com.sfy.demo.service;

import com.sfy.boot.transaction.MyConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "sfy-auth-v1-test", path = "sfy-auth-v1-test", url = "${feign-url}", configuration = MyConfiguration.class, fallback = IAuthTestServiceClient.FeignClientFallback.class)
public interface IAuthTestServiceClient {

    @RequestMapping(value = "/demo/save",method = RequestMethod.GET)
    int save();

    @Component
    @Slf4j
    class FeignClientFallback implements IAuthTestServiceClient {
        @Override
        public int save() {
            log.info("断=======");
            return 0;
        }
    }
}
