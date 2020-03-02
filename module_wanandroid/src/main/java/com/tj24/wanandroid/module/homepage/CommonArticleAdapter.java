package com.tj24.wanandroid.module.homepage;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.wanandroid.R;

import java.util.List;

import androidx.annotation.Nullable;

public class CommonArticleAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {
    public CommonArticleAdapter( @Nullable List<ArticleBean> data) {
        super(R.layout.wanandroid_adapter_common_article_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleBean item) {
        String shareUser = item.getShareUser()!=null?item.getShareUser():"";
        String author = item.getAuthor()!=null?item.getAuthor():"";
        if(TextUtils.isEmpty(shareUser) && TextUtils.isEmpty(author)){
            author = "匿名发布";
        }
        String title = item.getTitle()!=null?item.getTitle():"";
        String des =item.getDesc()!=null?item.getDesc():"";

        boolean isNew = item.isFresh();

        String picUrl = item.getEnvelopePic()!=null?item.getEnvelopePic():"";
        boolean hasPic = picUrl.startsWith("http");

        String chapter = "";
        if(!TextUtils.isEmpty(item.getSuperChapterName())){
            chapter = item.getSuperChapterName()+"/"+ item.getChapterName();
        }else {
            chapter = item.getChapterName()!=null?item.getChapterName():"";
        }
        String date;
        if(!TextUtils.isEmpty(item.getNiceShareDate()) && !item.getNiceShareDate().contains("未知")){
            date = item.getNiceShareDate();
        }else {
            date = item.getNiceDate()!=null?item.getNiceDate():"";
        }


        helper.setGone(R.id.tv_new,isNew)
                .setGone(R.id.iv_pic,hasPic)
                .setGone(R.id.tv_desc,hasPic)
                .setText(R.id.tv_shareuser,shareUser)
                .setText(R.id.tv_author,author)
                .setText(R.id.tv_date,date)
                .setText(R.id.tv_title,title)
                .setText(R.id.tv_desc,des)
                .setText(R.id.tv_chapter,chapter)
                .setBackgroundColor(R.id.iv_collect,item.isCollect()? Color.RED:Color.BLUE)
                .addOnClickListener(R.id.iv_collect,R.id.tv_author);

        ImageView ivPic = helper.getView(R.id.iv_pic);
        Glide.with(mContext).load(picUrl).into(ivPic);
    }
}
