package com.change.hippo.utils.kafka;

import com.change.hippo.utils.ri.multithread.AdapterFactory;
import org.springframework.core.task.TaskDecorator;

public class RidTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        return AdapterFactory.adapt(runnable);
    }
}
