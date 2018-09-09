package com.change.hippo.utils.http.adapter;

/**
 *
 * 返回的消息解析器
 *
 * @author:fulong
 * @create:2018/2/8 11:46.
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
