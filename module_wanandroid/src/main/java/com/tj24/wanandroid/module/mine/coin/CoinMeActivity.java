package com.tj24.wanandroid.module.mine.coin;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kennyc.view.MultiStateView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tj24.base.bean.wanandroid.CoinProgressBean;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.user.LoginInterceptorCallBack;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

@Route(path = ARouterPath.WanAndroid.COIN_ACTIVITY, extras = ARouterPath.NEED_LOGIN)
public class CoinMeActivity extends BaseWanAndroidActivity implements OnRefreshLoadMoreListener {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.rv_coin_info)
    RecyclerView rvCoinInfo;
    @BindView(R2.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R2.id.msv)
    MultiStateView msv;
    @BindView(R2.id.tv_coin_count)
    TextView tvCoinCount;

    private CoinLoadAdpater coinRankAdpater;
    private LinearLayoutManager layoutManager;

    private static String EXT_COIN_COUNT = "coin_count";

    private static int FIRST_PAGE = 1;
    int page = FIRST_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh.setOnRefreshLoadMoreListener(this);
        layoutManager = new LinearLayoutManager(this);
        rvCoinInfo.setLayoutManager(layoutManager);
        coinRankAdpater = new CoinLoadAdpater();
        rvCoinInfo.setAdapter(coinRankAdpater);

        int coinCount = getIntent().getIntExtra(EXT_COIN_COUNT,0);
        tvCoinCount.setText(coinCount+"");

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
        return R.layout.wanandroid_activity_coin_me;
    }

    private void initData() {
        if(page ==FIRST_PAGE){
            MultiStateUtils.toLoading(msv);
        }
        refresh.setNoMoreData(false);
        CoinRequest.getMyCoinLoad(page, new WanAndroidCallBack<ArticleRespon<CoinProgressBean>>() {
            @Override
            public void onSucces(ArticleRespon<CoinProgressBean> coinBeanArticleRespon) {
                finishRefresh(coinBeanArticleRespon);
                List<CoinProgressBean> datas = coinBeanArticleRespon.getDatas();

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

    private void finishRefresh(ArticleRespon<CoinProgressBean> coinBeanArticleRespon) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_coin_rule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_coin_rule) {
//           "https://www.wanandroid.com/blog/show/2653"
        }
        return super.onOptionsItemSelected(item);
    }


    public static void actionStart(Context context,int coinCount) {
        ARouter.getInstance().build(ARouterPath.WanAndroid.COIN_ACTIVITY)
                .withInt(EXT_COIN_COUNT,coinCount)
                .navigation(context, new LoginInterceptorCallBack(context));
    }
}
