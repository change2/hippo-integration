package com.change.hippo.utils.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * User: change.long
 * Date: 2017/10/12
 * Time: 上午1:13
 */
public class FastJsonConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonConverter.class);
    private String utf8 = "UTF-8";

    public FastJsonConverter() {
    }

    public FastJsonConverter(String utf8) {
        this.utf8 = utf8;
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        long start = System.currentTimeMillis();
        String charset = this.utf8;
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType(), charset);
        }
        InputStream in = null;
        try {
            in = body.in();
            return JSON.parseObject(in, Charset.forName(charset), type);
        } catch (IOException e) {
            throw new ConversionException(e);
        } catch (JSONException e) {
            throw new ConversionException(e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage(), e);
                }
            }
            LOGGER.info("fromBody cost={}ms", (System.currentTimeMillis() - start));
        }
    }

    @Override
    public TypedOutput toBody(Object object) {
        return new JsonTypedOutput(JSON.toJSONBytes(object), utf8);
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }

}
