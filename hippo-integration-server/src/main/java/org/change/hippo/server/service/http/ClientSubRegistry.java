package org.change.hippo.server.service.http;

import org.change.hippo.server.http.IntegrationClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: change.long
 * Date: 2017/11/21
 * Time: 下午1:41
 */
public class ClientSubRegistry {
    private static Map<String, IntegrationClient> restTemplate = new ConcurrentHashMap<String, IntegrationClient>();


    public static void put(String url, IntegrationClient client) {
        restTemplate.put(url, client);
    }


    public static IntegrationClient get(String url) {
        return restTemplate.get(url);
    }
}
