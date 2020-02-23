package com.tj24.wanandroid.module.treenavigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.NavigationBean;
import com.tj24.wanandroid.R;

import java.util.LinkedList;
import java.util.Queue;

import androidx.annotation.NonNull;

public class NaviAdapter extends BaseQuickAdapter<NavigationBean, BaseViewHolder> {

    private LayoutInflater mInflater = null;
    private Queue<TextView> mFlexItemTextViewCaches = new LinkedList<>();
    private int height = 0;
    private int lastId;
    public void setOnNaviItemClickListener(OnNaviItemClickListener onNaviItemClickListener) {
        this.onNaviItemClickListener = onNaviItemClickListener;
    }

    private OnNaviItemClickListener onNaviItemClickListener;

    public NaviAdapter() {
        super(R.layout.wanandroid_adapter_navi_item);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public void setLastId(int cid) {
        this.lastId = cid;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(mData.get(position).getCid() == lastId){
            if (holder.itemView.getHeight() < height) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.height = height;
                holder.itemView.setLayoutParams(params);
            }
        }else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationBean item) {
        helper.setText(R.id.tv_name,item.getName());
        FlexboxLayout flex = helper.getView(R.id.flex);
        for(int i = 0; i<item.getArticles().size();i++){
            TextView tv = createOrGetCacheFlexItemTextView(flex);
            ArticleBean childItem = item.getArticles().get(i);
            tv.setText(childItem.getTitle());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onNaviItemClickListener != null){
                        onNaviItemClickListener.onClick(childItem);
                    }
                }
            });
            flex.addView(tv);
        }
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        FlexboxLayout fbl = holder.getView(R.id.flex);
        for (int i = 0; i < fbl.getChildCount(); i++) {
            mFlexItemTextViewCaches.offer((TextView) fbl.getChildAt(i));
        }
        fbl.removeAllViews();
    }

    private TextView createOrGetCacheFlexItemTextView(FlexboxLayout flex) {
        TextView tv = mFlexItemTextViewCaches.poll();
        if (tv != null) {
            return tv;
        }
        return createFlexItemTextView(flex);
    }

    private TextView createFlexItemTextView(FlexboxLayout fbl) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(fbl.getContext());
        }
        return (TextView) mInflater.inflate(R.layout.wanandroid_rv_item_knowledge_child, fbl, false);
    }

    public interface OnNaviItemClickListener {
        void onClick(ArticleBean treeBean);
    }
}
