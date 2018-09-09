package com.change.hippo.utils.result;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import static com.change.hippo.utils.result.StatusCode.SUCCESS;

public class ActionResult<T> extends BaseResult implements Serializable {
    private static final long serialVersionUID = 7643069707993375942L;
    /**
     * 响应码
     */
    private String code;
    /**
     * 消息
     */
    private String message;
    /**
     * 返回数据
     */
    private T result;


    public ActionResult() {
    }

    public ActionResult(String code) {
        this.code = code;
    }

    public ActionResult(String code, String message) {
        this(code);
        this.message = message;
    }

    public ActionResult(String code, String message, T data) {
        this(code, message);
        this.result = data;
    }

    public ActionResult(StatusCode code) {
        this(code.getCode(), code.getMessage());
    }

    public ActionResult(StatusCode code, T data) {
        this(code);
        this.result = data;
    }

    public ActionResult(StatusCode code, String message, T data) {
        this(code);
        this.message = message;
        this.result = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static boolean success(String code) {
        return StringUtils.equalsIgnoreCase(SUCCESS.getCode(), code);
    }

    @Override
    public String toString() {
        return "ActionResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                "} " + super.toString();
    }
}
