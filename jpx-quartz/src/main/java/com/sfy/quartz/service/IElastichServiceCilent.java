package com.sfy.quartz.service;

import com.sfy.quartz.entity.feign.es.DelJsonForm;
import com.sfy.quartz.entity.feign.es.EsInsertVo;
import com.sfy.utils.tools.apiResult.ApiResult;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 金鹏祥
 * @date 2019/4/8 15:54
 * @description
 */
@FeignClient(name = "sfy-elastich", path = "sfy-elastich", url = "${feign-url}", fallback = IElastichServiceCilent.FeignClientFallback.class)
//@FeignClient(name = "elastich-service-v1",url = "http://192.168.1.8:9993", fallback = IElastichServiceCilent.FeignClientFallback.class)
public interface IElastichServiceCilent {

    /**
     * 插入数据
     *
     * @param esInsertVo
     * @return
     */
    @PostMapping("/es/insertJson")
    ApiResult<String> insertJosn(@RequestBody EsInsertVo esInsertVo);

    /**
     * 插入数据
     *
     * @param esInsertVo
     * @return
     */
    @PostMapping("/es/insertModel")
    ApiResult<String> insertModel(@RequestBody EsInsertVo esInsertVo);

    @PostMapping("/es/batchInsertModel")
    ApiResult<String> batchInsertModel(@RequestBody List<EsInsertVo> esInsertVo);

    @PostMapping("/es/updateModel")
    ApiResult<String> updateModel(@RequestBody EsInsertVo esInsertVo);

    @GetMapping("/es/getData")
    ApiResult<String> getData(@RequestParam("indexName") String indexName, @RequestParam("esType") String esType, @RequestParam("id") String id);

    @DeleteMapping("/es/delete")
    ApiResult<String> delete(@RequestParam("indexName") String indexName, @RequestParam("esType") String esType, @RequestParam("id") String id);

    @DeleteMapping("/es/delIndex")
    ApiResult<String> delIndex(@RequestParam("indexName") String indexName);

    @PostMapping("/es/batchDelete")
    ApiResult<Integer> batchDelete(@ApiParam("请求体") @RequestBody DelJsonForm form);

    @GetMapping("/es/queryMatchDataAll")
    ApiResult<String> queryMatchDataAll(@RequestParam("indexName") String indexName,
                                     @RequestParam("esType") String esType,
                                     @RequestParam("sort") String sort);

    @RequestMapping(method = RequestMethod.GET, value = "/queryMatchDataIn")
    ApiResult<String> queryMatchDataIn(@RequestParam("indexName") String indexName,
                                              @RequestParam("esType") String esType,
                                              @RequestParam("field") String field,
                                              @RequestParam("value") String value,
                                              @RequestParam("sort") String sort,
                                              @RequestParam("showField") String showField);
    @Component
    class FeignClientFallback implements IElastichServiceCilent {

        @Override
        public ApiResult<String> insertJosn(EsInsertVo esInsertVo) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> insertModel(EsInsertVo esInsertVo) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> batchInsertModel(List<EsInsertVo> esInsertVo) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> updateModel(EsInsertVo esInsertVo) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> getData(String indexName, String esType, String id) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> delete(String indexName, String esType, String id) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> delIndex(String indexName) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<Integer> batchDelete(DelJsonForm form) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> queryMatchDataAll(String indexName, String esType, String sort) {
            return ApiResult.error(1006, "feign断开连接", null);
        }

        @Override
        public ApiResult<String> queryMatchDataIn(String indexName, String esType, String field, String value, String sort, String showField) {
            return ApiResult.error(1006, "feign断开连接", null);
        }
    }
}
