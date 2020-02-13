package com.tj24.wanandroid.common.http;

import android.util.Log;

import com.tj24.base.base.app.BaseApplication;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加 cookie的请求拦截器
 */
public class CookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) BaseApplication.getContext().getSharedPreferences("config",
                BaseApplication.getContext().MODE_PRIVATE).getStringSet("wanandroidcookie", null);
        if (preferences != null) {
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("CookieInterceptor", "Adding Header: " + cookie);
            }
        }
        return chain.proceed(builder.build());
    }
}
