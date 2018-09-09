package com.change.hippo.utils.result;

import java.io.Serializable;

/**
 * User: change.long
 * Date: 2017/9/25
 * Time: 下午2:14
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = -4030965430133127252L;
    private String id;
    private Integer connection_retry_interval;


    public Integer getConnection_retry_interval() {
        return connection_retry_interval;
    }

    public void setConnection_retry_interval(Integer connection_retry_interval) {
        this.connection_retry_interval = connection_retry_interval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "id='" + id + '\'' +
                ", connection_retry_interval=" + connection_retry_interval +
                '}';
    }
}
