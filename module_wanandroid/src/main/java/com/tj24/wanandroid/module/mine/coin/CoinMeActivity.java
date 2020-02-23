package com.tj24.wanandroid.module.mine.coin;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tj24.base.constant.ARouterPath;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.user.LoginInterceptorCallBack;

@Route(path = ARouterPath.WanAndroid.COIN_ACTIVITY, extras = ARouterPath.NEED_LOGIN)
public class CoinMeActivity extends BaseWanAndroidActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_coin_me;
    }

    public static void actionStart(Context context){
        ARouter.getInstance().build(ARouterPath.WanAndroid.COIN_ACTIVITY)
                .navigation(context,new LoginInterceptorCallBack(context));
    }
}
