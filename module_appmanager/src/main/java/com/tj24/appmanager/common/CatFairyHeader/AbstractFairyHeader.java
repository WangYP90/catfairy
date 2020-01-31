package com.tj24.appmanager.common.CatFairyHeader;

import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.tj24.appmanager.R;

public abstract  class AbstractFairyHeader implements RefreshHeader {
    private LayoutInflater inflater;
    private View view;
    private ExpendPoint expendPoint;
    private RecyclerView rc;
    Vibrator vibrator;
    //三个点在一个周期内变化所需下拉的高度
    private float points3LifeHeight;
    //列表的高度
    private float listHeight;
    //下拉是否达到了列表的高度 只在下拉未开始刷新时有效
    boolean arrivedListHeight;

    public AbstractFairyHeader(Context mContext) {
        vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
        inflater = LayoutInflater.from(mContext);
        initView();
        bindData(rc);
        rc.post(new Runnable() {
            @Override
            public void run() {
                listHeight = rc!=null?rc.getHeight():360;
            }
        });
        points3LifeHeight = dip2px(90);
    }

    /**
     * 初始化header的view
     */
    private void initView() {
        view = inflater.inflate(R.layout.app_cat_fairy_header,null);
        rc = view.findViewById(R.id.rv_header);
        expendPoint = view.findViewById(R.id.expend_point);
        reset();
    }

    /**
     * 给header的列表设置 数据 和动作
     */
    public abstract void bindData(RecyclerView rc);

    /**
     * 恢复header 的初始状态
     */
    protected void reset(){
        expendPoint.setVisibility(View.VISIBLE);
        expendPoint.setAlpha(1);
        expendPoint.setTranslationY(0);
        rc.setTranslationY(0);
        arrivedListHeight = false;
    }

    @NonNull
    @Override
    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }


    /**
     * header移动过程中会不断的调用，因此在次方法中根据 划出高度的不断变化 来显示三个黑点 和 列表的缩放动画。
     * @param isDragging 是否拖动
     * @param percents  拖动的距离占header高度的百分比
     * @param offset     拖动的距离
     * @param height      header的高度
     * @param maxDragHeight  最大的拖动距离
     */
    @Override
    public void onMoving(boolean isDragging, float percents, int offset, int height, int maxDragHeight) {
        if (!arrivedListHeight) {
            expendPoint.setVisibility(View.VISIBLE);
            float percent = Math.abs(offset) / points3LifeHeight;
            int moreOffset = (int) (Math.abs(offset) - points3LifeHeight);
            if (percent <= 1.0f) {
                expendPoint.setPercent(percent);
                expendPoint.setTranslationY(-Math.abs(offset) / 2 + expendPoint.getHeight() / 2);
                rc.setTranslationY(-height);
            } else {
                float subPercent = (moreOffset) / (listHeight - points3LifeHeight);
                subPercent = Math.min(1.0f, subPercent);
                expendPoint.setTranslationY(-(int) points3LifeHeight / 2 + expendPoint.getHeight() / 2 + (int) points3LifeHeight * subPercent / 2);
                expendPoint.setPercent(1.0f);
                float alpha = (1 - subPercent * 2);
                expendPoint.setAlpha(Math.max(alpha, 0));
                rc.setAlpha(Math.max(1-alpha,0));
                rc.setTranslationY(-(1 - subPercent) * points3LifeHeight);
            }
            if(Math.abs(offset)==listHeight){
                vibrator.vibrate(60);
                arrivedListHeight = true;
            }
        }

        if (Math.abs(offset) >= listHeight) {
            expendPoint.setVisibility(View.GONE);
            rc.setTranslationY(-(Math.abs(offset) - listHeight) / 2);
        }
    }

    @Override
    public void setPrimaryColors(int... colors) {
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState){
            case None:
            case PullDownToRefresh:
                break;
            case Refreshing:
                arrivedListHeight = true;
                break;
            case RefreshFinish:
                reset();
                break;
        }
    }

    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
