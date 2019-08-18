package com.tj24.module_appmanager.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.bean.MsgApkData;

import java.util.List;

public class MsgAPkAdapter extends BaseSectionQuickAdapter<MsgApkData,BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public MsgAPkAdapter(int layoutResId, int sectionHeadResId, List<MsgApkData> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, MsgApkData item) {
        helper.setText(R.id.tv_times,item.t.getCreatDay());
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgApkData item) {
        helper.setText(R.id.tv_creatHour,item.t.getCreatHour())
                .setText(R.id.tv_action,item.t.getAction())
                .setText(R.id.tv_name,item.t.getAppName());
    }
}
