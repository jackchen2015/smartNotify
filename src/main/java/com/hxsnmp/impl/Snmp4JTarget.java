package com.hxsnmp.impl;

import com.hxsnmp.api.SnmpTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Target;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;

public class Snmp4JTarget extends SnmpTarget {

	public Snmp4JTarget(String ip) {
		super(ip);
	}

	public Snmp4JTarget(String ip, int port) {
		super(ip, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.snmphibernate.api.ISnmpTarget#getReadTarget()
	 */
	public Target getReadTarget() {
		return getTarget(getCommunity());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.snmphibernate.api.ISnmpTarget#getWriteTarget()
	 */
	public Target getWriteTarget() {
		return getTarget(getWriteCommunity());
	}

	private Target getTarget(String comm) {
		if (getVersion() == V1 || getVersion() == V2C) {
			Address address = GenericAddress.parse("udp:" + getIp() + "/"
					+ getPort());
			CommunityTarget target = new CommunityTarget(address,
					new OctetString(comm));
			target.setVersion(getVersion());
			return target;
		}
		// TODO: not support snmp v3 now.
		throw new RuntimeException("Do not support snmpv3 now!");
	}

}
