package com.tj24.wanandroid.module.project;

import android.view.View;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.CollectChangeEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.view.ArticleListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class ProjectsFragment extends BaseWanAndroidFragment {
    @BindView(R2.id.tablayout)
    VerticalTabLayout tablayout;
    @BindView(R2.id.articleListView)
    ArticleListView articleListView;

    private static final int FIRST_PAGE = 0;

    TreeBean currentTab;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_project;
    }

    @Override
    public void init(View view) {
        articleListView.setFirstPage(FIRST_PAGE);
        articleListView.setRefreshAndLoadMoreListener(new ArticleListView.RefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshProjectArticle();
            }

            @Override
            public void onLoadMore(int page) {
                loadMoreArtile(false);
            }
        });

        initPrejectType();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedCollectChangeEvent(CollectChangeEvent event){
        if(articleListView!=null){
            articleListView.onReceiveCollectChange(event.getId(),event.isCollected());
        }
    }

    private void initPrejectType() {
        MultiStateUtils.toLoading(articleListView.getMultiStateView());
        ProjectRequest.requestProjectWithoutCache(new WanAndroidCallBack<List<TreeBean>>() {
            @Override
            public void onSucces(List<TreeBean> treeBeans) {
                if(ListUtil.isNullOrEmpty(treeBeans)){
                    MultiStateUtils.toEmpty(articleListView.getMultiStateView());
                }else {
                    MultiStateUtils.toContent(articleListView.getMultiStateView());
                }
                initTab(treeBeans);
            }

            @Override
            public void onFail(String fail) {
                MultiStateUtils.toError(articleListView.getMultiStateView());
                ToastUtil.showShortToast(mActivity, fail);
            }
        });
    }


    private void initTab(List<TreeBean> treeBeans) {
        tablayout.setTabAdapter(new ProjectTabAdapter(treeBeans));
        tablayout.setTabSelected(0);
        currentTab = treeBeans.get(0);
        MultiStateUtils.toLoading(articleListView.getMultiStateView());
        loadMoreArtile(true);
        tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                currentTab = treeBeans.get(position);
                tablayout.setTabSelected(position);
                MultiStateUtils.toLoading(articleListView.getMultiStateView());
                loadMoreArtile(true);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    /**
     * @param isFirst true表示为切换选项卡后加载第一页  false为下拉加载
     */
    private void loadMoreArtile(boolean isFirst) {
        int page = isFirst ? FIRST_PAGE : articleListView.getCurrentPage();
        ProjectRequest.requestProjectArticle(page,currentTab.getId(), new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                if (isFirst) {
                    articleListView.clear();
                }
                if(ListUtil.isNullOrEmpty(articleRespon.getDatas()) || articleRespon ==null){
                    MultiStateUtils.toEmpty(articleListView.getMultiStateView());
                }else {
                    MultiStateUtils.toContent(articleListView.getMultiStateView());
                }
                articleListView.onLoadMoreSuccess(articleRespon);
            }

            @Override
            public void onFail(String fail) {
                articleListView.onLoadMoreFail(fail);
                MultiStateUtils.toError(articleListView.getMultiStateView());
            }
        });
    }

    private void refreshProjectArticle() {
        ProjectRequest.requestProjectArticle(FIRST_PAGE,currentTab.getId(), new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                articleListView.onRefreshSuccess(articleRespon);
                if(articleRespon == null ||ListUtil.isNullOrEmpty(articleRespon.getDatas())){
                    MultiStateUtils.toEmpty(articleListView.getMultiStateView());
                }else {
                    MultiStateUtils.toContent(articleListView.getMultiStateView());
                }
            }

            @Override
            public void onFail(String fail) {
                articleListView.onRefreshFail(fail);
                MultiStateUtils.toError(articleListView.getMultiStateView());
            }
        });
    }


    class ProjectTabAdapter implements TabAdapter {
        private List<TreeBean> treeBeans = new ArrayList<>();

        public ProjectTabAdapter(List<TreeBean> treeBeans) {
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
                    .setTextColor(ContextCompat.getColor(mActivity,R.color.wanandroid_main_color),ContextCompat.getColor(mActivity,R.color.base_black_666))
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
