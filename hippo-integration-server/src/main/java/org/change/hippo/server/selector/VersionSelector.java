package org.change.hippo.server.selector;

import com.alibaba.fastjson.JSON;
import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.util.AcmsUtil;
import org.change.hippo.server.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

public class VersionSelector implements MessageSelector {

    private static final Logger logger = LoggerFactory.getLogger(VersionSelector.class);

    @Resource
    private ApolloConfig apolloConfig;

    @Override
    public boolean accept(Message<?> message) {
        //检查开关,默认不检查(true)
        if (AcmsUtil.switchON(this)) {
            return true;
        }
        // 旧版本的过渡升级
        String requestUrl = message.getHeaders().get(Constants.REQUEST_URL, String.class);
        if (requestUrl.contains(Constants.OLD_VERSION_URL)) {
            logger.info("Operation:{},Params:{},Content:{} ", "老版本自服请求，升级新版本", JSON.toJSONString(message.getPayload()), requestUrl);
            return false;
        }
        return true;
    }
}
