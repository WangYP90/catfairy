package com.tj24.wanandroid;

import android.content.Context;
import android.util.Log;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.base.app.IApplication;
import com.tj24.base.common.smartrefresh.CatRefreshHeader;
import com.tj24.wanandroid.common.http.cache.CacheWan;

public class WanAndroidApp extends BaseApplication implements IApplication {
    public static final String TAG = WanAndroidApp.class.getSimpleName();

    @Override
    public void onCreat(BaseApplication application) {
        Log.e(TAG,"开始初始化WanAndroidApp!");
        CacheWan.init();
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.base_color_white, android.R.color.white);//全局设置主题颜色
                return new CatRefreshHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
}
