package com.tj24.wanandroid.module.homepage;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tj24.base.bean.wanandroid.BannerBean;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.HomePageRefreshEvent;
import com.tj24.wanandroid.common.event.HomePageRefreshFinishEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class HomePageFragment extends BaseWanAndroidFragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rb_article)
    RadioButton rbArticle;
    @BindView(R.id.rb_project)
    RadioButton rbProject;
    @BindView(R.id.rg_homePage)
    RadioGroup rgHomePage;
    @BindView(R.id.fragment_container)
    LinearLayout fragmentContainer;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;


    Fragment articleFragment;
    Fragment projectFragment;

    private int currentItem = 0;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_home_page;
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void init(View view) {
        showFragment(0);
        initBanner();
        initRadio();
        initRfresh();
    }

    private void initRfresh() {
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HomePageRefreshEvent.postLoadMoreEvent(currentItem);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                HomePageRefreshEvent.postRefreshEvent(currentItem);
            }
        });
    }
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onReceivRefreshFinish(HomePageRefreshFinishEvent event){
        if(refresh.isRefreshing()){
            refresh.finishRefresh();
        }
        if(refresh.isLoading()){
            if(event.isHaveMoreData()){
                refresh.finishLoadMore();
            }else {
                refresh.finishLoadMoreWithNoMoreData();
                refresh.setNoMoreData(false);
            }
        }
    }

    private void initRadio() {
        rgHomePage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbArticle.getId()) {
                    showFragment(0);
                } else if (checkedId == rbProject.getId()) {
                    showFragment(1);
                }
            }
        });
    }


    private void initBanner() {
        HomePageRequest.reqeustBanner(new WanAndroidCallBack<List<BannerBean>>() {
            @Override
            public void onSucces(List<BannerBean> bannerBeans) {
                List<String> urls = new ArrayList<>();
                for (BannerBean bannerBean : bannerBeans) {
                    urls.add(bannerBean.getImagePath());
                }
                banner.setImageLoader(new GlideImageLoader()).setImages(urls).start();
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }

    public void showFragment(int index) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        hideFragment(ft);
        if (index == 0) {
            if (articleFragment == null) {
                articleFragment = new HomePageArticleFragment();
                ft.add(R.id.fragment_container, articleFragment);
            } else {
                ft.show(articleFragment);
            }
        } else if (index == 1) {
            if (projectFragment == null) {
                projectFragment = new HomePageProjectFragment();
                ft.add(R.id.fragment_container, projectFragment);
            } else {
                ft.show(projectFragment);
            }
        }
        ft.commitAllowingStateLoss();
        currentItem = index;
    }

    public void hideFragment(FragmentTransaction ft) {
        if (articleFragment != null) {
            ft.hide(articleFragment);
        }
        if (projectFragment != null) {
            ft.hide(projectFragment);
        }
    }
}
