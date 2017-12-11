/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */
package com.hxrd.job;

import com.hxct.entity.AcEntity;
import com.hxct.entity.AccesspointEntity;
import com.hxct.notify.QueueLink;
import com.hxct.util.SnmpUtil;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author chenwei Created on 2016-12-26, 18:27:10
 */
public class ApPollJob implements Job
{

	private static Log log = LogFactory.getLog(ApPollJob.class);

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
		SqlSession mysqlSession = (SqlSession)context.getMergedJobDataMap().get("mysqlSession");
//		int type = (int)context.getMergedJobDataMap().get("type");
//		String emailTitle = (String)context.getMergedJobDataMap().get("emailTitle");
//		String emailMsg = (String)context.getMergedJobDataMap().get("emailMsg");
//		String smsMsg = (String)context.getMergedJobDataMap().get("smsMsg");
		QueueLink queue = (QueueLink)context.getMergedJobDataMap().get("queue");
		try
		{
			//if ok return, else go to start snmp process
//			obj = SnmpUtil.
//					snmpGet(dev.getIp(), dev.getCommunity(), dev.getPort(), dev.getVersion(), "1.3.6.1.2.1.1.3.0");
			//get all attention AP
			Map<String, Object> cmaps = new HashMap<String, Object>();
			Map<Integer, AcEntity> allAcMaps = new HashMap<Integer, AcEntity>();
			cmaps.put("acid", 0);
			cmaps.put("iskey", 1);
			List<AcEntity> allAcs = mysqlSession.selectList("com.hxct.dao.AcDao.queryList");
			for(AcEntity ac:allAcs)
			{
				allAcMaps.put(ac.getId(), ac);
			}
			List<AccesspointEntity> apLst = mysqlSession.selectList("com.hxct.dao.AccesspointDao.queryList", cmaps);	
			//get ap status
			if(apLst.size()==0)
			{
				return;
			}
			for(AccesspointEntity ape : apLst)
			{
				int acid = ape.getAcid();
				AcEntity acc = allAcMaps.get(acid);
				String statusOid = "";
				if(acc.getModel().equals("ZXWL"))
				{
					statusOid = "1.3.6.1.4.1.3902.2505.1.1.15.102.1.1.1.5."+ape.getIndex();
				}
				else//新AC
				{
					statusOid = "1.3.6.1.4.1.3902.154.8.11.1.3.1.1.3.1.1.9."+ape.getIndex();
				}
				obj = SnmpUtil.
						snmpGet(acc.getIpaddr(), acc.getReadco(), acc.getSnmpport(), 1, statusOid);
				if(acc.getModel().equals("ZXWL"))
				{
					if(obj!=null&&!obj.equals("4")&&!obj.equalsIgnoreCase("noSuchInstance"))
					{
						queue.put(ape);
					}					
				}
				else
				{
					if(obj!=null&&!obj.equals("6")&&!obj.equalsIgnoreCase("noSuchInstance"))
					{
						queue.put(ape);
					}
				}
				log.debug(obj);
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
