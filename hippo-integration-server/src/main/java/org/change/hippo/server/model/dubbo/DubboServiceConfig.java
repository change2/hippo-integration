package org.change.hippo.server.model.dubbo;

import org.change.hippo.server.model.ServiceConfig;

import java.util.Arrays;

public class DubboServiceConfig extends ServiceConfig {

    private static final long serialVersionUID = 1299741352624280761L;
    private String interfaze;
	private String method;
	private String[] arguments;

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

    @Override
    public String toString() {
        return "DubboServiceConfig{" +
                "interfaze='" + interfaze + '\'' +
                ", method='" + method + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
