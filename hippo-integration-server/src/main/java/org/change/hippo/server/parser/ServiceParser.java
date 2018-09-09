package org.change.hippo.server.parser;

import org.change.hippo.server.model.ServiceConfig;

import java.util.List;

public interface ServiceParser<T extends ServiceConfig> {

	public List<T> parse() throws Exception;

}
