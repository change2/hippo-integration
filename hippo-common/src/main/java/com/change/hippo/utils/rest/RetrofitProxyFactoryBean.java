package com.change.hippo.utils.rest;

import com.squareup.okhttp.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;

import java.util.concurrent.TimeUnit;

/**
 * User: change.long
 * Date: 2017/8/21
 * Time: 下午7:19
 */

public class RetrofitProxyFactoryBean implements FactoryBean, InitializingBean {

    private String serviceUrl;

    private Class<?> serviceInterface;

    private Object object;
    private long connectTimeout;
    private long readTimeout;

    private Client client;

    private Converter converter;

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //日志使用logger aspect替代
        if (client == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(this.connectTimeout, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(this.readTimeout, TimeUnit.SECONDS);
            okHttpClient.interceptors().add(new RidInterceptor());
            client = new OkClient(okHttpClient);
        }
        if (converter == null) {
            converter = new FastJsonConverter();
        }
        object = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(serviceUrl)
                .setErrorHandler(new RetrofitErrorHandler())
                .setConverter(converter)
                .setClient(client)
                .setLog(new RestLog())
                .setRequestInterceptor(new RidRequestInterceptor())
                .build().create(serviceInterface);

    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
}
