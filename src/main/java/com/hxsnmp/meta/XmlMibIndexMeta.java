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

/**
 * Mib index metadata got from XML mapping file.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 */
public class XmlMibIndexMeta extends XmlMibPropertyMeta implements
		IMibIndexMeta {

	public XmlMibIndexMeta(Element element) {
		super(element);
	}

	public int getIndexLength() {
		String length = getElement().getAttributeValue("length");
		if (length == null) {
			length = "1";
		}
		return Integer.parseInt(length);
	}

	public int getIndexNo() {
		String no = getElement().getAttributeValue("no");
		if (no == null) {
			no = "0";
		}
		return Integer.parseInt(no);
	}

}