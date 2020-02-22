package com.tj24.wanandroid.module.homepage;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.wanandroid.R;

import java.util.List;

import androidx.annotation.Nullable;

public class CommonArticleAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {
    public CommonArticleAdapter(int layoutResId, @Nullable List<ArticleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleBean item) {
        helper.setText(R.id.tv_author,item.getShareUser()!=null?item.getShareUser():" ")
                .setText(R.id.tv_date,item.getNiceShareDate()!=null?item.getNiceShareDate():" ")
                .setText(R.id.tv_title,item.getTitle()!=null?item.getTitle():" ")
                .setText(R.id.tv_chapter,item.getSuperChapterName()+"/"+item.getChapterName())
                .setBackgroundColor(R.id.iv_collect,item.isCollect()? Color.RED:Color.BLUE)
                .addOnClickListener(R.id.iv_collect,R.id.tv_author);
    }
}
