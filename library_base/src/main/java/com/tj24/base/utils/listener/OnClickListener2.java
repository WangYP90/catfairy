package com.tj24.base.utils.listener;

import android.view.View;

/**
 * 防止过快点击的单机监听
 */
public abstract class OnClickListener2 implements View.OnClickListener {

    @Override
    public final void onClick(final View v) {
        ClickHelper.onlyFirstSameView(v, new ClickHelper.Callback() {
            @Override
            public void onClick(View view) {
                onClick2(view);
            }
        });
    }

    public abstract void onClick2(View v);
}
