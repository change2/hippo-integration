package com.change.hippo.utils.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoDTOConsumer extends MdpMessageConsumer<Dto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDTOConsumer.class);

    @Override
    protected void consumerInternal(Dto message) {
        LOGGER.info("mdp message={}", message);
    }
}
