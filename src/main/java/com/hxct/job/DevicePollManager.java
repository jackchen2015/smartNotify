/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */
package com.hxct.job;

/**
 *
 * @author chenwei Created on 2016-12-26, 19:39:26
 */

import com.hxct.entity.GateWayEntity;
import com.hxct.notify.QueueLink;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.quartz.CronScheduleBuilder;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import org.quartz.CronTrigger;

import static org.quartz.JobBuilder.newJob;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class DevicePollManager
{
	private static Log log = LogFactory.getLog(DevicePollManager.class);
	
	private static SchedulerFactory sf = new StdSchedulerFactory();
	private static String GW_JOB_GROUP_NAME = "jobgroup";
	private static String GW_TRIGGER_GROUP_NAME = "triggergroup";

	private static List<String> allGWJob = new ArrayList<String>();

	/**
	 * 	 */
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 *
	 * @param jobName 任务名
	 * @param job 任务
	 * @param time 时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addGwJob(GateWayEntity dev, Integer interval)
	{
		Scheduler sched = null;
		try
		{
			sched = sf.getScheduler();
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ip = dev.getIpaddress();

		String jobName = ip + "-job-device";
		String triggerName = ip + "-trigger-device";

	    JobDetail jobDetail = newJob(GwPollJob.class).withIdentity(jobName, GW_JOB_GROUP_NAME).build();
		CronTrigger trigger = newTrigger().withIdentity(triggerName, GW_TRIGGER_GROUP_NAME).withSchedule(cronSchedule("0 0/" + interval + " * * * ?"))
        .build();

//		JobDetail jobDetail = new JobDetail(jobName, MP_JOB_GROUP_NAME, job
//				.getClass());// 任务名，任务组，任务执行类
		jobDetail.getJobDataMap().put("device", dev);
//		CronTrigger trigger = new CronTrigger(jobName, MP_JOB_GROUP_NAME);// 触发器名,触发器组
//		try
//		{
//			trigger.setCronExpression("0 0/" + interval + " * * * ?");// 5 mins
//		}
//		catch(ParseException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}// 触发器时间设定
		try
		{
			sched.scheduleJob(jobDetail, trigger);
			log.info("add ac "+ ip +" job to task list");
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 启动
		try
		{
			if(!sched.isStarted())
			{
				sched.start();
			}
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		allGWJob.add(ip);
	}
	
	public static void startJob(SqlSession sqliteSession, GateWayEntity gw, int intervalId, QueueLink<GateWayEntity> queue)
	{
		Scheduler sched = null;
		try
		{
			sched = sf.getScheduler();
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jobName = "alert-job-device-" + gw.getId();
		String triggerName = "alert-trigger-device-" + gw.getId();

	    JobDetail jobDetail = newJob(GwPollJob.class).withIdentity(jobName, GW_JOB_GROUP_NAME).build();
		String cronStr = "";
		switch(intervalId)
		{
			case 0://每5分钟一次，分工作时段和非工作时段
				cronStr = "0 0/5 9,10,11,12,13,14,15,16,17,18 * * ?";//"0 0/5 9,10,11,12,13,14,15,16,17,18 * * ?";
				break;
			case 1://每15分钟一次，分工作时段和非工作时段
				cronStr = "0 0/15 9,10,11,12,13,14,15,16,17,18 * * ? ";//"0 0/15 9,10,11,12,13,14,15,16,17,18 * * ? ";
				break;
			case 2://每30分钟一次，分工作时段和非工作时段
				cronStr = "0 0/30 9,10,11,12,13,14,15,16,17,18 * * ? ";//"0 0/30 9,10,11,12,13,14,15,16,17,18 * * ? ";
				break;
			case 3://每小时一次，分工作时段和非工作时段
				cronStr = "0 0 9,10,11,12,13,14,15,16,17,18 * * ? *";//0 0 * * * ? //"0 0 9,10,11,12,13,14,15,16,17,18 * * ? *"
				break;
			case 4://每12小时一次 9点，18点 
				cronStr = "0 0 9,18 * * ? *";//"0 0 9,18 * * ? *"
				break;
			case 5://每天一次12点 
				cronStr = "0 0 12 * * ? *";//0 0 12 * * ? *
				break;
			case 6://每周一次
				cronStr = "0 15 10 ? 0 1 *";//每周一上午10点15分执行
				break;
			case 7://每月一次
				cronStr = "0 15 10 1 * ?";//每月1号10点15分钟执行任务 0 15 10 1 * ?
				break;
			default://每5分钟一次，不分工作时段
				cronStr = "0 0/5 * * * ?";
				break;
		}
		CronTrigger trigger = newTrigger().withIdentity(triggerName, GW_TRIGGER_GROUP_NAME).withSchedule(cronSchedule(cronStr)).build();

//		JobDetail jobDetail = new JobDetail(jobName, MP_JOB_GROUP_NAME, job
//				.getClass());// 任务名，任务组，任务执行类
		jobDetail.getJobDataMap().put("sqliteSession", sqliteSession);//query db session
//		jobDetail.getJobDataMap().put("type", type);//0:email,1:sms,2:email&sms
//		jobDetail.getJobDataMap().put("emailTitle", emailTitle);
//		jobDetail.getJobDataMap().put("emailMsg", emailMsg);
		jobDetail.getJobDataMap().put("gateway", gw);
		jobDetail.getJobDataMap().put("queue", queue);
		try
		{
			sched.scheduleJob(jobDetail, trigger);
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 启动
		try
		{
			if(!sched.isStarted())
			{
				sched.start();
			}
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除某GW轮询任务
	 * @param ip 
	 */
	public static void removeGwJob(String ip)
	{
		if(!allGWJob.contains(ip))
		{
			return;
		}
		allGWJob.remove(ip);
		String jobName = ip + "-job-device";
		String triggerName = ip + "-trigger-device";
		Scheduler sched = null;
		try
		{
			sched = sf.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, GW_TRIGGER_GROUP_NAME);
            sched.pauseTrigger(triggerKey);// 停止触发器  
            sched.unscheduleJob(triggerKey);// 移除触发器  
            sched.deleteJob(JobKey.jobKey(jobName, GW_JOB_GROUP_NAME));// 删除任务  
			log.info("delete ac "+ ip +" job from task list");
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// just for stop/start polling
	public static void stopJob(boolean b)
	{
		try
		{
			Collection<Scheduler> asc = (Collection<Scheduler>)sf
					.getAllSchedulers();
			if(b)
			{
				for(Scheduler schd : asc)
				{
					schd.pauseAll();
					schd.shutdown();
					// schd.standby();
					log.info("stop all ac jobs from one scheduler");
				}
			}
			else
			{
				for(Scheduler schd : asc)
				{
					if(!schd.isShutdown())
					{
						// schd.start();
						schd.resumeAll();
						log.info("start all ac jobs from one scheduler");
					}
				}
			}
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * pause all poll job
	 */
	public static void pauseAllGwPollJob()
	{
		Scheduler sched = null;
		try
		{
			sched = sf.getScheduler();
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(String ac : allGWJob)
		{
			String jobName = ac + "-job-device";
			try
			{
				sched.pauseJob(JobKey.jobKey(jobName));//停止触发器   
			}
			catch(SchedulerException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 继续所有AC轮询任务
	 */
	public static void resumeAllGwPollJob()
	{
		Scheduler sched = null;
		try
		{
			sched = sf.getScheduler();
		}
		catch(SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String ac : allGWJob)
		{
			String jobName = ac + "-job-device";
			try
			{
				sched.resumeJob(JobKey.jobKey(jobName));//停止触发器   
			}
			catch(SchedulerException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 	 */
	/**
	 * 修改一个任务的触发时间
	 *
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void modifyJobTime(String jobName, String jobGroupName, String triggerName,
			String triggerGroupName, String timeCron) throws SchedulerException,
			ParseException
	{
		Scheduler sched = sf.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
		Trigger trigger = sched.getTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
		if(trigger != null)
		{
			CronTrigger ct = (CronTrigger)trigger;
			// 修改时间
			String oldTime = ct.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(timeCron)) 
			{  
                // 触发器  
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组  
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定  
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(timeCron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                sched.rescheduleJob(triggerKey, trigger);			
            }
		}
	}

    /** 
     * @Description:关闭所有定时任务 
     */  
    public static void shutdownJobs() {  
        try {  
			allGWJob.clear();
            Scheduler sched = sf.getScheduler();  
            if (!sched.isShutdown()) {  
                sched.shutdown();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  	
}
