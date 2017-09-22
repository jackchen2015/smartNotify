package com.hxsnmp.impl;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.hxsnmp.annotation.MibIndex;
import com.hxsnmp.annotation.MibObjectType.Access;
import com.hxsnmp.api.AbstractSnmpSession;
import com.hxsnmp.api.BulkData;
import com.hxsnmp.api.ISnmpTarget;
import com.hxsnmp.api.SmiType;
import com.hxsnmp.api.SnmpAnnotationException;
import com.hxsnmp.api.SnmpException;
import com.hxsnmp.meta.IMibClassMeta;
import com.hxsnmp.meta.IMibIndexMeta;
import com.hxsnmp.meta.IMibPropertyMeta;
import com.hxsnmp.meta.IRowStatusMeta;
import com.hxsnmp.meta.MibMetaCache;

public class Snmp4JSession extends AbstractSnmpSession {

	private Snmp snmp4J;

	private Snmp4JTarget target;

	private static String dolkey = ".1.3.6.1.2.1.";
	private static String pridot = "1.3.6.1.4.1.";
	private String key;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 */
	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	public Snmp4JSession(ISnmpTarget target) throws IOException {
		this.target = (Snmp4JTarget) target;
		TransportMapping tm = new DefaultUdpTransportMapping();
		this.snmp4J = new Snmp(tm);
		this.snmp4J.listen();
	}

	public Target getReadTarget() {
		Target targetImpl = target.getReadTarget();
		initTimeoutRetries(targetImpl);
		return targetImpl;
	}

	public void setTarget(Snmp4JTarget target) {
		this.target = target;
	}

	private void initTimeoutRetries(Target targetImpl) {
		targetImpl.setTimeout(getTimeout());
		targetImpl.setRetries(getRetries());
	}

	private Target getWriteTarget() {
		Target targetImpl = target.getWriteTarget();
		initTimeoutRetries(targetImpl);
		return targetImpl;
	}

	@Override
	public <T> T get(Class<T> scalarClass) throws IOException, SnmpException,
			SnmpAnnotationException {
		PDU reqPDU = newGetPDU(scalarClass);
		return get(scalarClass, reqPDU);
	}

	@Override
	public <T> T get(Class<T> scalarClass, String field) throws IOException,
			SnmpException, SnmpAnnotationException {
		return get(scalarClass, new String[] { field });
	}

	@Override
	public <T> T get(Class<T> scalarClass, String[] fields) throws IOException,
			SnmpException, SnmpAnnotationException {
		try {
			PDU reqPDU = newGetPDU(scalarClass, fields);
			return get(scalarClass, reqPDU);
		} catch (SecurityException e) {
			throw new SnmpAnnotationException(e);
		} catch (NoSuchFieldException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public <T> List<T> getTable(Class<T> entryClass) throws IOException,
			SnmpException, SnmpAnnotationException {
		try {
			List<T> list = new ArrayList<T>();
			// init pdu
			PDU reqPDU = newGetNextFirstEntryPDU(entryClass);
			checkReqError(reqPDU);
			OID firstReqOid = reqPDU.get(0).getOid();
			while (true) {
				ResponseEvent event = snmp4J.getNext(reqPDU, getReadTarget());
				checkEventError(event);
				PDU respPDU = event.getResponse();
				checkResError(respPDU);
				OID firstRespOid = respPDU.get(0).getOid();
				if (isTableEnd(firstReqOid, firstRespOid)) {
					break;
				}
				int[] indexOids = extractIndexOids(firstRespOid, firstReqOid);
				T entry = entryClass.newInstance();
				fillIndices(entry, indexOids);
				fillProperties(entry, respPDU);
				list.add(entry);
				reqPDU = newGetNextEntryPDU(entry);
			}
			return list;
		} catch (IllegalArgumentException e) {
			throw new SnmpAnnotationException(e);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public <T> List<T> getTable(Class<T> entryClass, int size)
			throws IOException, SnmpException, SnmpAnnotationException {
		int count = 0;
		try {
			List<T> list = new ArrayList<T>();
			// init pdu
			PDU reqPDU = newGetNextFirstEntryPDU(entryClass);
			checkReqError(reqPDU);
			OID firstReqOid = reqPDU.get(0).getOid();
			while (true) {
				ResponseEvent event = snmp4J.getNext(reqPDU, getReadTarget());
				checkEventError(event);
				PDU respPDU = event.getResponse();
				checkResError(respPDU);
				OID firstRespOid = respPDU.get(0).getOid();
				if (isTableEnd(firstReqOid, firstRespOid)) {
					break;
				}
				int[] indexOids = extractIndexOids(firstRespOid, firstReqOid);
				T entry = entryClass.newInstance();
				fillIndices(entry, indexOids);
				fillProperties(entry, respPDU);
				list.add(entry);
				count++;
				if (count == size) {
					break;
				}
				reqPDU = newGetNextEntryPDU(entry);
			}
			return list;
		} catch (IllegalArgumentException e) {
			throw new SnmpAnnotationException(e);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	/**
	 * get next size element from entry0
	 */
	@Override
	public <T> List<T> getTableNext(T entry0, int nxtSize) throws IOException,
			SnmpException, SnmpAnnotationException {
		int count = 0;
		try {
			List<T> list = new ArrayList<T>();
			// init pdu
			PDU reqPDU = newGetNextEntryPDU(entry0);
			checkReqError(reqPDU);
			PDU firstPDU = newGetNextFirstEntryPDU(entry0.getClass());
			OID firstReqOid = firstPDU.get(0).getOid();
			while (true) {
				ResponseEvent event = snmp4J.getNext(reqPDU, getReadTarget());
				checkEventError(event);
				PDU respPDU = event.getResponse();
				checkResError(respPDU);
				OID firstRespOid = respPDU.get(0).getOid();
				if (isTableEnd(firstReqOid, firstRespOid)) {
					break;
				}
				int[] indexOids = extractIndexOids(firstRespOid, firstReqOid);
				T entry = (T) entry0.getClass().newInstance();
				fillIndices(entry, indexOids);
				fillProperties(entry, respPDU);
				list.add(entry);
				count++;
				if (count == nxtSize) {
					break;
				}
				reqPDU = newGetNextEntryPDU(entry);
			}
			return list;
		} catch (IllegalArgumentException e) {
			throw new SnmpAnnotationException(e);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public <T> T getByIndex(Class<T> entryClass, Serializable indices)
			throws IOException, SnmpException, SnmpAnnotationException {
		try {
			T entry = buildEntryWithIndices(entryClass, indices);
			PDU reqPDU = newGetEntryPDU(entry);
			return getEntryByIndex(entry, reqPDU);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public <T> T getByIndex(Class<T> entryClass, Serializable indices,
			String[] fields) throws IOException, SnmpException,
			SnmpAnnotationException {
		try {
			T entry = buildEntryWithIndices(entryClass, indices);
			PDU reqPDU = newGetEntryPDU(entry, fields);
			return getEntryByIndex(entry, reqPDU);
		} catch (IllegalArgumentException e) {
			throw new SnmpAnnotationException(e);
		} catch (SecurityException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		} catch (NoSuchFieldException e) {
			throw new SnmpAnnotationException(e);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public void set(Object entry) throws IOException, SnmpException,
			SnmpAnnotationException {
		try {
			PDU reqPDU = newSetPDU(entry);
			checkReqError(reqPDU);
			ResponseEvent event = snmp4J.set(reqPDU, getWriteTarget());
			checkEventError(event);
			PDU resPDU = event.getResponse();
			checkResError(resPDU);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			throw new SnmpAnnotationException(e);
		} catch (NoSuchMethodException e) {
			throw new SnmpAnnotationException(e);
		} catch (InvocationTargetException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public void create(Object entry) throws IOException, SnmpException,
			SnmpAnnotationException {
		try {
			PDU reqPDU = newCreatePDU(entry);
			checkReqError(reqPDU);
			ResponseEvent event = snmp4J.set(reqPDU, getWriteTarget());
			checkEventError(event);
			PDU resPDU = event.getResponse();
			checkResError(resPDU);
		} catch (IllegalArgumentException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		} catch (SecurityException e) {
			throw new SnmpAnnotationException(e);
		} catch (NoSuchMethodException e) {
			throw new SnmpAnnotationException(e);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (InvocationTargetException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public void delete(Object entry) throws IOException, SnmpException,
			SnmpAnnotationException {
		try {
			PDU reqPDU = newDeletPDU(entry);
			checkReqError(reqPDU);
			ResponseEvent event = snmp4J.set(reqPDU, getWriteTarget());
			checkEventError(event);
			PDU resPDU = event.getResponse();
			checkResError(resPDU);
		} catch (IllegalArgumentException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public void close() throws IOException {
		snmp4J.close();
	}

	private Field[] getWritePropFields(Class<?> clazz) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		IMibClassMeta mibClassMeta = getMibClassMeta(clazz, target);
		for (Field field : fields) {
			IMibPropertyMeta propertyMeta = mibClassMeta.getPropertyMeta(field
					.getName());
			if (propertyMeta != null
					&& !(propertyMeta instanceof IMibIndexMeta)
					&& propertyMeta.getAccess() != Access.READ) {
				list.add(field);
			}
		}
		return list.toArray(new Field[list.size()]);
	}

	private OID buildIndexOid(Object entry) throws IllegalArgumentException,
			IllegalAccessException {
		Class<?> clazz = entry.getClass();
		IMibClassMeta classMeta = getMibClassMeta(clazz, target);
		Field[] indexFields = getIndexFields(clazz);
		OID oid = null;
		for (Field indexField : indexFields) {
			indexField.setAccessible(true);
			Object value = indexField.get(entry);
			IMibIndexMeta mibIndexMeta = (IMibIndexMeta) classMeta
					.getPropertyMeta(indexField.getName());
			int length = mibIndexMeta.getIndexLength();
			if (length == MibIndex.VARSTR_WITH_LENGTH) {
				byte[] bytes = ((String) value).getBytes();
				int[] integers = new int[bytes.length + 1];
				integers[0] = bytes.length;
				int i = 1;
				for (byte b : bytes) {
					integers[i++] = b;
				}
				oid = appendRawOids(oid, integers);
			} else if (length == MibIndex.VARSTR_WITHOUT_LENGTH) {
				byte[] bytes = ((String) value).getBytes();
				int[] integers = new int[bytes.length];
				int i = 0;
				for (byte b : bytes) {
					integers[i++] = b;
				}
				oid = appendRawOids(oid, integers);
			} else if (length == 1) {
				Class<?> smiTypeClass = getSmiTypeProvider().getSmiType(
						mibIndexMeta.getSmiType());
				if (smiTypeClass.equals(Integer32.class)) {
					int v = ((Integer) value).intValue();
					oid = appendRawOids(oid, new int[] { v });
				} else if (UnsignedInteger32.class
						.isAssignableFrom(smiTypeClass)) {
					int v = (int) ((Long) value).longValue();
					oid = appendRawOids(oid, new int[] { v });
				} else {
					throw new RuntimeException("Index length should not be 1."
							+ indexField);
				}
			} else if (length >= 1) {
				SmiType smiType = mibIndexMeta.getSmiType();
				if (smiType == SmiType.OID) {
					if (oid == null)
						oid = new OID();
					oid.append((String) value);
				} else if (smiType == SmiType.DISPLAY_STRING) {
					byte[] bytes = ((String) value).getBytes();
					int[] integers = new int[length];
					for (int i = 0; i < integers.length; i++) {
						integers[i] = bytes[i];
					}
//					if(oid==null){
//						oid = new OID("17");//change by chenwei for mac index
//					}
					oid = appendRawOids(oid, integers);
				} else if (smiType == SmiType.OCTET_STRING) {
					byte[] bytes = ((byte[]) value);
					int[] integers = new int[length];
					for (int i = 0; i < integers.length; i++) {
						integers[i] = bytes[i];
					}
					oid = appendRawOids(oid, integers);
				} else if (smiType == SmiType.IPADDRESS) {
					String[] strBytes = ((String) value).split("\\.");
					if (strBytes.length != 4)
						throw new RuntimeException(
								"Assert faild. IpAddres length must be 1.");
					int[] integers = new int[strBytes.length];
					for (int i = 0; i < integers.length; i++) {
						integers[i] = Integer.parseInt(strBytes[i]);
					}
					oid = appendRawOids(oid, integers);
				} else {
					throw new RuntimeException("Unknow smiType: " + smiType);
				}
			} else {
				throw new RuntimeException(
						"Assert Failed! Unknow index length.");
			}
		}
		return oid;
	}

	private OID appendRawOids(OID oid, int[] integers) {
		if (oid == null)
			return new OID(integers);
		oid.append(new OID(integers));
		return oid;
	}

	private void fillIndices(Object entry, int[] indexOids)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = entry.getClass();
		IMibClassMeta mibClassMeta = getMibClassMeta(clazz, target);
		Field[] indexFields = getIndexFields(clazz);
		for (int i = 0, j = 0; i < indexFields.length && j < indexOids.length; i++) {
			Field indexField = indexFields[i];
			indexField.setAccessible(true);
			IMibIndexMeta mibIndexMeta = (IMibIndexMeta) mibClassMeta
					.getPropertyMeta(indexField.getName());
			int length = mibIndexMeta.getIndexLength();
			if (length == MibIndex.VARSTR_WITH_LENGTH) {
				byte[] bytes = new byte[indexOids[j++]];
				bytes = copyBytes(indexOids, j, bytes);
				indexField.set(entry, new String(bytes));
				j += bytes.length;
			} else if (length == MibIndex.VARSTR_WITHOUT_LENGTH) {
				byte[] bytes = new byte[indexOids.length - j];
				bytes = copyBytes(indexOids, j, bytes);
				indexField.set(entry, new String(bytes));
				j += bytes.length;
			} else if (length == 1) {
				Class<?> smiTypeClass = getSmiTypeProvider().getSmiType(
						mibIndexMeta.getSmiType());
				if (smiTypeClass.equals(Integer32.class)) {
					indexField.set(entry, indexOids[j++]);
				} else if (UnsignedInteger32.class
						.isAssignableFrom(smiTypeClass)) {
					indexField.set(entry, (long) indexOids[j++]);
				} else {
					throw new RuntimeException("Index length should not be 1."
							+ indexField);
				}
			} else if (length >= 1) {
				SmiType smiType = mibIndexMeta.getSmiType();
				if (smiType == SmiType.OID) {
					int[] oidValue = new int[length];
					System.arraycopy(indexOids, j, oidValue, 0, length);
					indexField.set(entry, intAry2Str(oidValue));
				} else if (smiType == SmiType.DISPLAY_STRING) {
					if(length==6){
						StringBuffer sb = new StringBuffer();
						for(int c=0;c<6;c++){
							String hoid = Integer.toHexString(indexOids[j+c]);
							if(hoid.length()==1){
								sb.append("0");
							}
							sb.append(hoid).append(":");
						}
						sb.deleteCharAt(sb.length()-1);
						indexField.set(entry, sb.toString());
					}else{
						byte[] bytes = new byte[length];
						bytes = copyBytes(indexOids, j, bytes);
						indexField.set(entry, new String(bytes));
					}
				} else if (smiType == SmiType.OCTET_STRING) {
					byte[] bytes = new byte[length];
					bytes = copyBytes(indexOids, j, bytes);
					indexField.set(entry, bytes);
				} else if (smiType == SmiType.IPADDRESS) {
					if (length != 4)
						throw new RuntimeException(
								"Asser Failed, IpAddress length must be 4. length="
										+ length);
					StringBuffer sb = new StringBuffer();
					sb.append(indexOids[j]).append('.');
					sb.append(indexOids[j + 1]).append('.');
					sb.append(indexOids[j + 2]).append('.');
					sb.append(indexOids[j + 3]);
					indexField.set(entry, sb.toString());
				} else {
					throw new RuntimeException("Unknow smiType: " + smiType);
				}
				j += length;
			} else {
				throw new RuntimeException(
						"Assert Failed! Unknow index length.");
			}
		}
	}

	private String intAry2Str(int[] oidValue) {
		StringBuffer sb = new StringBuffer();
		for (int i : oidValue) {
			sb.append(i).append('.');
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private byte[] copyBytes(int[] srcArray, int srcPos, byte[] destBytes) {
		for (int k = 0; k < destBytes.length; k++) {
			destBytes[k] = (byte) (srcArray[srcPos + k] & 0xff);
		}
		return destBytes;
	}

	private Field[] getIndexFields(Class<?> clazz) {
		IMibClassMeta mibClassMeta = getMibClassMeta(clazz, target);
		Field[] fields = clazz.getDeclaredFields();
		List<Field> list = new ArrayList<Field>();
		for (int i = 0; i < fields.length; i++) {
			if (mibClassMeta.isIndex(fields[i].getName())) {
				fields[i].setAccessible(true);
				list.add(fields[i]);
			}
		}
		Field[] indexFields = list.toArray(new Field[list.size()]);
		// bubble sort.
		for (int i = 0; i < indexFields.length; i++) {
			for (int j = i + 1; j < indexFields.length; j++) {
				Field top = indexFields[i];
				int no = mibClassMeta.getIndexMeta(top.getName()).getIndexNo();
				Field another = indexFields[j];
				int anotherNo = mibClassMeta.getIndexMeta(another.getName())
						.getIndexNo();
				if (anotherNo < no) {
					indexFields[i] = another;
					indexFields[j] = top;
				}
			}
		}
		return indexFields;
	}

	private int[] extractIndexOids(OID firstRespOid, OID firstReqOid) {
		int[] respOids = firstRespOid.getValue();
		int[] reqOids = firstReqOid.getValue();
		int[] indexOids = new int[respOids.length - reqOids.length];
		System.arraycopy(respOids, reqOids.length, indexOids, 0,
				indexOids.length);
		return indexOids;
	}

	private boolean isTableEnd(OID firstReqOid, OID firstRespOid) {
		// TODO: END OF MIB
		return !firstRespOid.startsWith(firstReqOid);
	}

	private void fillProperties(Object object, PDU pdu)
			throws InstantiationException, IllegalAccessException {
		IMibClassMeta mibClassMeta = getMibClassMeta(object.getClass(), target);
		Field[] propFields = getPropFields(object.getClass());
		Vector<?> variableBindings = pdu.getVariableBindings();
		for (Field propField : propFields) {
			propField.setAccessible(true);
			IMibPropertyMeta propertyMeta = mibClassMeta
					.getPropertyMeta(propField.getName());
			OID oid = new OID(propertyMeta.getOid());
			Variable variable = findVariableByOid(oid, variableBindings);
			if (variable != null) {
				// Object value = variable.getValue();
				// if (propertyMeta.getSmiType() == SmiType.DISPLAY_STRING) {
				// value = new String((byte[]) value);
				// } else if (propertyMeta.getSmiType() == SmiType.OID) {
				// value = ((OID) variable).toString();
				// }
				// change by chenwei 2010.10.20
				// add trim function for index value to trim 2010.12.10
				String syntaxStr = variable.getSyntaxString();
				Object value = null;
				if (variable instanceof OctetString) {
					if (propertyMeta.getSmiType() == SmiType.OCTET_STRING) {
						if (variable.toString().equalsIgnoreCase("")
								|| variable.toString().indexOf(":") > -1) {
							value = variable.toString();
						} else {
							byte[] bt = ((OctetString) variable).getValue();
							value = bt;
						}
					}
					// value = ((OctetString)variable).getValue();
				}
				// if(propertyMeta.getSmiType() == SmiType.DISPLAY_STRING){
				// byte[] bt = ((OctetString)variable).getValue();
				// value = new String(bt);
				// }
				// else if(propertyMeta.getSmiType() == SmiType.OCTET_STRING){
				// value = variable.toString();
				// }
				if (propertyMeta.getSmiType() == SmiType.DISPLAY_STRING) {
					if (variable instanceof OctetString) {
						String spVal = variable.toString();
						if (spVal.equalsIgnoreCase("")
								|| spVal.indexOf(":") > 0) {// mac address
							value = spVal;
						} else {
							value = new String(((OctetString) variable)
									.getValue());
						}
					} else {
						value = variable.toString().trim();
					}
				} else if (propertyMeta.getSmiType() == SmiType.OID) {
					value = variable.toString().trim();
				} else if (propertyMeta.getSmiType() == SmiType.TIMETICKS) {
					value = variable.toLong();
				} else if (propertyMeta.getSmiType() == SmiType.INTEGER32
						|| propertyMeta.getSmiType() == SmiType.INTEGER) {
					if (variable instanceof OctetString) {
						value = Integer.parseInt(variable.toString());
					} else {
						value = variable.toInt();
					}
				} else if (propertyMeta.getSmiType() == SmiType.GAUGE32
						|| propertyMeta.getSmiType() == SmiType.COUNTER32
						|| propertyMeta.getSmiType() == SmiType.COUNTER64) {
					value = variable.toLong();
				} else if (propertyMeta.getSmiType() == SmiType.OCTET_STRING) {
					if (variable instanceof IpAddress) {
						value = variable.toString().trim();
					}
				} else if (propertyMeta.getSmiType() == SmiType.IPADDRESS) {
					value = variable.toString();
				}
				propField.set(object, value);
			}
		}
	}

	private Variable findVariableByOid(OID oid, Vector<?> variableBindings) {
		for (Iterator<?> it = variableBindings.iterator(); it.hasNext();) {
			VariableBinding vb = (VariableBinding) it.next();
			if (vb.getOid().startsWith(oid)) {
				return vb.getVariable();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private PDU newSetPDU(Object entry) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			InstantiationException, InvocationTargetException {
		PDU pdu = new PDU();
		pdu.setType(PDU.SET);
		OID indexOid = buildIndexOid(entry);
		Field[] writePropFields = getWritePropFields(entry.getClass());
		for (Field writeField : writePropFields) {
			writeField.setAccessible(true);
			IMibPropertyMeta propertyMeta = getMibClassMeta(entry.getClass(),
					target).getPropertyMeta(writeField.getName());
			OID oid = new OID(propertyMeta.getOid());
			if (indexOid != null) {
				oid.append(indexOid);
			}
			Class<?> type = writeField.getType();
			Constructor<?> constructor = getSmiTypeProvider().getSmiType(
					propertyMeta.getSmiType()).getConstructor(
					new Class[] { type });
			Variable variable = (Variable) constructor
					.newInstance(new Object[] { writeField.get(entry) });
			pdu.add(new VariableBinding(oid, variable));
		}
		return pdu;
	}

	@SuppressWarnings("unchecked")
	private PDU newSetPDU(Object entry, ArrayList<String> fields)
			throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchMethodException, InstantiationException,
			InvocationTargetException {
		PDU pdu = new PDU();
		pdu.setType(PDU.SET);
		OID indexOid = buildIndexOid(entry);
		Field[] writePropFields = getWritePropFields(entry.getClass());
		for (Field writeField : writePropFields) {
			if (!fields.contains(writeField.getName())) {
				continue;
			}
			writeField.setAccessible(true);
			IMibPropertyMeta propertyMeta = getMibClassMeta(entry.getClass(),
					target).getPropertyMeta(writeField.getName());
			String oidStr = propertyMeta.getOid();
			if (key != null) {
				oidStr = oidStr.replace("$key", key);
			}
			OID oid = new OID(oidStr);
			if (indexOid != null) {
				oid.append(indexOid);
			}
			Class<?> type = writeField.getType();
			Constructor<?> constructor = getSmiTypeProvider().getSmiType(
					propertyMeta.getSmiType()).getConstructor(
					new Class[] { type });
			Variable variable = (Variable) constructor
					.newInstance(new Object[] { writeField.get(entry) });
			pdu.add(new VariableBinding(oid, variable));
		}
		return pdu;
	}

	/**
	 * 
	 * @param entry
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SnmpAnnotationException
	 */
	private PDU newDeletPDU(Object entry) throws IllegalArgumentException,
			IllegalAccessException, SnmpAnnotationException {
		PDU pdu = new PDU();
		pdu.setType(PDU.SET);
		pdu.add(newDeleteVB(entry));
		return pdu;
	}

	private VariableBinding newDeleteVB(Object entry)
			throws IllegalAccessException, SnmpAnnotationException {
		OID indexOid = buildIndexOid(entry);
		if (indexOid == null)
			throw new SnmpAnnotationException(new NullPointerException(
					"No index oid."));
		IRowStatusMeta rowStatusMeta = getMibClassMeta(entry.getClass(), target)
				.getRowStatusMeta();
		if (rowStatusMeta == null)
			throw new SnmpAnnotationException(new NullPointerException(
					"No RowStatus Annotation."));
		OID oid = new OID(rowStatusMeta.getOid());
		oid.append(indexOid);
		Integer32 var = new Integer32(rowStatusMeta.getDeleteEnum());
		VariableBinding vb = new VariableBinding(oid, var);
		return vb;
	}

	private PDU newCreatePDU(Object entry) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchMethodException,
			InstantiationException, InvocationTargetException,
			SnmpAnnotationException {
		PDU pdu = newSetPDU(entry);
		pdu.add(newCreateVB(entry));
		return pdu;
	}

	private VariableBinding newCreateVB(Object entry)
			throws IllegalAccessException, SnmpAnnotationException {
		OID indexOid = buildIndexOid(entry);
		checkIndexOid(indexOid);
		IRowStatusMeta rowStatusMeta = getMibClassMeta(entry.getClass(), target)
				.getRowStatusMeta();
		checkRowStatusAnnotation(rowStatusMeta);
		OID oid = new OID(rowStatusMeta.getOid());
		oid.append(indexOid);
		Integer32 var = new Integer32(rowStatusMeta.getCreateEnum());
		return new VariableBinding(oid, var);
	}

	private PDU newGetPDU(Class<?> scalarClass) {
		PDU pdu = new PDU();
		pdu.setType(PDU.GET);
		Field[] propFields = getPropFields(scalarClass);
		for (Field propField : propFields) {
			IMibPropertyMeta propertyMeta = getMibClassMeta(scalarClass, target)
					.getPropertyMeta(propField.getName());
			pdu.add(new VariableBinding(new OID(propertyMeta.getOid())));
		}
		return pdu;
	}

	private PDU newGetPDU(Class<?> scalarClass, String[] fields)
			throws SecurityException, NoSuchFieldException {
		PDU pdu = new PDU();
		pdu.setType(PDU.GET);
		for (String fn : fields) {
			Field field = scalarClass.getDeclaredField(fn);
			IMibPropertyMeta propertyMeta = getMibClassMeta(scalarClass, target)
					.getPropertyMeta(field.getName());
			pdu.add(new VariableBinding(new OID(propertyMeta.getOid())));
		}
		return pdu;
	}

	private <T> T get(Class<T> scalarClass, PDU reqPDU) throws IOException,
			SnmpException, SnmpAnnotationException {
		try {
			checkReqError(reqPDU);
			ResponseEvent event = snmp4J.get(reqPDU, getReadTarget());
			checkEventError(event);
			PDU resPDU = event.getResponse();
			checkResError(resPDU);
			T mibObj = scalarClass.newInstance();
			fillProperties(mibObj, resPDU);
			return mibObj;
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	private PDU newGetEntryPDU(Object entry) throws IllegalArgumentException,
			IllegalAccessException {
		PDU pdu = new PDU();
		pdu.setType(PDU.GET);
		OID indexOid = buildIndexOid(entry);
		Field[] propFields = getPropFields(entry.getClass());
		for (Field propField : propFields) {
			IMibPropertyMeta propertyMeta = getMibClassMeta(entry.getClass(),
					target).getPropertyMeta(propField.getName());
			OID oid = new OID(propertyMeta.getOid());
			if (indexOid != null) {
				oid.append(indexOid);
			}
			pdu.add(new VariableBinding(oid));
		}
		return pdu;
	}

	private PDU newGetNextFirstEntryPDU(Class<?> entryClass) {
		PDU pdu = new PDU();
		pdu.setType(PDU.GETNEXT);
		Field[] propFields = getPropFields(entryClass);
		for (Field propField : propFields) {
			IMibPropertyMeta propertyMeta = getMibClassMeta(entryClass, target)
					.getPropertyMeta(propField.getName());
			pdu.add(new VariableBinding(new OID(propertyMeta.getOid())));
		}
		if (pdu.size() <= 0) {
			// in some mib, there are only indices.
			Field[] indexFields = getIndexFields(entryClass);
			if (indexFields.length > 0) {
				IMibIndexMeta indexMeta = getMibClassMeta(entryClass, target)
						.getIndexMeta(indexFields[0].getName());
				pdu.add(new VariableBinding(new OID(indexMeta.getOid())));
			}
		}
		return pdu;
	}

	private PDU newGetNextEntryPDU(Object entry)
			throws IllegalArgumentException, IllegalAccessException {
		PDU pdu = newGetEntryPDU(entry);
		pdu.setType(PDU.GETNEXT);
		IMibClassMeta mibClassMeta = getMibClassMeta(entry.getClass(), target);
		if (pdu.size() <= 0) {
			OID indexOid = buildIndexOid(entry);
			Field[] indexFields = getIndexFields(entry.getClass());
			if (indexFields.length > 0) {
				IMibIndexMeta mibIndexMata = mibClassMeta
						.getIndexMeta(indexFields[0].getName());
				OID oid = new OID(mibIndexMata.getOid());
				if (indexOid != null) {
					oid.append(indexOid);
				}
				pdu.add(new VariableBinding(oid));
			}
		}
		return pdu;
	}

	private PDU newGetEntryPDU(Object entry, String[] fields)
			throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchFieldException {
		PDU pdu = new PDU();
		pdu.setType(PDU.GET);
		OID indexOid = buildIndexOid(entry);
		for (String fn : fields) {
			Field field = entry.getClass().getDeclaredField(fn);
			IMibPropertyMeta mibPropertyMeta = getMibClassMeta(
					entry.getClass(), target).getPropertyMeta(field.getName());
			OID oid = new OID(mibPropertyMeta.getOid());
			if (indexOid != null) {
				oid.append(indexOid);
			}
			pdu.add(new VariableBinding(oid));
		}
		return pdu;
	}

	private <T> T getEntryByIndex(T entry, PDU reqPDU) throws IOException,
			SnmpException, InstantiationException, IllegalAccessException {
		checkReqError(reqPDU);
		ResponseEvent event = snmp4J.get(reqPDU, getReadTarget());
		checkEventError(event);
		PDU resPDU = event.getResponse();
		checkResError(resPDU);
		fillProperties(entry, resPDU);
		return entry;
	}

	private <T> T buildEntryWithIndices(Class<T> entryClass,
			Serializable indices) throws InstantiationException,
			IllegalAccessException {
		T entry = entryClass.newInstance();
		Field[] indexFields = getIndexFields(entryClass);
		if (indexFields.length == 1) {
			indexFields[0].set(entry, ((Serializable[])indices)[0]);
		} else {
			Serializable[] indicesArray = (Serializable[]) indices;
			for (int i = 0; i < indexFields.length; i++) {
				indexFields[i].set(entry, indicesArray[i]);
			}
		}
		return entry;
	}

	private Field[] getPropFields(Class<?> clazz) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			IMibPropertyMeta propertyMeta = getMibClassMeta(clazz, target)
					.getPropertyMeta(field.getName());
			if (propertyMeta != null
					&& !(propertyMeta instanceof IMibIndexMeta)) {
				list.add(field);
			}
		}
		return list.toArray(new Field[list.size()]);
	}

	private void checkRowStatusAnnotation(IRowStatusMeta rowStatusMeta)
			throws SnmpAnnotationException {
		if (rowStatusMeta == null) {
			throw new SnmpAnnotationException(new NullPointerException(
					"No RowStatus Annotation."));
		}
	}

	private void checkIndexOid(OID indexOid) throws SnmpAnnotationException {
		if (indexOid == null) {
			throw new SnmpAnnotationException(new NullPointerException(
					"No index oid."));
		}
	}

	private void checkResError(PDU resPDU) throws SnmpException {
		if (resPDU == null) {
			SnmpException e = new SnmpException(SnmpException.NO_RESPONSE_PDU,
					-1);
			e.setSnmpErrorMsgProvider(getSnmpErrorMsgProvider());
			throw e;
		}
		if (resPDU.getErrorStatus() != 0) {
			SnmpException e = new SnmpException(resPDU.getErrorStatus(), resPDU
					.getErrorIndex());
			e.setSnmpErrorMsgProvider(getSnmpErrorMsgProvider());
			throw e;
		}
	}

	private void checkReqError(PDU reqPDU) {
		if (reqPDU.size() == 0) {
			throw new IllegalArgumentException("No declarative mib object.");
		}
	}

	private void checkEventError(ResponseEvent event) throws SnmpException {
		if (event.getError() != null) {
			SnmpException e = new SnmpException(event.getError());
			e.setSnmpErrorMsgProvider(getSnmpErrorMsgProvider());
			throw e;
		}
	}

	private IMibClassMeta getMibClassMeta(Class<?> clazz, ISnmpTarget target) {
		return MibMetaCache.getMibClassMeta(clazz, target);
	}

	/**
	 * add by chenwei
	 */

	/**
	 * @author chenwei
	 * @param oids
	 * @return
	 * @throws IOException
	 * @throws SnmpException
	 *             get multi-mibs node values, oid must start with ".",
	 *             otherwise insert into oid at before with dolkey.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Variable> get(Collection<String> oids) {
		Map<String, Variable> reqMap = new HashMap<String, Variable>();
		PDU reqPDU = new PDU();
		for (String oid : oids) {
			if (oid.indexOf(".") > 0) {
				if(oid.startsWith(pridot)){
					oid = "."+oid;
				}else{
					oid = dolkey + oid;
				}
			}
			if (key != null) {
				oid = oid.replace("$key", key);
			}
			VariableBinding vb = new VariableBinding(new OID(oid));
			reqPDU.add(vb);
		}
		checkReqError(reqPDU);
		ResponseEvent event = null;
		try {
			event = snmp4J.get(reqPDU, getReadTarget());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			checkEventError(event);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PDU resPDU = event.getResponse();
		try {
			checkResError(resPDU);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resPDU == null) {
			return null;
		}
		for (VariableBinding vb : (Vector<VariableBinding>) resPDU
				.getVariableBindings()) {
			reqMap.put(vb.getOid().toString(), vb.getVariable());
		}
		return reqMap;
	}

	public Map<String, Variable> queryTableCells(String rowindex,
			String[] columnList) {
		Vector<String> oidVec = new Vector<String>();
		String[] oidList = columnList.clone();
		for (int column = 0; column < columnList.length; ++column) {
			oidList[column] = columnList[column] + "." + rowindex;
		}
		oidVec.addAll(Arrays.asList(oidList));

		return get(oidVec);
	}

	/**
	 * get value for the column of table
	 * 
	 * @param oid
	 * @return
	 * @throws IOException
	 * @throws SnmpException
	 * @throws SnmpAnnotationException
	 */
	public List<VariableBinding> getMultiple(String oid) {
		List<VariableBinding> list = new ArrayList<VariableBinding>();
		List<OID> resOid = new ArrayList<OID>();
		PDU reqPDU = new PDU();
		reqPDU.setType(PDU.GETNEXT);
		if (oid.indexOf(".") > 0) {
			oid = dolkey + oid;
		}
		OID firstReqOid = new OID(oid);
		reqPDU.addOID(new VariableBinding(firstReqOid));
		while (true) {
			ResponseEvent event = null;
			try {
				event = snmp4J.getNext(reqPDU, getReadTarget());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				checkEventError(event);
			} catch (SnmpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PDU respPDU = event.getResponse();
			try {
				checkResError(respPDU);
			} catch (SnmpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			OID firstRespOid = respPDU.get(0).getOid();
			if (isTableEnd(firstReqOid, firstRespOid)) {
				break;
			}
			// Variable value = respPDU.get(0).getVariable();
			VariableBinding value = respPDU.get(0);
			if (!resOid.contains(firstRespOid))
				list.add(value);
			resOid.add(firstRespOid);
			reqPDU = respPDU;
		}
		return list;
	}

	@Override
	public void setOne(Object object) throws IOException, SnmpException {
		PDU pdu = new PDU();
		VariableBinding arg = (VariableBinding) object;
		System.out.println(arg);
		pdu.add(arg);
		pdu.setType(PDU.SET);
		ResponseEvent event = snmp4J.set(pdu, getWriteTarget());
		checkEventError(event);
		PDU resPDU = event.getResponse();
		checkResError(resPDU);
	}

	@Override
	public boolean set(Hashtable<String, Properties> devProp,
			Map<String, String> setVal) throws IOException, SnmpException {
		boolean success = false;
		PDU pdu = new PDU();
		for (Map.Entry<String, String> entry : setVal.entrySet()) {
			Variable value = null;
			String key = entry.getKey();
			String val = entry.getValue();
			Properties pp = devProp.get(key);
			String name = pp.getProperty("name");
			String oid = pp.getProperty("oid");
			String writeAccess = pp.getProperty("access");
			if (!"write".equalsIgnoreCase(writeAccess)) {
				continue;
			}
			String smitype = pp.getProperty("smitype");
			if (smitype.equalsIgnoreCase("integer")) {
				value = new Integer32(Integer.parseInt(setVal.get(name)));
			} else if(smitype.equalsIgnoreCase("gauge")){
				value = new Gauge32(Long.parseLong(setVal.get(name)));
			} else if (smitype.equalsIgnoreCase("display_string")) {
				value = new OctetString(setVal.get(name));
			} else if (smitype.equalsIgnoreCase("ipaddress")) {
				value = new IpAddress(setVal.get(name));
			} else if (smitype.equalsIgnoreCase("timeticks")) {
				value = new TimeTicks();
			} else if (smitype.equalsIgnoreCase("oid")) {
				value = new OID(setVal.get(name));
			}
			if (key != null) {
				oid = oid.replace("$key", key);
			}
			if (oid.startsWith(".")) {
				oid = oid.substring(1);
			}
			VariableBinding arg = new VariableBinding(new OID(oid), value);
			pdu.add(arg);
		}
		pdu.setType(PDU.SET);
		ResponseEvent event = snmp4J.set(pdu, getWriteTarget());
		checkEventError(event);
		PDU resPDU = event.getResponse();
		checkResError(resPDU);
		success = true;
		return success;
	}

	@Override
	public List<String> getIndexOid(String index) {
		List<String> indexOid = new ArrayList<String>();

		PDU reqPDU = new PDU();
		reqPDU.setType(PDU.GETNEXT);
		OID firstReqOid = new OID(index);
		reqPDU.addOID(new VariableBinding(firstReqOid));
		while (true) {
			ResponseEvent event = null;
			try {
				event = snmp4J.getNext(reqPDU, getReadTarget());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				checkEventError(event);
			} catch (SnmpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PDU respPDU = event.getResponse();
			try {
				checkResError(respPDU);
			} catch (SnmpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OID firstRespOid = respPDU.get(0).getOid();
			if (isTableEnd(firstReqOid, firstRespOid)) {
				break;
			}
			String value = respPDU.get(0).getOid().toString();
			if (!value.startsWith(".")) {
				value = "." + value;
			}
			String indexVal = value.replaceAll(index + ".", "");
			reqPDU = respPDU;
			indexOid.add(indexVal);
			System.out.println("add index is:" + indexVal);
		}
		return indexOid;
	}

	@Override
	public Map<String, Variable> queryIndexColumnMap(String oidColumn) {
		Map<String, Variable> map = new HashMap<String, Variable>();
		PDU reqPDU = new PDU();
		reqPDU.setType(PDU.GETNEXT);
		if (key != null) {
			oidColumn = oidColumn.replace("$key", key);
		}
		OID firstReqOid = new OID(oidColumn);
		reqPDU.addOID(new VariableBinding(firstReqOid));
		while (true) {
			ResponseEvent event = null;
			try {
				event = snmp4J.getNext(reqPDU, getReadTarget());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				checkEventError(event);
			} catch (SnmpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PDU respPDU = event.getResponse();
			try {
				checkResError(respPDU);
			} catch (SnmpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OID firstRespOid = respPDU.get(0).getOid();
			if (isTableEnd(firstReqOid, firstRespOid)) {
				break;
			}
			Variable value = respPDU.get(0).getVariable();
			String oid = firstRespOid.toString();
			if (!oid.startsWith(".")) {
				oid = "." + oid;
			}
			String indexVal = oid.replaceAll(oidColumn + ".", "");
			map.put(indexVal, value);
			reqPDU = respPDU;
		}
		return map;
	}

	@Override
	public VariableBinding queryNextOid(String oid) {
		String result = null;
		PDU reqPDU = new PDU();
		reqPDU.setType(PDU.GETNEXT);
		if (oid.indexOf(".") > 0) {
			oid = "." + oid;
		}
		OID firstReqOid = new OID(oid);
		reqPDU.addOID(new VariableBinding(firstReqOid));
		// while(true){
		ResponseEvent event = null;
		try {
			event = snmp4J.getNext(reqPDU, getReadTarget());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			checkEventError(event);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PDU respPDU = event.getResponse();
		try {
			checkResError(respPDU);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(respPDU==null){
			return null;
		}
		OID firstRespOid = respPDU.get(0).getOid();
//		if (isTableEnd(firstReqOid, firstRespOid)) {
//			// break;
//			return null;
//		}
//		Variable value = respPDU.get(0).getVariable();
//		reqPDU = respPDU;
//		result = value.toString();
		// }
		return respPDU.get(0);
	}
	
	public String queryNext2String(String oid){
		VariableBinding vb = queryNextOid(oid);
		Variable value = vb.getVariable();
		return value.toString();
	}

	@Override
	public String queryOid(String oid) {
		String result = null;
		PDU reqPDU = new PDU();
		reqPDU.setType(PDU.GET);
		if (oid.indexOf(".") > 0) {
			oid = dolkey + oid;
		}
		OID firstReqOid = new OID(oid);
		reqPDU.addOID(new VariableBinding(firstReqOid));
		// while(true){
		ResponseEvent event = null;
		try {
			event = snmp4J.get(reqPDU, getReadTarget());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			checkEventError(event);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PDU respPDU = event.getResponse();
		try {
			checkResError(respPDU);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// OID firstRespOid = respPDU.get(0).getOid();
		// if (isTableEnd(firstReqOid, firstRespOid)) {
		// // break;
		// return null;
		// }
		Variable value = respPDU.get(0).getVariable();
		reqPDU = respPDU;
		result = value.toString();
		// }
		return result;
	}

	@Override
	public void setCustField(Object entry, ArrayList<String> fields)
			throws IOException, SnmpException, SnmpAnnotationException {
		try {
			PDU reqPDU = newSetPDU(entry, fields);
			checkReqError(reqPDU);
			ResponseEvent event = snmp4J.set(reqPDU, getWriteTarget());
			checkEventError(event);
			PDU resPDU = event.getResponse();
			checkResError(resPDU);
		} catch (InstantiationException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalAccessException e) {
			throw new SnmpAnnotationException(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			throw new SnmpAnnotationException(e);
		} catch (NoSuchMethodException e) {
			throw new SnmpAnnotationException(e);
		} catch (InvocationTargetException e) {
			throw new SnmpAnnotationException(e);
		}
	}

	@Override
	public List<VariableBinding> getBulk(String[] oids) {
		PDU pdu = new PDU();
		for (String oid : oids) {
			pdu.add(new VariableBinding(new OID(oid)));
		}
		pdu.setMaxRepetitions(10);
		// Get ifNumber only once
		pdu.setNonRepeaters(0);

		Target ct = getReadTarget();
		ct.setVersion(SnmpConstants.version2c);
		ct.setRetries(2);
		ct.setTimeout(3000);
		PDU reqPDU = pdu;
		Map<Integer, String> req = new HashMap<Integer, String>();
		BulkData blkData = new BulkData();
		blkData.setReqOid(new Vector(Arrays.asList(oids)));
		while (true) {
			try {
				ResponseEvent re = snmp4J.getBulk(reqPDU, getReadTarget());
				PDU respPDU = re.getResponse();
				if (respPDU == null) {
					break;
				}
				handleResp(blkData, respPDU);
				if (blkData.isEnd()) {
					break;
				}
				int maxrt = reqPDU.getMaxRepetitions();
				int nonrt = reqPDU.getNonRepeaters();
				reqPDU = blkData.getNxtReq();
				reqPDU.setMaxRepetitions(maxrt);
				reqPDU.setNonRepeaters(nonrt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return blkData.getResp();
	}

	@Override
	public Vector<Map<String, VariableBinding>> getBulk(
			Map<String, Properties> mp, int repetitions, boolean isSingle) {
		// PDU pdu = new PDU();
		// // Map<Integer, Properties> mpp = new HashMap<Integer, Properties>();
		Vector<Map<String, VariableBinding>> allblk = new Vector<Map<String, VariableBinding>>();
		Map<String, String> mapoid = new HashMap<String, String>();
		Vector<String> sortOid = new Vector<String>();
		for (Map.Entry<String, Properties> e : mp.entrySet()) {
			String name = e.getKey();
			Properties p = e.getValue();
			String oid = p.getProperty("oid");
			if(oid.equals("")){
				continue;
			}
			mapoid.put(oid, name);
			// pdu.add(new VariableBinding(new OID(oid)));
			sortOid.addElement(oid);
		}
		//		

		List<VariableBinding> allVbs = getBulkVar(mp, repetitions, isSingle);

		Map<String, VariableBinding> map = null;
		int row = 1;
		int oidsize = sortOid.size();
		boolean[] b = new boolean[oidsize];
		for (VariableBinding vb : allVbs) {
			String oid = vb.getOid().toString();
			for (int i = 0; i < oidsize; i++) {
				String o = sortOid.elementAt(i);
				if (oid.equals(o)||(oid.startsWith(o)&&oid.charAt(o.length())=='.')) {
					boolean cstat = b[i];
					if (cstat) {
						map = new HashMap<String, VariableBinding>();
						b = new boolean[oidsize];
						allblk.addElement(map);
					} else if (map == null) {
						map = new HashMap<String, VariableBinding>();
						allblk.addElement(map);
					}
					map.put(mapoid.get(o), vb);
					b[i] = true;
					break;
				}
			}
		}

		return allblk;
	}

	private void handleResp(BulkData blkData, PDU respPDU) {
		Vector<? extends VariableBinding> vv = respPDU.getVariableBindings();
		Map<Integer, String> req = new HashMap<Integer, String>();
		Vector<String> oids = blkData.getReqOid();
		Vector<VariableBinding> respVb = blkData.getResp();
		PDU nxtPDU = blkData.getNxtReq();
		Map<String, Boolean> oidst = blkData.getOidStatus();
		int size = oids.size();
		boolean stop = false;
		for (int i = 0; i < vv.size(); i++) {
			if (stop) {
				break;
			}
			VariableBinding vb = vv.elementAt(i);
			String oidstr = vb.getOid().toString();
			int mod = i % size;
			for (int j = 0; j < size; j++) {
				if (blkData.isEnd()) {
					stop = true;
					break;
				}
				if (oidstr.startsWith(oids.get(j)) && mod == j) {
					req.put(j, oidstr);
					respVb.add(vb);
					break;
				}
				if (j == size - 1) {
					// blkData.setEnd(true);
					oidst.put(oids.get(mod), true);
					break;
				}
			}
		}
		nxtPDU.clear();
		Vector<String> rmreqOid = new Vector<String>();
		for (int i = 0; i < size; i++) {
			String oid = oids.get(i);
			if (oidst.get(oid)) {
				rmreqOid.add(oid);
				continue;
			}
			nxtPDU.add(new VariableBinding(new OID(req.get(i))));
		}
		oids.removeAll(rmreqOid);
	}

	private void handleSingleOid(BulkData blkData, PDU respPDU) {
		Vector<? extends VariableBinding> vv = respPDU.getVariableBindings();
		Map<Integer, String> req = new HashMap<Integer, String>();
		Vector<String> oids = blkData.getReqOid();
		Vector<VariableBinding> respVb = blkData.getResp();
		PDU nxtPDU = blkData.getNxtReq();
		Map<String, Boolean> oidst = blkData.getOidStatus();
		int size = oids.size();
		for (int i = 0; i < vv.size(); i++) {
			VariableBinding vb = vv.elementAt(i);
			String oidstr = vb.getOid().toString();
			for (int j = 0; j < oids.size(); j++) {
				if (req.values().contains(oidstr)) {
					continue;
				}
				String tmpoid = oids.get(j);
				if (oidstr.startsWith(tmpoid)&& 
						(oidstr.equals(tmpoid)||oidstr.charAt(oids.get(j).length())=='.')) {
					req.put(j, oidstr);
					respVb.add(vb);
					oidst.put(oids.get(j), true);
					break;
				}
			}
		}
		nxtPDU.clear();
		Vector<String> rmreqOid = new Vector<String>();
		for (int i = 0; i < size; i++) {
			String oid = oids.get(i);
			if (oidst.get(oid)) {
				rmreqOid.add(oid);
				continue;
			}
			if(req.size()==0){
				continue;
			}
			String v = req.get(i);
			if(v!=null)
				nxtPDU.add(new VariableBinding(new OID(req.get(i))));
		}
		oids.removeAll(rmreqOid);
	}
	
	public VariableBinding get(String oid){
		PDU reqPDU = new PDU();

		VariableBinding vb = new VariableBinding(new OID(oid));
		reqPDU.add(vb);
//		checkReqError(reqPDU);
//		ResponseEvent event = null;
//		try {
//			event = snmp4J.get(reqPDU, getReadTarget());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(event==null){
//			return null;
//		}
//		try {
//			checkEventError(event);
//		} catch (SnmpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PDU resPDU = event.getResponse();
//		try {
//			checkResError(resPDU);
//		} catch (SnmpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (resPDU == null) {
//			return null;
//		}
		PDU resPDU = get(reqPDU);
		if(resPDU==null){
			return null;
		}
		return resPDU.get(0);
	}

	@Override
	public Variable getone(String oid) {
		this.setTimeout(5000);
		VariableBinding vb = get(oid);
		if(vb == null){
			return null;
		}
		return vb.getVariable();
	}
	
	private PDU get(PDU reqPDU){
		checkReqError(reqPDU);
		ResponseEvent event = null;
		try {
			event = snmp4J.get(reqPDU, getReadTarget());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(event==null){
			return null;
		}
		try {
			checkEventError(event);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PDU resPDU = event.getResponse();
		try {
			checkResError(resPDU);
		} catch (SnmpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resPDU == null) {
			return null;
		}
		return resPDU;
	}

	// it contain single and table variable
	@Override
	public Map<String, Object> getMixBulk(Map<String, Properties> mp) {
		Map<String, Object> rowVal = new HashMap<String, Object>();
		Map<String, Properties> scalaroids = new HashMap<String, Properties>();
		Map<String, Properties> tableoids = new HashMap<String, Properties>();
		Map<String, Properties> addoids = new HashMap<String, Properties>();
		Map<String, Properties> tablesumoids = new HashMap<String, Properties>();
		for (Map.Entry<String, Properties> e : mp.entrySet()) {
			String name = e.getKey();
			Properties p = e.getValue();
			String oid = p.getProperty("oid");
			if(oid==null||oid.equals("")){
				continue;
			}
			String type = p.getProperty("type");
			if ("scalar".equalsIgnoreCase(type)) {
				scalaroids.put(name, p);
			} else if("add".equalsIgnoreCase(type)){
				addoids.put(name, p);
			} else if("multisum".equalsIgnoreCase(type)){
				tablesumoids.put(name, p);
			} else if(("blank").equalsIgnoreCase(type)){
				rowVal.put(name, "");
			} else {
				tableoids.put(name, p);				
			}
		}
		Vector<Map<String, VariableBinding>> val = new Vector<Map<String, VariableBinding>>();
		if (scalaroids.size() > 0) {
//			System.out.println("");
			Map<String, VariableBinding> singleVal = getSimpleVar(scalaroids);
			// val.addAll(scalarvars);
			// Map<String, VariableBinding> singleVal = scalarvars.get(0);
			for (Map.Entry<String, VariableBinding> et : singleVal.entrySet()) {
				rowVal.put(et.getKey(), et.getValue());
			}

		}
		if (tableoids.size() > 0) {
			Vector<Map<String, VariableBinding>> tablevars = getBulk(tableoids,
					50, false);
			val.addAll(tablevars);
			for (Map.Entry<String, Properties> entry : tableoids.entrySet()) {
				Properties pp = entry.getValue();
				List<VariableBinding> v = getBulk(pp);
				rowVal.put(entry.getKey(), v);

			}
		}
		
		if(addoids.size() > 0){//exist A+B
			for(Map.Entry<String, Properties> entry : addoids.entrySet()){
				Properties pp = entry.getValue();
				String oids = pp.getProperty("oid");
				String[] splst = oids.split("\\+");
				long result = 0;
				for(String sp:splst){
					Object obj = rowVal.get(sp);
					if(obj instanceof List){
						List<VariableBinding> lst = (List)obj;
						for(VariableBinding vb:lst){
							result += vb.getVariable().toLong();
						}
					}else{
						result += Long.valueOf(obj+"");
					}
				}
				rowVal.put(entry.getKey(), result);
			}
		}
		
		if(tablesumoids.size() > 0){//such as 1...3
			for(Map.Entry<String, Properties> entry : addoids.entrySet()){
				Properties pp = entry.getValue();
				String oids = pp.getProperty("oid");
				String befp = oids.substring(0, oids.lastIndexOf("."));
				String endp = oids.substring(oids.lastIndexOf("."));
				String[] beginend = endp.split("-");
				int intStart = Integer.parseInt(beginend[0]);
				int intEnd = Integer.parseInt(beginend[1]);
				String[] oidv = new String[intEnd-intStart+1];
				for(int i=0;i<oidv.length;i++){
					oidv[i] = befp+"."+(intStart+i);
				}
				List<VariableBinding> res = this.getBulk(oidv);
				int sum = 0;
				for(VariableBinding vb:res){
					sum += vb.getVariable().toInt();
				}
				rowVal.put(entry.getKey(), sum);
				//need consider
			}
			
		}

		return rowVal;
	}

	// this method is used in the single node, you can use get method
	public Map<String, VariableBinding> getSimpleVar(
			Map<String, Properties> scalaroids) {
		PDU pdu = new PDU();
		Map<String, String> tmp = new HashMap<String, String>();
		Map<String, VariableBinding> result = new HashMap<String, VariableBinding>();
		for (Map.Entry<String, Properties> entry : scalaroids.entrySet()) {
			String key = entry.getKey();
			String oidstr = entry.getValue().getProperty("oid");
			if(oidstr==null||oidstr.equals("")){
				continue;
			}
			pdu.add(new VariableBinding(new OID(oidstr)));
			tmp.put(oidstr, key);
		}
		Target ct = getReadTarget();
		ct.setVersion(SnmpConstants.version2c);
		ct.setTimeout(3000);
		ResponseEvent re = null;
		try {
			re = snmp4J.get(pdu, getReadTarget());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PDU respPDU = re.getResponse();
		if (respPDU == null) {
			return result;
		}
		Vector respv = respPDU.getVariableBindings();
		for (int i = 0; i < respv.size(); i++) {
			VariableBinding vb = (VariableBinding) respv.get(i);
			result.put(tmp.get(vb.getOid().toString()), vb);
		}
		return result;
	}

	@Override
	public Vector<VariableBinding> getBulkVar(Map<String, Properties> mp,
			int repetitions, boolean isSingle) {
		PDU pdu = new PDU();
		// Map<Integer, Properties> mpp = new HashMap<Integer, Properties>();
		Vector<Properties> allblk = new Vector<Properties>();
		Map<String, String> mapoid = new HashMap<String, String>();
		Vector<String> sortOid = new Vector<String>();
		boolean secHandle = false;
		for (Map.Entry<String, Properties> e : mp.entrySet()) {
			String name = e.getKey();
			Properties p = e.getValue();
			String oid = p.getProperty("oid");

			mapoid.put(oid, name);
			pdu.add(new VariableBinding(new OID(oid)));
			sortOid.addElement(oid);
		}

		pdu.setMaxRepetitions(repetitions);
		// Get ifNumber only once
		pdu.setNonRepeaters(0);

		Target ct = getReadTarget();
		ct.setVersion(SnmpConstants.version2c);
		ct.setRetries(4);
		ct.setTimeout(5000);
		PDU reqPDU = pdu;
		Map<Integer, String> req = new HashMap<Integer, String>();
		BulkData blkData = new BulkData();
		blkData.setReqOid((Vector) sortOid.clone());
		while (true) {
			try {
				ResponseEvent re = snmp4J.getBulk(reqPDU, getReadTarget());
				PDU respPDU = re.getResponse();
				if (respPDU == null) {
					System.out.println("respPDU is null");
					break;
				}
				int rqlen = reqPDU.getVariableBindings().get(0).getOid().toIntArray().length;
				Vector<? extends VariableBinding> vvb = respPDU.getVariableBindings();
				int rslen = vvb.get(0).getOid().toIntArray().length;
				if(rslen==rqlen){
					isSingle = true;
					secHandle = true;
					break;
				}
				if (isSingle) {
					handleSingleOid(blkData, respPDU);
				} else {
					handleResp(blkData, respPDU);
				}
				if (blkData.isEnd()) {
					break;
				}
				int maxrt = reqPDU.getMaxRepetitions();
				int nonrt = reqPDU.getNonRepeaters();
				reqPDU = blkData.getNxtReq();
				reqPDU.setMaxRepetitions(maxrt);
				reqPDU.setNonRepeaters(nonrt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break; 
			}
		}
		if(secHandle){
			try {
				ResponseEvent mv = snmp4J.get(reqPDU, getReadTarget());
				PDU respPDU = mv.getResponse();
				if (respPDU == null) {
					System.out.println("respPDU is null");
				}
				handleSingleOid(blkData, respPDU);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		return blkData.getResp();

	}
	
	public Map<String, Variable> getMultiOids(Map<String, Properties> mp){
		PDU pdu = new PDU();
		Map<String, Variable> mapRes = new HashMap<String, Variable>();
		Map<String, String> mapoid = new HashMap<String, String>();
		for (Map.Entry<String, Properties> e : mp.entrySet()) {
			String name = e.getKey();
			Properties p = e.getValue();
			String oid = p.getProperty("oid");

			mapoid.put(oid, name);
			pdu.add(new VariableBinding(new OID(oid)));
		}
		PDU res = get(pdu);
		Vector<? extends VariableBinding> result = res.getVariableBindings();
		for(VariableBinding vb:result){
			mapRes.put(mapoid.get(vb.getOid().toString()), vb.getVariable());
		}
		return mapRes;
	}

	public Map<String, Variable> getMultiOids(Map<String, Properties> mp, String key, String value){
		PDU pdu = new PDU();
		Map<String, Variable> mapRes = new HashMap<String, Variable>();
		Map<String, String> mapoid = new HashMap<String, String>();
		for (Map.Entry<String, Properties> e : mp.entrySet()) {
			String name = e.getKey();
			Properties p = e.getValue();
			String oid = p.getProperty("oid");
			if(key!=null){
				oid = oid.replaceAll(key, value);
			}
			mapoid.put(oid, name);
			pdu.add(new VariableBinding(new OID(oid)));
		}
		PDU res = get(pdu);
		Vector<? extends VariableBinding> result = res.getVariableBindings();
		for(VariableBinding vb:result){
			mapRes.put(mapoid.get(vb.getOid().toString()), vb.getVariable());
		}
		return mapRes;
	}	
	
	@Override
	public Vector<Map<String, VariableBinding>> getBulk(
			Map<String, Properties> mp, Object[] idx, int repetitions,
			boolean isSingle) {
		OID appendOID = null;
		for (Object o : idx) {
			if (o instanceof String) {
				String val = (String) o;
				byte[] b = val.getBytes();
				int[] integers = new int[b.length];
				for (int i = 0; i < integers.length; i++) {
					integers[i] = b[i];
					appendOID = new OID(integers);
				}
			} else if (o instanceof Integer) {
				int v = (Integer) o;
				appendOID = new OID(new int[] { v });
			}
		}
		for (Map.Entry<String, Properties> entry : mp.entrySet()) {
			Properties pp = entry.getValue();
			String oid = pp.getProperty("oid");
			OID newoid = new OID(oid).append(appendOID);
			String oidstr = newoid.toString();
			pp.setProperty("oid", oidstr);
		}
		return getBulk(mp, repetitions, isSingle);
	}

	// sameIdx is the first index, and every leaf must have
	// it fit with multiple or not same index, such as col1 idx:a,b col2 idx:a,c
	public Map<String, Object> getComplexTable(Map<String, Properties> mp,
			Object[] sameIdx) {
		Map<String, Object> result = new HashMap<String, Object>();
		OID appendOID = new OID();
		for (Object obj : sameIdx) {
			if (obj instanceof String) {
				String val = (String) obj;
				if(val.indexOf(":")==-1){
					if(val.length()==20){
						byte[] b = val.getBytes();
						int[] integers = new int[b.length + 1];
						integers[0] = 20;
						for (int i = 1; i < integers.length; i++) {
							integers[i] = b[i - 1];
						}
						appendOID.append(new OID(integers));
					}else{
						appendOID.append(val);
					}
				} else {
					byte[] b = val.getBytes();
					int[] integers = new int[b.length + 1];
					integers[0] = 17;
					for (int i = 1; i < integers.length; i++) {
						integers[i] = b[i - 1];
					}
					appendOID.append(new OID(integers));
				}
			} else if (obj instanceof Integer) {
				int v = (Integer) obj;
				appendOID.append(new OID(new int[] { v }));
			}
		}
		Map<String, Properties> canSingle = new HashMap<String, Properties>();
		// Map<String, Properties> canBulk = new HashMap<String, Properties>();
		Map<String, List<String>> shouldAdd = new HashMap<String, List<String>>();
		for (Map.Entry<String, Properties> entry : mp.entrySet()) {
			String key = entry.getKey();
			Properties pp = entry.getValue();
			String oid = pp.getProperty("oid");
			if(oid==null||oid.equals("")||oid.length()<5){
				continue;
			}
			if(oid.indexOf("+")>-1){
				String spl[] = oid.split("\\+");				
				shouldAdd.put(key, Arrays.asList(spl));
				continue;
			}
			OID newoid = new OID(oid).append(appendOID);
			String oidstr = newoid.toString();
			Properties pts = (Properties) pp.clone();
			pts.setProperty("oid", oidstr);
			String idx = pp.getProperty("index");
			if(idx==null){
				String type = pp.getProperty("type");
				if(type!=null&&type.equalsIgnoreCase("blank")){//don't find oid
					VariableBinding v = new VariableBinding();
					v.setVariable(new Integer32(0));
					result.put(key, v);
					continue;
				}				
			}
			String[] split = idx.split(",");
			if (split.length == sameIdx.length) {
				canSingle.put(key, pts);
			} else {
				List<VariableBinding> v = getBulk(pts);
				result.put(key, v);
			}
		}
		Map<String, VariableBinding> simple = this.getSimpleVar(canSingle);
		result.putAll(simple);
		if(shouldAdd.size()>0){
			for(Map.Entry<String, List<String>> e:shouldAdd.entrySet()){
				String key = e.getKey();
				List<String> val = e.getValue();
				int res = 0;
				for(String s:val){
					VariableBinding vb = simple.get(s);
					if(vb!=null)
						res += vb.getVariable().toInt();
				}
				VariableBinding v = new VariableBinding();
				v.setVariable(new Integer32(res));
				result.put(key, v);
			}
		}
		return result;
	}

	private List<VariableBinding> getBulk(Properties pp) {
		List<VariableBinding> allvb = getBulk(new String[] { pp
				.getProperty("oid") });
		return allvb;
	}

	@Override
	public Map<String, Variable> get(Map<String, String> oids) {
		Map<String, Variable> vresult = new HashMap<String, Variable>();
		Map<String, Variable> result = get(oids.keySet());
		for(Map.Entry<String, Variable> entry:result.entrySet()){
			String oid = entry.getKey();
			String v = oids.get(oid);
			if(v!=null){
				vresult.put(v, entry.getValue());
			}
		}
		return vresult;
	}

}