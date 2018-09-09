package com.change.hippo.utils.rest;


import com.change.hippo.utils.result.BaseException;
import com.change.hippo.utils.result.StatusCode;

/**
 * User: change.long
 * Date: 2017/8/21
 * Time: 下午7:50
 */
public class RestException extends BaseException {

    private static final long serialVersionUID = 3501058081203209469L;

    public RestException(String message) {
        super(message);
    }

    public RestException(String message, Throwable e) {
        super(message, e);
    }

    public RestException(StatusCode statusCode) {
        super(statusCode);
    }

    public RestException(StatusCode exceptionCode, Throwable e) {
        super(exceptionCode, e);
    }

    public RestException(StatusCode statusCode, Throwable cause, String message) {
        super(statusCode, cause, message);
    }
}
