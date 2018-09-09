package org.change.hippo.server.parser.strategy;

import org.apache.commons.beanutils.BeanUtils;
import org.change.hippo.server.model.mdp.MdpServiceConfig;
import java.util.*;
import java.util.Map.Entry;

public class MdpServiceConverter implements ServiceConverter<MdpServiceConfig> {

    public static final String ARGUMENT = "argument";

    @Override
    public List<MdpServiceConfig> convert(Collection collection) throws Exception {
        Set<Entry<Object, Object>> set = (Set<Entry<Object, Object>>) collection;
        Map<String, MdpServiceConfig> map = new HashMap<String, MdpServiceConfig>();
        for (Entry<Object, Object> entry : set) {
            String name = (String) entry.getKey();
            String[] subNames = name.split("\\.");

            if (subNames.length != 2) {
                throw new Exception("Bad service config: " + name);
            }

            MdpServiceConfig mdpServiceConfig = map.get(subNames[0]);
            if (mdpServiceConfig == null) {
                mdpServiceConfig = new MdpServiceConfig();
                map.put(subNames[0], mdpServiceConfig);
            }

            mdpServiceConfig.setServiceId(subNames[0]);

            String fileName = subNames[1];
            String value = (String) entry.getValue();
           if (fileName.equals(ARGUMENT)) {
                if (entry.getValue() != null && !"".equals(entry.getValue())) {
                    mdpServiceConfig.setArguments((value).split(","));
                }
            }else {
                BeanUtils.setProperty(mdpServiceConfig, fileName, value);
            }
        }
        return new ArrayList<MdpServiceConfig>(map.values());
    }
}
