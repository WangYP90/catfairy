package com.tj24.wanandroid.module.square;

import android.view.View;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.CollectChangeEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.view.ArticleListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class SquareFragment extends BaseWanAndroidFragment {

    public static final int FIRST_PAGE = 0;
    @BindView(R2.id.articleListView)
    ArticleListView articleListView;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_square;
    }

    @Override
    public void init(View view) {
        articleListView.setFirstPage(FIRST_PAGE);
        articleListView.setCanRefresh(true);
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
        SquareRequest.requestSquareArticle(page, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
             articleListView.onLoadMoreSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                articleListView.onLoadMoreFail(fail);
            }
        });
    }



    public void refreshData() {
        SquareRequest.requestSquareArticleWitoutCache(FIRST_PAGE,new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                articleListView.onRefreshSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                articleListView.onRefreshFail(fail);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedCollectChangeEvent(CollectChangeEvent event){
        if(articleListView!=null){
            articleListView.onReceiveCollectChange(event.getId(),event.isCollected());
        }
    }
}
