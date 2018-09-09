package com.change.hippo.utils.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: change.long
 * Date: 2017/12/8
 * Time: 下午2:51
 */
public class DemoConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoConsumer.class);

    @Override
    public void consume(String payload) {
        LOGGER.info("consumer" + payload);
    }
}
