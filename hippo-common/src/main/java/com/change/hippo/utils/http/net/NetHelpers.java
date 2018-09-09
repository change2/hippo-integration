package com.change.hippo.utils.http.net;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class NetHelpers {


    private static final Logger LOGGER = LoggerFactory.getLogger(NetHelpers.class);

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.75 Safari/537.36";
    public static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    public static final String ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
    public static final String ACCEPT_ENCODING = "gzip, deflate";
    public static final String CONNECTION = "keep-alive";
    public static final String PRAGRAM = "no-cache";
    public static final String CACHE_CONTROL = "no-cache";

    /**
     * 默认的最大池
     */
    public static final int DEFAULT_MAX_TOTAL = 600;
    /**
     * 每个host+port默认并发数为60个
     */
    public static final int DEFAULT_MAX_PER_ROUTE = 60;
    /**
     * 默认池名称
     */
    public static final String DEFAULT_POOLING_NAME = "DEFAULT_POOLING_NAME";

    /**
     * 构造资源池创建者缓存
     */
    private static final ConcurrentHashMap<String,PoolingBuilder> poolingBuilderMappings = new ConcurrentHashMap<>();
    /**
     * httpclient资源池
     */
    private PoolingHttpClientConnectionManager poolingManager;
    /**
     * 资源名称
     */
    private String poolingName;

    public NetHelpers(){
        this(DEFAULT_POOLING_NAME);
    }

    public NetHelpers(String poolingName){
        this(poolingName,DEFAULT_MAX_TOTAL,DEFAULT_MAX_PER_ROUTE);
    }

    /**
     * 初始化网络工具对象
     * @param poolingName 池名称
     * @param maxTotal 最大池数量
     * @param maxPerRoute 每个host+port默认并发数
     */
    public NetHelpers(String poolingName,int maxTotal, int maxPerRoute){
        String poolingBuilderKey = StringUtils.isNoneBlank(poolingName)?poolingName:DEFAULT_POOLING_NAME;
        this.poolingName = poolingBuilderKey;
        PoolingBuilder poolingBuilder = poolingBuilderMappings.get(poolingBuilderKey);
        if(poolingBuilder == null){
            poolingBuilder = new PoolingBuilder(maxTotal,maxPerRoute);
            poolingBuilderMappings.put(poolingBuilderKey,poolingBuilder);
        }
        this.poolingManager = poolingBuilder.build();
    }

    /**
     * 兼容原有的模式，原先应用的代码不需要做修改
     * @return
     */
    public static NetHelpers me(){
        return new NetHelpers();
    }

    private CloseableHttpClient create() {
        HttpClientBuilder builder = HttpClients.custom().setConnectionManager(this.poolingManager);
        SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
        builder.setDefaultSocketConfig(socketConfig);
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(5, true));
        return builder.build();
    }

    /**
     * 发送POST请求
     *
     * @param context
     * @return 返回请求状态码 ， 2表示未知异常信息
     */
    public int post(NetContext context) {
        Charset charset = Charset.forName(context.getCharset());
        RequestBuilder builder = RequestBuilder.post();
        builder.setUri(context.getReqUrl())
                .setCharset(charset);
        RequestConfig.Builder requestConfig = RequestConfig.custom();
        requestConfig.setConnectTimeout(context.getConnectTimeout()) //tcp三次握手超时时间
                .setSocketTimeout(context.getSocketTimeout()) //接口获取数据超时时间
                .setConnectionRequestTimeout(context.getConnectionRequestTimeout()); //从资源池获取超时时间
        builder.setConfig(requestConfig.build());

        CloseableHttpClient client = create();
        CloseableHttpResponse response = null;
        try {
            if (StringUtils.isNotBlank(context.getRequestBody())) {
                StringEntity input = new StringEntity(context.getRequestBody(),charset);
                input.setContentType("application/json");
                input.setContentEncoding(context.getCharset());
                builder.setEntity(input);
            } else {
                for (Entry<String, String> entry : context.getParams().entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpUriRequest request = builder.build();
            disguise(request,context);

            if (context.isAuthorization()) {
                //使用basic认证
                response = client.execute(request, createBasicAuthContext(context.getUsername(), context.getPassword()));
            } else {
                response = client.execute(request);
            }
            int statusCode = response.getStatusLine().getStatusCode();
            NetStatus status = NetStatus.getStatus(statusCode);
            context.setStatus(status);
            String result = IOUtils.toString(response.getEntity().getContent(), context.getCharset());
            context.setResponse(result);
            return statusCode;
        }catch (ConnectTimeoutException | HttpHostConnectException | SocketTimeoutException e){
            context.setStatus(NetStatus.C_TIMEOUT).setResponse(null);
            LOGGER.error("send http request detected timeout.request url is {}", context.getReqUrl(), e);
        }catch (Exception e) {
            LOGGER.error("send http request detected error.request url is {}", context.getReqUrl(), e);
            context.setStatus(NetStatus.C_UN_KNOWN).setResponse(null);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (Exception e) {
                    LOGGER.error("http request consume error.request url is {}", context.getReqUrl(), e);
                }
            }
        }
        return context.getStatus().getCode();
    }

    private HttpClientContext createBasicAuthContext(String username, String password) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Credentials defaultCreds = new UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), defaultCreds);
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        return context;
    }

    public void destroy() {
        if (poolingManager != null) {
            poolingManager.close();
            poolingBuilderMappings.remove(this.poolingName);
        }
    }

    private void disguise(HttpUriRequest request,NetContext context) {
        /*request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept", ACCEPT);
        request.setHeader("Accept-Language", ACCEPT_LANGUAGE);
        request.setHeader("Accept-Encoding", ACCEPT_ENCODING);
        request.setHeader("Connection", CONNECTION);
        request.setHeader("Pragram", PRAGRAM);
        request.setHeader("Cache-Control", CACHE_CONTROL);*/
        RequestInfo requestInfo = RequestIdentityHolder.get(true);
        if (requestInfo != null) {
            request.addHeader("rid", requestInfo.getId());
            request.addHeader("rstep", requestInfo.getStep() + "");
            try {
                request.addHeader("rname", URLEncoder.encode(StringUtils.trimToEmpty(requestInfo.getName()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                //ignore
            }
            request.addHeader("rversion", StringUtils.trimToEmpty(requestInfo.getVersion()));
        }
        if(StringUtils.isBlank(context.getRequestBody())){
            request.setHeader("Content-Type", "application/x-www-form-urlencoded;charset="+context.getCharset());
        }
    }

    /**
     * Httpclient资源池构造器
     */
    private static class PoolingBuilder {

        private int maxTotal;
        private int maxPerRoute;
        private PoolingHttpClientConnectionManager poolingManager;

        PoolingBuilder(int maxTotal, int maxPerRoute) {
            super();
            this.maxTotal = maxTotal;
            this.maxPerRoute = maxPerRoute;
        }

        /**
         * 创建Httpclient连接池
         * @return
         */
        public PoolingHttpClientConnectionManager build() {
            if(poolingManager == null){
                synchronized (poolingBuilderMappings){
                    if(poolingManager == null){
                        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.INSTANCE)
                                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
                        poolingManager = new PoolingHttpClientConnectionManager(registry);
                        if (maxTotal > 0) {
                            poolingManager.setMaxTotal(maxTotal);
                        }
                        if (maxPerRoute > 0) {
                            poolingManager.setDefaultMaxPerRoute(maxPerRoute);
                        }
                    }
                }
            }
            return poolingManager;
        }
    }
}
