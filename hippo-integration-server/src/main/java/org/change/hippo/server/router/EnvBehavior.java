package org.change.hippo.server.router;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;

/**
 * User: change.long
 * Date: 2017/11/30
 * Time: 下午2:54
 */
public class EnvBehavior implements InitializingBean {


    public String route(Message<String> message) throws Exception {
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
