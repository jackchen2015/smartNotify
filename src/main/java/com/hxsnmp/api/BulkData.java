package com.hxsnmp.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

public class BulkData {

	private PDU nxtReq = new PDU();
	// private boolean isEnd = false;
	private Map<String, Boolean> oidStatus = new HashMap<String, Boolean>();
	private Vector<String> reqOid = null;
	private Vector<VariableBinding> resp = new Vector<VariableBinding>();

	public BulkData() {
		nxtReq.setMaxRepetitions(10);
		nxtReq.setNonRepeaters(0);
	}

	public PDU getNxtReq() {
		return nxtReq;
	}

	public void setNxtReq(PDU nxtReq) {
		this.nxtReq = nxtReq;
	}

	public boolean isEnd() {
		Collection vals = oidStatus.values();
		if (vals.contains(Boolean.FALSE)) {
			return false;
		}
		return true;
	}

	// public void setEnd(boolean isEnd) {
	// this.isEnd = isEnd;
	// }
	public Map<String, Boolean> getOidStatus() {
		return oidStatus;
	}

	public void setOidStatus(Map<String, Boolean> oidStatus) {
		this.oidStatus = oidStatus;
	}

	public Vector<String> getReqOid() {
		return reqOid;
	}

	public void setReqOid(Vector<String> reqOid) {
		this.reqOid = reqOid;
		for (String oid : reqOid) {
			oidStatus.put(oid, false);
		}
	}

	public Vector<VariableBinding> getResp() {
		return resp;
	}

	public void setResp(Vector<VariableBinding> resp) {
		this.resp = resp;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (VariableBinding vb : resp) {
			sb.append(vb.getOid() + " is:----->" + vb.getVariable().toString()
					+ "\n");
		}
		return sb.toString();
	}
}
