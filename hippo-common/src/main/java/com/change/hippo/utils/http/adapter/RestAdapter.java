package com.change.hippo.utils.http.adapter;


import com.change.hippo.utils.message.Msg;

/**
 * Rest 转换消息成标准的Msg对象
 *
 */
public interface RestAdapter {

    /**
     * 请求消息转换
     *
     * @param responseText
     * @param responseCode
     * @return
     */
    Msg adapter(String responseText, int responseCode);

    /**
     * 返回具体消息对象
     *
     * @param restResponseParser
     * @param <T>
     * @return
     */
    <T> T build(RestResponseParser<T> restResponseParser);
}
