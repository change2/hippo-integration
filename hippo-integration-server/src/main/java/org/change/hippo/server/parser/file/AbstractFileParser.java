package org.change.hippo.server.parser.file;

import org.change.hippo.server.parser.AbstractServiceParser;
import org.change.hippo.server.model.ServiceConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractFileParser<T extends ServiceConfig> extends AbstractServiceParser<T> implements FileParser<T> {

    private String path;
    private String encoding = "UTF-8";

    @Override
    public List<T> parse() throws Exception {
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        Resource[] resource = resourceLoader.getResources(path);
        if (resource != null && resource.length > 0) {
            List<T> parseList = new ArrayList<T>();
            for (int i = 0, len = resource.length; i < len; i++) {
                parseList.addAll(doParse(resource[i].getInputStream()));
            }
            return parseList;
        }
        return Collections.emptyList();
    }

    public  Collection<? extends T> doParse(InputStream input) throws Exception{
        return null;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
