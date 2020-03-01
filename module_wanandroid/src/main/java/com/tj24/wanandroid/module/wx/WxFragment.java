package com.tj24.wanandroid.module.wx;

import android.view.View;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.view.ArticleListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class WxFragment extends BaseWanAndroidFragment {

    @BindView(R2.id.tablayout)
    VerticalTabLayout tablayout;
    @BindView(R2.id.articleListView)
    ArticleListView articleListView;

    private static final int FIRST_PAGE = 0 ;

    TreeBean currentTab;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_wx;
    }

    @Override
    public void init(View view) {
       articleListView.setFirstPage(FIRST_PAGE);
       articleListView.setRefreshAndLoadMoreListener(new ArticleListView.RefreshAndLoadMoreListener() {
           @Override
           public void onRefresh() {
               refreshWxArticle();
           }

           @Override
           public void onLoadMore(int page) {
                loadMoreArtile(false);
           }
       });

       initWXAccout();
    }

    private void initWXAccout() {
        WxRequest.getWxsWithoutCache(new WanAndroidCallBack<List<TreeBean>>() {
            @Override
            public void onSucces(List<TreeBean> treeBeans) {
                initTab(treeBeans);
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mActivity,fail);
            }
        });
    }


    private void initTab(List<TreeBean> treeBeans) {
        tablayout.setTabAdapter(new MyTabAdapter(treeBeans));
        tablayout.setTabSelected(0);
        currentTab = treeBeans.get(0);
        loadMoreArtile(true);
        tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                currentTab = treeBeans.get(position);
                tablayout.setTabSelected(position);
                loadMoreArtile(true);
            }
            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    /**
     *
     * @param isFirst true表示为切换选项卡后加载第一页  false为下拉加载
     */
    private void loadMoreArtile(boolean isFirst) {
        int page = isFirst?FIRST_PAGE:articleListView.getCurrentPage();
        WxRequest.getWxArticle(currentTab.getId(), page, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                if(isFirst){
                    articleListView.clear();
                }
                articleListView.onLoadMoreSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                articleListView.onLoadMoreFail(fail);
            }
        });
    }

    private void refreshWxArticle() {
        WxRequest.getWxArticle(currentTab.getId(), FIRST_PAGE, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
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


    class MyTabAdapter implements TabAdapter {
        private List<TreeBean> treeBeans = new ArrayList<>();

        public MyTabAdapter(List<TreeBean> treeBeans) {
            this.treeBeans = treeBeans;
        }

        @Override
        public int getCount() {
            return treeBeans.size();
        }

        @Override
        public ITabView.TabBadge getBadge(int position) {
            return null;
        }

        @Override
        public ITabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public ITabView.TabTitle getTitle(int position) {
            ITabView.TabTitle title = new ITabView.TabTitle.Builder()
                    .setContent(treeBeans.get(position).getName())
                    .setTextSize(14)
                    .build();
            return title;
        }

        @Override
        public int getBackground(int position) {
            return R.color.base_color_white;
        }
    }
}
