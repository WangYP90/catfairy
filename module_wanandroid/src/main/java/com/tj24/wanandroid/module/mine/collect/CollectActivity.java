package com.tj24.wanandroid.module.mine.collect;

import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tj24.base.constant.ARouterPath;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.event.CollectRefreshEvent;
import com.tj24.wanandroid.common.event.CollectRefreshFinishEvent;
import com.tj24.wanandroid.user.LoginInterceptorCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;

@Route(path = ARouterPath.WanAndroid.COLLECT_ACTIVITY, extras = ARouterPath.NEED_LOGIN)
public class CollectActivity extends BaseWanAndroidActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R2.id.rb_article)
    RadioButton rbArticle;
    @BindView(R2.id.rb_link)
    RadioButton rbLink;
    @BindView(R2.id.rg_collect)
    RadioGroup rgCollect;
    @BindView(R2.id.viewpager)
    ViewPager2 viewpager;
    @BindView(R2.id.refresh)
    SmartRefreshLayout refresh;

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_collect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refresh.setEnableRefresh(true);
        refresh.setOnRefreshListener(this);

        viewpager.setAdapter( new CollectAdapter(this));
        rgCollect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbArticle.getId()) {
                    viewpager.setCurrentItem(0);
                } else if (checkedId == rbLink.getId()) {
                    viewpager.setCurrentItem(1);
                }
            }
        });

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    rgCollect.check(rbArticle.getId());
                    refresh.setEnableLoadMore(true);
                    refresh.setOnLoadMoreListener(CollectActivity.this);
                }else {
                    rgCollect.check(rbLink.getId());
                    refresh.setEnableLoadMore(false);
                    refresh.setOnLoadMoreListener(null);
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        CollectRefreshEvent.postRefreshEvent(viewpager.getCurrentItem());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        CollectRefreshEvent.postLoadMoreEvent(viewpager.getCurrentItem());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveFinishRefresh(CollectRefreshFinishEvent event){
        if (refresh.isRefreshing()) {
            refresh.finishRefresh();
        }
        if (refresh.isLoading()) {
            if (event.isHaveMoreData()) {
                refresh.finishLoadMore();
            } else {
                refresh.finishLoadMoreWithNoMoreData();
                refresh.setNoMoreData(false);
            }
        }
    }


    class CollectAdapter extends FragmentStateAdapter {
        public CollectAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new CollectArticleFragment();
            } else if (position == 1) {
                return new CollectLinkFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    public static void actionStart(Context context) {
        ARouter.getInstance().build(ARouterPath.WanAndroid.COLLECT_ACTIVITY).navigation(context, new LoginInterceptorCallBack(context));
    }
}
