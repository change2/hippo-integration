package com.change.hippo.utils.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Message-Driver-Pojo Consumer
 * @param <T>
 */
public abstract class MdpMessageConsumer<T> implements MessageConsumer {

    private final Class<T> entityClass;
    private final boolean isArray;

    public MdpMessageConsumer() {
        super();
        Type[] params = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        if (params[0] instanceof ParameterizedType) {
            ParameterizedType param = (ParameterizedType) params[0];
            this.entityClass = (Class<T>) param.getActualTypeArguments()[0];
            this.isArray = true;
        } else {
            this.entityClass = (Class<T>) params[0];
            this.isArray = false;
        }
    }

    @Override
    public void consume(String payload) {
        if (isArray) {
            JSONArray jsonArray = JSON.parseArray(payload);
            T message = (T) jsonArray.toJavaList(entityClass);
            this.consumerInternal(message);
        } else {
            this.consumerInternal(JSON.parseObject(payload, entityClass));
        }
    }

    protected abstract void consumerInternal(T message);

}
