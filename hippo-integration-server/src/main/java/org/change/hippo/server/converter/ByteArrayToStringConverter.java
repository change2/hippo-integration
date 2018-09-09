package org.change.hippo.server.converter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ByteArrayToStringConverter extends AbstractHttpMessageConverter<String> {
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public ByteArrayToStringConverter() {
		super(new MediaType[] {MediaType.APPLICATION_JSON});
	}
	@Override
	protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return StreamUtils.copyToString(inputMessage.getBody(), getContentTypeCharset(inputMessage.getHeaders().getContentType()));
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		if(!clazz.isArray()){
			return false;
		}
		Class<?> componentType = clazz.getComponentType();
		return Byte.TYPE==componentType || Byte.class==componentType;
	}
	
	@Override
	protected Long getContentLength(String str, MediaType contentType) throws IOException {
		try {
			return (long) str.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException(ex);
		}		
	}

	@Override
	protected void writeInternal(String str, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		StreamUtils.copy(str, getContentTypeCharset(outputMessage.getHeaders().getContentType()), outputMessage.getBody());
	}
	
	private Charset getContentTypeCharset(MediaType contentType) {
		if ((contentType != null) && (contentType.getCharset() != null)) {
			return contentType.getCharset();
		}

		return DEFAULT_CHARSET;
	}

	
}
