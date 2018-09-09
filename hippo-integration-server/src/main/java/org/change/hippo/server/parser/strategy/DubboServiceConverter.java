package org.change.hippo.server.parser.strategy;

import org.apache.commons.beanutils.BeanUtils;
import org.change.hippo.server.model.dubbo.DubboServiceConfig;

import java.util.*;

/**
 * User: change.long
 * Date: 2017/11/23
 * Time: 下午2:28
 */
public class DubboServiceConverter implements ServiceConverter<DubboServiceConfig> {
    public static final String ARGUMENT = "argument";

    @Override
    public List<DubboServiceConfig> convert(Collection collection) throws Exception {
        Set<Map.Entry<Object, Object>> set = (Set<Map.Entry<Object, Object>>) collection;
        Map<String, DubboServiceConfig> map = new HashMap<String, DubboServiceConfig>();
        for (Map.Entry<Object, Object> entry : set) {
            String name = (String) entry.getKey();
            String[] subNames = name.split("\\.");

            if (subNames.length != 2) {
                throw new Exception("Bad service config: " + name);
            }

            DubboServiceConfig dubboServiceConfig = map.get(subNames[0]);
            if (dubboServiceConfig == null) {
                dubboServiceConfig = new DubboServiceConfig();
                map.put(subNames[0], dubboServiceConfig);
            }

            dubboServiceConfig.setServiceId(subNames[0]);

            String fileName = subNames[1];
            Object value = entry.getValue();
            if (fileName.equals(ARGUMENT)) {
                if (value != null && !"".equals(value)) {
                    dubboServiceConfig.setArguments(((String) value).split(","));
                }
            }else {
                BeanUtils.setProperty(dubboServiceConfig, fileName, value);
            }
        }
        return new ArrayList<DubboServiceConfig>(map.values());
    }
}