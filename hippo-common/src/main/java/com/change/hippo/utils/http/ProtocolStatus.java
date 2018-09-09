package com.change.hippo.utils.http;

/**
 * Created by mike on 2017/11/22.
 */
public enum ProtocolStatus {

    HTTP("http://"),
    HTTPS("https://"),
    TCP("tcp://"),;

    ProtocolStatus(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
