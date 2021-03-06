package com.tj24.wanandroid.common.http;

import com.tj24.wanandroid.common.http.inspector.CookieInterceptor;
import com.tj24.wanandroid.common.http.inspector.ReceivedCookiesInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by energy on 2019/4/10.
 */

public class RetrofitWan {

    public static final String BASE_URL = "https://www.wanandroid.com/";

    private static final int CONNECT_TIMEOUT = 50;
    private static final int READ_TIMEOUT = 50;
    private static final int WRITE_TIMEOUT = 50;

    private APIClient apiClient;

    private volatile static RetrofitWan instance;

    public static RetrofitWan getInstance(){
        if(instance == null){
            synchronized (RetrofitWan.class){
                if(instance == null){
                    instance = new RetrofitWan();
                }
            }
        }
        return instance;
    }

    public RetrofitWan() {
        //OKHttp进行超时设置
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS); // 连接超时时间阈值
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);   // 数据获取时间阈值
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);  // 写数据超时时间阈值

        builder.retryOnConnectionFailure(true);              //错误重连
        builder.addInterceptor(new ReceivedCookiesInterceptor());
        builder.addInterceptor(new CookieInterceptor());
        OkHttpClient okHttpClient = builder.build();
        // 2 创建 Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)         //  *** baseUrl 中的路径(baseUrl)必须以 / 结束 ***
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(rxjava.create())
                .client(okHttpClient)
                .build();

        // 3 创建接口的代理对象
        apiClient = retrofit.create(APIClient.class);
    }


    public APIClient getApiClient(){
        return apiClient;
    }
}
