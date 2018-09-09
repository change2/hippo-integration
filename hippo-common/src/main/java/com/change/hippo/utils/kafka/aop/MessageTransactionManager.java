package com.change.hippo.utils.kafka.aop;


public class MessageTransactionManager {

    private ThreadLocal<MessageTransaction> threadLocal = new ThreadLocal<MessageTransaction>() {
        @Override
        protected MessageTransaction initialValue() {
            return new MessageTransaction();
        }
    };


    public MessageTransaction getMessageTransaction() {
        return this.threadLocal.get();
    }

    public void clearMessageTransaction(){
        this.threadLocal.remove();
    }


}
