package com.tj24.wanandroid.common.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tj24.base.bean.wanandroid.NetUrlBean;
import com.tj24.base.utils.ScreenUtil;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;

public class UrlLinkEditPopup extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private NetUrlBean netUrlBean;
    private float mAlpha = 1f; //背景灰度  0-1  1表示全透明

    private TextView tvCopy;
    private TextView tvEdit;
    private TextView tvDel;

    private UrlLinkEditListener urlLinkEditListener;
    public UrlLinkEditPopup(Context context, NetUrlBean netUrlBean) {
        super(context);
        this.mContext = context;
        this.netUrlBean = netUrlBean;
        initView();
    }

    public void setUrlLinkEditListener(UrlLinkEditListener urlLinkEditListener) {
        this.urlLinkEditListener = urlLinkEditListener;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.wanandroid_url_edit_popup,null);
        rootView.setZ(8F);
        setContentView(rootView);
        setBackgroundDrawable(null);
        setWidth((int) ScreenUtil.dip2px(mContext,160));
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        tvCopy = rootView.findViewById(R.id.tv_copy);
        tvEdit = rootView.findViewById(R.id.tv_edit);
        tvDel = rootView.findViewById(R.id.tv_del);
        tvCopy.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        tvDel.setOnClickListener(this);
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
        if(i == R.id.tv_copy){
            copyLink();
        }else if(i==R.id.tv_del){
            if(urlLinkEditListener != null){
                urlLinkEditListener.onDel();
            }
        }else if(i ==R.id.tv_edit){
            if(urlLinkEditListener != null){
                urlLinkEditListener.onEdit();
            }
        }
        dismiss();
    }

    private void copyLink() {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", netUrlBean.getLink());
        clipboard.setPrimaryClip(clip);
        ToastUtil.showShortToast(mContext,"链接已复制");
    }

    public interface UrlLinkEditListener{
        public void onDel();
        public void onEdit();
    }
}
