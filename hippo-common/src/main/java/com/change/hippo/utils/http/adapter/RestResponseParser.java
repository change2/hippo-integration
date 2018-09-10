package com.change.hippo.utils.http.adapter;

/**
 *
 * 返回的消息解析器
 *
 */
public interface RestResponseParser<T> {

    /**
     * 解析返回的消息
     *
     * @param responseText
     * @return
     */
    T parse(String responseText);
}
