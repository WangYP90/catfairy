package com.tj24.appmanager.login;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.UserHelper;

/**
 * 登录拦截器
 */
@Interceptor(name = "login",priority = 5)
public class LoginInterceptor implements IInterceptor {
    public static final String TAG = "LoginInterceptor";

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {

        boolean isLogin = UserHelper.getCurrentUser() != null;
        int needLogin = postcard.getExtra();
        if(isLogin || needLogin != ARouterPath.NEED_LOGIN){  // 如果已经登录不拦截
            callback.onContinue(postcard);
        }else {
           callback.onInterrupt(null);
        }
    }

    @Override
    public void init(Context context) {

        LogUtil.i(TAG,"登录拦截器初始化成功！");
    }
}
