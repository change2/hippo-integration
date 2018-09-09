package org.change.hippo.server.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ParamCheckUtils {

    private static final Logger logger = LoggerFactory.getLogger(ParamCheckUtils.class);

    /**
     * 参数非空检查
     *
     * @param p     请求参数
     * @param param 非空参数
     * @return
     */
    public static boolean checkParam(String p, String[] param) {
        try {
            Map map = JSONObject.parseObject(p, Map.class);
            for (String s : param) {
                OUT:
                if (s.contains("|")) {
                    String[] c = s.split("\\|");
                    for (String s1 : c) {
                        if (isNotNull(map.get(s1))) {
                            break OUT;
                        }
                    }
                    logger.warn("Param Error!   Params:{}  ErrorMsg:{} 不可同时为空", p, s);
                    return false;
                } else if (isNull(map.get(s))) {
                    logger.warn("Param Error!   Params:{}  ErrorMsg:{} 缺少关键字", p, s);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            // 解析错误放行 让后台校验
            return true;
        }
    }

    public static String checkParam2(String p, String[] param) {
        try {
            Map map = JSONObject.parseObject(p, Map.class);
            for (String s : param) {
                OUT:
                if (s.contains("|")) {
                    String[] c = s.split("\\|");
                    for (String s1 : c) {
                        if (isNotNull(map.get(s1))) {
                            break OUT;
                        }
                    }
                    logger.warn("Param Error!   Params:{}  ErrorMsg:{} 不可同时为空", p, s);
                    return "缺少关键字:"+s;
                } else if (isNull(map.get(s))) {
                    logger.warn("Param Error!   Params:{}  ErrorMsg:{} 缺少关键字", p, s);
                    return "缺少关键字:"+s;
                }
            }
            return null;
        } catch (Exception e) {
            // 解析错误放行 让后台校验
            return null;
        }
    }

    public static boolean isNull(Object object) {
        if (object == null || StringUtils.isBlank(object.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    public static void main(String[] args) {
        String p = "{\"loid\":\"SHDX00000009\",\"serviceName\":1,,\"serviceId\":1,\"status\":true}";
       // String param = "[loid,serviceId1|serviceName1|serviceName2,status]";
       // System.out.println(checkParam(p, param));
    }


}
