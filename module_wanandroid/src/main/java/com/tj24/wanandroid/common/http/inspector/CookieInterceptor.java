package com.tj24.wanandroid.common.http.inspector;

import android.util.Log;

import com.tj24.base.base.app.BaseApplication;
import com.tj24.wanandroid.common.Const;

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
        HashSet<String> preferences = (HashSet) BaseApplication.getContext().getSharedPreferences(Const.MODULE_NAME,
                BaseApplication.getContext().MODE_PRIVATE).getStringSet(Const.SP_COOKIE, null);
        if (preferences != null) {
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("CookieInterceptor", "从SP中取出并添加cookie"+ cookie);
            }
        }
        return chain.proceed(builder.build());
    }
}
