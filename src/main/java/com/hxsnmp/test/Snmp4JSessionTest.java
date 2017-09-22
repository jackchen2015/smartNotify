package com.hxsnmp.test;

import java.util.List;

import com.hxsnmp.api.ISnmpClientFacade;
import com.hxsnmp.api.ISnmpSession;
import com.hxsnmp.api.ISnmpSessionFactory;
import com.hxsnmp.api.ISnmpTargetFactory;
import com.hxsnmp.impl.Snmp4JClientFacade;

public class Snmp4JSessionTest {

	public static void main(String[] args) throws Exception {
		ISnmpClientFacade facade = new Snmp4JClientFacade();
		ISnmpSessionFactory sessionFactory = facade.getSnmpSessionFactory();
		ISnmpTargetFactory targetFactory = facade.getSnmpTargetFactory();
		ISnmpSession session = sessionFactory.newSnmpSession(targetFactory
				.newSnmpTarget("172.17.1.113", 161));

		System sysMIB = session.get(System.class);
		java.lang.System.out.println("sysDesc: " + sysMIB.getSysDesc());
		// sysMIB = (System) session.get(System.class,
		// new String[] { "sysObjectID" });
		java.lang.System.out.println("sysObjectID: " + sysMIB.getSysObjectID());

		List<IfEntry> list = session.getTable(IfEntry.class);
		for (Object o : list) {
			java.lang.System.out.println("ifDescr: "
					+ ((IfEntry) o).getIfDescr());
		}

		sysMIB.setSysLocation("");
		session.set(sysMIB);

		// List<WtpInfo> wtps = session.getTable(WtpInfo.class);
		// java.lang.System.out.println(wtps.size());

		session.close();
	}
}
