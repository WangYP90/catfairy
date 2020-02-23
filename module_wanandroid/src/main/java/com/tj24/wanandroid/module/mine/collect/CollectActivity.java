package com.tj24.wanandroid.module.mine.collect;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tj24.base.base.ui.widget.NoScrollViewPager;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.user.LoginInterceptorCallBack;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
@Route(path = ARouterPath.WanAndroid.COLLECT_ACTIVITY,extras = ARouterPath.NEED_LOGIN)
public class CollectActivity extends BaseWanAndroidActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.refresh_collect)
    SmartRefreshLayout refreshCollect;

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_collect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshCollect.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                ToastUtil.showShortToast(mActivity,"dd");
            }
        });
    }

    public static void actionStart(Context context){
        ARouter.getInstance().build(ARouterPath.WanAndroid.COLLECT_ACTIVITY)
                .navigation(context,new LoginInterceptorCallBack(context));
    }
}
