package com.tj24.wanandroid.module.homepage;

import android.view.View;
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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
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
    @BindView(R.id.viewpager)
    ViewPager2 viewpager;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

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
        initViewPager();
        initBanner();
        initRadio();
        initRfresh();
    }

    private void initViewPager() {
        viewpager.setAdapter(new HomeAdapter(this));
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    rgHomePage.check(rbArticle.getId());
                }else if(position == 1){
                    rgHomePage.check(rbProject.getId());
                }
            }
        });
    }

    private void initRfresh() {
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HomePageRefreshEvent.postLoadMoreEvent(viewpager.getCurrentItem());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                HomePageRefreshEvent.postRefreshEvent(viewpager.getCurrentItem());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivRefreshFinish(HomePageRefreshFinishEvent event) {
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

    private void initRadio() {
        rgHomePage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbArticle.getId()) {
                    viewpager.setCurrentItem(0);
                } else if (checkedId == rbProject.getId()) {
                    viewpager.setCurrentItem(1);
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


    class HomeAdapter extends FragmentStateAdapter {

        public HomeAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new HomePageArticleFragment();
            } else if (position == 1) {
                return new HomePageProjectFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
