package com.tj24.base.common.smartrefresh;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.tj24.base.R;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


/**
 * Created by energy on 2018/2/22.
 */

public class CatRefreshHeader extends LinearLayout implements RefreshHeader {
    private ImageView ivCat;
    private TextView tvLoading;
    private LayoutInflater inflater;
    public CatRefreshHeader(Context context) {
        super(context);
        initView(context);
    }

    public CatRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CatRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.base_cat_refresh_header,null);
        ivCat = view.findViewById(R.id.iv_cat);
        tvLoading = view.findViewById(R.id.tv_loading);
        addView(view);
    }



    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }


    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success){
            tvLoading.setText("刷新完成");
        } else {
            tvLoading.setText("刷新失败");
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState){
            case PullDownToRefresh:
                ivCat.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(getContext()).load(R.drawable.base_ic_loading).asBitmap().into(ivCat);
                ivCat.setVisibility(VISIBLE);
                tvLoading.setVisibility(VISIBLE);
                tvLoading.setText("下拉为你刷新...");
                break;
            case ReleaseToRefresh:
                tvLoading.setText("释放立即刷新");
                break;
            case RefreshReleased:
                tvLoading.setText("正在努力刷新...");
                Glide.with(getContext()).load(R.drawable.base_ic_loading).asGif().into(ivCat);
                break;
            case RefreshFinish:
                tvLoading.setText("刷新完成!");
                Glide.with(getContext()).load(R.drawable.base_ic_loading).asBitmap().into(ivCat);
                break;
            //            case Refreshing:
            //                tvLoading.setText("正在努力刷新...");
            //                break;
        }
    }
}
