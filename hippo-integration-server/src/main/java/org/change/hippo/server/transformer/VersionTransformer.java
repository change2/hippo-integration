package org.change.hippo.server.transformer;

import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

import static org.change.hippo.server.util.Constants.OK;
import static org.change.hippo.server.util.Constants.OK_MSG;

public class VersionTransformer {

    private static final Logger logger = LoggerFactory.getLogger(VersionTransformer.class);

    @Resource
    private ApolloConfig apolloConfig;


    /**
     * 版本升级通道
     *
     * @param message
     * @return
     */
    public Message<String> buildVersionResponse(Message<?> message) throws Exception {
        MessageHeaders header = message.getHeaders();
        String requestUrl = (String) header.get(Constants.REQUEST_URL);
        String upgrade;

        // 过渡升级
        if (requestUrl.contains(Constants.OLD_VERSION_IOS_URL)){
            upgrade = apolloConfig.iosUpgrade;
        } else{
            upgrade = apolloConfig.androidUpgrade;
        }

        logger.info("版本升级 !   oldVersion:{}    newVersion:{}", message.getPayload(), upgrade);
        return MessageBuilder.withPayload(upgrade).copyHeaders(header)
                .setHeader(Constants.X_SERVICE_MESSAGE, OK_MSG)
                .setHeader(Constants.X_SERVICE_CODE, OK)
                .build();
    }

}
