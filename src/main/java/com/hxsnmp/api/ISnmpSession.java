/*
 * Copyright 2005-2006 the original authors and www.opengoss.org community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hxsnmp.api;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 * Core interface of SnmpHiberate that is used to execute SNMP operations.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 */
public interface ISnmpSession {

	/**
	 * Get SNMP retries.
	 * 
	 * @return SNMP retries.
	 */
	int getRetries();

	/**
	 * Set SNMP retries.
	 * 
	 * @param retries
	 *            SNMP retries
	 */
	void setRetries(int retries);

	/**
	 * Get SNMP Timeout.
	 * 
	 * @return SNMP timeout
	 */
	int getTimeout();

	/**
	 * Set SNMP Timeout.
	 * 
	 * @param timeout
	 *            SNMP timeout.
	 */
	void setTimeout(int timeout);

	/**
	 * Get scalar group.
	 * 
	 * @param scalarClass
	 *            class of the Java Value Object that represents one scalar
	 *            group.
	 * 
	 * @return Return an instance of the class.
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	<T> T get(Class<T> scalarClass) throws IOException, SnmpException,
			SnmpAnnotationException;

	/**
	 * Get a scalar mib item.
	 * 
	 * @param scalarClass
	 *            class of the Java Value Object that represents one scalar
	 *            group.
	 * @param field
	 *            field name of scalar class.
	 * 
	 * @return Return an instance of the class.
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	<T> T get(Class<T> scalarClass, String field) throws IOException,
			SnmpException, SnmpAnnotationException;

	/**
	 * Get specified fields of a scalar group.
	 * 
	 * @param scalarClass
	 *            class of the Java Value Object that represents one scalar
	 *            group.
	 * @param fields
	 *            specified fields of the scalar object.
	 * @return Return an instance of the class that only has the specified
	 *         attributes
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	<T> T get(Class<T> scalarClass, String[] fields) throws IOException,
			SnmpException, SnmpAnnotationException;

	/**
	 * Get a whole mib table.
	 * 
	 * @param entryClass
	 *            Class of the Java Value Object that represents one mib entry.
	 * 
	 * @return a list of instances of the class.
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	<T> List<T> getTable(Class<T> entryClass) throws IOException,
			SnmpException, SnmpAnnotationException;

	// add by chenwei to get 'size' records
	<T> List<T> getTable(Class<T> entryClass, int size) throws IOException,
			SnmpException, SnmpAnnotationException;

	<T> List<T> getTableNext(T entry, int size) throws IOException,
			SnmpException, SnmpAnnotationException;

	/**
	 * Get a mib entry by index.
	 * 
	 * @param entryClass
	 *            Class of the Java Value Object that represents one mib entry.
	 * @param indices
	 *            If there is only one index, this param is the just the object;
	 *            if there are multiple indices, this param is an array.
	 * 
	 * @return a mib entry with whole properties.
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	<T> T getByIndex(Class<T> entryClass, Serializable indices)
			throws IOException, SnmpException, SnmpAnnotationException;

	/**
	 * Get specified fields of a mib entry by index.
	 * 
	 * @param entryClass
	 *            Class of the Java Value Object that represents one mib entry.
	 * @param indices
	 *            If there is only one index, this param is the just the object;
	 *            if there are multiple indices, this param is an array.
	 * @param fileds
	 *            specified fields of the mib entry object.
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	<T> T getByIndex(Class<T> entryClass, Serializable indices, String[] fields)
			throws IOException, SnmpException, SnmpAnnotationException;

	/**
	 * Set a scalar group or a mib entry.
	 * 
	 * @param object
	 *            a Java Value Object represents a scalar group or a mib entry.
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	void set(Object object) throws IOException, SnmpException,
			SnmpAnnotationException;

	/**
	 * Create a mib entry.
	 * 
	 * @param entry
	 *            a mib entry.
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	void create(Object entry) throws IOException, SnmpException,
			SnmpAnnotationException;

	/**
	 * Delete a mib entry.
	 * 
	 * @param entry
	 * 
	 * @throws IOException
	 *             When IO errors occured while communicating.
	 * @throws SnmpException
	 *             When SNMP errors occured.
	 * @throws SnmpAnnotationException
	 *             Annotaction Error for your Java Value Object.
	 */
	void delete(Object entry) throws IOException, SnmpException,
			SnmpAnnotationException;

	/**
	 * Close this session.
	 * 
	 * @throws IOException
	 *             When IO errors occured.
	 */
	void close() throws IOException;

	/**
	 * add by chenwei
	 */
	// add by chenwei for custom field to set, 2010.02.02
	void setCustField(Object entry, ArrayList<String> fields)
			throws IOException, SnmpException, SnmpAnnotationException;

	VariableBinding get(String oid);
	
	Variable getone(String oid);

	/**
	 * @author chenwei set one mib key
	 */
	void setOne(Object object) throws IOException, SnmpException;

	/**
	 * @author chenwei set multi mibs key
	 */
	boolean set(Hashtable<String, Properties> devProp,
			Map<String, String> setVal) throws IOException, SnmpException;

	void setKey(String key);

	String getKey();

	Map get(Collection<String> oids);

	List<VariableBinding> getMultiple(String oid);

	Vector<Map<String, VariableBinding>> getBulk(Map<String, Properties> mp,
			int repetitions, boolean isSingle);

	Vector<Map<String, VariableBinding>> getBulk(Map<String, Properties> mp,
			Object[] idx, int repetitions, boolean isSingle);

	Vector<VariableBinding> getBulkVar(Map<String, Properties> mp,
			int repetitions, boolean isSingle);

	Map<String, Object> getMixBulk(Map<String, Properties> mp);

	Map<String, VariableBinding> getSimpleVar(Map<String, Properties> scalaroids);

	Map<String, Object> getComplexTable(Map<String, Properties> mp,
			Object[] sameIdx);

	List<VariableBinding> getBulk(String[] oids);
	
	Map<String, Variable> get(Map<String, String> oids);

	List<String> getIndexOid(String index);

	Map<String, Variable> queryTableCells(String rowindex, String[] columnList);

	Map<String, Variable> queryIndexColumnMap(String oid);

	String queryOid(String oid);

	VariableBinding queryNextOid(String oid);
	
	String queryNext2String(String oid);
	
	Map<String, Variable> getMultiOids(Map<String, Properties> mp);

	Map<String, Variable> getMultiOids(Map<String, Properties> mp, String key, String value);
}
