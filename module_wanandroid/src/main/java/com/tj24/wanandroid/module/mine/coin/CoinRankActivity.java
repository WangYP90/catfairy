package com.tj24.wanandroid.module.mine.coin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kennyc.view.MultiStateView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tj24.base.bean.wanandroid.CoinBean;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CoinRankActivity extends BaseWanAndroidActivity implements OnRefreshLoadMoreListener {

    @BindView(R2.id.refresh)
    RefreshLayout refresh;
    @BindView(R2.id.rv_coin_rank)
    RecyclerView rvCoinRank;
    @BindView(R2.id.msv)
    MultiStateView msv;

    private CoinRankAdpater coinRankAdpater;
    private LinearLayoutManager layoutManager;

    private static int FIRST_PAGE = 1;
    int page = FIRST_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh.setOnRefreshLoadMoreListener(this);
        layoutManager = new LinearLayoutManager(this);
        rvCoinRank.setLayoutManager(layoutManager);
        coinRankAdpater = new CoinRankAdpater();
        rvCoinRank.setAdapter(coinRankAdpater);

        MultiStateUtils.setEmptyAndErrorClick(msv, new SimpleListener() {
            @Override
            public void onResult() {
                initData();
            }
        });
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_coin;
    }


    private void initData() {
        if(page ==FIRST_PAGE){
            MultiStateUtils.toLoading(msv);
        }
        refresh.setNoMoreData(false);
        CoinRequest.getCoinRank(page, new WanAndroidCallBack<ArticleRespon<CoinBean>>() {
            @Override
            public void onSucces(ArticleRespon<CoinBean> coinBeanArticleRespon) {
                finishRefresh(coinBeanArticleRespon);
                List<CoinBean> datas = coinBeanArticleRespon.getDatas();

                if(page == FIRST_PAGE){
                    coinRankAdpater.setNewData(datas);
                    refresh.setNoMoreData(false);
                    if(ListUtil.isNullOrEmpty(datas)){
                        MultiStateUtils.toEmpty(msv);
                    }else {
                        MultiStateUtils.toContent(msv);
                    }
                }else {
                    coinRankAdpater.addData(datas);
                }
                page ++;
            }

            @Override
            public void onFail(String fail) {
                showShortToast(fail);
                MultiStateUtils.toError(msv);
            }
        });
    }

    private void finishRefresh(ArticleRespon<CoinBean> coinBeanArticleRespon) {
        if(refresh.isRefreshing()){
            refresh.finishRefresh();
        }
        if(refresh.isLoading()){
            if(coinBeanArticleRespon.getCurPage()>coinBeanArticleRespon.getPageCount()){
                refresh.finishLoadMoreWithNoMoreData();
            }else {
                refresh.finishLoadMore();
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = FIRST_PAGE;

        initData();
    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, CoinRankActivity.class);
        context.startActivity(i);
    }
}
