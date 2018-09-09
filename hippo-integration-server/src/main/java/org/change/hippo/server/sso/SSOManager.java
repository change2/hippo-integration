package org.change.hippo.server.sso;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.change.hippo.server.util.Messages;

/**
 * User: change.long
 * Date: 2017/11/21
 * Time: 下午7:43
 */
public class SSOManager {


    /**
     * 判断用户是否登录
     * @param sessionKey sessionkey
     * @return true登录，false非法
     */
    public static boolean isLogin(String sessionKey) {
        String sessionKeyToUse = Messages.decrypt(StringEscapeUtils.unescapeJava(sessionKey));
        String userId = getUserId(sessionKeyToUse);
        return StringUtils.isNotBlank(userId) && RedisUtil.exists(sessionKeyToUse);
    }

    /**
     * 解析sessionkey的用户id
     * @param sessionKeyToUse sessionkey
     * @return 用户的userId
     */
    public static String getUserId(String sessionKeyToUse) {
        if (StringUtils.isNotBlank(sessionKeyToUse)) {
            String[] userItems = sessionKeyToUse.split(";");
            if (userItems.length > 0) {
                return userItems[0];
            }
        }
        return null;
    }

    /**
     * 解析sessionkey的用户mobile
     * @param sessionKeyToUse sessionkey
     * @return 用户的mobile
     */
    public static String getMobile(String sessionKeyToUse){
        try {
            if (StringUtils.isNotBlank(sessionKeyToUse)) {
                sessionKeyToUse = Messages.decrypt(StringEscapeUtils.unescapeJava(sessionKeyToUse));
                String[] userItems = sessionKeyToUse.split(";");
                if (userItems.length > 0) {
                    return userItems[2];
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 判断用户是否是管理员
     * @param sessionKey
     * @return
     */
    public static Boolean getUserIsSuper(String sessionKey){
        try {
            String sessionKeyToUse = Messages.decrypt(StringEscapeUtils.unescapeJava(sessionKey));
            Object obj = RedisUtil.get(sessionKeyToUse);
            if (obj == null) return false;
            JSONObject jsonObject = JSON.parseObject(obj.toString());
            return jsonObject.get("issuper").toString().equals("1") ? true : false;
        } catch (Exception e) {
            return false;
        }
    }


    public static void main(String[] args) {
        for (int i = 0, len = 100000; i < len; i++) {
            System.out.println(isLogin("ig9zF02tFPJtdKXPULT6ldi5f/PIr8e1o93FBU92K1ItNWK8FgV9YgMk7/I222ghEzF32F6WmOqG\n1jqoO5Eljg=="));
        }
    }
}
