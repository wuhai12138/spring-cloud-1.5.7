package com.sfy.quartz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sfy.quartz.mapper.IJobAndTriggerDAO;
import com.sfy.quartz.entity.JobAndTrigger;
import com.sfy.quartz.service.IJobAndTriggerService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@Service
public class JobAndTriggerImpl implements IJobAndTriggerService {

	@Autowired
	private IJobAndTriggerDAO jobAndTriggerDAO;

	@Autowired
	private Scheduler scheduler;

	@Override
	public IPage<JobAndTrigger> getJobAndTriggerDetails(IPage<JobAndTrigger> page) throws SchedulerException {
		List<JobAndTrigger> list = jobAndTriggerDAO.getJobAndTriggerDetails();
		Iterator<JobAndTrigger> listIt = list.iterator();
		while (listIt.hasNext()) {
			JobAndTrigger jobAndTrigger = listIt.next();
			Trigger.TriggerState triggerState = scheduler.getTriggerState(new TriggerKey(jobAndTrigger.getTriggerName(), jobAndTrigger.getTriggerGroup()));
			if(triggerState == Trigger.TriggerState.NORMAL){
				jobAndTrigger.setRunning(true);
			}
		}
		page.setRecords(list);
		return page;
	}

}