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

import com.hxsnmp.annotation.MibObjectType.Access;
import com.hxsnmp.api.SmiType;

/**
 * Mib property metadata.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 */
public interface IMibPropertyMeta {

	/**
	 * Mib Oid of the property.
	 * 
	 * @return mib oid.
	 */
	String getOid();

	/**
	 * SMI type of the property
	 * 
	 * @return smib type.
	 */
	SmiType getSmiType();

	/**
	 * Access type of the property
	 * 
	 * @return access type
	 * @see Access
	 */
	Access getAccess();

}
