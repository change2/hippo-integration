package org.change.hippo.server.parser.file;

import org.apache.commons.io.IOUtils;
import org.change.hippo.server.model.ServiceConfig;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PropertiesParser<T extends ServiceConfig> extends AbstractFileParser<T> {

    @Override
    public List<T> doParse(InputStream inputStream) throws Exception {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            if (properties.isEmpty()) {
                return Collections.emptyList();
            }
            return getServiceConverter().convert(properties.entrySet());
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
