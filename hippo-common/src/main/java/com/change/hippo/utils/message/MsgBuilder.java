package com.change.hippo.utils.message;

import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.json.JsonHelpers;

import java.util.HashMap;
import java.util.Map;

/**
 * 适配common提供的Msg类
 */
public class MsgBuilder {

	private int code;
	private boolean ok;
	private String data;
	private String msg;
	private Map<String,Object> extra = new HashMap<>();

	private MsgBuilder(){}

	public static MsgBuilder me(){
		return new MsgBuilder();
	}

	public int getCode() {
		return code;
	}

	public MsgBuilder setCode(int code) {
		this.code = code;
		return this;
	}

	public boolean isOk() {
		return ok;
	}

	public MsgBuilder setOk(boolean ok) {
		this.ok = ok;
		return this;
	}

	public String getData() {
		return data;
	}

	public MsgBuilder setData(String data) {
		this.data = data;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public MsgBuilder setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public MsgBuilder put(String key,String value){
		this.extra.put(key,value);
		return this;
	}

	public Msg build(){
		if( StringUtils.isBlank(data) && !this.extra.isEmpty()){
			data = JsonHelpers.me().toJson(extra);
		}
		return new Msg(code,ok,data,msg);
	}
}
