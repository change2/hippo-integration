package org.change.hippo.server.transformer;

import com.alibaba.fastjson.JSON;
import com.change.hippo.utils.message.Msg;
import org.change.hippo.server.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import static org.change.hippo.server.util.Constants.ERROR_999;
import static org.change.hippo.server.util.Constants.ERROR_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class ExceptionTransformer {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionTransformer.class);

    /**
     * @return 构建请求拒绝消息
     */
    private static final String refusedText = JSON.toJSONString(new Msg(ERROR_CODE, false, null, ERROR_999));


    /**
     * 错误通道
     *
     * @param message
     * @return
     */
    public Message<String> buildErrorResponse(Message<?> message) {
        Throwable error = ((Throwable) message.getPayload());
        MessageHeaders header = message.getHeaders();
        String xAppContext = (String) header.get(Constants.X_APP_CONTEXT);
        String xServiceId = (String) header.get(Constants.X_SERVICE_ID);
        logger.error("Error!  appContext={},ServiceId={},ErrorMsg:{}", xAppContext, xServiceId, error.getMessage(),error);
        return MessageBuilder.withPayload(refusedText)
                .copyHeaders(message.getHeaders())
                .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .setHeader(Constants.X_SERVICE_CODE, ERROR_CODE)
                .setHeader(Constants.X_SERVICE_MESSAGE, error.getMessage())
                .build();
    }

    /**
     * 请求被拒绝通道
     *
     * @param message
     * @return
     */
    public Message<String> buildRefusedResponse(Message<?> message) {
        MessageHeaders header = message.getHeaders();
        String xAppContext = (String) header.get(Constants.X_APP_CONTEXT);
        String xServiceId = (String) header.get(Constants.X_SERVICE_ID);
        String errorCode = (String) header.get("error_Code");
        String errorMsg = (String) header.get("error_Msg");
        logger.info("No Privileges!appContext={},ServiceId={},ErrorCode={},ErrorMsg={}", xAppContext, xServiceId, errorCode, errorMsg);
        return MessageBuilder.withPayload(JSON.toJSONString(
                new Msg(Integer.parseInt(errorCode), false, null, errorMsg))).copyHeaders(header)
                .setHeader(Constants.X_SERVICE_CODE, errorCode)
                .setHeader(Constants.X_SERVICE_MESSAGE, errorMsg)
                .build();
    }

}