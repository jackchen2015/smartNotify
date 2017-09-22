/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */

package com.hxct.notify;

import com.hxct.entity.AccesspointEntity;
import com.hxct.entity.AlertnotifyEntity;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author chenwei
 * Created on 2017-8-16, 15:49:36
 */
public class NotifyThread implements Runnable
{
	private QueueLink<AccesspointEntity> allNotifys;
	private int pollTimes;
	private ChuanglanSMS client;
	private Map<String, Integer> apTimes = new HashMap<String, Integer>();
	private Map<String, String> apNotifyDate = new HashMap<String, String>();
	private AlertnotifyEntity altNotify;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	public NotifyThread(QueueLink<AccesspointEntity> queue, int pollTimes, AlertnotifyEntity altNotify)
	{
		this.allNotifys = queue;
		this.pollTimes = pollTimes;
		this.altNotify = altNotify;
		client = new ChuanglanSMS(altNotify.getSmssendurl(), altNotify.getSmsaccount(),altNotify.getSmspassword());
	}

	@Override
	public void run()
	{
		while(true)
		{
			if(!allNotifys.isEmpty())
			{
				AccesspointEntity notify = allNotifys.pop();
				String apip = notify.getIpaddress();
				String loc = notify.getLocation();
				String mac = notify.getMacaddr();
				String hp = notify.getHotspotid();
				
				try
				{
					Integer time = 0;
					if(apTimes.containsKey(mac))
					{
						time = apTimes.get(mac);
					}
					time++;
					apTimes.put(mac, time);
					int type = altNotify.getType();
					String host = altNotify.getSmtpserver();
					boolean isSSL = altNotify.getSmtpssl()==1?true:false;
					String username = altNotify.getMailsender();
					String password = altNotify.getMailpwd();
					String from = altNotify.getMailsender();
					String emailTitle = altNotify.getMailtitle();
					String emailMsg = altNotify.getMailbody();
					String emails = altNotify.getMailrecv();
					
					String mobiles = altNotify.getSmsrecvnumber();
					String smsMsg = altNotify.getSmsmsg();
					if(time>=pollTimes)
					{
						String currentDate = sdf.format(new Date());
						String date = apNotifyDate.get(mac);						
						if(date!=null)
						{
							if(currentDate.equals(date))
							{
								apTimes.put(mac, 0);
								continue;
							}
						}
						apNotifyDate.put(mac, currentDate);
						if(type!=1)
						{
							String title = emailTitle.replaceAll("\\$hotspot\\$", "'"+hp+"'");
							String details = emailMsg.replaceAll("\\$hotspot\\$", "'"+hp+"'").replaceAll("\\$setuploc\\$", "'"+loc+"'").replaceAll("\\$macaddr\\$", "'"+mac+"'");
							MailUtils.sendMail(host, isSSL, username, password, from,  emails, title, details, null);
						}
						if(type!=0)
						{
							String newStr = smsMsg.replaceAll("\\$hotspot\\$", "'"+hp+"'").replaceAll("\\$setuploc\\$", "'"+loc+"'").replaceAll("\\$macaddr\\$", "'"+mac+"'");
							client.send(mobiles, newStr);
						}
						apTimes.put(mac, 0);
					}
				}
				catch(MessagingException ex)
				{
					Logger.getLogger(NotifyThread.class.getName()).
							log(Level.SEVERE, null, ex);
				}
				catch(UnsupportedEncodingException ex)
				{
					Logger.getLogger(NotifyThread.class.getName()).
							log(Level.SEVERE, null, ex);
				}
				catch(Exception ex)
				{
					Logger.getLogger(NotifyThread.class.getName()).
							log(Level.SEVERE, null, ex);
				}				
			}
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException ex)
			{
				Logger.getLogger(NotifyThread.class.getName()).
						log(Level.SEVERE, null, ex);
			}
		}
	}

}
