package com.tj24.wanandroid.module.treenavigation.knowledge;

import android.view.View;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.view.ArticleListView;
import com.tj24.wanandroid.module.treenavigation.TreeNaviRequest;

import butterknife.BindView;

public class KnowledgeArticleFragment extends BaseWanAndroidFragment {
    private static final int FIRST_PAGE = 0;

    @BindView(R2.id.articleListView)
    ArticleListView articleListView;

    TreeBean treeBean;

    private boolean isFirstLoad = true;

    public KnowledgeArticleFragment(TreeBean treeBean) {
        this.treeBean = treeBean;
    }

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_knowledge_article;
    }


    @Override
    public void init(View view) {
        articleListView.setFirstPage(0);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            MultiStateUtils.toLoading(articleListView.getMultiStateView());
            loadMoreData(FIRST_PAGE);
            isFirstLoad = false;
        }
    }

    private void loadMoreData(int page) {
        TreeNaviRequest.requestTreeArticle(page,treeBean.getId(),new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
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

    private void refreshData() {
        TreeNaviRequest.requestTreeArticleWithOutCache(FIRST_PAGE,treeBean.getId(),new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
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
}
