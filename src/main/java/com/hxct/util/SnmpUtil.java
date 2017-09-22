/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */
package com.hxct.util;

/**
 *
 * @author chenwei Created on 2016-12-22, 18:53:36
 */
import java.io.IOException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpUtil
{
	private static Log log = LogFactory.getLog(SnmpUtil.class);
	private Snmp snmp = null;
	private Address targetAddress = null;
	private static String protocol = "udp"; // 监控时使用的协议   

	private static String port = "161"; // 监控时使用的端口

	public void initComm(String ip, int port) throws IOException
	{
		// 设置Agent方的IP和端口   
		targetAddress = GenericAddress.parse("udp:" + ip + "/" + port);
		TransportMapping transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		transport.listen();
	}

	public Object getPDU(String ip, String community) throws IOException
	{
		String getV = null;
		try
		{
			getV = snmpGet(ip, "public", 161, 1, "1.3.6.1.2.1.1.3.0");
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getV;
	}

	/**
	 * 获取SNMP节点值
	 *
	 * @param ipAddress 目标IP地址
	 * @param community 公同体
	 * @param oid 对象ID
	 * @return String 监控结果代号
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public static String snmpGet(String ipAddress, String community, int port,
			int version, String oid) throws Exception
	{
		String resultStat = null; // 监控结果状态   

		StringBuffer address = new StringBuffer();
		address.append(protocol);
		address.append(":");
		address.append(ipAddress);
		address.append("/");
		address.append(port);

		Address targetAddress = GenericAddress.parse(address.toString());
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(oid)));
		pdu.setType(PDU.GET);

		// 创建共同体对象CommunityTarget   
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(targetAddress);
		target.setVersion(version);//0:snmpv1;1:snmpv2   
		target.setTimeout(2000);
		target.setRetries(1);

		DefaultUdpTransportMapping udpTransportMap = null;
		Snmp snmp = null;
		try
		{
			// 发送同步消息   
			udpTransportMap = new DefaultUdpTransportMapping();
			udpTransportMap.listen();
			snmp = new Snmp(udpTransportMap);
			ResponseEvent response = snmp.send(pdu, target);
			PDU resposePdu = response.getResponse();

			if(resposePdu == null)
			{
				log.info(ipAddress + ": Request timed out.");
			}
			else
			{
				//errorStatus = resposePdu.getErrorStatus();   
				Object obj = resposePdu.getVariableBindings().firstElement();
				VariableBinding variable = (VariableBinding)obj;
				resultStat = variable.getVariable().toString();
			}
		}
		catch(Exception e)
		{
			throw new Exception("获取SNMP节点状态时发生错误!", e);
		}
		finally
		{
			if(snmp != null)
			{
				try
				{
					snmp.close();
				}
				catch(IOException e)
				{
					snmp = null;
				}
			}
			if(udpTransportMap != null)
			{
				try
				{
					udpTransportMap.close();
				}
				catch(IOException e)
				{
					udpTransportMap = null;
				}
			}
		}

		return resultStat;
	}

	public void readResponse(ResponseEvent respEvnt)
	{
		// 解析Response   
		log.info("----------解析Response-------------");
		if(respEvnt != null && respEvnt.getResponse() != null)
		{
			Vector<? extends VariableBinding> recVBs = respEvnt.getResponse()
					.getVariableBindings();
			for(int i = 0; i < recVBs.size(); i++)
			{
				VariableBinding recVB = recVBs.elementAt(i);
				log.info(recVB.getOid() + " : " + recVB.getVariable());
			}
		}
	}
}
