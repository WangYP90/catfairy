package com.tj24.wanandroid.module.treenavigation.navi;

import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kennyc.view.MultiStateView;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.NavigationBean;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.TreeNaviRefreshEvent;
import com.tj24.wanandroid.common.event.TreeNaviRefreshFinishEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.module.treenavigation.TreeNaviRequest;
import com.tj24.wanandroid.module.web.WebViewActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class NaviFragment extends BaseWanAndroidFragment {
    @BindView(R2.id.rv_tab)
    RecyclerView rvTab;
    @BindView(R2.id.rv_navi)
    RecyclerView rvNavi;
    @BindView(R2.id.msv)
    MultiStateView msv;

    private NaviAdapter naviContentAdapter;
    private LinearLayoutManager contentLayoutManager;

    List<TabBean> tabs = new ArrayList<>();
    private NaviTabAadapter naviTabAadapter;
    private LinearLayoutManager tabLayoutManager;

    //判读是否是recyclerView主动引起的滑动，true- 是，false- 否，由tablayout引起的
    private boolean isRecyclerScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos;
    //用于recyclerView滑动到指定的位置
    private boolean canScroll;
    private int scrollToPosition;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_navi;
    }


    @Override
    public void init(View view) {
        contentLayoutManager = new LinearLayoutManager(mActivity);
        rvNavi.setLayoutManager(contentLayoutManager);
        naviContentAdapter = new NaviAdapter();
        rvNavi.post(new Runnable() {
            @Override
            public void run() {
                naviContentAdapter.setHeight(rvNavi.getHeight());
            }
        });
        naviContentAdapter.setOnNaviItemClickListener(new NaviAdapter.OnNaviItemClickListener() {
            @Override
            public void onClick(ArticleBean articleBean) {
                WebViewActivity.actionStart(mActivity,articleBean.getTitle(),articleBean.getLink());
            }
        });
        rvNavi.setAdapter(naviContentAdapter);

        tabLayoutManager = new LinearLayoutManager(mActivity);
        rvTab.setLayoutManager(tabLayoutManager);
        naviTabAadapter = new NaviTabAadapter();
        rvTab.setAdapter(naviTabAadapter);

        initData();
        MultiStateUtils.setEmptyAndErrorClick(msv, new SimpleListener() {
            @Override
            public void onResult() {
                initData();
            }
        });
    }

    private void initData() {
        MultiStateUtils.toLoading(msv);
        msv.setViewState(MultiStateView.ViewState.LOADING);
        TreeNaviRequest.requestNavi(new WanAndroidCallBack<List<NavigationBean>>() {
            @Override
            public void onSucces(List<NavigationBean> navigationBeans) {
                bindData(navigationBeans);
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mActivity, fail);
                MultiStateUtils.toError(msv);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefresh(TreeNaviRefreshEvent event) {
        if (event.getItem() == 1) {
            MultiStateUtils.toLoading(msv);
            TreeNaviRequest.requestNaviWithoutCache(new WanAndroidCallBack<List<NavigationBean>>() {
                @Override
                public void onSucces(List<NavigationBean> navigationBeans) {
                    TreeNaviRefreshFinishEvent.postRefreshFinishEvent();
                    bindData(navigationBeans);
                }

                @Override
                public void onFail(String fail) {
                    TreeNaviRefreshFinishEvent.postRefreshFinishEvent();
                    ToastUtil.showShortToast(mActivity, fail);
                    MultiStateUtils.toError(msv);
                }
            });
        }
    }

    private void bindData(List<NavigationBean> navigationBeans){
        if (ListUtil.isNullOrEmpty(navigationBeans)) {
            MultiStateUtils.toEmpty(msv);
        } else {
            MultiStateUtils.toContent(msv);
        }

        tabs.clear();
        for(int i = 0; i< navigationBeans.size(); i++){
            tabs.add(new TabBean(navigationBeans.get(i).getName(),i==0?true:false));
            if(i == navigationBeans.size()-1){
                naviContentAdapter.setLastId(navigationBeans.get(navigationBeans.size()-1).getCid());
            }
            lastPos = 0;
        }
        naviTabAadapter.setNewData(tabs);
        naviContentAdapter.setNewData(navigationBeans);

        linkedRecyclerViews();
    }

    private void linkedRecyclerViews() {
        naviTabAadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击标签，使recyclerView滑动，isRecyclerScroll置false
                isRecyclerScroll = false;
                if(lastPos != position){
                    tabLayoutManager.scrollToPosition(position);
                    tabs.get(position).setSelected(true);
                    tabs.get(lastPos).setSelected(false);
                    naviTabAadapter.notifyItemChanged(position);
                    naviTabAadapter.notifyItemChanged(lastPos);
                    lastPos = position;
                    moveToPosition(contentLayoutManager, rvNavi, position);
                }
            }
        });

        rvNavi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //当滑动由recyclerView触发时，isRecyclerScroll 置true
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRecyclerScroll = true;
                }
                return false;
            }
        });
        rvNavi.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (canScroll) {
                    canScroll = false;
                    moveToPosition(contentLayoutManager, recyclerView, scrollToPosition);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isRecyclerScroll) {
                    //第一个可见的view的位置，即tablayou需定位的位置
                    int position = contentLayoutManager.findFirstVisibleItemPosition();
                    if (lastPos != position) {
                        tabLayoutManager.scrollToPosition(position);
                        tabs.get(position).setSelected(true);
                        tabs.get(lastPos).setSelected(false);
                        naviTabAadapter.notifyItemChanged(position);
                        naviTabAadapter.notifyItemChanged(lastPos);
                    }
                    lastPos = position;
                }
            }
        });
    }


    private void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int position) {
        // 第一个可见的view的位置
        int firstItem = manager.findFirstVisibleItemPosition();
        // 最后一个可见的view的位置
        int lastItem = manager.findLastVisibleItemPosition();
        if (position <= firstItem) {
            // 如果跳转位置firstItem 之前(滑出屏幕的情况)，就smoothScrollToPosition可以直接跳转，
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在firstItem 之后，lastItem 之间（显示在当前屏幕），smoothScrollBy来滑动到指定位置
            int top = mRecyclerView.getChildAt(position - firstItem).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            // 如果要跳转的位置在lastItem 之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用当前moveToPosition方法，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            scrollToPosition = position;
            canScroll = true;
        }
    }
}
