package com.tj24.wanandroid.module.mine.collect;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tj24.base.base.ui.widget.NoScrollViewPager;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

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
}
