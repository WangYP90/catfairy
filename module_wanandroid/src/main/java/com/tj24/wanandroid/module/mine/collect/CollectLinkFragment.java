package com.tj24.wanandroid.module.mine.collect;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kennyc.view.MultiStateView;
import com.tj24.base.bean.wanandroid.NetUrlBean;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.CollectRefreshEvent;
import com.tj24.wanandroid.common.event.CollectRefreshFinishEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.module.web.WebViewActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CollectLinkFragment extends BaseWanAndroidFragment {
    @BindView(R2.id.rv_links)
    RecyclerView rvLinks;
    @BindView(R2.id.msv)
    MultiStateView msv;

    private CollectLinkAdapter linkAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_collect_link;
    }

    @Override
    public void init(View view) {
        linearLayoutManager = new LinearLayoutManager(mActivity);
        rvLinks.setLayoutManager(linearLayoutManager);
        linkAdapter = new CollectLinkAdapter();
        linkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NetUrlBean urlBean = linkAdapter.getData().get(position);
                WebViewActivity.actionStart(mActivity,urlBean.getName(),urlBean.getLink());
            }
        });
        rvLinks.setAdapter(linkAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new SimpleListener() {
            @Override
            public void onResult() {
                refreshData();
            }
        });
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefresh(CollectRefreshEvent event) {
        if (event.getItem() == 1) {
            if (event.getType() == 0) {
                refreshData();
            }
        }
    }

    public void refreshData() {
        MultiStateUtils.toLoading(msv);
        CollectRequest.getCollectLinks(new WanAndroidCallBack<List<NetUrlBean>>() {
            @Override
            public void onSucces(List<NetUrlBean> urlBeans) {
                if (urlBeans.size() > 0) {
                    MultiStateUtils.toContent(msv);
                } else {
                    MultiStateUtils.toEmpty(msv);
                }
                CollectRefreshFinishEvent.postRefreshFinishEvent();
                linkAdapter.setNewData(urlBeans);
            }

            @Override
            public void onFail(String fail) {
                MultiStateUtils.toError(msv);
                CollectRefreshFinishEvent.postRefreshFinishEvent();
            }
        });
    }
}
