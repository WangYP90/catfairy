package com.tj24.base.utils;

import android.view.View;

import com.kennyc.view.MultiStateView;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.utils.listener.OnClickListener2;

public class MultiStateUtils {
    public static void toLoading(MultiStateView view){
        view.setViewState(MultiStateView.ViewState.LOADING);
    }

    public static void toEmpty(MultiStateView view){
        if (view.getViewState() == MultiStateView.ViewState.CONTENT) {
            return;
        }
        view.setViewState(MultiStateView.ViewState.EMPTY);
    }

    public static void toError(MultiStateView view){
        if (view.getViewState() == MultiStateView.ViewState.CONTENT) {
            return;
        }
        view.setViewState(MultiStateView.ViewState.ERROR);
    }

    public static void toContent(MultiStateView view){
        view.setViewState(MultiStateView.ViewState.CONTENT);
    }

    public static void setEmptyAndErrorClick(MultiStateView view,  SimpleListener listener){
        setEmptyClick(view, listener);
        setErrorClick(view, listener);
    }

    public static void setEmptyClick(MultiStateView view, SimpleListener listener){
        View empty = view.getView(MultiStateView.ViewState.EMPTY);
        if (empty != null) {
            empty.setOnClickListener(new OnClickListener2() {
                @Override
                public void onClick2(View v) {
                    listener.onResult();
                }
            });
        }
    }

    public static void setErrorClick(MultiStateView view, SimpleListener listener){
        View error = view.getView(MultiStateView.ViewState.ERROR);
        if (error != null) {
            error.setOnClickListener(new OnClickListener2() {
                @Override
                public void onClick2(View v) {
                    listener.onResult();
                }
            });
        }
    }
}
