package com.change.hippo.utils.rest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * User: change.long
 * Date: 2017/12/28
 * Time: 下午6:54
 */
public class StringConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringConverter.class);
    private String encoding = "UTF-8";

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        InputStream in = null;
        try {
            in = body.in();
            return IOUtils.toString(in, encoding);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    @Override
    public TypedOutput toBody(Object object) {
        return new TypedString(object.toString());
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
