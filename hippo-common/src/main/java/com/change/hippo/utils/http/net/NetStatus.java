package com.change.hippo.utils.http.net;

public enum NetStatus {
	C_UN_EXEC(1),//未执行
	C_TIMEOUT(2),//请求超时
	C_UN_KNOWN(3),//未知异常
	C_200(200),
	C_401(401),
	C_404(404),
	C_405(405),
	C_500(500),
	C_302(302),
	;
	private int code;

	private NetStatus(int code) {
		this.code = code;
	}

	public static NetStatus getStatus(int code){
		for(NetStatus status : values()){
			if(status.getCode() == code){
				return status;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

}
