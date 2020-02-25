package com.tj24.wanandroid.module.mine.share;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.http.respon.ShareRespon;
import com.tj24.wanandroid.common.view.ArticleListView;
import com.tj24.wanandroid.user.LoginInterceptorCallBack;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

@Route(path = ARouterPath.WanAndroid.SHARE_ACTIVITY, extras = ARouterPath.NEED_LOGIN)
public class MyShareActivity extends BaseWanAndroidActivity {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.articleListView)
    ArticleListView articleListView;

    private static final int FIRST_PAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshData();
        MultiStateUtils.toLoading(articleListView.getMultiStateView());
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_my_share;
    }

    public void loadMoreData(int page) {
        ShareRequest.requestMyShare(page, new WanAndroidCallBack<ShareRespon<ArticleBean>>() {
            @Override
            public void onSucces(ShareRespon shareRespon) {
                ArticleRespon<ArticleBean> articleRespon = shareRespon.getShareArticles();
                articleListView.onLoadMoreSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                articleListView.onLoadMoreFail(fail);
            }
        });
    }

    public void refreshData() {
        ShareRequest.requestMyShare(FIRST_PAGE,new WanAndroidCallBack<ShareRespon<ArticleBean>>() {
            @Override
            public void onSucces(ShareRespon shareRespon) {
                ArticleRespon<ArticleBean> articleRespon = shareRespon.getShareArticles();
                articleListView.onRefreshSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                articleListView.onRefreshFail(fail);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_addto_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_addto_share){
            ShareArticleActivity.actionStart(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context) {
        ARouter.getInstance().build(ARouterPath.WanAndroid.SHARE_ACTIVITY).navigation(context, new LoginInterceptorCallBack(context));
    }
}
