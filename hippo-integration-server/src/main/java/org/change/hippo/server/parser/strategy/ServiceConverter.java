package org.change.hippo.server.parser.strategy;

import org.change.hippo.server.model.ServiceConfig;

import java.util.Collection;
import java.util.List;

public interface ServiceConverter<T extends ServiceConfig> {

	public List<T> convert(Collection collection) throws Exception;

}
