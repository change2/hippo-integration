package org.change.hippo.server.parser.strategy;

import org.apache.commons.beanutils.BeanUtils;
import org.change.hippo.server.model.hessian.HessianServiceConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HessianServiceConverter implements ServiceConverter<HessianServiceConfig> {

	public static final String INTERFACE = "interface";
	public static final String METHOD = "method";
	public static final String ARGUMENT = "argument";
	public static final String URL = "url";

	@Override
    public List<HessianServiceConfig> convert(Collection collection) throws Exception {
		Set<Entry<Object, Object>> set = (Set<Entry<Object, Object>>) collection;
		Map<String, HessianServiceConfig> map = new HashMap<String, HessianServiceConfig>();
		for (Entry<Object, Object> entry : set) {
			String name = (String) entry.getKey();
			String[] subNames = name.split("\\.");

			if (subNames.length != 2) {
				throw new Exception("Bad service config: " + name);
			}

			HessianServiceConfig hessianServiceConfig = map.get(subNames[0]);
			if (hessianServiceConfig == null) {
				hessianServiceConfig = new HessianServiceConfig();
				map.put(subNames[0], hessianServiceConfig);
			}

			hessianServiceConfig.setServiceId(subNames[0]);

			String fileName = subNames[1];
			Object value = entry.getValue();
			 if (fileName.equals(ARGUMENT)) {
				hessianServiceConfig.setArguments(((String) value).split(","));
			} else {
				BeanUtils.setProperty(hessianServiceConfig, fileName, value);
			}
		}
		return new ArrayList<>(map.values());
	}
}
