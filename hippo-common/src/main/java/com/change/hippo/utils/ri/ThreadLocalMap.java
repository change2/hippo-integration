package com.change.hippo.utils.ri;

/**
 */
public class ThreadLocalMap extends InheritableThreadLocal<RequestInfo> {

	@Override
	protected RequestInfo childValue(RequestInfo parentValue) {
		return RequestIdentityHolder.join(parentValue);
	}

}

