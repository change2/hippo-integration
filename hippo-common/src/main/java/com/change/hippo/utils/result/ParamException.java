package com.change.hippo.utils.result;

/**
 * User: change.long
 * Date: 2017/8/8
 * Time: 上午11:57
 */
public class ParamException extends BaseException {
    private static final long serialVersionUID = 5612786756846927606L;

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable e) {
        super(message, e);
    }

    public ParamException(StatusCode statusCode) {
        super(statusCode);
    }

    public ParamException(StatusCode exceptionCode, Throwable e) {
        super(exceptionCode, e);
    }

    public ParamException(StatusCode statusCode, Throwable cause, String message) {
        super(statusCode, cause, message);
    }

    public ParamException(String errorCode, String errorInfo) {
        super(errorCode, errorInfo);
    }

}
