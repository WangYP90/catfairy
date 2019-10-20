package com.tj24.appmanager.view.dialog;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tj24.appmanager.R;
import com.tj24.appmanager.common.AppEditHelper;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.base.utils.ScreenUtil;

public class AppEditPopup extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private AppBean appBean;
    private AppClassfication currentClassification;
    private float mAlpha = 1f; //背景灰度  0-1  1表示全透明

    private TextView tvMove;
    private TextView tvUninstall;
    private TextView tvInfo;
    private TextView tvPrority;
    private TextView tvShare;

    public AppEditPopup(Context context,AppBean appBean,AppClassfication currentClassification) {
        super(context);
        this.mContext = context;
        this.appBean = appBean;
        this.currentClassification = currentClassification;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.app_popup_edit_app,null);
        rootView.setZ(8F);
        setContentView(rootView);
        setBackgroundDrawable(null);
        setWidth((int) ScreenUtil.dip2px(mContext,120));
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        tvMove = rootView.findViewById(R.id.tv_move);
        tvUninstall = rootView.findViewById(R.id.tv_uninstall);
        tvInfo = rootView.findViewById(R.id.tv_info);
        tvPrority = rootView.findViewById(R.id.tv_prority);

        tvShare = rootView.findViewById(R.id.tv_share);
        tvMove.setOnClickListener(this);
        tvUninstall.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        tvPrority.setOnClickListener(this);
        tvShare.setOnClickListener(this);
    }


    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha) {
        if (mContext == null) return;
        if (mContext instanceof Activity) {
            Window window = ((Activity) mContext).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = alpha;
            window.setAttributes(layoutParams);
        }
    }

    /**
     * 窗口显示，窗口背景透明度渐变动画
     */
    public void showBackgroundAnimator() {
        if (mAlpha >= 1f) return;
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        animator.setDuration(360);
        animator.start();
    }

    public void setmAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_move) {
            AppEditHelper moveHelper = new AppEditHelper(mContext, appBean, currentClassification);
            moveHelper.createMoveDialog();
            dismiss();
        } else if (i == R.id.tv_uninstall) {
            AppEditHelper uninstallHelper = new AppEditHelper(mContext, appBean, currentClassification);
            uninstallHelper.unInstall();
            dismiss();
        } else if (i == R.id.tv_info) {
            AppEditHelper infoHelper = new AppEditHelper(mContext, appBean, currentClassification);
            infoHelper.goAppInfoActivity();
            dismiss();
        } else if (i == R.id.tv_prority) {
            AppEditHelper levleHelper = new AppEditHelper(mContext, appBean, currentClassification);
            levleHelper.setLevle();
            dismiss();
        } else if (i == R.id.tv_share) {
            AppEditHelper sharelHelper = new AppEditHelper(mContext, appBean, currentClassification);
            sharelHelper.share();
            dismiss();
        }
    }
}
