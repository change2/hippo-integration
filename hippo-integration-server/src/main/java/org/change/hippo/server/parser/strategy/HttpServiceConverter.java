package org.change.hippo.server.parser.strategy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.change.hippo.server.util.AcmsUtil;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.model.http.HttpConfig;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * User: change.long
 * Date: 2017/11/20
 * Time: 下午10:55
 */
public class HttpServiceConverter implements ServiceConverter<HttpConfig>, EnvironmentAware {

    public static final String DOMAIN_NAME = "domain";

    private Environment environment;

    @Override
    public List<HttpConfig> convert(Collection collection) throws Exception {
        Set<Map.Entry<Object, Object>> set = (Set<Map.Entry<Object, Object>>) collection;
        Map<String, HttpConfig> map = new HashMap<String, HttpConfig>();
        for (Map.Entry<Object, Object> entry : set) {
            String name = (String) entry.getKey();
            String[] subNames = name.split("\\.");

            if (subNames.length != 2) {
                throw new Exception("Bad service config: " + name);
            }

            HttpConfig httpConfig = map.get(subNames[0]);
            if (httpConfig == null) {
                httpConfig = new HttpConfig();
                map.put(subNames[0], httpConfig);
                httpConfig.setServiceId(subNames[0]);
            }

            String value = entry.getValue().toString();
            String fileName = subNames[1];
            switch (fileName) {
                case DOMAIN_NAME:
                    value = environment.resolvePlaceholders(value);
                    value = AcmsUtil.initDefEnv(value, value);
                    BeanUtils.setProperty(httpConfig, "domain", value);
                    break;
                case Constants.REQUEST_PARAMS:
                    String[] paramArray = getParamArray(value);
                    BeanUtils.setProperty(httpConfig, "param", paramArray);
                    break;
                default:
                    //反射处理
                    BeanUtils.setProperty(httpConfig, fileName, value);
            }
        }
        return new ArrayList<>(map.values());
    }

    public String[] getParamArray(String val) {
        try {
            if (StringUtils.isBlank(val)) {
                return null;
            } else {
                return val.substring(1, val.length() - 1).split(",");
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
