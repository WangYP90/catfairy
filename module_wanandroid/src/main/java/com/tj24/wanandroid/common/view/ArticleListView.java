package com.tj24.wanandroid.common.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.module.homepage.CommonArticleAdapter;
import com.tj24.wanandroid.module.mine.collect.CollectRequest;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleListView extends FrameLayout implements  BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener, OnRefreshLoadMoreListener {

    private RecyclerView rvArticle;
    private SmartRefreshLayout refresh;

    private Context mContext;
    private CommonArticleAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<ArticleBean> articleBeans = new ArrayList<>();

    //是否启用 本view 内 的refresh
    private boolean isCanRefresh = true;
    //列表的页数
    private int page;
    //最开始的页数
    private int firstPage;
    /**
     * 刷新 加载监听 提供给次view持有者
     */
    private RefreshAndLoadMoreListener refreshAndLoadMoreListener;

    public ArticleListView(@NonNull Context context) {
        this(context, null);
    }

    public ArticleListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArticleListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.wanandroid_article_list_view, this);
        rvArticle = findViewById(R.id.rv_article);
        refresh = findViewById(R.id.refresh);

        layoutManager = new LinearLayoutManager(mContext);
        rvArticle.setLayoutManager(layoutManager);
        mAdapter = new CommonArticleAdapter(R.layout.wanandroid_adapter_common_article_item,articleBeans);
        rvArticle.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        refresh.setOnRefreshLoadMoreListener(this);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleBean articleBean = articleBeans.get(position);
        ToastUtil.showShortToast(mContext, articleBean.getLink());
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleBean articleBean = articleBeans.get(position);
        if (view.getId() == R.id.tv_author) {
            ToastUtil.showShortToast(mContext, articleBean.getAuthor());
        } else if (view.getId() == R.id.iv_collect) {
            if(!articleBean.isCollect()){
                collectArticle((ImageView) view,articleBean.getId());
            }else {
                unCollectArticle((ImageView) view,articleBean.getId());
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if(refreshAndLoadMoreListener != null){
            refreshAndLoadMoreListener.onLoadMore(page);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if(refreshAndLoadMoreListener != null){
            refreshAndLoadMoreListener.onRefresh();
        }
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
                ToastUtil.showShortToast(mContext,"收藏成功");
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mContext,"收藏失败");
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
                ToastUtil.showShortToast(mContext,"取消收藏成功");
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mContext,"取消收藏失败");
            }
        });
    }


    public void setRefreshAndLoadMoreListener(RefreshAndLoadMoreListener refreshAndLoadMoreListener) {
        this.refreshAndLoadMoreListener = refreshAndLoadMoreListener;
    }

    public void setFirstPage(int firstPage){
        this.firstPage = firstPage;
    }

    public int getCurrentPage(){
        return page;
    }

    /**
     * 清除数据
     * @param
     */
    public void clear() {
        page = firstPage;
        articleBeans.clear();
        refresh.setNoMoreData(false);
    }

    public void setCanRefresh(boolean isCanRefresh){
        this.isCanRefresh = isCanRefresh;
        if(!isCanRefresh){
            refresh.setEnableLoadMore(false);
            refresh.setEnableRefresh(false);
        }
    }

    /*
        强制刷新数据成功
     */
    public void onRefreshSuccess(ArticleRespon<ArticleBean> articleRespon){
        if(isCanRefresh){
            if(articleRespon.getDatas().size() == articleRespon.getTotal()){  //说明只有一页
                refresh.finishRefreshWithNoMoreData();
            }else {     //大于一页
                refresh.setNoMoreData(false);
                refresh.finishRefresh(true);
            }
        }
        page = firstPage+1;
        articleBeans.clear();
        articleBeans.addAll(articleRespon.getDatas());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 强制刷新数据失败
     * @param msg
     */
    public void onRefreshFail(String msg){
        ToastUtil.showShortToast(mContext,msg);
        if(isCanRefresh){
            refresh.finishRefresh(false);
        }
    }

    /**
     * 根据页码加载数据成功
     */
    public void onLoadMoreSuccess(ArticleRespon<ArticleBean> articleRespon){
        page++;
        articleBeans.addAll(articleRespon.getDatas());
        mAdapter.notifyDataSetChanged();
        if(isCanRefresh){
            if(articleBeans.size() == articleRespon.getTotal()){  //数据已经全部加载
                refresh.finishLoadMoreWithNoMoreData();
            }else {
                refresh.finishLoadMore(true);
            }
        }
    }

    /**
     * 根据页码加载数据失败
     */
    public void onLoadMoreFail(String msg){
        ToastUtil.showShortToast(mContext,msg);
        if(isCanRefresh){
            refresh.finishLoadMore(false);
        }
    }

    public interface RefreshAndLoadMoreListener{
        public void  onRefresh();
        public void  onLoadMore(int page);
    }
}
