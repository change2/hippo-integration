package org.change.hippo.server.parser.database;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import com.change.hippo.maintain.model.RouteDefinition;
import org.change.hippo.server.parser.strategy.ServiceConverter;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.util.SpringContextUtils;
import org.change.hippo.server.model.ServiceConfig;
import org.change.hippo.server.parser.AbstractServiceParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MySQLParser<T extends ServiceConfig> extends AbstractServiceParser<T> implements DatabaseParser<T>, InitializingBean {


    private JdbcTemplate jdbcTemplate;

    private Map<String, ServiceConverter> serviceConverters;

    @Override
    public List<T> parse() throws Exception {
        List<RouteDefinition> results = jdbcTemplate.query(Constants.GATEWAY_API_DEFINE_SQL,
                new BeanPropertyRowMapper<>(RouteDefinition.class));

        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }
        List<T> routes = Lists.newArrayList();
        for (RouteDefinition routeVO : results) {
            ServiceConverter<T> serviceConverter = serviceConverters.get(routeVO.getServiceModel());
            if (serviceConverter == null) {
                continue;
            }

            Map<String, Object> properties = PropertyUtils.describe(routeVO);
            Properties propertiesToUse = new Properties();
            for (Object key : properties.keySet()) {
                if (key.equals("class")) continue;
                propertiesToUse.put(routeVO.getServiceId() + "." + key, properties.get(key));
            }
            routes.addAll(serviceConverter.convert(propertiesToUse.entrySet()));
        }
        return routes;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        this.serviceConverters = SpringContextUtils.getBeans(ServiceConverter.class);
    }
}
