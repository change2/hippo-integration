package org.change.hippo.server.parser.strategy;

import org.apache.commons.beanutils.BeanUtils;
import org.change.hippo.server.model.soap.SoapServiceConfig;

import java.util.*;
import java.util.Map.Entry;

public class SoapServiceConverter implements ServiceConverter<SoapServiceConfig> {

	public static final String INTERFACE = "interface";
	public static final String METHOD = "method";
	public static final String ARGUMENT = "argument";
	public static final String URL = "url";

	@Override
    public List<SoapServiceConfig> convert(Collection collection) throws Exception {
		Set<Entry<Object, Object>> set = (Set<Entry<Object, Object>>) collection;
		Map<String, SoapServiceConfig> map = new HashMap<String, SoapServiceConfig>();
		for (Entry<Object, Object> entry : set) {
			String name = (String) entry.getKey();
			String[] subNames = name.split("\\.");

			if (subNames.length != 2) {
				throw new Exception("Bad service config: " + name);
			}

			SoapServiceConfig soapServiceConfig = map.get(subNames[0]);
			if (soapServiceConfig == null) {
				soapServiceConfig = new SoapServiceConfig();
				map.put(subNames[0], soapServiceConfig);
			}

			soapServiceConfig.setServiceId(subNames[0]);

            String fileName = subNames[1];
            String value = (String) entry.getValue();
            if (fileName.equals(ARGUMENT)) {
                soapServiceConfig.setArguments((value).split(","));
            }else {
                BeanUtils.setProperty(soapServiceConfig, fileName, value);
            }
		}
		return new ArrayList<>(map.values());
	}
}
