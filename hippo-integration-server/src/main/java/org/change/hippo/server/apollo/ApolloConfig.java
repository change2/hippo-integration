package org.change.hippo.server.apollo;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.change.hippo.server.selector.CheckSumSelector;
import org.change.hippo.server.selector.PrivilegeSelector;
import org.change.hippo.server.util.ColumnUtils;
import org.change.hippo.server.model.http.HttpConfig;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Setter
@Getter
@Component
public class ApolloConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApolloConfig.class);

    // 充许的设备类型
    @Value("${portal.privileges.appContext:null}")
    public String portalPrivilegesAppContext;

    // 签名Key
    @Value("${PORTAL.INTEGRATION.CHECKSUM:null}")
    public String checkSum;

    // 版本升级验证开关
    @Value("${PORTAL.VERSION.CHECK:true}")
    public boolean versionCheck;

    // 用户权限验证开关
    @Value("${PORTAL.AUTH.CHECK:true}")
    public boolean authCheck;

    // 签名验证开关
    @Value("${PORTAL.SUM.CHECK:true}")
    public boolean sumCheck;

    // 设备类型验证开关
    @Value("${PORTAL.PRIVILEGE.CHECK:true}")
    public boolean privilegeCheck;

    // 用户登录拦截开关
    @Value("${PORTAL.USERSESSION.CHECK:true}")
    public boolean userSessionCheck;

    // 参数校验拦截开关
    @Value("${PORTAL.PARAM.CHECK:true}")
    public boolean paramCheck;

    // iOS升级
    @Value("${PORTAL.VERSION.IOS.UPGRADE:null}")
    public String iosUpgrade;

    // Android升级
    @Value("${PORTAL.VERSION.ANDROID.UPGRADE:null}")
    public String androidUpgrade;

    // 接口动态配置
    @Value("${PORTAL.HTTP.CONFIG:null}")
    public String httpConfig;

    @Autowired
    private PrivilegeSelector privilegeSelector;

    @Autowired
    private CheckSumSelector checkSumSelector;

    public static final Map<String, String> map;

    private ServicePool servicePool;

    static {
        map = new HashMap();
        map.put("portal.privileges.appContext", "portalPrivilegesAppContext");
        map.put("PORTAL.INTEGRATION.CHECKSUM", "checkSum");
        map.put("PORTAL.VERSION.CHECK", "versionCheck");
        map.put("PORTAL.AUTH.CHECK", "authCheck");
        map.put("PORTAL.SUM.CHECK", "sumCheck");
        map.put("PORTAL.PRIVILEGE.CHECK", "privilegeCheck");
        map.put("PORTAL.USERSESSION.CHECK", "userSessionCheck");
        map.put("PORTAL.PARAM.CHECK", "paramCheck");
        map.put("PORTAL.VERSION.IOS.UPGRADE", "iosUpgrade");
        map.put("PORTAL.VERSION.ANDROID.UPGRADE", "androidUpgrade");
        map.put("PORTAL.HTTP.CONFIG", "httpConfig");
    }


    @ApolloConfigChangeListener
    private void changeHandler(ConfigChangeEvent changeEvent) {
        Set<String> changedKeys = changeEvent.changedKeys();
        if (null != changedKeys && changedKeys.size() > 0) {
            Iterator<String> iterator = changedKeys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                ConfigChange change = changeEvent.getChange(key);
                instantOrUpdate(key, change.getNewValue());
                setProperties(key,change);
            }
        }
    }

    /**
     * 更新值
     *
     * @param key
     * @param value
     * @throws Exception
     */
    private void instantOrUpdate(String key, String value) {
        String lastStringField = map.get(key);
        try {
            logger.info("Apollo updata [{}]  oldValue:{},newValue:{}", lastStringField, BeanUtils.getProperty(this, lastStringField), value);
            if (StringUtils.isNotBlank(value)) {
                if (StringUtils.isNotBlank(lastStringField)) {
                    BeanUtils.setProperty(this, lastStringField, value);
                }
            }
        } catch (Exception e) {
            logger.error("Apollo updata [{}]  error:{}", key, e.getMessage());
        }
    }

    /**
     * 重新装载配置
     *
     * @param key
     * @throws Exception
     */
    public void setProperties(String key, ConfigChange change) {
        try {
            switch (key) {
                case "portal.privileges.appContext":
                    privilegeSelector.afterPropertiesSet();
                    break;
                case "PORTAL.INTEGRATION.CHECKSUM":
                    checkSumSelector.afterPropertiesSet();
                    break;
                case "PORTAL.HTTP.CONFIG":
                    setHttpConfig(change);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 加载Apollo配置接口
     * @param change
     * @throws Exception
     */
    public void setHttpConfig(ConfigChange change) throws Exception {
        // 删除旧的接口
        if (!StringUtils.isBlank(change.getOldValue()) && !"null".equals(change.getOldValue().toLowerCase())) {
            List<HttpConfig> oldlist = JSON.parseArray(change.getOldValue(), HttpConfig.class);
            for (HttpConfig config : oldlist) {
                servicePool.delService(config.getServiceId());
                logger.info("Apollo动态配置接口信息变更：删除旧的接口信息  info:{}",JSON.toJSONString(config));
            }
        }
        // 增加新的接口
        if (StringUtils.isBlank(change.getNewValue()) || "null".equals(change.getNewValue().toLowerCase()))
            return;
        List<HttpConfig> newlist = JSON.parseArray(change.getNewValue(), HttpConfig.class);
        for (HttpConfig config : newlist) {
            HttpService httpService = new HttpService();
            for (String src : ColumnUtils.columns(config.getDomain())) {
                config.setDomain(config.getDomain().replace(String.format("{%s}", src), System.getenv(src)));
            }
            httpService.postConstruct(config);
            httpService.init(config);
            servicePool.addService(config.getServiceId(), httpService);
            logger.info("Apollo动态配置接口信息变更：增加新的接口信息  info:{}",JSON.toJSONString(config));
        }
    }

    public void setServicePool(ServicePool servicePool) {
        this.servicePool = servicePool;
    }

}
