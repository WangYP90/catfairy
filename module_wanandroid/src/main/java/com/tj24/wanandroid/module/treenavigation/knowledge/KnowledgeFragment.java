package com.tj24.wanandroid.module.treenavigation.knowledge;

import android.view.View;

import com.kennyc.view.MultiStateView;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.TreeNaviRefreshEvent;
import com.tj24.wanandroid.common.event.TreeNaviRefreshFinishEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.module.treenavigation.TreeNaviRequest;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class KnowledgeFragment extends BaseWanAndroidFragment {
    @BindView(R.id.rv_konwledge)
    RecyclerView rvKonwledge;
    @BindView(R.id.msv)
    MultiStateView msv;

    private KnowledgeAdapter knowledgeAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_knowledge;
    }

    @Override
    public void init(View view) {
        linearLayoutManager = new LinearLayoutManager(mActivity);
        rvKonwledge.setLayoutManager(linearLayoutManager);
        knowledgeAdapter = new KnowledgeAdapter();
        knowledgeAdapter.setOnKnowledgeItemClickListener(new KnowledgeAdapter.OnKnowledgeItemClickListener() {
            @Override
            public void onClick(TreeBean treeBean,int position) {
                KnowledgeArticleActivity.actionStart(mActivity,treeBean,position);
            }
        });
        rvKonwledge.setAdapter(knowledgeAdapter);

        MultiStateUtils.setEmptyAndErrorClick(msv, new SimpleListener() {
            @Override
            public void onResult() {
                initData();
            }
        });
        initData();
    }

    private void initData() {
       MultiStateUtils.toLoading(msv);
        TreeNaviRequest.requestKnowledge(new WanAndroidCallBack<List<TreeBean>>() {
            @Override
            public void onSucces(List<TreeBean> treeBeans) {
                knowledgeAdapter.setNewData(treeBeans);
                if(ListUtil.isNullOrEmpty(treeBeans)){
                    MultiStateUtils.toEmpty(msv);
                }else {
                    MultiStateUtils.toContent(msv);
                }
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mActivity,fail);
                MultiStateUtils.toError(msv);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefresh(TreeNaviRefreshEvent event){
        if(event.getItem() == 0){
            MultiStateUtils.toLoading(msv);
            TreeNaviRequest.requestKnowledgeWithoutCache(new WanAndroidCallBack<List<TreeBean>>() {
                @Override
                public void onSucces(List<TreeBean> treeBeans) {
                    TreeNaviRefreshFinishEvent.postRefreshFinishEvent();
                    knowledgeAdapter.setNewData(treeBeans);
                    if(ListUtil.isNullOrEmpty(treeBeans)){
                        MultiStateUtils.toEmpty(msv);
                    }else {
                        MultiStateUtils.toContent(msv);
                    }
                }

                @Override
                public void onFail(String fail) {
                    TreeNaviRefreshFinishEvent.postRefreshFinishEvent();
                    ToastUtil.showShortToast(mActivity,fail);
                    MultiStateUtils.toError(msv);
                }
            });
        }
    }
}
