package com.hxsnmp.impl;

import com.hxsnmp.api.ISnmpClientFacade;
import com.hxsnmp.api.ISnmpSessionFactory;
import com.hxsnmp.api.ISnmpTargetFactory;

public class Snmp4JClientFacade implements ISnmpClientFacade {

	private Snmp4JSessionFactory snmp4JSessionFactory = null;

	private Snmp4JTargetFactory snmp4JTargetFactory = null;

	public ISnmpSessionFactory getSnmpSessionFactory() {
		if (snmp4JSessionFactory == null) {
			snmp4JSessionFactory = new Snmp4JSessionFactory();
		}
		return snmp4JSessionFactory;
	}

	public ISnmpTargetFactory getSnmpTargetFactory() {
		if (snmp4JTargetFactory == null) {
			snmp4JTargetFactory = new Snmp4JTargetFactory();
		}
		return snmp4JTargetFactory;
	}

}
