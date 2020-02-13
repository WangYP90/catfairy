package com.tj24.wanandroid.common.http;

import android.content.SharedPreferences;
import android.util.Log;

import com.tj24.base.base.app.BaseApplication;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;
/**
 * @Description:获取cookie并存储 的 响应拦截器
 * @Createdtime:2020/2/13 17:55
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            SharedPreferences.Editor config = BaseApplication.getContext()
                    .getSharedPreferences("config", BaseApplication.getContext().MODE_PRIVATE)
                    .edit();
            config.putStringSet("wanandroidcookie", cookies);
            config.commit();
            Log.v("CookiesInterceptor",cookies.toString());
        }

        return originalResponse;
    }
}
