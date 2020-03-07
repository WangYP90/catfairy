package com.tj24.wanandroid.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kennyc.view.MultiStateView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.event.CollectChangeEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.module.homepage.CommonArticleAdapter;
import com.tj24.wanandroid.module.mine.collect.CollectRequest;
import com.tj24.wanandroid.module.web.WebViewActivity;

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
    private MultiStateView msv;

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
    // 是否是在收藏页面
    boolean isInCollect;
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

    public void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.wanandroid_article_list_view, this);
        rvArticle = findViewById(R.id.rv_article);
        refresh = findViewById(R.id.refresh);

//        msv = findViewById(R.id.msv);
        msv = findViewWithTag("msv");

        layoutManager = new LinearLayoutManager(mContext);
        rvArticle.setLayoutManager(layoutManager);
        mAdapter = new CommonArticleAdapter(articleBeans);
        rvArticle.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        refresh.setOnRefreshLoadMoreListener(this);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleBean articleBean = articleBeans.get(position);
        if (view.getId() == R.id.tv_author) {
            ToastUtil.showShortToast(mContext, articleBean.getAuthor());
        } else if (view.getId() == R.id.iv_collect) {
            if(isInCollect){  //在个人收藏页面 则只有取消
                unCollectArticleInMine(articleBean,position);
            }else {
                if(!articleBean.isCollect()){
                    collectArticle(articleBean,position);
                }else {
                    unCollectArticle(articleBean,position);
                }
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleBean articleBean = articleBeans.get(position);
        WebViewActivity.actionStart(mContext,articleBean.getTitle(),articleBean.getLink());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if(refreshAndLoadMoreListener != null){
            if(page == firstPage){
                MultiStateUtils.toLoading(msv);
            }
            refreshAndLoadMoreListener.onLoadMore(page);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if(refreshAndLoadMoreListener != null){
            MultiStateUtils.toLoading(msv);
            refreshAndLoadMoreListener.onRefresh();
        }
    }

    /**
     * 收到收藏状态改变事件
     * @param id
     * @param isCollected
     */
    public void onReceiveCollectChange(int id,boolean isCollected){
        for(int i = 0; i<articleBeans.size();i++){
            if(articleBeans.get(i).getId() == id){
                articleBeans.get(i).setCollect(isCollected);
                mAdapter.notifyItemChanged(i);
            }
        }
    }

    /**
     * 收藏
     * @param
     * @param articleBean
     */
    private void collectArticle(ArticleBean articleBean,int position) {
        CollectRequest.collectArticleInStation(articleBean.getId(), new WanAndroidCallBack() {
            @Override
            public void onSucces(Object o) {
                articleBean.setCollect(true);
                mAdapter.notifyItemChanged(position);
                CollectChangeEvent.postCollectChangeEvent(articleBean.getId(),true);
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
     * @param articleBean
     */
    private void unCollectArticle(ArticleBean articleBean,int position){
            CollectRequest.unCollectAtArticle(articleBean.getId(), new WanAndroidCallBack() {
                @Override
                public void onSucces(Object o) {
                    articleBean.setCollect(false);
                    mAdapter.notifyItemChanged(position);
                    CollectChangeEvent.postCollectChangeEvent(articleBean.getId(),false);
                    ToastUtil.showShortToast(mContext,"取消收藏成功");
                }

                @Override
                public void onFail(String fail) {
                    ToastUtil.showShortToast(mContext,"取消收藏失败");
                }
            });
    }

    /**
     * 在个人收藏页面取消收藏
     * @param articleBean
     * @param position
     */
    private void unCollectArticleInMine(ArticleBean articleBean, int position) {
        CollectRequest.unCollectAtMine(articleBean.getId(), articleBean.getOriginId(),new WanAndroidCallBack() {
            @Override
            public void onSucces(Object o) {
                mAdapter.remove(position);
                CollectChangeEvent.postCollectChangeEvent(articleBean.getOriginId(),false);
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

    public void setIsInCollect(boolean isInCollect) {
        this.isInCollect = isInCollect;
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

    public MultiStateView getMultiStateView(){
        return msv;
    }
    /*
        强制刷新数据成功
     */
    public void onRefreshSuccess(ArticleRespon<ArticleBean> articleRespon){
        if(ListUtil.isNullOrEmpty(articleRespon.getDatas())){
            MultiStateUtils.toEmpty(msv);
        }else {
            MultiStateUtils.toContent(msv);
        }

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
        MultiStateUtils.toError(msv);
        ToastUtil.showShortToast(mContext,msg);
        if(isCanRefresh){
            refresh.finishRefresh(false);
        }
    }

    /**
     * 根据页码加载数据成功
     */
    public void onLoadMoreSuccess(ArticleRespon<ArticleBean> articleRespon){
        articleBeans.addAll(articleRespon.getDatas());
        mAdapter.notifyDataSetChanged();

        if(page == firstPage){
            if(ListUtil.isNullOrEmpty(articleBeans)){
                MultiStateUtils.toEmpty(msv);
            }else {
                MultiStateUtils.toContent(msv);
            }
        }

        if(isCanRefresh){
            if(articleBeans.size() == articleRespon.getTotal()){  //数据已经全部加载
                refresh.finishLoadMoreWithNoMoreData();
            }else {
                refresh.finishLoadMore(true);
            }
        }

        page++;
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
