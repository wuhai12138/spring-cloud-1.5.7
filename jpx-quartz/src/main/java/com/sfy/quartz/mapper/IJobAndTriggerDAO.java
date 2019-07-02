package com.sfy.quartz.mapper;


import com.sfy.quartz.entity.BaseDAO;
import com.sfy.quartz.entity.JobAndTrigger;

import java.util.List;

public interface IJobAndTriggerDAO extends BaseDAO<JobAndTrigger> {
	public List<JobAndTrigger> getJobAndTriggerDetails();
}
