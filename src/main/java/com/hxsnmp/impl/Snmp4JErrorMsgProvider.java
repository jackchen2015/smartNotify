package com.hxsnmp.impl;

import com.hxsnmp.api.ISnmpErrorMsgProvider;
import org.snmp4j.mp.SnmpConstants;

public class Snmp4JErrorMsgProvider implements ISnmpErrorMsgProvider {

	public String getErrorMsg(int errorStatus, int errorIndex) {
		if (0 <= errorStatus
				&& errorStatus <= SnmpConstants.SNMP_ERROR_MESSAGES.length) {
			return SnmpConstants.SNMP_ERROR_MESSAGES[errorStatus];
		}
		return "";
	}

}
