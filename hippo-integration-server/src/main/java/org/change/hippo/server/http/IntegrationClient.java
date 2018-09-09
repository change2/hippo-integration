package org.change.hippo.server.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.util.Messages;
import org.change.hippo.server.util.RidUtil;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;
import org.change.hippo.server.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * http 请求类
 */
public class IntegrationClient implements InitializingBean, DisposableBean {

    /**
     * 组名
     */
    private String group;
    /**
     * 应用名
     */
    private String appName;

    /**
     * 连接池最大连接数
     */
    private int max;

    /**
     * 前置机地址
     */
    private String URL;

    /**
     * 数据传输超时时间
     */
    private int socketTimeOut;
    /**
     * 从连接池获取连接超时时间
     */
    private int requestConnTimeOut;
    /**
     * 连接超时时间
     */
    private int connTimeOut;

    /**
     * 请求方式
     */
    private String method;

    private final static Logger logger = LoggerFactory.getLogger(IntegrationClient.class);
    private final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private IdleConnectionMonitorThread cleanThread;
    private HttpConnectionKeepAliveStrategy keepAliveStrategy;
    private RequestConfig requestConfig = null;
    private String nodeName;
    private String appContext;
    private final static String PROPERTY_CAN_NOT_NULL = "The property '%s' can't be null!";
    private final static int DEFAULT_CONNECTION_REQUEST_TIME_OUT = 10* 1000;
    private final static int DEFAULT_SOCKET_TIME_OUT = 30 * 1000;
    private final static int DEFAULT_CONNECTION_TIME_OUT = 10 * 1000;
    private final static int KEEP_ALIVE_TIME = 60 * 1000;

    private HttpClient httpClient;
    private final static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        }
    };

    private final static String SUCCESS_LOG = "Received response,path={},requestTime={},responseTime={},useTime={}";
    private final static String ERROR_LOG = "Service {} Error,path={},requestTime={},responseTime={},useTime={}";
    private final static String STATUS_ERROR = "Service Invoking Error,entity=%s,path=%s,statusCode=%s";

    public String httpRequest(String requestObject, String requestPath,String requestMethod) throws Exception {
        HttpResponse response = null;
        long l1 = System.currentTimeMillis();
        String requestTime = threadLocal.get().format(new Date(l1));
        try {
            this.method = requestMethod;
            HttpRequestBase httpRequest = httpRequestBase(requestObject, requestPath, requestTime);
            requestPath = httpRequest.getURI().getPath();
            response = httpClient.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseString;
            if (statusCode == HttpStatus.SC_OK) {
                responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            } else {
                throw new Exception(String.format(STATUS_ERROR, this.URL, requestPath, statusCode));
            }
            return responseString;
        } catch (Exception e) {
            long l2 = System.currentTimeMillis();
            String responseTime = threadLocal.get().format(new Date(l2));
            logger.error(e.getMessage(),e);
            logger.error(ERROR_LOG, requestPath, requestPath, requestTime, responseTime, l2 - l1);
            throw e;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (Exception e) {
                    logger.error("Error", e);
                }
            }
            long l2 = System.currentTimeMillis();
            String responseTime = threadLocal.get().format(new Date(l2));
            logger.info(SUCCESS_LOG, requestPath, requestTime, responseTime, l2 - l1);
        }
    }

    /**
     * 请求方式
     * @param requestObject
     * @param method
     * @param requestTime
     * @return
     * @throws Exception
     */
    public HttpRequestBase httpRequestBase(String requestObject, String method, String requestTime) throws Exception{
        HttpRequestBase httpRequest;
        switch (this.method) {
            case "GET":
                httpRequest = getHttpGet(requestObject, method, requestTime);
                break;
            case "PUT":
                httpRequest = getHttpPut(requestObject, method, requestTime);
                break;
            case "DELETE":
                httpRequest = getHttpDelete(requestObject, method, requestTime);
                break;
            default:
                httpRequest = getHttpPost(requestObject, method, requestTime);
        }
        return httpRequest;
    }

    /**
     * POST 请求
     * @param requestString
     * @param method
     * @param requestTime
     * @return
     * @throws Exception
     */
    private HttpPost getHttpPost(String requestString, String method, String requestTime)
            throws Exception {
        if (requestString == null) {
            requestString = "[]";
        }
        HttpPost httpPost = new HttpPost(this.URL +  method);
        httpPost.addHeader(Constants.X_REQUEST_TIME, requestTime);
        httpPost.addHeader(Constants.X_APP_CONTEXT, this.appContext);
        httpPost.addHeader(Constants.X_NODE_NAME, this.nodeName);
        httpPost.addHeader(Constants.USER_SESSION_KEY, Messages.getHeader(Constants.USER_SESSION_KEY));
        RequestInfo requestInfo = RequestIdentityHolder.get();
        if(requestInfo != null){
            RidUtil.doRid(httpPost);
        }
        httpPost.setConfig(this.requestConfig);
        httpPost.setEntity(new StringEntity(requestString, ContentType.APPLICATION_JSON));
        return httpPost;
    }

    /**
     * GET 请求
     * @param requestString
     * @param method
     * @param requestTime
     * @return
     * @throws Exception
     */
    private HttpGet getHttpGet(String requestString, String method, String requestTime)
            throws Exception {
        if (requestString == null) {
            requestString = "[]";
        }
        HttpGet httpGet = new HttpGet(UrlUtils.getUrl(this.URL,method,requestString));
        httpGet.addHeader(Constants.X_REQUEST_TIME, requestTime);
        httpGet.addHeader(Constants.X_APP_CONTEXT, this.appContext);
        httpGet.addHeader(Constants.X_NODE_NAME, this.nodeName);
        httpGet.addHeader(Constants.USER_SESSION_KEY, Messages.getHeader(Constants.USER_SESSION_KEY));
        RequestInfo requestInfo = RequestIdentityHolder.get();
        if(requestInfo != null){
            RidUtil.doRid(httpGet);
        }
        httpGet.setConfig(this.requestConfig);
        return httpGet;
    }

    /**
     * PUT 请求
     * @param requestString
     * @param method
     * @param requestTime
     * @return
     * @throws Exception
     */
    private HttpPut getHttpPut(String requestString, String method, String requestTime)
            throws Exception {
        if (requestString == null) {
            requestString = "[]";
        }
        HttpPut httpPut = new HttpPut(this.URL +  method);
        httpPut.addHeader(Constants.X_REQUEST_TIME, requestTime);
        httpPut.addHeader(Constants.X_APP_CONTEXT, this.appContext);
        httpPut.addHeader(Constants.X_NODE_NAME, this.nodeName);
        httpPut.addHeader(Constants.USER_SESSION_KEY, Messages.getHeader(Constants.USER_SESSION_KEY));
        RequestInfo requestInfo = RequestIdentityHolder.get();
        if(requestInfo != null){
            RidUtil.doRid(httpPut);
        }
        httpPut.setConfig(this.requestConfig);
        httpPut.setEntity(new StringEntity(requestString, ContentType.APPLICATION_JSON));
        return httpPut;
    }

    /**
     * DELETE 请求
     * @param requestString
     * @param method
     * @param requestTime
     * @return
     * @throws Exception
     */
    private HttpDelete getHttpDelete(String requestString, String method, String requestTime)
            throws Exception {
        if (requestString == null) {
            requestString = "[]";
        }
        HttpDelete httpDelete = new HttpDelete(UrlUtils.getUrl(this.URL,method,requestString));
        httpDelete.addHeader(Constants.X_REQUEST_TIME, requestTime);
        httpDelete.addHeader(Constants.X_APP_CONTEXT, this.appContext);
        httpDelete.addHeader(Constants.X_NODE_NAME, this.nodeName);
        httpDelete.addHeader(Constants.USER_SESSION_KEY, Messages.getHeader(Constants.USER_SESSION_KEY));
        RequestInfo requestInfo = RequestIdentityHolder.get();
        if(requestInfo != null){
            RidUtil.doRid(httpDelete);
        }
        httpDelete.setConfig(this.requestConfig);
        return httpDelete;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("Begin destroy MbpIntegrationClient……");
        if (cm != null) {//关闭连接池
            cm.shutdown();
        }
        if (cleanThread != null) {//关闭监控线程
            cleanThread.shutdown();
        }
        logger.info("MbpIntegrationClient destroyed!");
    }

    private void validate() throws Exception {
        if (StringUtils.isBlank(group)) {
            throw new Exception(String.format(PROPERTY_CAN_NOT_NULL, "group"));
        }
        if (StringUtils.isBlank(appName)) {
            throw new Exception(String.format(PROPERTY_CAN_NOT_NULL, "appName"));
        }
        if (max <= 0) {
            throw new Exception(String.format(PROPERTY_CAN_NOT_NULL, "max"));
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        long l1 = System.currentTimeMillis();
        logger.info("Begin initialize IntegrationClient……");
        //校验参数
        validate();
        if (socketTimeOut <= 0) {
            socketTimeOut = DEFAULT_SOCKET_TIME_OUT;
        }
        if (connTimeOut <= 0) {
            connTimeOut = DEFAULT_CONNECTION_TIME_OUT;
        }
        if (requestConnTimeOut <= 0) {
            requestConnTimeOut = DEFAULT_CONNECTION_REQUEST_TIME_OUT;
        }
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeOut)
                .setConnectTimeout(connTimeOut)
                .setConnectionRequestTimeout(requestConnTimeOut)
                .build();

        this.cm.setMaxTotal(this.max);
        this.cm.setDefaultMaxPerRoute(this.max);
        if (this.cleanThread == null) {
            this.cleanThread = new IdleConnectionMonitorThread(cm, KEEP_ALIVE_TIME);
        }
        if (this.keepAliveStrategy == null) {
            this.keepAliveStrategy = new HttpConnectionKeepAliveStrategy(KEEP_ALIVE_TIME);
        }
        this.cleanThread.start();//开启清理线程
        try {
            WebAppUtil.initDeployInfo();
            this.appContext = WebAppUtil.getDeployName();
            this.nodeName = WebAppUtil.getDeployNode();
        } catch (Exception e) {
            logger.error("Fetch JVM info Error!", e);
        }

        httpClient = HttpClients.custom().setConnectionManager(cm)
                .setKeepAliveStrategy(keepAliveStrategy).setDefaultRequestConfig(requestConfig).build();

        logger.info("IntegrationClient initialized!cost={}", System.currentTimeMillis() - l1);
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }

    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public void setRequestConnTimeOut(int requestConnTimeOut) {
        this.requestConnTimeOut = requestConnTimeOut;
    }

    public void setConnTimeOut(int connTimeOut) {
        this.connTimeOut = connTimeOut;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setCleanThread(IdleConnectionMonitorThread cleanThread) {
        this.cleanThread = cleanThread;
    }

    public void setKeepAliveStrategy(HttpConnectionKeepAliveStrategy keepAliveStrategy) {
        this.keepAliveStrategy = keepAliveStrategy;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
