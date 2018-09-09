package org.change.hippo.server.model.hessian;

import org.change.hippo.server.model.ServiceConfig;

public class HessianServiceConfig extends ServiceConfig {

	private static final long serialVersionUID = 1L;

	private String url;
	private String interfaze;
	private String method;
	private String[] arguments;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInterfaze() {
		return interfaze;
	}

	public void setInterfaze(String interfaze) {
		this.interfaze = interfaze;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

}
