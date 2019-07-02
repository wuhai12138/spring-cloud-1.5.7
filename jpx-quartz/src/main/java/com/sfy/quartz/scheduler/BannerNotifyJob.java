package com.sfy.quartz.scheduler;

import com.alibaba.fastjson.JSON;
import com.sfy.boot.exception.SFYException;
import com.sfy.quartz.entity.feign.es.DelJsonForm;
import com.sfy.quartz.entity.feign.es.MqErrorLog;
import com.sfy.quartz.entity.feign.es.StringResult;
import com.sfy.quartz.scheduler.jobFactory.BaseSchedulerJop;
import com.sfy.quartz.service.IElastichServiceCilent;
import com.sfy.quartz.service.IMQTTServiceClient;
import com.sfy.quartz.utils.ConstantSFY;
import com.sfy.utils.tools.log.MessageSFY;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 金鹏祥
 * @date 2019/5/14 18:14
 * @description
 */
@Slf4j
@Component
public class BannerNotifyJob implements BaseSchedulerJop {

    @Resource
    private IMQTTServiceClient mqttServiceClient;

    @Override
    public void execute(JobExecutionContext context) {
        log.info(MessageSFY.getMessageTitle("【定时】开始执行banner更新通知"));
        //1. 需要通知的设备列表
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String products;
        try {
            products = jobDataMap.get("products").toString();
        }catch (Exception e){
            throw new SFYException(ConstantSFY.CODE_500,"设备列表为空");
        }
        List<String> productList = JSON.parseArray(products, String.class);

        log.info(jobDataMap.get("products").toString());

        for (String productCode : productList) {
            mqttServiceClient.sendMqtt("banner_" + productCode, "ok");
        }
    }
}
