package com.change.hippo.utils.rest.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import com.change.hippo.utils.ri.RIDConst;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;

import java.io.UnsupportedEncodingException;

import static java.net.URLEncoder.encode;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class RidFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        RequestInfo requestInfo = RequestIdentityHolder.get();
        if (requestInfo != null) {
            template.header(RIDConst.RID, trimToEmpty(requestInfo.getId()));
            template.header(RIDConst.RSTEP, trimToEmpty(requestInfo.getStep() + ""));
            try {
                template.header(RIDConst.RNAME, encode(trimToEmpty(requestInfo.getName()), "UTF-8"));
            } catch (UnsupportedEncodingException ignore) {
            }
            template.header(RIDConst.RVERSION, trimToEmpty(requestInfo.getVersion()));
        }
    }
}
