package org.change.hippo.server.util;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: change.long
 * Date: 2017/11/30
 * Time: 下午4:44
 */
public class AcmsUtil {
    public static final Config configService = ConfigService.getAppConfig();

    private static final Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");

    public static boolean switchON(String key, boolean def) {
        if (key == null || key.length() == 0) {
            return false;
        }
        return configService.getBooleanProperty(key, def);
    }

    public static String initDefEnv(String value, String def) {
        String content = replaceText(value, new char[]{'{'}, new char[]{'}'});
        return checkContent(content) ? content : def;
    }

    private static boolean checkContent(String content) {

        Matcher matcher = pattern.matcher(content);
        return !matcher.find();
    }


    public static String replaceText(String text, char[] start, char[] end) {
        if (text != null && text.length() != 0) {
            int slen = start.length;
            int elen = end.length;
            if (text != null && text.length() >= slen + elen) {
                StringBuilder sb = new StringBuilder(text);
                int i = 0;
                int j = 0;
                int k = 0;
                int offset = 0;
                int len = text.length();

                for(int mark = -1; i < len; ++i) {
                    char c = text.charAt(i);
                    if (mark > -1) {
                        if (end[k] == c) {
                            ++k;
                            if (k == elen) {
                                String param = text.substring(mark + slen, i - elen + 1);
                                Object val = System.getenv(param);
                                if (val != null) {
                                    String v = val.toString();
                                    sb.replace(mark + offset, i + 1 + offset, v);
                                    int dis = i - mark + 1;
                                    offset += v.length() - dis;
                                }

                                mark = -1;
                                k = 0;
                            }
                        } else {
                            k = end[0] == c ? 1 : 0;
                            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_' && (c < '0' || c > '9')) {
                                mark = -1;
                                j = start[0] == c ? 1 : 0;
                            }
                        }
                    } else if (start[j] == c) {
                        ++j;
                        if (j == slen) {
                            mark = i - j + 1;
                            j = 0;
                        }
                    } else if (j > 0) {
                        j = start[0] == c ? 1 : 0;
                    }
                }

                return sb.toString();
            } else {
                return text;
            }
        } else {
            return text;
        }
    }

    public static boolean switchON(Object clazz) {
        if (clazz == null) {
            return false;
        }
        String simpleName = clazz.getClass().getSimpleName();
        return configService.getBooleanProperty(simpleName, false);
    }
}
