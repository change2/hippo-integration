package com.change.hippo.utils.kafka;

import java.io.Serializable;
import java.util.Date;

public class Message<T> implements Serializable {

    private static final long serialVersionUID = -8660676604220685447L;
    private String messageId;

    private Date sendTime;

    private String appId;

    private String eventType;

    private String clientIp;

    private T bizParam;

    public Message() {
    }

    public Message(T bizParam) {
        this.bizParam = bizParam;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public T getBizParam() {
        return bizParam;
    }

    public void setBizParam(T bizParam) {
        this.bizParam = bizParam;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", sendTime=" + sendTime +
                ", appId='" + appId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", bizParam=" + bizParam +
                '}';
    }
}
