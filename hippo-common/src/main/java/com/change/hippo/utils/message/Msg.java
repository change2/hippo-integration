package com.change.hippo.utils.message;

import java.io.Serializable;

/**
 * 定义api信息接口返回的消息
 */
public class Msg implements Serializable {

    // 返回业务的处理code编码
    private int code;
    // 定义返回的业务处理状态结果
    private boolean ok;
    // 返回的数据体 一般json格式
    private String data;
    // 返回是message信息
    private String msg;

    public Msg() {
    }

    public Msg(int code, String data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Msg(int code, boolean ok, String data, String msg) {
        this.code = code;
        this.ok = ok;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "code=" + code +
                ", ok=" + ok +
                ", data='" + data + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
