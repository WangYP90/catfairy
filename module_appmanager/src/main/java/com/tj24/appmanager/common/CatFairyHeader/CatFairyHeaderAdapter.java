package com.tj24.appmanager.common.CatFairyHeader;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.appmanager.R;

import java.util.List;

public class CatFairyHeaderAdapter extends BaseQuickAdapter<ModuleBean,BaseViewHolder> {

    public CatFairyHeaderAdapter(int layoutResId, @Nullable List<ModuleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleBean item) {
        helper.setText(R.id.tv_name,item.getName())
                .setImageResource(R.id.iv_pic,item.getPicRes());
    }
}
