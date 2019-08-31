package com.tj24.module_appmanager.adapter;

import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.bean.AppBean;

import java.util.List;

public class SearchAdapter extends BaseQuickAdapter<AppBean, BaseViewHolder> {

    public SearchAdapter(int layoutResId, @Nullable List<AppBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppBean item) {
        helper.setText(R.id.tv_name,item.getName())
        .setText(R.id.tv_packageName,item.getPackageName())
        .addOnClickListener(R.id.tv_open);
        Glide.with(mContext).load(item.getIco()).error(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.iv_ico));
    }
}
