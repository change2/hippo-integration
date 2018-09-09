package com.change.hippo.admin.exception;

import com.change.hippo.admin.config.Constant;
import com.change.hippo.admin.entity.LogEntity;
import com.change.hippo.admin.entity.UserEntity;
import com.change.hippo.admin.utils.HttpServletUtils;
import com.change.hippo.admin.utils.R;
import com.change.hippo.admin.utils.ShiroUtils;
import org.apache.shiro.authz.AuthorizationException;
import com.change.hippo.admin.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class BDExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LogService logService;

    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        if (HttpServletUtils.jsAjax(request)) {
            return R.error(403, "未授权");
        }
        return new ModelAndView("error/403");
    }


    @ExceptionHandler({Exception.class})
    public Object handleException(Exception e, HttpServletRequest request) {
        LogEntity logDO = new LogEntity();
        logDO.setGmtCreate(new Date());
        logDO.setOperation(Constant.LOG_ERROR);
        logDO.setMethod(request.getRequestURL().toString());
        logDO.setParams(e.toString());
        UserEntity current = ShiroUtils.getUser();
        if (null != current) {
            logDO.setUserId(current.getUserId());
            logDO.setUsername(current.getUsername());
        }
        logService.save(logDO);
        logger.error(e.getMessage(), e);
        if (HttpServletUtils.jsAjax(request)) {
            return R.error(500, "服务器错误，请联系管理员");
        }
        return new ModelAndView("error/500");
    }
}
