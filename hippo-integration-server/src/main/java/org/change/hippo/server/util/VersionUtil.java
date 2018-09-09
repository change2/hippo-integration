package org.change.hippo.server.util;

public class VersionUtil {


    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param ver1
     * @param ver2
     */
    public static int compareVersion(String ver1, String ver2) {

        String[] split1 = ver1.split("\\.");  //以.分隔需要转义
        String[] split2 = ver2.split("\\.");

        int length = Math.min(split1.length, split2.length);
        int diff = 0;
        for (int i = 0; i < length; i++) {
            diff = split1[i].length() - split2[i].length();
            if (diff == 0) {   //位数相同时，比较大小
                if (split1[i].compareTo(split2[i]) > 0) {
                    return 1;
                } else if (split1[i].compareTo(split2[i]) < 0) {
                    return -1;
                } else if (i == length - 1) {
                    return length == split1.length ? -1 : 1;
                }

            } else if (diff != 0) {  //位数不同时，直接输出
                return diff;
            }

        }
        return 0;
    }

}
