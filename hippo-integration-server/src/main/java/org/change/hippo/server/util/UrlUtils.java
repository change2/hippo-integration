package org.change.hippo.server.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class UrlUtils {

    public static final String REPLACE_MATCHER = "{%s}";

    /**
     * 拼装URL
     *
     * @param url
     * @param method
     * @param json
     * @return
     */
    public static String getUrl(String url, String method, String json) throws Exception {
        if (JsonUtils.isObject(json)) {
            method += "?";
            Map map = JSON.parseObject(json, Map.class);
            String src;
            for (Object key : map.keySet()) {
                src = String.format(REPLACE_MATCHER, key);
                if (method.contains(src)) {
                    method = method.replace(src, map.get(key.toString()).toString());
                }else {
                    method += key + "=" + map.get(key.toString()) + "&";
                }
            }
            String s = method.substring(method.length() - 1, method.length());
            if (s.equals("&") || s.equals("?"))
                method = method.substring(0, method.length() - 1);
        }
        return url + method;
    }

}
