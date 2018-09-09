package com.change.hippo.utils.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoDTOErrorConsumer extends MdpMessageConsumer<Dto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDTOErrorConsumer.class);

    @Override
    protected void consumerInternal(Dto message) {
        LOGGER.info("mdp message={}", message);
        throw new RuntimeException("mdp error");
    }
}
