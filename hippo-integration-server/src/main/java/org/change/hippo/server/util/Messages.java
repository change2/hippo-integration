package org.change.hippo.server.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import com.change.hippo.utils.encryption.AESUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Messages {


    public final static ThreadLocal<DateFormat> TIMES = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }
    };

    private static final String EMPTY_BRACKET = "{}";

    public static String decrypt(String payload) {
        if (StringUtils.isBlank(payload) || StringUtils.equalsIgnoreCase(payload, EMPTY_BRACKET)) {
            return payload;
        }
        try {
            String decrypt = AESUtil.Decrypt(StringEscapeUtils.unescapeJava(payload), Constants.KEY);
            return StringUtils.defaultIfBlank(decrypt, payload);
        } catch (Exception e) {
            return payload;
        }
    }

    public static String getHeader(String headerName) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getHeader(headerName);
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static void setResponseHeader(String key, String value) {
        HttpServletResponse response = getResponse();
        response.setHeader(key, value);
    }

    public static String getResponseHeader(String key) {
        HttpServletResponse request = getResponse();
        return request.getHeader(key);
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }
}
