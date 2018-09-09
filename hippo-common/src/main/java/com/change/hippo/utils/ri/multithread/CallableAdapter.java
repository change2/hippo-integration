package com.change.hippo.utils.ri.multithread;

import com.change.hippo.utils.ri.RequestIdentityHolder;

import java.util.concurrent.Callable;

public class CallableAdapter<V> extends AbstractAdapter implements Callable<V> {

    private Callable<V> task;

    public CallableAdapter(Callable<V> task) {
        this(task, false);
    }

    public CallableAdapter(Callable<V> task, boolean autoCreator) {
        if (autoCreator) {
            this.requestInfo = RequestIdentityHolder.generateNew();
            RequestIdentityHolder.set(requestInfo);
        }
        this.task = task;
    }

    @Override
    public V call() throws Exception {
        super.supportRequestIdentity();
        return task.call();
    }

}

