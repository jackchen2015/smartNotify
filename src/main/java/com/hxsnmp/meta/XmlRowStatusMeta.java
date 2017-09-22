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
 * Rowstatus medatadata got from XML mapping file.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 */
public class XmlRowStatusMeta implements IRowStatusMeta {

	public Element rsElement;

	public XmlRowStatusMeta(Element element) {
		this.rsElement = element;
	}

	public int getCreateEnum() {
		String create = rsElement.getAttributeValue("create");
		if (create == null) {
			create = "4";
		}
		return Integer.parseInt(create);
	}

	public int getDeleteEnum() {
		String delete = rsElement.getAttributeValue("delete");
		if (delete == null) {
			delete = "6";
		}
		return Integer.parseInt(delete);
	}

	public String getOid() {
		return rsElement.getAttributeValue("oid");
	}

}