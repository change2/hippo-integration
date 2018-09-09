package com.change.hippo.utils.ri;

import org.apache.commons.lang3.StringUtils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * Http请求工具类
 * 
 *
 */
public class RequestUtils {

    /**
     * 获取请求的IP地址
     * 
     * @param request
     * @return
     */
    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }
        if(StringUtils.isNotBlank(ip)) {
			int index = ip.indexOf(',');
			if(index > 0) {
				ip = ip.substring(0, index);
			}
		}
        return ip;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    /**
     * 验证请求的合法性，防止跨域攻击
     * 
     * @param request
     * @return
     */
    public static boolean validateRequest(HttpServletRequest request) {
        String referer = "";
        boolean referer_sign = true; // true 站内提交，验证通过 //false 站外提交，验证失败
        Enumeration<?> headerValues = request.getHeaders("referer");
        while (headerValues.hasMoreElements()) {
            referer = (String)headerValues.nextElement();
        }
        // 判断是否存在请求页面
        if (StringUtils.isBlank(referer)){
            referer_sign = false;
        }
        else {
            // 判断请求页面和getRequestURI是否相同
            String servername_str = request.getServerName();
            if (StringUtils.isNotBlank(servername_str)) {
                int index = 0;
                if (StringUtils.indexOf(referer, "https://") == 0) {
                    index = 8;
                } else if (StringUtils.indexOf(referer, "http://") == 0) {
                    index = 7;
                }
                if (referer.length() - index < servername_str.length()) {// 长度不够
                    referer_sign = false;
                } else { // 比较字符串（主机名称）是否相同
                    String referer_str = referer.substring(index, index + servername_str.length());
                    if (!servername_str.equalsIgnoreCase(referer_str)){
                        referer_sign = false;
                    }
                }
            } else{
                referer_sign = false;
            }
        }
        return referer_sign;
    }
}
