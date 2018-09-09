package com.change.hippo.utils.rest;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * User: change.long
 * Date: 2017/8/31
 * Time: 下午2:05
 */
public class RidInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        RequestInfo requestInfo = RequestIdentityHolder.get();
        Request request = chain.request();
        Request newRequest;
        Request.Builder builder = request.newBuilder();
        if (requestInfo != null) {
            builder = builder
                    .addHeader("rid", StringUtils.trimToEmpty(requestInfo.getId()))
                    .addHeader("rstep", StringUtils.trimToEmpty(requestInfo.getStep() + ""))
                    .addHeader("rname", URLEncoder.encode(StringUtils.trimToEmpty(requestInfo.getName()), "UTF-8"))
                    .addHeader("rversion", StringUtils.trimToEmpty(requestInfo.getVersion()));
        }
        newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
