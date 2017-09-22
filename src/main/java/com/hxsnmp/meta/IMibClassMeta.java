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
package com.hxsnmp.meta;

import java.util.Map;

/**
 * Mib class metadata.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 2007-01-12
 */
public interface IMibClassMeta {

	/**
	 * Is a table?
	 * 
	 * @return true if a mib table class.
	 */
	boolean isTable();

	/**
	 * Get property metadatas.
	 * 
	 * @return property metadatas.
	 */
	Map<String, IMibPropertyMeta> getPropertyMetaMap();

	/**
	 * Get rowstatus metadata. Return null if this is not a mib table.
	 * 
	 * @return rowstatus metadata.
	 */
	IRowStatusMeta getRowStatusMeta();

	/**
	 * Get property metadata by property name.
	 * 
	 * @param property
	 *            property name.
	 * 
	 * @return property metadata
	 */
	IMibPropertyMeta getPropertyMeta(String property);

	/**
	 * Get index property metadata by property name.
	 * 
	 * @param index
	 *            index property name.
	 * @return index property metadata
	 */
	IMibIndexMeta getIndexMeta(String index);

	/**
	 * Is the property a mib index?
	 * 
	 * @param property
	 *            property name.
	 * @return true if the property is a mib index.
	 */
	boolean isIndex(String property);

}