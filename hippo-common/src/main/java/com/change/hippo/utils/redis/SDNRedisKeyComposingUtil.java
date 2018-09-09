package com.change.hippo.utils.redis;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by mike on 2017/11/22.
 */
public class SDNRedisKeyComposingUtil {

    public static String separators = ":";

    /**
     * SDN redis的keyName定义
     *
     * @param projectName 项目名字
     * @param featureName 业务分类/业务线
     * @param keyword     关键字
     * @return
     */
    public static String getReidsKey(String projectName, String featureName, String keyword) {

        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(featureName) || StringUtils.isBlank(keyword)) {
            return null;
        }
        return String.format("%s:%s:%s:", projectName, featureName, keyword);
    }

    /**
     * SDN redis的keyName定义
     *
     * @param projectName 项目名字
     * @param featureName 务分类/业务线
     * @return
     */
    public static String getReidsKey(String projectName, String featureName) {

        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(featureName)) {
            return null;
        }
        return String.format("%s:%s:", projectName, featureName);
    }

    /**
     * SDN redis的keyName定义
     *
     * @param projectName 项目名字
     * @return
     */
    public static String getReidsKey(String projectName) {

        if (StringUtils.isBlank(projectName)) {
            return null;
        }
        return String.format("%s:", projectName);
    }

    public void testRedisKey() {
        String a = SDNRedisKeyComposingUtil.getReidsKey("auth","a","111");
        System.out.println(a);
    }
}
