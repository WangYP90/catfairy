package com.tj24.wanandroid.module.treenavigation;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.wanandroid.R;

import java.util.LinkedList;
import java.util.Queue;

import androidx.annotation.NonNull;

public class KnowledgeAdapter extends BaseQuickAdapter<TreeBean, BaseViewHolder> {

    private LayoutInflater mInflater = null;
    private Queue<TextView> mFlexItemTextViewCaches = new LinkedList<>();

    public void setOnKnowledgeItemClickListener(OnKnowledgeItemClickListener onKnowledgeItemClickListener) {
        this.onKnowledgeItemClickListener = onKnowledgeItemClickListener;
    }

    private OnKnowledgeItemClickListener onKnowledgeItemClickListener;

    public KnowledgeAdapter() {
        super(R.layout.wanandroid_adapter_knowledge_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreeBean item) {
        helper.setText(R.id.tv_name,item.getName());
        FlexboxLayout flex = helper.getView(R.id.flex);
        for(int i = 0; i<item.getChildren().size();i++){
            TextView tv = createOrGetCacheFlexItemTextView(flex);
            TreeBean childItem = item.getChildren().get(i);
            tv.setText(childItem.getName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onKnowledgeItemClickListener != null){
                        onKnowledgeItemClickListener.onClick(childItem);
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

    public interface OnKnowledgeItemClickListener {
        void onClick(TreeBean treeBean);
    }
}
