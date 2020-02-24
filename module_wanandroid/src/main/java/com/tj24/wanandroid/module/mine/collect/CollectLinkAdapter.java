package com.tj24.wanandroid.module.mine.collect;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.bean.wanandroid.NetUrlBean;
import com.tj24.wanandroid.R;

public class CollectLinkAdapter extends BaseQuickAdapter<NetUrlBean, BaseViewHolder> {
    public CollectLinkAdapter() {
        super(R.layout.wanandroid_adapter_collect_link);
    }

    @Override
    protected void convert(BaseViewHolder helper, NetUrlBean item) {
        helper.setText(R.id.tv_title,item.getName())
                .setText(R.id.tv_url,item.getLink());
    }
}
