/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */
package com.hxct.job;

import com.hxct.entity.GateWayEntity;
import com.hxct.notify.QueueLink;
import com.hxct.util.PingUtil;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author chenwei Created on 2016-12-26, 18:27:10
 */
public class GwPollJob implements Job
{

	private static Log log = LogFactory.getLog(GwPollJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException
	{
		String obj = null;
		long currentTime = System.currentTimeMillis();
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(currentTime);
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
		{
			return;
		}
//		SqlSession sqliteSession = (SqlSession)context.getMergedJobDataMap().get("sqliteSession");
//		int type = (int)context.getMergedJobDataMap().get("type");
//		String emailTitle = (String)context.getMergedJobDataMap().get("emailTitle");
//		String emailMsg = (String)context.getMergedJobDataMap().get("emailMsg");
//		String smsMsg = (String)context.getMergedJobDataMap().get("smsMsg");
		QueueLink queue = (QueueLink)context.getMergedJobDataMap().get("queue");
                GateWayEntity gw = (GateWayEntity) context.getMergedJobDataMap().get("gateway");
		try
		{
			//if ok return, else go to start snmp process
//			obj = SnmpUtil.
//					snmpGet(dev.getIp(), dev.getCommunity(), dev.getPort(), dev.getVersion(), "1.3.6.1.2.1.1.3.0");
			//get all attention gw

                        boolean success = PingUtil.pingAcce(gw.getIpaddress(), 3);
                        if(!success){
                            queue.put(gw);
                        }
			//send notify
		}
		catch(Exception ex)
		{
			log.info("error:");
		}
		if(obj != null)
		{
			return;
		}
	}

}
