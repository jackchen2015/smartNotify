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

import java.util.Collections;
import java.util.Map;

/**
 * Abstract mib class meta.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 */
public abstract class MibClassMeta implements IMibClassMeta {

	private Map<String, IMibPropertyMeta> propertyMetaMap = null;

	public boolean isIndex(String property) {
		return (getPropertyMeta(property) instanceof IMibIndexMeta);
	}

	public IMibIndexMeta getIndexMeta(String index) {
		return (IMibIndexMeta) getPropertyMeta(index);
	}

	public IMibPropertyMeta getPropertyMeta(String property) {
		return getPropertyMetaMap().get(property);
	}

	public Map<String, IMibPropertyMeta> getPropertyMetaMap() {
		if (propertyMetaMap == null) {
			propertyMetaMap = initPropertyMetaMap();
		}
		return Collections.synchronizedMap(propertyMetaMap);
	}

	protected abstract Map<String, IMibPropertyMeta> initPropertyMetaMap();

}