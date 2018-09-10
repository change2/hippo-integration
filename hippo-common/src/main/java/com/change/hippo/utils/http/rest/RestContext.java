package com.change.hippo.utils.http.rest;

import com.change.hippo.utils.http.adapter.RestAdapter;

import java.io.Serializable;
import java.util.Map;

/**
 */
public class RestContext implements Serializable {

	private static final long serialVersionUID = -7116740851652707555L;

	/**
	 * 请求地址
	 */
	private String reqUrl;
	/**
	 * 采用stream方式发送请求
	 */
	private String requestBody;
	/**
	 * form格式请求(?key=value)
	 */
	private Map<String,String> params;

	/**
	 * 是否是POST请求
	 */
	private boolean post = true;
	/**
	 * basic认证用户名
	 */
	private String username;
	/**
	 * basic认证密码
	 */
	private String password;
    /**
     * 消息转换
     */
	private RestAdapter restAdapter;

	private RestContext(){}

	public static RestContext me(){
		return new RestContext();
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public RestContext setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
		return this;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public RestContext setRequestBody(String requestBody) {
		this.requestBody = requestBody;
		return this;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public RestContext setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}

	public boolean isPost() {
		return post;
	}

	public RestContext setPost(boolean post) {
		this.post = post;
		return this;
	}

    public String getUsername() {
        return username;
    }

    public RestContext setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RestContext setPassword(String password) {
        this.password = password;
        return this;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public RestContext setRestAdapter(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
        return this;
    }
}
