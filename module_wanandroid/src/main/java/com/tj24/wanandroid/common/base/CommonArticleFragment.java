package com.tj24.wanandroid.common.base;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.module.homepage.CommonArticleAdapter;
import com.tj24.wanandroid.module.mine.collect.CollectRequest;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public abstract class CommonArticleFragment extends BaseWanAndroidFragment implements
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, OnRefreshLoadMoreListener {
    @BindView(R.id.rv_article)
    RecyclerView rvArticle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    public List<ArticleBean> datas = new ArrayList<>();
    public CommonArticleAdapter articleAdapter;
    private LinearLayoutManager layoutManager;

    //当前列表分页加载的页数
    public int pageNum = 0;
    //当前fragment是否需要有刷新功能 默认是有的
    private boolean isCanRefresh = true;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_common_article;
    }

    @Override
    public void init(View view) {
        pageNum = getFirstPage();
        isCanRefresh = isCanRefresh();
        initView();
        initData(pageNum);
    }

    private void initView() {
        if(isCanRefresh){
            refresh.setOnRefreshLoadMoreListener(this);
            refresh.setEnableAutoLoadMore(true);
        }else {
            refresh.setEnableRefresh(false);
            refresh.setEnableLoadMore(false);
        }

        layoutManager = new LinearLayoutManager(mActivity);
        rvArticle.setLayoutManager(layoutManager);
        articleAdapter = new CommonArticleAdapter(R.layout.wanandroid_adapter_common_article_item, datas);
        rvArticle.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListener(this);
        articleAdapter.setOnItemChildClickListener(this);
        //        articleAdapter.setEmptyView(null);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleBean articleBean = datas.get(position);
        ToastUtil.showShortToast(mActivity, articleBean.getLink());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleBean articleBean = datas.get(position);
        if (view.getId() == R.id.tv_author) {
            ToastUtil.showShortToast(mActivity, articleBean.getAuthor());
        } else if (view.getId() == R.id.iv_collect) {
            if(!articleBean.isCollect()){
                collectArticle((ImageView) view,articleBean.getId());
            }else {
                unCollectArticle((ImageView) view,articleBean.getId());
            }
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        initData(pageNum);
    }

    /**
     * 收藏
     * @param iv
     * @param articleId
     */
    private void collectArticle(ImageView iv,int articleId) {
        CollectRequest.collectArticleInStation(articleId, new WanAndroidCallBack() {
            @Override
            public void onSucces(Object o) {
                iv.setBackgroundColor(Color.RED);
                ToastUtil.showShortToast(mActivity,"收藏成功");
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mActivity,"收藏失败");
            }
        });
    }

    /**
     * 取消收藏
     * @param iv
     * @param articleId
     */
    private void unCollectArticle(ImageView iv,int articleId){
        CollectRequest.unCollectAtArticle(articleId, new WanAndroidCallBack() {
            @Override
            public void onSucces(Object o) {
                iv.setBackgroundColor(Color.BLUE);
                ToastUtil.showShortToast(mActivity,"取消收藏成功");
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mActivity,"取消收藏失败");
            }
        });
    }


    /**
     * 完成刷新
     * @param withNoMoreData
     */
    public void  finishRefresh(boolean withNoMoreData){
        if(refresh.isRefreshing()){
            refresh.finishRefresh();
        }
        if(refresh.isLoading()){
            if(withNoMoreData){
                refresh.finishLoadMoreWithNoMoreData();
                refresh.setNoMoreData(false);
            }else {
                refresh.finishLoadMore();
            }
        }
    }

    public abstract boolean isCanRefresh();

    public abstract void initData(int page);

    public abstract int getFirstPage();

    public abstract void refreshData();
}
