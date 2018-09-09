package org.change.hippo.server.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColumnUtils {

    private static final String REPLACE_MATCHER = "{%s}";
    
    public static final Pattern pattern = Pattern.compile("\\{\\w+\\}");

    public static void main(String[] args) {
        List<String> columns = ColumnUtils.columns("http://{USERCENTER_MASTER_SERVICE_HOST}:{USERCENTER_MASTER_SERVICE_PORT}");
        System.out.println(columns);
    }

    public static List<String> columns(String text) {
        return replaceText(text, new char[]{'{'}, new char[]{'}'});
    }

    /**
     * @param text
     * @param start
     * @param end
     * @return
     */
    public static List<String> replaceText(String text, char[] start, char[] end) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        List<String> columns = new ArrayList<>();

        int slen = start.length, elen = end.length;
        if (text == null || text.length() < slen + elen) {
            return Collections.emptyList();
        }
        StringBuilder sb = new StringBuilder(text);
        int i = 0, j = 0, k = 0, offset = 0, len = text.length(), mark = -1;
        while (i < len) {
            char c = text.charAt(i);
            if (mark > -1) {
                if (end[k] == c) {
                    if (++k == elen) {
                        String param = text.substring(mark + slen, i - elen + 1);//字段
                        columns.add(param);
                        Object val = null;
                        if (val != null) {
                            String v = val.toString();
                            sb.replace(mark + offset, i + 1 + offset, v);
                            int dis = i - mark + 1;//所替换字符的长度
                            offset += v.length() - dis;//替换后字符的偏移量
                        }
                        mark = -1;
                        k = 0;
                    }
                } else {
                    k = (end[0] == c ? 1 : 0);
                    //---非单词字符重新匹配
                    if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_' && (c < '0' || c > '9')) {
                        mark = -1;
                        j = (start[0] == c ? 1 : 0);
                    }
                }
            } else {
                if (start[j] == c) {
                    if (++j == slen) {
                        mark = i - j + 1;
                        j = 0;
                    }
                } else {
                    if (j > 0) {
                        j = (start[0] == c ? 1 : 0);
                    }
                }
            }
            i++;
        }
        return columns;
    }

    public static String replace(String key, String val) {
        String content = AcmsUtil.initDefEnv(String.format(REPLACE_MATCHER, key), val);

        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return val;
        }
        return content;
    }
}
