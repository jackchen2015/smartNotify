/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */

package com.hxsnmp.test.data;

import com.hxct.entity.AcEntity;
import com.hxct.entity.AccesspointEntity;
import com.hxct.entity.ApsetupEntity;
import com.hxct.entity.FitapEntity;
import com.hxct.entity.KeyapEntity;
import com.hxct.util.MyBatisUtil;
import com.hxsnmp.api.ISnmpClientFacade;
import com.hxsnmp.api.ISnmpSession;
import com.hxsnmp.api.ISnmpSessionFactory;
import com.hxsnmp.api.ISnmpTarget;
import com.hxsnmp.api.ISnmpTargetFactory;
import com.hxsnmp.impl.Snmp4JClientFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.snmp4j.smi.VariableBinding;

/**
 *
 * @author chenwei
 * Created on 2017-8-9, 14:47:26
 */
public class OperateTable
{
	private SqlSession mysqlSession = null;
	private ISnmpClientFacade facade = new Snmp4JClientFacade();
	private ISnmpSessionFactory sessionFactory = facade.getSnmpSessionFactory();
	private ISnmpTargetFactory targetFactory = facade.getSnmpTargetFactory();
	private ISnmpSession session = null;

	public ISnmpSession getSnmpSession(String ip, int port, String community,
			String writeCommunity) {
		try {
			ISnmpTarget target = targetFactory.newSnmpTarget(ip, port);
			target.setCommunity(community);
			target.setWriteCommunity(writeCommunity);
			session = sessionFactory.newSnmpSession(target);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return session;
	}
	
	private void oper(){
		Map<Integer, Map<String, AccesspointEntity>> allapss = new HashMap<Integer, Map<String, AccesspointEntity>>();
		mysqlSession = MyBatisUtil.getSqlSessionFactory(MyBatisUtil.DataSourceEnvironment.MYSQL).openSession();
		List<AcEntity> allAcs = mysqlSession.selectList("com.hxct.dao.AcDao.queryList");
		Map<String, Integer> acNames = new HashMap<String, Integer>();
		for(AcEntity ac:allAcs)
		{
			acNames.put(ac.getName(), ac.getId());
			allapss.put(ac.getId(), new HashMap<String, AccesspointEntity>());
		}
		
		List<FitapEntity> allfitaps = mysqlSession.selectList("com.hxct.dao.FitapDao.queryList");
		List<ApsetupEntity> allapsetups = mysqlSession.selectList("com.hxct.dao.ApsetupDao.queryList");
		List<KeyapEntity> allkeyaps = mysqlSession.selectList("com.hxct.dao.KeyapDao.queryList");
		
		ISnmpSession session = null;
//		Map<Integer, Map<String, Integer>> allMaps = new HashMap<Integer, Map<String, Integer>>();
		for(AcEntity ac:allAcs)
		{
			Map<String, Integer> maps = new HashMap<String, Integer>();
			session = getSnmpSession(ac.getIpaddr(), 161,	"public", "private");
			String macoid = "1.3.6.1.4.1.3902.2505.1.1.15.102.1.1.1.3";
			List<VariableBinding> allapMac = session.getBulk(new String[] { macoid });
			if (allapMac == null || allapMac.size() == 0) {
				return;
			}
			for(VariableBinding vb:allapMac)
			{
				AccesspointEntity ape = new AccesspointEntity();
				int idx = vb.getOid().last();
				String mac = vb.toValueString();
//				ape.setAcid(ac.getId());
				ape.setIndex(idx);
				ape.setMacaddr(mac);
				maps.put(mac, idx);
				allapss.get(ac.getId()).put(mac, ape);
			}
//			allMaps.put(ac.getId(), maps);
		}
		for(FitapEntity fap:allfitaps)
		{
			String apac = fap.get所属ac();
			String apMac = fap.getMac地址();
			int acid = acNames.get(apac);
			AccesspointEntity ape = allapss.get(acid).get(apMac);
			if(ape==null)
			{
				continue;
			}
			String apName = fap.getAp名称();
			String apip = fap.getIp地址();
			ape.setName(apName);
			ape.setIpaddress(apip);
		}
		
		for(ApsetupEntity sep:allapsetups)
		{
			for(AcEntity ac:allAcs)
			{
				AccesspointEntity ape = allapss.get(ac.getId()).get(sep.getApmac());
				if(ape==null)
				{
					continue;
				}
				ape.setLocation(sep.getAp安装位置描述());
				ape.setHotspotid(sep.get热点());
				System.out.println(sep.get热点());
			}
		}
		
		for(KeyapEntity key:allkeyaps)
		{
			for(AcEntity ac:allAcs)
			{
				AccesspointEntity ape = allapss.get(ac.getId()).get(key.getApmac());
				if(ape==null)
				{
					continue;
				}
				ape.setIskey(1);
			}
		}
		
		for(Map.Entry<Integer,Map<String, AccesspointEntity>> entry:allapss.entrySet())
		{
			Map<String,AccesspointEntity> aps = entry.getValue();
			for(Map.Entry<String, AccesspointEntity> ap:aps.entrySet())
			{
				if(ap.getValue().getMacaddr().equalsIgnoreCase("8c:79:67:01:dd:b8"))
				{
					System.out.print(ap.getValue().getName());
				}
//				mysqlSession.insert("com.hxct.dao.AccesspointDao.insertBatch", new ArrayList<AccesspointEntity>(aps.values()));
				System.out.println("ac is:"+ap.getValue().getAcid()+",ap mac is:"+ap.getValue().getMacaddr()+",热点:"+ap.getValue().getHotspotid());
				mysqlSession.insert("com.hxct.dao.AccesspointDao.save", ap.getValue());
				mysqlSession.commit();
			}
		}
		
		
	}
	
	public static void main(String[] args){
		OperateTable ot = new OperateTable();
		ot.oper();
	}
	
}
