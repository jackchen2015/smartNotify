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

import com.hxsnmp.annotation.MibObjectType;
import com.hxsnmp.annotation.MibObjectType.Access;
import com.hxsnmp.api.SmiType;

/**
 * Mib property metadata got from annotation <code>MibObjectType</code>.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 * @see MibObjectType
 */
public class AnnoMibPropertyMeta implements IMibPropertyMeta {

	private MibObjectType mibObjectType;

	public AnnoMibPropertyMeta(MibObjectType mibObjectType) {
		this.mibObjectType = mibObjectType;
	}

	public String getOid() {
		return mibObjectType.oid();
	}

	public SmiType getSmiType() {
		return mibObjectType.smiType();
	}

	public Access getAccess() {
		return mibObjectType.access();
	}

}
