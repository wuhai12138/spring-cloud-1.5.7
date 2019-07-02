package com.sfy.quartz.scheduler;

import com.sfy.quartz.scheduler.jobFactory.BaseSchedulerJop;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author king-pc
 * @version V1.0
 * @Description: 测试job
 * @date 2018/3/24.
 */
@Slf4j
@Component
public class TestJob implements BaseSchedulerJop {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      log.info("aaaaaaaaaaa");
    }
}