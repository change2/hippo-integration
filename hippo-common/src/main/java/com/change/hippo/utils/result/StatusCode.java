package com.change.hippo.utils.result;

import java.io.Serializable;

/**
 * User: change.long
 * Date: 2017/8/7
 * Time: 下午2:41
 * 错误码规范  系统二位 + 模块二位+ 状态码二位
 */
public class StatusCode implements Serializable {
    private static final long serialVersionUID = -7779533657422585611L;


    /**
     * 成功
     */
    public static final StatusCode SUCCESS = new StatusCode("200000");

    /**
     * 强制升级
     */
    public static final StatusCode UPGRADE_FORCE = new StatusCode("200001");

    /**
     * 请求中缺少必要参数
     */
    public static final StatusCode PARAMETER_ERROR = new StatusCode("200002");

    /**
     * 请求中值不合法
     */
    public static final StatusCode JSON_PARSE_ERROR = new StatusCode("200003");

    /**
     * 找不到用户
     */
    public static final StatusCode USER_NOT_FOUND = new StatusCode("200004");


    /**
     * 设备处于黑名单状态
     */
    public static final StatusCode BLACK_ERROR = new StatusCode("200005");


    public static final String SERVER_ERROR_CODE = "200006";


    public static final StatusCode SERVER_ERROR = new StatusCode(SERVER_ERROR_CODE);

    /**
     * Server Error
     */
    public static final StatusCode REST_ERROR = new StatusCode("200007");


    /**
     * check device rate overload
     */
    public static final StatusCode RATE_BLACK_ERROR = new StatusCode("200009");


    /**
     * check device upgrade overload
     */
    public static final StatusCode UPGRADE_RATE_BLACK_ERROR = new StatusCode("200010");

    /**
     * PAGE NOT FOUND 404
     */
    public static final StatusCode PAGE_NOT_FOUND = new StatusCode("200011");


    private String code;
    private String message;

    public StatusCode(String code) {
        this(code, convert2Message(code, code));
    }

    public StatusCode(String code, String message) {
        this.code = isEmpty(code) ? USER_NOT_FOUND.getCode() : code;
        this.message = message;
    }

    private static boolean isEmpty(String code) {
        return code == null || "".equals(code);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private static String convert2Message(String code, String defaultMessage) {
        String message = ExceptionMessageConfigure.getMessage(code);
        return isEmpty(message) ? defaultMessage : message;
    }

    @Override
    public String toString() {
        return "StatusCode{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
