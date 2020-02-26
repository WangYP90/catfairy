package com.tj24.wanandroid.module.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.HistoryKey;
import com.tj24.base.bean.wanandroid.HotKeyBean;
import com.tj24.base.greendao.HistoryKeyDao;
import com.tj24.base.greendao.daohelper.GreenDaoManager;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.view.ArticleListView;

import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseWanAndroidActivity {

    private static final int FIRST_PAGE = 0;
    @BindView(R2.id.flex)
    FlexboxLayout flex;
    @BindView(R2.id.rv_search_histroy)
    RecyclerView rvSearchHistroy;
    @BindView(R2.id.articleListView)
    ArticleListView articleListView;
    @BindView(R2.id.ll_ready_search)
    LinearLayout llReadySearch;
    @BindView(R2.id.tv_clear_history)
    TextView tvClearHistory;
    private SearchView mSearchView;

    private HistorySearchAdapter historySearchAdapter;
    private LinearLayoutManager linearLayoutManager;

    private LayoutInflater mInflater;
    private String searchKey;

    private HistoryKeyDao historyKeyDao;

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_search;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        requestHotKey();
        initHistroryKey();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initHistroryKey();
    }

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articleListView.getVisibility() == View.VISIBLE) {
                    articleListView.setVisibility(View.GONE);
                    llReadySearch.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
            }
        });
    }

    private void initHistroryKey() {
        List<HistoryKey> historyKeys = historyKeyDao.queryBuilder().orderDesc(HistoryKeyDao.Properties.SearchTime).list();
        historySearchAdapter.setNewData(historyKeys);
    }

    private void requestHotKey() {
        SearchRequest.requestHotKey(new WanAndroidCallBack<List<HotKeyBean>>() {
            @Override
            public void onSucces(List<HotKeyBean> hotKeyBeans) {
                for (int i = 0; i < hotKeyBeans.size(); i++) {
                    TextView tv = creatTextView(flex);
                    HotKeyBean hotKey = hotKeyBeans.get(i);
                    tv.setText(hotKey.getName());
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            search(FIRST_PAGE, hotKey.getName(), false);
                        }
                    });
                    flex.addView(tv);
                }
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }

    private TextView creatTextView(FlexboxLayout flex) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(flex.getContext());
        }
        return (TextView) mInflater.inflate(R.layout.wanandroid_rv_item_knowledge_child, flex, false);
    }

    private void initView() {
        historyKeyDao = GreenDaoManager.getDaoSession().getHistoryKeyDao();

        linearLayoutManager = new LinearLayoutManager(this);
        rvSearchHistroy.setLayoutManager(linearLayoutManager);
        historySearchAdapter = new HistorySearchAdapter();
        historySearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HistoryKey historyKey = (HistoryKey) adapter.getData().get(position);
                search(FIRST_PAGE,historyKey.getName(),false);
            }
        });
        rvSearchHistroy.setAdapter(historySearchAdapter);

        articleListView.setFirstPage(0);
        articleListView.setRefreshAndLoadMoreListener(new ArticleListView.RefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                search(FIRST_PAGE, searchKey, false);
            }

            @Override
            public void onLoadMore(int page) {
                search(FIRST_PAGE, searchKey, true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        initSearchView();
        return super.onCreateOptionsMenu(menu);
    }

    private void initSearchView() {
        mSearchView.setQueryHint("输入搜索内容");
        mSearchView.setIconifiedByDefault(false);
        //设置是否显示搜索框展开时的提交按钮
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(FIRST_PAGE, query, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void search(int page, String searchKey, boolean isLoadMore) {
        this.searchKey = searchKey;
        mSearchView.setQuery(searchKey,false);
        HistoryKey historyKey = new HistoryKey(searchKey, System.currentTimeMillis());
        historyKeyDao.insertOrReplace(historyKey);
        initHistroryKey();

        llReadySearch.setVisibility(View.GONE);
        articleListView.setVisibility(View.VISIBLE);

        if(page == FIRST_PAGE){
            MultiStateUtils.toLoading(articleListView.getMultiStateView());
        }
        SearchRequest.requestSearchResult(page, searchKey, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                if (isLoadMore) {
                    articleListView.onLoadMoreSuccess(articleRespon);
                } else {
                    articleListView.onRefreshSuccess(articleRespon);
                }
            }

            @Override
            public void onFail(String fail) {
                if (isLoadMore) {
                    articleListView.onLoadMoreFail(fail);
                } else {
                    articleListView.onRefreshFail(fail);
                }
            }
        });
    }

    @OnClick(R2.id.tv_clear_history)
    public void onClick() {
        historyKeyDao.deleteAll();
        initHistroryKey();
    }

    @Override
    public void onBackPressed() {
        if (articleListView.getVisibility() == View.VISIBLE) {
            articleListView.setVisibility(View.GONE);
            llReadySearch.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

}
