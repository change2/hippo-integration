package com.change.hippo.utils.reflect;

/**
 * 反射自定义异常
 *
 * @author:fulong
 * @create:2017/8/6 16:27.
 */
public class ReflectException extends RuntimeException {

	private static final long serialVersionUID = -6305622126367292589L;

	public ReflectException() {
	}

	public ReflectException(String message) {
		super(message);
	}

	public ReflectException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectException(Throwable cause) {
		super(cause);
	}

	public ReflectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
