package com.sfy.quartz.scheduler;

import com.alibaba.fastjson.JSON;
import com.sfy.quartz.entity.feign.es.DelJsonForm;
import com.sfy.quartz.entity.feign.es.EsInsertVo;
import com.sfy.quartz.entity.feign.es.StringResult;
import com.sfy.quartz.entity.feign.es.SyncErrLog;
import com.sfy.quartz.scheduler.jobFactory.BaseSchedulerJop;
import com.sfy.quartz.service.IElastichServiceCilent;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.utils.tools.log.MessageSFY;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 金鹏祥
 * @date 2019/5/14 18:14
 * @description
 */
@Slf4j
@Component
public class SyncMysql2EsJob implements BaseSchedulerJop {

    @Resource
    private IElastichServiceCilent elastichService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info(MessageSFY.getMessageTitle("开始执行语义槽增量同步"));
        //list所有异常数据
        String errLogJson = elastichService.queryMatchDataAll("sync_err_log", "sync_err_log", "err_date").getData();
        if (StringUtils.isNotBlank(errLogJson) && !"null".equals(errLogJson)) {
            List<SyncErrLog> syncErrLogs = JSON.parseArray(errLogJson, SyncErrLog.class);
            if (CollectionUtils.isEmpty(syncErrLogs)) {
                log.info(MessageSFY.getMessageTitle("执行结束，没有需要同步的数据"));
                return;
            }
            //重新同步异常数据
            List<StringResult> ids = new ArrayList<>();
            for (SyncErrLog syncLog : syncErrLogs) {
                log.info(MessageSFY.getMessageTitle("获取数据"+ syncLog));
                //1. 查询已存在数据
                String data = elastichService.getData(syncLog.getErr_index(), syncLog.getErr_es_type(), syncLog.getErr_id()).getData();
                if (StringUtils.isNotBlank(data) && !"null".equals(data)) {
                    //2.如果数据存在
                    long date = (long) JSON.parseObject(data).get("date");
                    if (syncLog.getErr_date() < date) {
                        //3.已有新数据
                        log.info(MessageSFY.getMessageTitle("脏数据，无需更新"));
                        //4. 将脏数据防御待删队列
                        ids.add(new StringResult(syncLog.getId()));
                        continue;
                    }
                }
                //TODO 如果删除后恢复新增/修改操作
                ApiResult<String> result = null;
                String err_data = syncLog.getErr_data();
                switch (syncLog.getEvent_type()) {
                    case "INSERT":
                        log.info(MessageSFY.getMessageTitle("新增执行"));
                        result = elastichService.insertModel(new EsInsertVo(syncLog.getErr_index(), syncLog.getErr_es_type(), "id", JSON.parseObject(err_data, Map.class)));
                        break;
                    case "UPDATE":
                        log.info(MessageSFY.getMessageTitle("修改执行"));
                        result = elastichService.updateModel(new EsInsertVo(syncLog.getErr_index(), syncLog.getErr_es_type(), "id", JSON.parseObject(err_data, Map.class)));
                        break;
                    case "DELETE":
                        log.info(MessageSFY.getMessageTitle("删除执行"));
                        result = elastichService.delete(syncLog.getErr_index(), syncLog.getErr_es_type(), syncLog.getErr_id());
                        break;
                    default:
                        log.info(MessageSFY.getMessageTitle("暂不支持的操作：" + syncLog.getEvent_type()));
                        continue;
                }
                if ("200".equals(result.getCode())) {
                    //将同步成功的数据放入待删队列
                    ids.add(new StringResult(syncLog.getId()));
                }
            }

            //删除已同步的数据 || 脏数据
            DelJsonForm delJsonForm = new DelJsonForm();
            delJsonForm.setEsType("sync_err_log");
            delJsonForm.setIndexName("sync_err_log");
            delJsonForm.setJsonArray(JSON.parseArray(JSON.toJSONString(ids)));
            elastichService.batchDelete(delJsonForm);
            log.info(MessageSFY.getMessageTitle("清除成功同步数据，执行结束"));
        }
    }
}
