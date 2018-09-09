package org.change.hippo.server.parser.strategy;

import org.apache.commons.beanutils.BeanUtils;
import org.change.hippo.server.model.thunder.ThunderServiceConfig;
import java.util.*;
import java.util.Map.Entry;

public class ThunderServiceConverter implements ServiceConverter<ThunderServiceConfig> {
	public static final String INTERFACE = "interface";
	public static final String METHOD = "method";
	public static final String ARGUMENT = "argument";

	@Override
	public List<ThunderServiceConfig> convert(Collection collection) throws Exception {
		Set<Entry<Object, Object>> set = (Set<Entry<Object, Object>>) collection;
		Map<String, ThunderServiceConfig> map = new HashMap<String, ThunderServiceConfig>();
		for (Entry<Object, Object> entry : set) {
			String name = (String) entry.getKey();
			String[] subNames = name.split("\\.");

			if (subNames.length != 2) {
				throw new Exception("Bad service config: " + name);
			}

			ThunderServiceConfig thunderServiceConfig = map.get(subNames[0]);
			if (thunderServiceConfig == null) {
				thunderServiceConfig = new ThunderServiceConfig();
				map.put(subNames[0], thunderServiceConfig);
			}

			thunderServiceConfig.setServiceId(subNames[0]);

			String fileName = subNames[1];
			Object value = entry.getValue();
			 if (fileName.equals(ARGUMENT)) {
				if (value != null && !"".equals(value)) {
					thunderServiceConfig.setArguments(((String) value).split(","));
				}
			}  else {
				BeanUtils.setProperty(thunderServiceConfig, fileName, value);
			}
		}
		return new ArrayList<>(map.values());
	}

}
