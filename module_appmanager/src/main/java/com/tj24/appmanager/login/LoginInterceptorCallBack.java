package com.tj24.appmanager.login;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tj24.appmanager.R;
import com.tj24.base.constant.ARouterPath;

import java.lang.ref.WeakReference;


/**
 * 登录拦截器的回调
 */
public class LoginInterceptorCallBack implements NavigationCallback {

    private static final int MSG_FOUND = 1;
    private static final int MSG_LOST = 2;
    private static final int MSG_ARRIVAL = 3;
    private static final int MSG_INTERRUPT = 4;
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
        MaterialDialog dialog =  new MaterialDialog.Builder(context)
                .title(R.string.app_points).content(context.getString(R.string.app_no_login_and_to_login))
                .negativeText(R.string.app_cancle).positiveText(R.string.app_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ARouter.getInstance().build(ARouterPath.AppManager.LOGIN_ACTIVITY)
                                .navigation();
                    }
                }).build();
        dialog.show();
    }
}
