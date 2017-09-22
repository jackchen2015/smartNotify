/*
 * Copyright 2005-2007 the original authors and www.opengoss.org community.
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

import java.util.HashMap;
import java.util.Map;

import nu.xom.Element;
import nu.xom.Elements;

/**
 * Mib class metadata got from XML mapping file.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 */
public class XmlMibClassMeta extends MibClassMeta {

	private Element mappingElement;

	public XmlMibClassMeta(Element mappingElement) {
		this.mappingElement = mappingElement;
	}

	public boolean isTable() {
		Elements indexElements = mappingElement.getChildElements("index");
		return (indexElements != null) && (indexElements.size() > 0);
	}

	public IRowStatusMeta getRowStatusMeta() {
		Elements rsElements = mappingElement.getChildElements("rowstatus");
		if (rsElements == null || rsElements.size() == 0) {
			return null;
		}
		return new XmlRowStatusMeta(rsElements.get(0));
	}

	@Override
	public Map<String, IMibPropertyMeta> initPropertyMetaMap() {
		Map<String, IMibPropertyMeta> propertyMetaMap = new HashMap<String, IMibPropertyMeta>();
		Elements indexElements = mappingElement.getChildElements("index");
		for (int i = 0; i < indexElements.size(); i++) {
			propertyMetaMap.put(indexElements.get(i).getAttributeValue("name"),
					new XmlMibIndexMeta(indexElements.get(i)));
		}
		Elements propertyElements = mappingElement.getChildElements("property");
		for (int i = 0; i < propertyElements.size(); i++) {
			propertyMetaMap.put(propertyElements.get(i).getAttributeValue(
					"name"), new XmlMibPropertyMeta(propertyElements.get(i)));
		}
		return propertyMetaMap;
	}

}