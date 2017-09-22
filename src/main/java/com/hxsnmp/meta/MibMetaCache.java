package com.hxsnmp.meta;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import nu.xom.Builder;
import nu.xom.Document;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hxsnmp.api.ISnmpTarget;

/**
 * Mib metadata cache.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 * @since 1.3 Feb 12, 2007
 */
public class MibMetaCache {

	static final Map<String, IMibClassMeta> metaCache = new WeakHashMap<String, IMibClassMeta>();

	public static IMibClassMeta getMibClassMeta(Class<?> clazz,
			ISnmpTarget target) {
		String cacheKey = getCacheKey(clazz.getName(), target);
		IMibClassMeta meta = metaCache.get(cacheKey);
		if (meta == null) {
			meta = getXmlMibClassMeta(clazz, target);
			metaCache.put(cacheKey, meta);
		}
		if (meta == null) {
			meta = getAnnoMibClassMeta(clazz);
			metaCache.put(cacheKey, meta);
		}
		return meta;
	}

	private static IMibClassMeta getXmlMibClassMeta(Class<?> clazz,
			ISnmpTarget target) {
		IMibClassMeta meta = null;

		List<String> params = toParams(clazz.getName(), target);
		for (int i = (params.size() - 1); meta == null && i >= 0; i--) {
			String cacheKey = getCacheKey(params.subList(0, i + 1));
			meta = metaCache.get(cacheKey);
			if (meta == null) {
				URL mappingXML = getXmlMappingFile(clazz, cacheKey);
				if (mappingXML != null) {
					meta = buildXmlMibClassMeta(mappingXML);
					metaCache.put(cacheKey, meta);
				}
			}
		}

		return meta;
	}

	private static String getCacheKey(String className, ISnmpTarget target) {
		return getCacheKey(toParams(className, target));
	}

	private static List<String> toParams(String className, ISnmpTarget target) {
		List<String> list = new ArrayList<String>(4);
		list.add(className);
		if (target.getVendor() == null) {
			return list;
		}
		list.add(target.getVendor());
		if (target.getDeviceType() == null) {
			return list;
		}
		list.add(target.getDeviceType());
		if (target.getDeviceVersion() == null) {
			return list;
		}
		list.add(target.getDeviceVersion());
		return list;
	}

	private static String getCacheKey(List<String> params) {
		StringBuilder sb = new StringBuilder();
		for (String param : params) {
			sb.append(param + "_");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private static URL getXmlMappingFile(Class<?> clazz, String cacheKey) {
		return clazz.getResource(cacheKey
				.substring(cacheKey.lastIndexOf(".") + 1)
				+ ".mapping.xml");
	}

	private static IMibClassMeta buildXmlMibClassMeta(URL mappingXML) {
		try {
			Document doc = new Builder().build(mappingXML.openStream());
			return new XmlMibClassMeta(doc.getRootElement());
		} catch (Exception e) {
			logger.error("Failed to build xml mapping file", e);
			return null;
		}
	}

	private static IMibClassMeta getAnnoMibClassMeta(Class<?> clazz) {
		return new AnnoMibClassMeta(clazz);
	}

	static Log logger = LogFactory.getLog(MibMetaCache.class);

}