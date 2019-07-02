package com.sfy.user.client;

import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.utils.ConstantSFY;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * Created by 金鹏祥 on 2019/5/20.
 */
@FeignClient(name = "sfy-product", path = "sfy-product", url = "${feign-url}", fallback = IProductServiceClient.FeignClientFallback.class)
public interface IProductServiceClient {


    @GetMapping("/feign/listBindProductCodes")
    ApiResult<Set<String>> listBindProductCodes(@RequestParam("productCode") String productCode);

    @GetMapping("/feign/initProduct")
    ApiResult initProduct(@RequestParam("productCode") String productCode);

    @Component
    class FeignClientFallback implements IProductServiceClient {

        @Override
        public ApiResult<Set<String>> listBindProductCodes(String productCode) {
            return ApiResult.error(ConstantSFY.CODE_1006, ConstantSFY.MESSAGE_10006, null);
        }

        @Override
        public ApiResult initProduct(String productCode) {
            return ApiResult.error(ConstantSFY.CODE_1006, ConstantSFY.MESSAGE_10006, null);
        }
    }
}
