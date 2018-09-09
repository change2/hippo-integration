package com.change.hippo.utils.rest;

import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;
import retrofit.RequestInterceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RidRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        RequestInfo requestInfo = RequestIdentityHolder.get();
        if (requestInfo != null) {
            request.addHeader("rid", StringUtils.trimToEmpty(requestInfo.getId()));
            request.addHeader("rstep", StringUtils.trimToEmpty(requestInfo.getStep() + ""));
            try {
                request.addHeader("rname", URLEncoder.encode(StringUtils.trimToEmpty(requestInfo.getName()), "UTF-8"));
            } catch (UnsupportedEncodingException ignore) {}
            request.addHeader("rversion", StringUtils.trimToEmpty(requestInfo.getVersion()));
        }
    }
}
