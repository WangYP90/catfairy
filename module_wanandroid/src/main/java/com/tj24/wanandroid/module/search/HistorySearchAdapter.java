package com.tj24.wanandroid.module.search;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.bean.wanandroid.HistoryKey;
import com.tj24.base.greendao.daohelper.GreenDaoManager;
import com.tj24.wanandroid.R;

public class HistorySearchAdapter extends BaseQuickAdapter<HistoryKey, BaseViewHolder> {

    public HistorySearchAdapter() {
        super(R.layout.wanandroid_adapter_history_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryKey item) {
        helper.setText(R.id.tv_searchKey,item.getName());
        helper.getView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GreenDaoManager.getDaoSession().getHistoryKeyDao().delete(item);
                remove(helper.getAdapterPosition());
            }
        });
    }
}
