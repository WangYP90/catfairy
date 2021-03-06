package com.tj24.appmanager.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.appmanager.R;
import com.tj24.base.bean.appmanager.AppBean;

import java.util.List;

import androidx.annotation.Nullable;

public class RcAppGrideAdapter extends BaseQuickAdapter<AppBean,BaseViewHolder> {

    private boolean isEditing;
    public RcAppGrideAdapter(int layoutResId, @Nullable List<AppBean> data,boolean isEditing) {
        super(layoutResId, data);
        this.isEditing = isEditing;
    }

    @Override
    protected void convert(BaseViewHolder helper, AppBean item) {
        helper.setText(R.id.tv_name,item.getName())
                .setGone(R.id.iv_selected,isEditing)
                .setImageResource(R.id.iv_selected,item.isSelected()?R.drawable.app_ico_app_selected :R.drawable.app_ico_app_unselected);
        ImageView ivIco = helper.getView(R.id.iv_ico);
        Glide.with(ivIco.getContext()).load(item.getIco()).placeholder(R.mipmap.app_ic_launcher_round).into(ivIco);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
