package com.sfy.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sfy.quartz.entity.JobAndTrigger;
import org.quartz.SchedulerException;

public interface IJobAndTriggerService {
	 IPage<JobAndTrigger> getJobAndTriggerDetails(IPage<JobAndTrigger> page) throws SchedulerException;
}
