package com.change.hippo.utils.ri.multithread;

import java.util.concurrent.Callable;

public final class AdapterFactory {

    private AdapterFactory() {
    }

    public static <V> Callable<V> adapt(Callable<V> callable) {
        return adapt(callable, false);
    }

    public static Runnable adapt(Runnable runnable) {
        return adapt(runnable, false);
    }

    public static <V> Callable<V> adapt(Callable<V> callable, boolean autoCreator) {
        if (callable != null) {
            callable = new CallableAdapter<V>(callable, autoCreator);
        }
        return callable;
    }

    public static Runnable adapt(Runnable runnable, boolean autoCreator) {
        if (runnable != null) {
            runnable = new RunnableAdapter(runnable, autoCreator);
        }
        return runnable;
    }

}

