package com.tj24.base.retrofit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description:body实体拦截器 需要被继承
 * @Createdtime:2020/2/11 20:34
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public abstract class AbstractParameterInterceptor implements Interceptor {

    private Map<String,String> map = new HashMap<>();

    public AbstractParameterInterceptor() {
        this.map = getParameterInterceptor();
    }

    public abstract Map<String, String> getParameterInterceptor();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request;

        HttpUrl.Builder builder = originalRequest.url().newBuilder();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry<String, String>) it.next();
            builder.addQueryParameter(entry.getKey(),entry.getValue());
        }
        request = originalRequest.newBuilder().url(builder.build()).build();
        return chain.proceed(request);
    }
}
