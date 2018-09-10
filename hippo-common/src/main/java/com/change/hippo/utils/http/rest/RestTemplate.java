package com.change.hippo.utils.http.rest;

import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.http.adapter.RestAdapter;
import com.change.hippo.utils.http.net.NetContext;
import com.change.hippo.utils.http.net.NetHelpers;
import com.change.hippo.utils.http.net.NetStatus;
import com.change.hippo.utils.json.JsonHelpers;
import com.change.hippo.utils.message.Msg;
import com.change.hippo.utils.message.MsgBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Rest工具
 */
public class RestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplate.class);
	/**
	 * 控制打印日志开关
	 */
	private static final boolean on = false;
    /**
     * 超时编码
     */
	private static final int timeoutCode = -2;

    /**
     * 设置连接超时时间，单位毫秒(tcp三次握手超时时间)
     * <p>默认为6秒超时</p>
     */
    private int connectTimeout = 6000;
    /**
     * 从连接池获取对象超时时间，单位毫秒
     * <p>连接池获取对象超时为3秒</p>
     */
    private int connectionRequestTimeout = 3000;
    /**
     * 接口请求超时时间，单位毫秒，如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
     * <p>默认在6s内接口未返回报超时</p>
     */
    private int socketTimeout = 6000;

	/**
	 * 重试次数
	 */
	private int retry = 1;
    /**
     * 最大池数量
     */
    private int maxTotal;
    /**
     * 每个host+port默认并发数
     */
    private int maxPerRoute;

    private String poolingName;

    public RestTemplate(){
        this(NetHelpers.DEFAULT_POOLING_NAME);
    }

    public RestTemplate(String poolingName){
        this(poolingName,NetHelpers.DEFAULT_MAX_TOTAL,NetHelpers.DEFAULT_MAX_PER_ROUTE);
    }

    /**
     * 初始化网络工具对象
     * @param poolingName 池名称
     * @param maxTotal 最大池数量
     * @param maxPerRoute 每个host+port默认并发数
     */
    public RestTemplate(String poolingName,int maxTotal, int maxPerRoute){
        this.poolingName = poolingName;
        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
    }

    /**
     * 兼容原有的模式，原先应用的代码不需要做修改
     * @return
     */
    public static RestTemplate me(){
        return new RestTemplate();
    }

	/**
	 * 发送POST请求
	 * @param reqUrl
	 * @param requestBody
	 * @return
	 */
	public Msg post(String reqUrl, String requestBody){
		return request(RestContext.me().setReqUrl(reqUrl).setRequestBody(requestBody));
	}

	/**
	 * FORM格式发送请求
	 * @param reqUrl
	 * @param params
	 * @return
	 */
	public Msg post(String reqUrl, Map<String,String> params){
		return request(RestContext.me().setReqUrl(reqUrl).setParams(params));
	}

    /**
     * 发送POST请求，basic认证
     *
     * @param reqUrl
     * @param requestBody
     * @param username
     * @param password
     * @return
     */
    public Msg post(String reqUrl, String requestBody, String username, String password) {
        RestContext context = RestContext.me().setReqUrl(reqUrl).setRequestBody(requestBody);
        context.setUsername(username).setPassword(password);
        return request(context);
    }

	/**
	 * 发送GET请求
	 * @param reqUrl
	 * @param params
	 * @return
	 */
	public Msg get(String reqUrl, Map<String,String> params){
		return request(RestContext.me().setReqUrl(reqUrl).setParams(params).setPost(false));
	}

	/**
	 * 做请求
	 * <p>在请求超时时，根据retry参数做请求重试</p>
	 * @param restContext
	 * @return
	 */
    public Msg request(RestContext restContext) {
        int requestTimes = 1;
		Msg msg = MsgBuilder.me().build();
        while(requestTimes<=retry){
            msg = doRequest(restContext);
            if(msg == null){
                //适配器方式请求 Msg为空表示请求成功
                return null;
            }
            //只有在超时时才进行重试，否则重试没有任何意义
            if(msg.getCode() == timeoutCode && ++requestTimes<=retry){
                logger.info("request timeout will be retry.retryTimes is : {}",requestTimes);
                continue;
            }
            break;
        }
		return msg;
	}

	/**
	 * 发送HTTP请求
	 * @param restContext
	 * @return
	 */
	private Msg doRequest(RestContext restContext){
		StringBuilder reqUrl = new StringBuilder(restContext.getReqUrl().trim());
		String requestBody = restContext.getRequestBody();
		Map<String,String> params = restContext.getParams();
		if(on){
			logger.info("request method is post: {},request body is : {},reqUrl is : {}",restContext.isPost(),requestBody,reqUrl);
		}
		NetContext netContext = NetContext.me("")
				.setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout);

        String username = restContext.getUsername();
        if (StringUtils.isNotBlank(username)) {
            //basic认证
            netContext.setUsername(username).setPassword(restContext.getPassword());
        }

		if(restContext.isPost()){
			if(StringUtils.isNotBlank(requestBody)){
				netContext.setRequestBody(requestBody);
			}else{
				netContext.setParams(params);
			}
		}else{
			if(params!=null){
				Set<Map.Entry<String, String>> entries = params.entrySet();
				StringBuilder sb = new StringBuilder();
				for(Map.Entry<String, String> entry : entries){
					String key = entry.getKey();
					String value = entry.getValue();
					sb.append(key).append("=").append(value).append("&");
				}
				if(sb.length()>0){
					sb.deleteCharAt(sb.length()-1);
					if(!"?".equals(reqUrl.substring(reqUrl.length()-1))){
						reqUrl.append("?");
					}
					reqUrl.append(sb);
				}
			}
		}
		netContext.setReqUrl(reqUrl.toString());
		int responseCode  = new NetHelpers(this.poolingName,this.maxTotal,this.maxPerRoute).post(netContext);
		String responseText = netContext.getResponse();
		if(on){
			logger.info("responseCode is :{} , responseText is : {}",responseCode,responseText);
		}
		if(responseCode == NetStatus.C_TIMEOUT.getCode()){
			//请求超时
			responseCode = timeoutCode;
		}

        if (StringUtils.isBlank(responseText) || responseCode == timeoutCode) {
            //响应消息为空/请求超时 返回失败
            return MsgBuilder.me()
                    .setOk(false)
                    .setCode(responseCode)
                    .setMsg(responseText)
                    .build();
        }

        RestAdapter restAdapter = restContext.getRestAdapter();
        if (restAdapter != null) {
            //当不是标准的Msg消息时，支持消息转换
            return restAdapter.adapter(responseText, responseCode);
        }

        try {
            return JsonHelpers.me().getBean(responseText, Msg.class);
        } catch (Exception e) {
            logger.error("response text is not normal json format", e);
        }

        //消息格式不正确
        return MsgBuilder.me()
                .setOk(false)
                .setCode(responseCode)
                .setMsg(responseText)
                .setData("response message is not in normal format")
                .build();
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public RestTemplate setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public RestTemplate setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public RestTemplate setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getRetry() {
        return retry;
    }

    public RestTemplate setRetry(int retry) {
        this.retry = retry;
        return this;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public RestTemplate setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        return this;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public RestTemplate setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }
}
