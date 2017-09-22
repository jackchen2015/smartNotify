package com.hxsnmp.impl;

import com.hxsnmp.api.ISnmpTarget;
import com.hxsnmp.api.ISnmpTargetFactory;

public class Snmp4JTargetFactory implements ISnmpTargetFactory {

	public Snmp4JTargetFactory() {
	}

	public ISnmpTarget newSnmpTarget(String ip) {
		return new Snmp4JTarget(ip);
	}

	public ISnmpTarget newSnmpTarget(String ip, int port) {
		return new Snmp4JTarget(ip, port);
	}

}
