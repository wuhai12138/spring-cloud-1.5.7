package com.sfy.quartz.scheduler;

import com.alibaba.fastjson.JSON;
import com.sfy.quartz.entity.feign.es.*;
import com.sfy.quartz.scheduler.jobFactory.BaseSchedulerJop;
import com.sfy.quartz.service.IElastichServiceCilent;
import com.sfy.utils.tools.log.MessageSFY;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 金鹏祥
 * @date 2019/5/14 18:14
 * @description
 */
@Slf4j
@Component
public class MqErrorProcessJob implements BaseSchedulerJop {

    @Resource
    private IElastichServiceCilent elastichService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        log.info(MessageSFY.getMessageTitle("【定时】开始执行mq异常消息处理"));
        //list所有异常数据
        String errLogJson = elastichService.queryMatchDataAll("mq_err_log", "mq_err_log", "mtime").getData();
        if (StringUtils.isNotBlank(errLogJson) && !"null".equals(errLogJson)) {
            List<MqErrorLog> mqErrorLogs = JSON.parseArray(errLogJson, MqErrorLog.class);
            if (CollectionUtils.isEmpty(mqErrorLogs)) {
                log.info(MessageSFY.getMessageTitle("执行结束，没有需要同步的mq数据"));
                return;
            }
            //重新同步异常数据
            for (MqErrorLog mqErrorLog : mqErrorLogs) {
                log.info(MessageSFY.getMessageTitle("重新发送"+ mqErrorLog));
                //重新发到mq
                amqpTemplate.convertAndSend(mqErrorLog.getQueue(),mqErrorLog.getErr_data());
            }

            //删除已同步的数据 || 脏数据
            List<StringResult> ids = mqErrorLogs.stream().map(e->new StringResult(e.getId())).collect(Collectors.toList());
            DelJsonForm delJsonForm = new DelJsonForm();
            delJsonForm.setEsType("mq_err_log");
            delJsonForm.setIndexName("mq_err_log");
            delJsonForm.setJsonArray(JSON.parseArray(JSON.toJSONString(ids)));
            elastichService.batchDelete(delJsonForm);
            log.info(MessageSFY.getMessageTitle("执行结束，清除日志执行"));
        }
    }
}
