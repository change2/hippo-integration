package com.change.hippo.utils.spring.mvc.support;


import com.alibaba.fastjson.JSONException;

import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.result.ActionResult;
import com.change.hippo.utils.result.BaseException;
import com.change.hippo.utils.result.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 专用的能够自动封装业务执行失败结果的异常处理器<br/>
 * 在handler出现异常且需要返回JSON/JSONP格式数据时自动包装Msg类型的错误信息返回到前端
 *
 * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
 * @see AbstractSmartExceptionResolver
 *
 */
public class DefaultExceptionResolver extends AbstractSmartExceptionResolver {

    private final static Logger logger = LoggerFactory.getLogger(DefaultExceptionResolver.class);


    @Override
    protected Object getErrorResponse(JsonResult jsonResult, Exception exception) {
        boolean hasJsonResult = jsonResult != null;
        String errorCode;
        String errorMsg;
        if (exception instanceof MissingServletRequestParameterException) {
            errorCode = StatusCode.PARAMETER_ERROR.getCode();
            errorMsg = "Miss parameter:" + ((MissingServletRequestParameterException) exception).getParameterName();
        } else if (exception instanceof BaseException) {
            errorCode = ((BaseException) exception).getCode();
            errorMsg = ((BaseException) exception).getMessage();
        } else if (exception instanceof JSONException) {
            StatusCode jsonParseError = StatusCode.JSON_PARSE_ERROR;
            errorCode = jsonParseError.getCode();
            errorMsg = jsonParseError.getMessage();
        } else if (exception instanceof HttpMessageNotReadableException) {
            StatusCode jsonParseError = StatusCode.JSON_PARSE_ERROR;
            errorCode = jsonParseError.getCode();
            errorMsg = jsonParseError.getMessage();
        }else {
            errorCode = hasJsonResult && StringUtils.isNotEmpty(jsonResult.errorCode()) ? jsonResult.errorCode() : StatusCode.SERVER_ERROR_CODE;
            errorMsg = StatusCode.SERVER_ERROR.getMessage();
        }
        ActionResult<Object> result = new ActionResult<Object>(errorCode, errorMsg);
        if (hasJsonResult && jsonResult.convertMsg()) {
            return MsgUtil.create(result);
        } else {
            return result;
        }
    }


    @Override
    protected boolean canUseJsonResult(HandlerMethod handler, MethodParameter returnType) {
        return returnType != null && returnType.getContainingClass().getName().contains("org.sdn.order");
    }

    @Override
    protected void processException(HttpServletRequest request, Object handler, String actionName, Exception ex) {
        if (ex instanceof MissingServletRequestParameterException) {
            if (logger.isWarnEnabled()) {
                logger.warn("缺少必须的参数:" + ((MissingServletRequestParameterException) ex).getParameterName());
            }
        } else {
            super.processException(request, handler, actionName, ex);
        }
    }

}