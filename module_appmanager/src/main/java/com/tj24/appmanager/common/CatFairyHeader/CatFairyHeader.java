package com.tj24.appmanager.common.CatFairyHeader;

import android.content.Context;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.appmanager.R;
import com.tj24.base.constant.ARouterPath;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CatFairyHeader extends AbstractFairyHeader {

    public CatFairyHeader(Context mContext) {
        super(mContext);
    }

    @Override
    public void bindData(RecyclerView rc) {
        List<ModuleBean> mDatas = new ArrayList<>();
        mDatas.add(new ModuleBean("玩Android",R.drawable.base_ic_wanandroid,ARouterPath.WanAndroid.MAIN_ACTIVITY));
        rc.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL,false));
        CatFairyHeaderAdapter adapter = new CatFairyHeaderAdapter(R.layout.app_rv_catfairy_header,mDatas);
        rc.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance().build(mDatas.get(position).getAroutPath()).navigation(mContext);
            }
        });
    }
}
