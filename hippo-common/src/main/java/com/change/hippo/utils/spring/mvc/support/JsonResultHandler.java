package com.change.hippo.utils.spring.mvc.support;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.result.ActionResult;
import com.change.hippo.utils.result.BaseException;
import com.change.hippo.utils.result.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JsonResultHandler implements HandlerMethodReturnValueHandler {

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    private final static Logger LOGGER = LoggerFactory.getLogger(JsonResultHandler.class);
    private final static Pattern PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    @Autowired(required = false)
    private StringHttpMessageConverter stringHttpMessageConverter;
    protected Charset encoding;

    public JsonResultHandler() {
        encoding = Charset.forName(UTF8);
        stringHttpMessageConverter = new StringHttpMessageConverter(encoding);
        stringHttpMessageConverter.setWriteAcceptCharset(false);
    }

    protected JsonResult getJsonResult(MethodParameter returnType) {
        JsonResult jsonResult;
        /*
         * 优先使用action方法上的注解
         */
        jsonResult = returnType.getMethodAnnotation(JsonResult.class);
        if (jsonResult == null) {
            /*
             *搜索Controller级别注解
             */
            jsonResult = AnnotationUtils.findAnnotation(returnType.getContainingClass(), JsonResult.class);
        }
        return jsonResult;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return getJsonResult(returnType) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        HttpServletResponse response = ((ServletWebRequest) webRequest).getResponse();
        writeResponse(returnValue, returnType, request, response);
    }

    public void writeResponse(Object returnValue, MethodParameter returnType, JsonResult jsonResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String controllerName = returnType.getContainingClass().getSimpleName();
        String actionName = returnType.getMethod().getName();
        if (jsonResult == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("未在接口[{}.{}]的Action方法及Controller上找到JsonResult注解配置", controllerName, actionName);
            }
            throw new BaseException("请指定JsonResult序列化配置");
        }
        String callback = jsonResult.callback();
        String jsonValue = this.convertReturnValue(jsonResult, returnValue);
        MediaType mediaType;
        if (StringUtils.isNotEmpty(callback) && StringUtils.isNotEmpty(callback = request.getParameter(callback))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("调用方指定了jsonp回调函数名[{}],将返回jsonp格式数据", callback);
            }
            jsonValue = new StringBuilder(callback).append("(").append(jsonValue).append(")").toString();
            mediaType = new MediaType("application", "javascript", encoding);
        } else {
            mediaType = new MediaType("application", "json", encoding);
        }
        if (jsonResult.useUnicode()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("使用unicode编码转换返回数据");
            }
            Matcher m = PATTERN.matcher(jsonValue);
            StringBuffer res = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(res, "\\" + toUnicode(m.group()));
            }
            jsonValue = m.appendTail(res).toString();
        }
        if (jsonResult.debug() && LOGGER.isInfoEnabled()) {
            LOGGER.info("接口[{}.{}:{}],返回结果:{}", controllerName, actionName, jsonResult.desc(), jsonValue);
        }
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        stringHttpMessageConverter.write(jsonValue, mediaType, outputMessage);
    }

    protected String convertReturnValue(JsonResult jsonResult, Object returnValue) {
        String value;
        /*
         * 包装器类型返回值
         */
        if (returnValue instanceof JsonResultWrapper) {
            value = JSON.toJSONString(((JsonResultWrapper) returnValue).getData());
        } else { // 其他未知类型均默认渲染到JSON
            //适配老的MSG对象
            if (returnValue instanceof ActionResult && jsonResult.convertMsg()) {
                value = JSON.toJSONString(MsgUtil.create((ActionResult) returnValue));
            } else {
                value = JSON.toJSONString(returnValue);
            }
        }
        return value;
    }

    public static String toUnicode(String str) {
        char[] arChar = str.toCharArray();
        int iValue;
        StringBuilder uStr = new StringBuilder();
        for (int i = 0; i < arChar.length; i++) {
            iValue = (int) str.charAt(i);
            if (iValue <= 256) {
                uStr.append("\\").append(Integer.toHexString(iValue));
            } else {
                uStr.append("\\u").append(Integer.toHexString(iValue));
            }
        }
        return uStr.toString();
    }

    /**
     * 将响应数据写回客户端
     *
     * @param returnValue action接口返回结果
     * @param returnType  action接口返回值类型
     */
    protected void writeResponse(Object returnValue, MethodParameter returnType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        writeResponse(returnValue, returnType, this.getJsonResult(returnType), request, response);
    }

    public void setStringHttpMessageConverter(StringHttpMessageConverter stringHttpMessageConverter) {
        this.stringHttpMessageConverter = stringHttpMessageConverter;
    }

    protected void writeResponse(StatusCode statusCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        MediaType mediaType = new MediaType("application", "json", encoding);
        stringHttpMessageConverter.write(convertReturnValue(null, statusCode), mediaType, outputMessage);
    }
}

