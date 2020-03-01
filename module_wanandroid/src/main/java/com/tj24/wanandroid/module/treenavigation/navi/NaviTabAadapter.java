package com.tj24.wanandroid.module.treenavigation.navi;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.wanandroid.R;

public class NaviTabAadapter extends BaseQuickAdapter<TabBean, BaseViewHolder> {
    public NaviTabAadapter() {
        super(R.layout.wanandroid_adapter_navi_tab);
    }

    @Override
    protected void convert(BaseViewHolder helper, TabBean item) {
        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.getName());
        tvName.setSelected(item.isSelected());
    }
}
