package com.sfy.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sfy.quartz.entity.JobAndTrigger;
import com.sfy.quartz.form.JobForm;
import com.sfy.quartz.scheduler.jobFactory.JobUtil;
import com.sfy.quartz.service.IJobAndTriggerService;
import com.sfy.quartz.utils.ConstantSFY;
import com.sfy.utils.tools.apiResult.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Api(description = "任务调度接口")
@RestController
@RequestMapping(value="/job")
@Slf4j
public class JobController {
	@Autowired
	private IJobAndTriggerService jobAndTriggerService;

	@ApiOperation(value = "增加任务调度", notes = "增加任务调度接口")
	@PostMapping(value="/addjob")
	public ApiResult<String> addjob(@RequestParam(value="jobClassName")String jobClassName,
									@RequestParam(value="jobGroupName")String jobGroupName,
									@RequestParam(value="cronExpression")String cronExpression){
		try {
			JobUtil.getInstance().addJob(jobClassName, jobGroupName, cronExpression);
		} catch (Exception e) {
			log.error("创建任务调度失败",e);
			return ApiResult.error(ConstantSFY.CODE_400,"创建任务调度失败", null);
		}
		return ApiResult.success(null);
	}

	@ApiOperation(value = "立即执行任务调度", notes = "立即执行任务调度")
	@PostMapping(value="/triggerjob")
	public ApiResult<String> triggerjob(@RequestBody JobForm form) {
		try {
			JobUtil.getInstance().triggerJob(form.getJobClassName(),form.getJobGroupName(),form.getData());
		} catch (Exception e) {
			log.error("立即执行任务调度",e);
			return ApiResult.error(ConstantSFY.CODE_400,"立即执行任务调度", null);
		}
		return ApiResult.success(null);
	}


	@ApiOperation(value = "停止任务调度", notes = "停止任务调度")
	@PostMapping(value="/pausejob")
	public ApiResult<String> pausejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName)

	{
		try {
			JobUtil.getInstance().pauseJob(jobClassName, jobGroupName);
		} catch (Exception e) {
			log.error("任务调度停止失败",e);
			return ApiResult.error(ConstantSFY.CODE_400,"任务调度停止失败", null);
		}
		return ApiResult.success(null);
	}

	@ApiOperation(value = "恢复任务调度", notes = "恢复任务调度")
	@PostMapping(value="/resume")
	public  ApiResult<String> resumejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
	{
		try {
			JobUtil.getInstance().resumeJob(jobClassName, jobGroupName);
		} catch (Exception e) {
			log.error("任务调度恢复失败");
			return ApiResult.error(ConstantSFY.CODE_400,"任务调度恢复失败", null);
		}
		return ApiResult.success(null);
	}



	@ApiOperation(value = "更新任务调度", notes = "更新任务调度")
	@PostMapping(value="/reschedule")
	public ApiResult<String> rescheduleJob(@RequestParam(value="jobClassName")String jobClassName,
			@RequestParam(value="jobGroupName")String jobGroupName,
			@RequestParam(value="cronExpression")String cronExpression) throws Exception
	{			
		try {
			JobUtil.getInstance().jobreschedule(jobClassName, jobGroupName, cronExpression);
		} catch (Exception e) {
			log.error("更新定时任务失败",e);
			return ApiResult.error(ConstantSFY.CODE_400,"更新定时任务失败", null);
		}
		return ApiResult.success(null);
	}
	


	@ApiOperation(value = "删除任务调度", notes = "删除任务调度")
	@PostMapping(value="/delete")
	public  ApiResult<String> deletejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
	{			
		try {
			JobUtil.getInstance().jobdelete(jobClassName, jobGroupName);
		} catch (Exception e) {
			log.error("删除定时任务失败",e);
			return ApiResult.error(ConstantSFY.CODE_400,"删除定时任务失败", null);
		}
		return ApiResult.success(null);
	}


	@ApiImplicitParams({
			@ApiImplicitParam(name = "curr", value = "ncurrame", dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "pageSize", dataType = "integer", paramType = "query")
		}
	)
	@ApiOperation(value = "查询任务", notes = "查询任务")
	@GetMapping(value="/findJob")
	public ApiResult<IPage<JobAndTrigger>> findJob(IPage<JobAndTrigger> page) {
		try {
			page = jobAndTriggerService.getJobAndTriggerDetails(page);
		} catch (SchedulerException e) {
			log.error("定时任务查询失败", e);
			return ApiResult.error(ConstantSFY.CODE_400,"定时任务查询失败", null);
		}
		return ApiResult.success(page);
	}
}
