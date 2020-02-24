package com.tj24.wanandroid.module.mine.coin;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.bean.wanandroid.CoinBean;
import com.tj24.wanandroid.R;

public class CoinRankAdpater extends BaseQuickAdapter<CoinBean, BaseViewHolder> {
    public CoinRankAdpater() {
        super(R.layout.wanandroid_adapter_coin_rank);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinBean item) {
        helper.setText(R.id.tv_number,item.getRank()+"")
                .setText(R.id.tv_name,item.getUsername())
                .setText(R.id.tv_coin,item.getCoinCount()+"");
    }
}
