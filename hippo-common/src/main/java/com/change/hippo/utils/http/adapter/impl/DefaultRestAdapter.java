package com.change.hippo.utils.http.adapter.impl;

import com.change.hippo.utils.http.adapter.RestAdapter;
import com.change.hippo.utils.http.adapter.RestResponseParser;
import com.change.hippo.utils.message.Msg;
import com.change.hippo.utils.message.MsgBuilder;

/**
 * 默认的转换器
 *
 * @author:fulong
 * @create:2018/2/8 11:43.
 */
public class DefaultRestAdapter implements RestAdapter {
    /**
     * 请求超时编码
     */
    private static final int TIMEOUT = -2;

    /**
     * 返回的消息信息
     */
    private String responseText;

    @Override
    public Msg adapter(String responseText, int responseCode) {
        this.responseText = responseText;
        if (responseCode == TIMEOUT) {
            return MsgBuilder.me().setOk(false).setCode(responseCode).setMsg("request timeout").build();
        }
        return null;
    }

    @Override
    public <T> T build(RestResponseParser<T> responseParser) {
        return responseParser.parse(responseText);
    }

    /**
     * 获取到返回的信息
     *
     * @return
     */
    public String getResponseText() {
        return responseText;
    }
}
