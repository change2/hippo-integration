package com.change.hippo.utils.result;


/**
 * User: change.long
 * Date: 2017/8/8
 * Time: 上午11:57
 */
public class BaseException extends RuntimeException {


    private static final long serialVersionUID = 3640031624252460815L;

    private String code;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable e) {
        super(message, e);
    }

    public BaseException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
    }

    public BaseException(StatusCode statusCode, final String message) {
        super(message);
        this.code = statusCode.getCode();
    }

    public BaseException(StatusCode exceptionCode, Throwable e) {
        super(exceptionCode.getMessage(), e);
        this.code = exceptionCode.getCode();
    }

    public BaseException(StatusCode statusCode, Throwable cause, String message) {
        this(message, cause);
        this.code = statusCode.getCode();
    }

    public BaseException(String errorCode, String errorInfo) {
        super(errorInfo);
        this.code = errorCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }
}