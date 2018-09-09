package com.change.hippo.utils.http.net;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class NetContext {

    private String reqUrl;

    private NetStatus status = NetStatus.C_UN_EXEC;

    private String charset = "utf-8";

    /**
     * 设置连接超时时间，单位毫秒(tcp三次握手超时时间)
     * <p>默认为6秒超时</p>
     */
    private int connectTimeout = 6000;
    /**
     * 从连接池获取对象超时时间，单位毫秒
     * <p>连接池获取对象超时为3秒</p>
     */
    private int connectionRequestTimeout = 3000;
    /**
     * 接口请求超时时间，单位毫秒，如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
     * <p>默认在6s内接口未返回报超时</p>
     */
    private int socketTimeout = 6000;

    /**
     * 扩展字段
     */
    private Map<String, Object> extras = new HashMap<>();
    /**
     * 请求参数
     */
    private Map<String, String> params = new HashMap<>();

    private String source;
    /**
     * 返回请求结果信息
     */
    private String response;

    /**
     * 账户登录
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 请求体
     */
    private String requestBody;

    private NetContext() {
    }

    public static NetContext me(String reqUrl) {
        return new NetContext().setReqUrl(reqUrl);
    }


    public String getReqUrl() {
        return reqUrl;
    }

    public NetContext setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
        return this;
    }

    public NetStatus getStatus() {
        return status;
    }

    public NetContext setStatus(NetStatus status) {
        this.status = status;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public NetContext setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtra(String key) {
        return (T) extras.get(key);
    }

    public NetContext putExtra(String key, Object extra) {
        extras.put(key, extra);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof NetContext) {
            NetContext context = (NetContext) obj;
            return context.getReqUrl().equals(this.getReqUrl());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return reqUrl.hashCode();
    }

    public String getSource() {
        return source;
    }

    public NetContext setSource(String source) {
        this.source = source;
        return this;
    }

    public NetContext setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public String getUsername() {
        return username;
    }

    public NetContext setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public NetContext setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 是否需要登录验证
     *
     * @return
     */
    public boolean isAuthorization() {
        return StringUtils.isNotBlank(username);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public NetContext setParams(Map<String, String> params) {
        if(params!=null){
            this.params.putAll(params);
        }
        return this;
    }

    public NetContext addParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public NetContext setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public NetContext setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public NetContext setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public NetContext setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public boolean isSuccess() {
        return NetStatus.C_200 == status;
    }

}
