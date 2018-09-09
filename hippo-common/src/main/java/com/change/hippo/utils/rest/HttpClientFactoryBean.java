package com.change.hippo.utils.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.IdleConnectionEvictor;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import java.io.Closeable;

/**
 * User: change.long
 * Date: 2017/9/21
 * Time: 23:48
 */
public class HttpClientFactoryBean implements FactoryBean<HttpClient>, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientFactoryBean.class);

    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 500;
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 200;
    public static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

    private Integer connectTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;

    private Integer socketTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;

    private Integer connectionRequestTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;

    private Integer maxTotal = DEFAULT_MAX_TOTAL_CONNECTIONS;

    private Integer defaultMaxPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

    private IdleConnectionEvictor idleConnectionEvictor;

    private boolean daemon = true;

    private HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(3, false);

    private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    private HttpClient httpClient;

    @Override
    public HttpClient getObject() throws Exception {
        RequestConfig.Builder custom = RequestConfig.custom();
        RequestConfig requestConfig = custom
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

        if (idleConnectionEvictor != null) {
            idleConnectionEvictor.start();
        }
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setMaxConnTotal(maxTotal)
                .setMaxConnPerRoute(defaultMaxPerRoute)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(requestRetryHandler);
        httpClient = httpClientBuilder.build();
        return httpClient;
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public HttpRequestRetryHandler getRequestRetryHandler() {
        return requestRetryHandler;
    }

    public void setRequestRetryHandler(HttpRequestRetryHandler requestRetryHandler) {
        this.requestRetryHandler = requestRetryHandler;
    }

    public PoolingHttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    public IdleConnectionEvictor getIdleConnectionEvictor() {
        return idleConnectionEvictor;
    }

    public void setIdleConnectionEvictor(IdleConnectionEvictor idleConnectionEvictor) {
        this.idleConnectionEvictor = idleConnectionEvictor;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("Begin destroy Client");
        if (this.httpClient instanceof Closeable) {
            ((Closeable) this.httpClient).close();
        }
        /*
         * 关闭连接池
         */
        if (connectionManager != null) {
            connectionManager.shutdown();
        }

        /*
         * 关闭监控线程
         */
        if (idleConnectionEvictor != null) {
            idleConnectionEvictor.shutdown();
        }
        logger.info("Client destroyed!");
    }
}
