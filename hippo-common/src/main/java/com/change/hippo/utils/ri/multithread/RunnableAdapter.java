package com.change.hippo.utils.ri.multithread;

import com.change.hippo.utils.ri.RequestIdentityHolder;

public class RunnableAdapter extends AbstractAdapter implements Runnable {

    private Runnable task;

    public RunnableAdapter(Runnable task) {
        this(task, false);
    }


    public RunnableAdapter(Runnable task, boolean autoCreator) {
        this.task = task;
        if (autoCreator) {
            this.requestInfo = RequestIdentityHolder.generateNew();
            RequestIdentityHolder.set(requestInfo);
        }
    }

    @Override
    public void run() {
        super.supportRequestIdentity();
        task.run();
    }

}

