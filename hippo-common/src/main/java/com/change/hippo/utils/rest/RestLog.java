package com.change.hippo.utils.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RestAdapter;

/**
 * User: change.long
 * Date: 2017/9/3
 * Time: 下午9:18
 */
public class RestLog implements RestAdapter.Log {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestLog.class);
    @Override
    public void log(String message) {
        LOGGER.info(message);
    }
}
