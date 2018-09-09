package com.change.hippo.utils.rest;
import com.change.hippo.utils.result.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * User: change.long
 * Date: 2017/8/21
 * Time: 下午7:40
 */
public class RetrofitErrorHandler implements ErrorHandler {

    private Logger logger = LoggerFactory.getLogger(RetrofitErrorHandler.class);

    public static final int OK = 200;
    @Override
    public Throwable handleError(RetrofitError cause) {
        RetrofitError.Kind kind = cause.getKind();
        switch (kind) {
            case UNEXPECTED:
            case CONVERSION:
            case NETWORK:
                return handleError1(cause);
            case HTTP:
                return handlerHttpError(cause);
            default:
                logger.error("cause={}",cause);
        }
        return null;
    }

    private RetrofitError handlerHttpError(RetrofitError cause) {
        Response response = cause.getResponse();
        int status = response.getStatus();
        logger.error("handlerHttpError: url:{},http status:{},reason:{}",
                response.getUrl(), response.getStatus(), response.getReason());
        if (status != OK) {
            StatusCode statusCode = StatusCode.REST_ERROR;
            String message = String.format("handlerHttpError %s url:[%s],code:[%s],http status:[%s],kind:[%s]",
                    statusCode.getMessage(),
                    cause.getUrl(),
                    status,
                    response.getStatus(),
                    cause.getKind());
            throw new RestException(statusCode, cause, message);
        }
        return cause;
    }

    private Throwable handleError1(RetrofitError cause) {
        StatusCode statusCode = StatusCode.REST_ERROR;
        String message = String.format("handleError %s url:[%s],error:[%s],exception : [%s],kind:[%s]",
                statusCode.getMessage(),
                cause.getUrl(),
                cause.getCause().getMessage(),
                cause.getCause().getClass(),
                cause.getKind());
        throw new RestException(statusCode, cause, message);
    }
}
