package com.change.hippo.utils.spring.mvc.support;


import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.result.ActionResult;
import com.change.hippo.utils.result.BaseException;
import com.change.hippo.utils.result.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 能够自动封装业务执行失败结果的异常处理器抽象基类
 *
 * @see SimpleMappingExceptionResolver
 *
 */
public abstract class AbstractSmartExceptionResolver extends SimpleMappingExceptionResolver {

	private final Logger logger = LoggerFactory.getLogger(AbstractSmartExceptionResolver.class);

	@Resource
	private JsonResultHandler jsonResultHandler;
	@Autowired(required = false)
	private Set<String> ignoreExceptions;
	@Autowired(required = false)
	private List<String> ignoreExceptionCodes;

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView modelAndView = null;
		if(handler instanceof HandlerMethod){
			HandlerMethod method = (HandlerMethod) handler;
			MethodParameter returnType = method.getReturnType();
			String actionName = returnType.getContainingClass().getSimpleName() + "." + returnType.getMethod().getName();

            /*
             * 支持通过JsonResult定义错误返回,因此此处尝试获取JsonResult配置
             */
			JsonResult jsonResult = jsonResultHandler.getJsonResult(returnType);
			boolean jsonResultSupport = jsonResult != null;
            /*
             * // 如果action配置了JsonResult且指定了desc信息
             */
			if(jsonResultSupport && StringUtils.isNotEmpty(jsonResult.desc())){
                /*
                 * 生成actionName
                 */
				actionName += ":" + jsonResult.desc();
			}
			try {
			    /*
			     *处理异常
			     */
				processException(request, handler, actionName, ex);
			} catch (Exception e) {
				if(logger.isWarnEnabled()){
					logger.warn("处理Action接口异常信息出错", e);
					logger.warn("Action接口异常", ex);
				}
			}
			try {
				if(jsonResultSupport){
					if(logger.isDebugEnabled()){
						logger.debug("当前Action支持JsonResult返回结果,正调用JsonResultHandlerMethodReturnValueHandler处理器渲染结果");
					}
					jsonResultHandler.writeResponse(getErrorResponse(jsonResult, ex), returnType, request, response);
					modelAndView = new ModelAndView();
				}else if(this.canUseJsonResult(method, returnType)){
                    /*
                     * 询问是否强制使用JsonResult渲染
                     */
					if(logger.isDebugEnabled()){
						logger.debug("未在接口[{}]的Action方法及Controller上找到JsonResult注解配置,将使用默认配置强制渲染数据", actionName);
					}
					jsonResultHandler.writeResponse(getErrorResponse(jsonResult, ex), returnType, this.getDefaultJsonResult(), request, response);
					modelAndView = new ModelAndView();
				}
			} catch (IOException e) {
				if(logger.isWarnEnabled()){
					logger.warn("回传响应数据到客户时出现IO错误", e);
				}
			}
		}else {
            if (ex != null) {
                if (handler != null) {
                    logger.warn("未知的处理器类型:{}", handler.getClass());
                }
                try {
                    jsonResultHandler.writeResponse(getPageNotFoundIfNeed(ex), request, response);
                } catch (IOException e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("回传响应数据到客户时出现IO错误", e);
                    }
                }
            }
        }
        if(modelAndView != null){
			return modelAndView;
		}
		if(logger.isDebugEnabled()){
			logger.debug("基于配置参数处理当前异常");
		}
		modelAndView = super.doResolveException(request, response, handler, ex);
		return modelAndView == null ? new ModelAndView() : modelAndView;
	}

    private StatusCode getPageNotFoundIfNeed(Exception ex) {
        if (ex instanceof NoHandlerFoundException) {
            NoHandlerFoundException pageNotFound = (NoHandlerFoundException) ex;
            if (logger.isWarnEnabled()) {
                logger.warn("未知的异常处理:{},未找到服务地址={},错误信息={}", ex.getClass(), pageNotFound.getRequestURL(), pageNotFound.getMessage());
            }
            return StatusCode.PAGE_NOT_FOUND;
        }
        return StatusCode.SERVER_ERROR;
    }

    public void setJsonResultHandler(JsonResultHandler jsonResultHandler) {
		this.jsonResultHandler = jsonResultHandler;
	}


    public Set<String> getIgnoreExceptions() {
        return ignoreExceptions;
    }

    public void setIgnoreExceptions(Set<String> ignoreExceptions) {
        this.ignoreExceptions = ignoreExceptions;
    }

    protected JsonResult getDefaultJsonResult() {
        /*
         * 默认JsonResult配置
         */
    	return new JsonResult() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return JsonResult.class;
			}

			@Override
			public boolean useUnicode() {
				return false;
			}

			@Override
			public String errorCode() {
				return "-1";
			}

			@Override
			public String desc() {
				return "";
			}

			@Override
			public String callback() {
				return "jsonpCallback";
			}

			@Override
			public boolean convertString() {
				return false;
			}

			@Override
			public boolean debug() {
				return true;
			}

			@Override
            public boolean convertMsg(){
			    return false;
            }
		};
	}

    /**
     * 处理异常信息——将异常信息打印到日志系统
     * @param request
     * @param handler
     * @param actionName
     * @param ex
     */
    protected void processException(HttpServletRequest request, Object handler, String actionName, Exception ex) {
		boolean ingoreStack = (ignoreExceptionCodes != null && ignoreExceptionCodes.contains(ex.getClass().getName()))
				|| (ex instanceof BaseException && ignoreExceptionCodes != null && ignoreExceptionCodes.contains(((BaseException) ex).getCode()));
		if (ingoreStack) {
			 if(logger.isWarnEnabled()){
				logger.warn(String.format("接口[%s]调用失败", actionName),ex);
			}
		}else if(logger.isWarnEnabled()){
			logger.warn(String.format("接口[%s]调用失败", actionName), ex);
		}
	}

    /**
     * 无JsonResult的接口是否强制使用JsonResult渲染结果
     * @param handler 请求处理器
     * @param returnType 返回结果类型
     * @return
     */
    protected boolean canUseJsonResult(HandlerMethod handler, MethodParameter returnType){
    	return false;
    }

    /**
     * 包装错误返回信息
     * @param jsonResult
     * @param exception
     * @return
     */
    protected Object getErrorResponse(JsonResult jsonResult, Exception exception){
    	boolean hasJsonResult = jsonResult != null;
    	String errorCode;
    	String errorMsg;
    	if(exception instanceof MissingServletRequestParameterException){
    		errorCode = StatusCode.PARAMETER_ERROR.getCode();
    		errorMsg = "缺少必须的参数:" + ((MissingServletRequestParameterException) exception).getParameterName();
    	}else if(exception instanceof BaseException){
    		errorCode = ((BaseException)exception).getCode();
    		errorMsg = ((BaseException) exception).getMessage();
    	}else{
    		errorCode = hasJsonResult && StringUtils.isNotEmpty(jsonResult.errorCode()) ? jsonResult.errorCode() : StatusCode.SERVER_ERROR_CODE;
    		errorMsg = hasJsonResult && StringUtils.isNotEmpty(jsonResult.desc()) ? (jsonResult.desc() + "执行失败") : StatusCode.SERVER_ERROR_CODE;
    	}
        ActionResult<Object> result = new ActionResult<Object>(errorCode, errorMsg);
        if (hasJsonResult && jsonResult.convertMsg()){
            return MsgUtil.create(result);
        }else {
            return result;
        }
    }
}