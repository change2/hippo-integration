package org.change.hippo.server.model;

import java.io.Serializable;

/**
 * User: change.long
 * Date: 2017/11/21
 * Time: 下午4:32
 */
public class SecuritySettings implements Serializable{
    private static final long serialVersionUID = 1537623544626563883L;
    private String appId;
    private String apiSecret;

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    @Override
    public String toString() {
        return "SecuritySettings{" +
                "appId='" + appId + '\'' +
                ", apiSecret='" + apiSecret + '\'' +
                '}';
    }
}
