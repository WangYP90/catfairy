package com.tj24.wanandroid.common.view;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 控制recyclerview是否可以垂直滑动
 */
public class ScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean canScrollVertically = true;

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        if(!canScrollVertically){
            return false;
        }
        return super.canScrollVertically();
    }

    public void setCanScrollVertically(boolean canScrollVertically) {
        this.canScrollVertically = canScrollVertically;
    }
}
