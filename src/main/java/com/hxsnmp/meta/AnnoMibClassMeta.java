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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hxsnmp.annotation.MibIndex;
import com.hxsnmp.annotation.MibObjectType;
import com.hxsnmp.annotation.MibTable;
import com.hxsnmp.annotation.RowStatus;

/**
 * Mib class metadata got from class annotations.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 * @see MibTable
 * @see MibObjectType
 * @see MibIndex
 */
public class AnnoMibClassMeta extends MibClassMeta {

	Log logger = LogFactory.getLog(AnnoMibClassMeta.class);

	private Class<?> mibClass;

	public AnnoMibClassMeta(Class<?> mibClass) {
		this.mibClass = mibClass;
	}

	public boolean isTable() {
		return mibClass.isAnnotationPresent(MibTable.class);
	}

	public IRowStatusMeta getRowStatusMeta() {
		return isTable() ? new AnnoRowStatusMeta(mibClass
				.getAnnotation(RowStatus.class)) : null;
	}

	@Override
	protected Map<String, IMibPropertyMeta> initPropertyMetaMap() {
		Map<String, IMibPropertyMeta> propertyMetaMap = new HashMap<String, IMibPropertyMeta>();
		Field[] fields = mibClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(MibIndex.class)) {
				MibIndex miAnno = field
						.getAnnotation(MibIndex.class);
				MibObjectType motAnno = field
						.getAnnotation(MibObjectType.class);
				propertyMetaMap.put(field.getName(), new AnnoMibIndexMeta(
						miAnno, motAnno));
			} else if (field.isAnnotationPresent(MibObjectType.class)) {
				propertyMetaMap.put(field.getName(), new AnnoMibPropertyMeta(
						field
								.getAnnotation(MibObjectType.class)));
			} else {
				logger.warn("Property without annotation: " + field);
			}
		}
		return propertyMetaMap;
	}

}