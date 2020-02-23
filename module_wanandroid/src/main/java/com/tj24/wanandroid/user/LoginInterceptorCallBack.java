package com.tj24.wanandroid.user;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.ToastUtil;

import java.lang.ref.WeakReference;


/**
 * 登录拦截器的回调
 */
public class LoginInterceptorCallBack implements NavigationCallback {

    private static final int MSG_INTERRUPT = 24;
    private Context mContext;
    private LoginHandler mLoginHandler;

    public LoginInterceptorCallBack(Context mContext) {
        this.mContext =  mContext;
        this.mLoginHandler = new LoginHandler(mContext);
    }

    @Override
    public void onFound(Postcard postcard) {

    }

    @Override
    public void onLost(Postcard postcard) {

    }

    @Override
    public void onArrival(Postcard postcard) {

    }

    @Override
    public void onInterrupt(Postcard postcard) {
        mLoginHandler.sendEmptyMessage(MSG_INTERRUPT);
    }


    private class LoginHandler extends Handler{
        private WeakReference<Context> mWeakReference;

        public LoginHandler(Context context) {
            this.mWeakReference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_INTERRUPT:
                    interruptToLogin(mContext);
                    break;
            }
        }
    }

    /**
     * 被拦截到去登陆
     */
    public static void interruptToLogin(Context context) {
        ToastUtil.showShortToast(context,"请先登录！");
        ARouter.getInstance().build(ARouterPath.WanAndroid.LOGIN_ACTIVITY)
                .navigation();
    }
}
