package com.tj24.appmanager.common.CatFairyHeader;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.appmanager.R;
import com.tj24.base.constant.ARouterPath;

import java.util.ArrayList;
import java.util.List;

public class CatFairyHeader extends AbstractFairyHeader {
    private Context mContext;
    public CatFairyHeader(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void bindData(RecyclerView rc) {
        List<ModuleBean> mDatas = new ArrayList<>();
        mDatas.add(new ModuleBean("1",R.drawable.app_ic_camera));
        mDatas.add(new ModuleBean("2",R.drawable.app_ic_camera));
        mDatas.add(new ModuleBean("3",R.drawable.app_ic_camera));
        rc.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL,false));
        CatFairyHeaderAdapter adapter = new CatFairyHeaderAdapter(R.layout.app_rv_catfairy_header,mDatas);
        rc.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance().build(ARouterPath.WanAndroid.MAIN_ACTIVITY).navigation(mContext);
            }
        });
    }
}
