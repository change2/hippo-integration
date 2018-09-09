package com.change.hippo.utils.json;

public class JsonException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JsonException() {
		super();
	}

	public JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonException(String message) {
		super(message);
	}

	public JsonException(Throwable cause) {
		super(cause);
	}
	
}
