package com.tj24.wanandroid.module.mine.coin;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.bean.wanandroid.CoinProgressBean;
import com.tj24.wanandroid.R;

public class CoinLoadAdpater extends BaseQuickAdapter<CoinProgressBean, BaseViewHolder> {
    public CoinLoadAdpater() {
        super(R.layout.wanandroid_adapter_coin_load);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinProgressBean item) {
        helper.setText(R.id.tv_reason,item.getReason())
                .setText(R.id.tv_desc,item.getDesc())
                .setText(R.id.tv_count,item.getCoinCount()+"");
    }
}
