package com.hxsnmp.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hxsnmp.api.ISnmpSession;
import com.hxsnmp.api.ISnmpSessionFactory;
import com.hxsnmp.api.ISnmpTarget;

public class Snmp4JSessionFactory implements ISnmpSessionFactory {

	private final Snmp4JSmiTypeProvider typeProvider;

	private final Snmp4JErrorMsgProvider errorMsgProvider;
	private Map<String, ISnmpSession> allSpSessions = new HashMap<String, ISnmpSession>();

	public Snmp4JSessionFactory() {
		typeProvider = new Snmp4JSmiTypeProvider();
		errorMsgProvider = new Snmp4JErrorMsgProvider();
	}

	public ISnmpSession newSnmpSession(ISnmpTarget target) throws IOException {
		ISnmpSession spsn = allSpSessions
				.get(target.getIp() + target.getPort());
		if (spsn != null) {
			((Snmp4JSession) spsn).setTarget((Snmp4JTarget) target);
			return spsn;
		}
		Snmp4JSession session = new Snmp4JSession(target);
		session.setSmiTypeProvider(typeProvider);
		session.setSnmpErrorMsgProvider(errorMsgProvider);
		allSpSessions.put(target.getIp() + target.getPort(), session);
		return session;
	}

}
