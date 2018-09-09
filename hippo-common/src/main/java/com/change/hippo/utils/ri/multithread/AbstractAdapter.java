package com.change.hippo.utils.ri.multithread;


import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;

public class AbstractAdapter {

    protected RequestInfo requestInfo;

    public AbstractAdapter() {
        this.requestInfo = RequestIdentityHolder.get();
    }

    public void supportRequestIdentity() {
        if (requestInfo != null) {
            RequestIdentityHolder.join(requestInfo);
        }
    }
}
