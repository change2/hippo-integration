package com.change.hippo.utils.spring.mvc;


import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.result.ExceptionMessageConfigure;
import com.change.hippo.utils.result.ParamException;
import com.change.hippo.utils.result.StatusCode;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;


/**
 * User: change.long
 * Date: 17/8/8
 * Time: 上午10:43
 */
public class Validators {

    public static final String LEFT_BRACE = "{";
    public static final String RIGHT_BRACE = "}";

    /**
     *
     * @param validator validator实现,hibernate
     * @param obj       需要校验的对象
     * @param property  需要校验的属性
     * @param clazz     validate group
     * @param <T>       忽略
     */
    public static <T> void validate(Validator validator, T obj, String property, Class... clazz) {

        if (obj == null) {
            throw new ParamException(StatusCode.PARAMETER_ERROR);
        }
        Set sets;
        if (StringUtils.isNotEmpty(property)) {
            sets = validator.validateProperty(obj, property, clazz);
        } else {
            sets = validator.validate(obj, clazz);
        }

        if (!sets.isEmpty()) {
            ConstraintViolation constraintViolation = (ConstraintViolation) sets.iterator().next();
            String message = constraintViolation.getMessage();
            StatusCode exceptionCode;
            if (StringUtils.isEmpty(message)) {
                exceptionCode = StatusCode.SERVER_ERROR;
                throw new ParamException(exceptionCode.getCode());
            } else if (message.contains(LEFT_BRACE)) {
                exceptionCode = StatusCode.PARAMETER_ERROR;
                message = message.replace(LEFT_BRACE, "").replace(RIGHT_BRACE, "").trim();
                throw new ParamException(exceptionCode.getCode(), ExceptionMessageConfigure.getMessage(message));
            } else {
                exceptionCode = StatusCode.PARAMETER_ERROR;
                exceptionCode.setMessage(message);
                throw new ParamException(exceptionCode.getCode(), exceptionCode.getMessage());
            }
        }
    }

    public static <T> void validate(Validator validator, T obj, Class... clazz) {
        validate(validator, obj, null, clazz);
    }


    /**
     * 判断IP格式和范围
     */
    static String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    private static Pattern pat = Pattern.compile(rexp);
    public static boolean validateIp(String deviceIp) {
        try {
            Matcher mat = pat.matcher(deviceIp);
            return mat.find();
        } catch (Exception e) {
            return false;
        }
    }
}
