package org.change.hippo.server.util;

public interface Constants {

    String X_SERVICE_ID = "x-service-id";
    String X_SERVICE_CODE = "x-service-code";
    String X_SERVICE_MESSAGE = "x-service-message";

    String X_CLIENT_GROUP = "x-client-group";
    String X_CLIENT_APPNAME = "x-client-appname";
    String X_REQUEST_TIME = "x-request-time";
    String X_MESSAGE_ID = "x-message-id";
    String X_FORWORD_FOR = "x-forwarded-for";
    String X_APP_CONTEXT = "x-app-context";
    String X_NODE_NAME = "x-node-name";
    String RECEIVE_TIME = "receiveTime";
    String RESPONSE_TIME = "responseTime";

    String X_CONTENT_SIGNATURE = "x-content-signature";
    String PERMISSION_NAME = "permission";
    String ACTION_TYPE_NAME = "actionType";
    String METHOD_NAME = "method";
    String REQUEST_PARAMS = "param";

    String USER_SESSION_KEY = "ssusersessionkey";
    String SYS_USER_SESSION_KEY = "sessionKey";

    String FROM = "hippo-integration";
    String DEFAULT_GROUP = "sdn";
    int DEF_MAX = 200;

    String KEY = "Shanghai@#Telecom";
    //String REFUSE = "Permission Denied! Please apply to Smartriver for permission! Msg:%s";
    String ERROR_999 = "系统异常！";

    int ERROR_CODE = 999;
    String OK = "00";
    String OK_MSG = "ok";

    String REQUEST_URL = "http_requestUrl";

    String OLD_VERSION_URL = "/data/api/version/";
    String OLD_VERSION_IOS_URL = "/data/api/version/queryIosAppIsUpdate";
    //FOR CIRCUIT BREAKER
    int CIRCUIT_BREAKER_ROLLING_TIME = 10 * 1000; //统计滚动的时间窗口
    int CIRCUIT_BREAKER_SLEEP_TIME = 5 * 1000; //熔断时间窗口，当熔断器打开5s后进入半开状态，允许部分流量进来重试
    int CIRCUIT_BREAKER_REQUEST_VALUME = 20; //熔断器在整个统计时间内是否开启的阀值
    int CIRCUIT_BREAKER_ERROR_PERCENT = 50;  //熔断打开最大错误率
    int CIRCUIT_BREAKER_MAX_CONCURRENT_REQUESTS = 200; //最大并发数


    String REMAINING_HEADER = "X-RateLimit-Remaining";
    String REPLENISH_RATE_HEADER = "X-RateLimit-Replenish-Rate";
    String BURST_CAPACITY_HEADER = "X-RateLimit-Burst-Capacity";

    String TOKEN_REQUESTED = "1";

    String GATEWAY_API_DEFINE_SQL = "select service_id, service_desc, service_model, service_log, ssd.domain, path, param," +
            "action_type, permission, argument, interfaze,CONCAT(mode,method) as method, replenishRate, burstCapacity," +
            "circuitBreakerEnabled, metricsRollingStatisticalWindowInMilliseconds, circuitBreakerSleepWindowInMilliseconds," +
            "circuitBreakerRequestVolumeThreshold, circuitBreakerErrorThresholdPercentage, executionIsolationSemaphoreMaxConcurrentRequests," +
            "fallbackIsolationSemaphoreMaxConcurrentRequests from sdn_service_config as ssc,sdn_service_domian as ssd where ssc.`status` = 1 and ssd.`name`= ssc.domain";

}
