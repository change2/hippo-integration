package org.change.hippo.server.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpRequestBase;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RidUtil {

    public static void doRid(HttpRequestBase httpRequestBase) throws UnsupportedEncodingException {
        RequestInfo requestInfo = RequestIdentityHolder.get(true);
        if (requestInfo != null) {
            httpRequestBase.addHeader("rid", requestInfo.getId());
            httpRequestBase.addHeader("rstep", requestInfo.getStep() + "");
            httpRequestBase.addHeader("rname", URLEncoder.encode(StringUtils.trimToEmpty(requestInfo.getName()), "UTF-8"));
            httpRequestBase.addHeader("rversion", StringUtils.trimToEmpty(requestInfo.getVersion()));
        }
    }

}
