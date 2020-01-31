package com.tj24.appmanager.common.CatFairyHeader;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;

public class CatFairyHeaderLayout extends SmartRefreshLayout {
    public CatFairyHeaderLayout(Context context) {
        super(context);
    }

    public CatFairyHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void overSpinner() {
        super.overSpinner();
        if (mState == RefreshState.Refreshing) {
            if (mSpinner > mHeaderHeight/2) {
                mKernel.animSpinner(mHeaderHeight);
            }else {
                mKernel.animSpinner(0);
                finishRefresh();
            }
        }
    }
}
