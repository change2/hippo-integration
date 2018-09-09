package org.change.hippo.server.util;

import org.springframework.util.StringUtils;

public class ClassUtils {

	public static Class forName(String type) throws Exception {
		if (!StringUtils.hasText(type)) {
			return null;
		}
		return Class.forName(type);
	}

	public static Class[] forNames(String[] types) throws Exception {
		if (types == null || types.length == 0) {
			return null;
		}

		Class[] classes = new Class[types.length];
		for (int i = 0; i < types.length; i++) {
			classes[i] = forName(types[i]);
		}
		return classes;
	}

}
