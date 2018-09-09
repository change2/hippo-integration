package com.change.hippo.utils.spring.mvc.support;

/**
 * User: change.long
 * Date: 2017/8/21
 * Time: 下午6:57
 */

import java.io.Serializable;

public class Msg implements Serializable {
    private int code;
    private boolean ok;
    private Object data;
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
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOk() {
        return this.ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Msg{code=" + this.code + ", ok=" + this.ok + ", data='" + this.data + '\'' + ", msg='" + this.msg + '\'' + '}';
    }
}
