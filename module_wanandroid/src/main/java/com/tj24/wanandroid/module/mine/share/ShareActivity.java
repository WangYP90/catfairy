package com.tj24.wanandroid.module.mine.share;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tj24.base.constant.ARouterPath;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.user.LoginInterceptorCallBack;

@Route(path = ARouterPath.WanAndroid.SHARE_ACTIVITY,extras = ARouterPath.NEED_LOGIN)
public class ShareActivity extends BaseWanAndroidActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_share;
    }

    public static void actionStart(Context context){
        ARouter.getInstance().build(ARouterPath.WanAndroid.SHARE_ACTIVITY)
                .navigation(context,new LoginInterceptorCallBack(context));
    }
}
