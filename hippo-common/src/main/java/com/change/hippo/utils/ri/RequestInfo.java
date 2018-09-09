package com.change.hippo.utils.ri;

import java.io.Serializable;

/**
 * 请求信息
 * 
 */
public class RequestInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标识
	 */
	private String id;
	/**
	 * 请求名称
	 */
	private String name;
	/**
	 * 请求IP
	 */
	private String ip;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 调用步骤
	 */
	private int step = 1;

	public RequestInfo(String id){
		this.id = id;
	}
	
	public RequestInfo(String id, String ip){
		this(id);
		this.ip = ip;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

    public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

    public void setIp(String ip) {
		this.ip = ip;
	}

	public String getVersion() {
		return version;
	}

    public void setVersion(String version) {
		this.version = version;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@Override
    public RequestInfo clone(){
		RequestInfo requestInfo = new RequestInfo(this.id, this.ip);
		requestInfo.setName(this.name);
		requestInfo.setVersion(this.version);
		requestInfo.setStep(this.getStep());
		return requestInfo;
	}

}