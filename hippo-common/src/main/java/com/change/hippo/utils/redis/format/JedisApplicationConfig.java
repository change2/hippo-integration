package com.change.hippo.utils.redis.format;


import com.change.hippo.utils.properties.ConfigUtil;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Author: Tao.Wang
 * Date: 2017/11/24
 * Time: 上午10:15
 * Org : 思笛恩
 * To change this template use File | Settings | File Templates.
 * Description:
 *
 * @author: Tao.Wang
 */
public class JedisApplicationConfig {

    private static final String PROPERTY_FILE_PATH = "META-INF/app.properties";

    private static Properties properties = new Properties();

    static {
        properties = ConfigUtil.loadProperties(PROPERTY_FILE_PATH, false, false);
    }

    public static Object getProperty(String name){
        return properties.get(name);
    }

    public static String getStringProperty(String name){
        return properties.getProperty(name);
    }

}
