package com.change.hippo.utils.kafka.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageAop {
    private Logger logger = LoggerFactory.getLogger(MessageAop.class);

    private MessageTransactionManager transactionManager;

    public void setTransactionManager(MessageTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object doTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MessageTransaction mtm = transactionManager.getMessageTransaction();
        boolean debugEnabled = logger.isDebugEnabled();
        try {
            mtm.begin();
            if (debugEnabled) {
                logger.debug("doTransaction Begin!");
            }
            Object object = proceedingJoinPoint.proceed();
            try {
                mtm.commit();
                if (debugEnabled) {
                    logger.debug("Message Transaction commit successful!");
                }
            } catch (Exception e) {
                logger.error("Message Transaction commit error!", e);
            }
            return object;
        } catch (Exception e) {
            mtm.rollback();
            logger.error("Message transaction rollback cause by ", e);
            throw e;
        } finally {
            transactionManager.clearMessageTransaction();
        }
    }

}
