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

import nu.xom.Element;

import com.hxsnmp.annotation.MibObjectType.Access;
import com.hxsnmp.api.SmiType;

/**
 * Mib property metadata got from XML mapping file
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 */
public class XmlMibPropertyMeta implements IMibPropertyMeta {

	private Element element;

	public XmlMibPropertyMeta(Element element) {
		this.element = element;
	}

	public Access getAccess() {
		String access = element.getAttributeValue("access");
		if (access == null) {
			return Access.READ;
		}
		return Access.valueOf(access.toUpperCase());
	}

	public String getOid() {
		return element.getAttributeValue("oid");
	}

	public SmiType getSmiType() {
		String smiType = element.getAttributeValue("smi_type");
		if (smiType == null) {
			return SmiType.INTEGER;
		}
		return SmiType.valueOf(smiType.toUpperCase());
	}

	protected Element getElement() {
		return element;
	}

}