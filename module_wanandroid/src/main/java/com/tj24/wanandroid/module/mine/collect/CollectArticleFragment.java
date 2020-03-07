package com.tj24.wanandroid.module.mine.collect;

import android.view.View;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.CollectChangeEvent;
import com.tj24.wanandroid.common.event.CollectRefreshEvent;
import com.tj24.wanandroid.common.event.CollectRefreshFinishEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.view.ArticleListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class CollectArticleFragment extends BaseWanAndroidFragment {

    public static final int FIRST_PAGE = 0;

    @BindView(R.id.articleListView)
    ArticleListView articleListView;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_collect_article;
    }

    @Override
    public void init(View view) {
        articleListView.setFirstPage(FIRST_PAGE);
        articleListView.setCanRefresh(false);
        articleListView.setIsInCollect(true);
        MultiStateUtils.setEmptyAndErrorClick(articleListView.getMultiStateView(), new SimpleListener() {
            @Override
            public void onResult() {
                refreshData();
            }
        });
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefresh(CollectRefreshEvent event){
        if(event.getItem() == 0){
            if(event.getType() == 0){
                refreshData();
            }else if(event.getType() ==1){
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

    public void loadMoreData(int page) {
        CollectRequest.getCollectAarticles(page, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                CollectRefreshFinishEvent.postLoadMoreFinishEvent(articleRespon.getPageCount()>=page);
                articleListView.onLoadMoreSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                CollectRefreshFinishEvent.postLoadMoreFinishEvent(false);
                articleListView.onLoadMoreFail(fail);
            }
        });
    }


    public void refreshData() {
        MultiStateUtils.toLoading(articleListView.getMultiStateView());
        CollectRequest.getCollectAarticles(FIRST_PAGE,new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                if(articleRespon.getDatas().size()>0){
                    MultiStateUtils.toContent(articleListView.getMultiStateView());
                }else {
                    MultiStateUtils.toEmpty(articleListView.getMultiStateView());
                }
                CollectRefreshFinishEvent.postRefreshFinishEvent();
                articleListView.onRefreshSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                MultiStateUtils.toError(articleListView.getMultiStateView());
                CollectRefreshFinishEvent.postRefreshFinishEvent();
                articleListView.onRefreshFail(fail);
            }
        });
    }
}
