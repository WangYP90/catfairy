package com.tj24.wanandroid.common.http.inspector;

import android.content.SharedPreferences;
import android.util.Log;

import com.tj24.base.base.app.BaseApplication;
import com.tj24.wanandroid.common.Const;

import java.io.IOException;
import java.net.URL;
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

        //若不是登录 则不管
        URL url = originalResponse.request().url().url();
        if(!url.getPath().equals("/user/login")){
            return originalResponse;
        }

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            SharedPreferences.Editor config = BaseApplication.getContext()
                    .getSharedPreferences(Const.MODULE_NAME, BaseApplication.getContext().MODE_PRIVATE)
                    .edit();
            config.putStringSet(Const.SP_COOKIE, cookies);
            config.commit();
            Log.v("CookieInterceptor","从请求中获取cookie并存储到SP："+cookies.toString());
        }

        return originalResponse;
    }
}
