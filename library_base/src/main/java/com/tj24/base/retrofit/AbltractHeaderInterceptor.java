package com.tj24.base.retrofit;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description: header拦截器 需要被继承
 * @Createdtime:2020/2/11 20:32
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public abstract class AbltractHeaderInterceptor implements Interceptor {

    private Map<String,String> headerMap = new HashMap<>();

    public AbltractHeaderInterceptor() {
        this.headerMap = getHeaderMap();
    }

    public abstract Map<String, String> getHeaderMap();


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();

        Iterator it = headerMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry<String, String>) it.next();
            builder.header(entry.getKey(),entry.getValue());
        }
        Request.Builder requestBuilder =builder.method(originalRequest.method(), originalRequest.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }


}
