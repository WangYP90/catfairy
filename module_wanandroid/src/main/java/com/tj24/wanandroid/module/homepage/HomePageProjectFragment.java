package com.tj24.wanandroid.module.homepage;

import android.view.View;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.CollectChangeEvent;
import com.tj24.wanandroid.common.event.HomePageRefreshEvent;
import com.tj24.wanandroid.common.event.HomePageRefreshFinishEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.view.ArticleListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class HomePageProjectFragment extends BaseWanAndroidFragment {
    public static final int FIRST_PAGE = 0;
    @BindView(R.id.articleListView)
    ArticleListView articleListView;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_home_page_project;
    }

    @Override
    public void init(View view) {
        articleListView.setFirstPage(FIRST_PAGE);
        articleListView.setCanRefresh(false);
        articleListView.setRefreshAndLoadMoreListener(new ArticleListView.RefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore(int page) {
                loadMoreData(page);
            }
        });
        MultiStateUtils.toLoading(articleListView.getMultiStateView());
        loadMoreData(FIRST_PAGE);
    }



    public void loadMoreData(int page) {
        HomePageRequest.requestProject(page, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                HomePageRefreshFinishEvent.postLoadMoreFinishEvent(articleRespon.getPageCount()>=page);
                articleListView.onLoadMoreSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                HomePageRefreshFinishEvent.postLoadMoreFinishEvent(false);
                articleListView.onLoadMoreFail(fail);
            }
        });
    }


    public void refreshData() {
        HomePageRequest.requestProjectByNet(FIRST_PAGE, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                HomePageRefreshFinishEvent.postRefreshFinishEvent();
                articleListView.onRefreshSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                HomePageRefreshFinishEvent.postRefreshFinishEvent();
                articleListView.onRefreshFail(fail);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefresh(HomePageRefreshEvent event){
        if(event.getItem() == 1){
            if(event.getType()==0){
                refreshData();
            }else if(event.getType()==1){
                loadMoreData(articleListView.getCurrentPage());
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedCollectChangeEvent(CollectChangeEvent event){
        if(articleListView!=null){
            articleListView.onReceiveCollectChange(event.getId(),event.isCollected());
        }
    }
}
