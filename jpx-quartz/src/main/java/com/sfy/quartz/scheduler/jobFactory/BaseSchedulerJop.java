package com.sfy.quartz.scheduler.jobFactory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author king-pc
 * @version V1.0
 * @Description: 基础任务调度job
 * @date 2018/3/24.
 */
public interface BaseSchedulerJop extends Job {
    @Override
    void execute(JobExecutionContext context)
            throws JobExecutionException;
}
