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

import com.hxsnmp.annotation.MibIndex;
import com.hxsnmp.annotation.MibObjectType;

/**
 * Mib index metadata provided by annotation <code>MibIndex</code>.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 * @see MibIndex
 */
public class AnnoMibIndexMeta extends AnnoMibPropertyMeta implements
		IMibIndexMeta {

	private MibIndex mibIndex;

	public AnnoMibIndexMeta(MibIndex mibIndex, MibObjectType mibObjectType) {
		super(mibObjectType);
		this.mibIndex = mibIndex;
	}

	public int getIndexLength() {
		return mibIndex.length();
	}

	public int getIndexNo() {
		return mibIndex.no();
	}

}
