package com.tj24.appmanager.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.tj24.appmanager.R;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.appmanager.common.AppEditHelper;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;

public class AppFooterView extends LinearLayout implements View.OnClickListener {
    private TextView tvMove;
    private TextView tvDelete;
    private TextView tvInfo;
    private TextView tvLevle;
    private TextView tvShare;
    private TextView tvMore;

    private Context context;
    private List<AppBean> edittingApps = new ArrayList<>();
    private  AppClassfication currentClassification;
    private   int selectColor;
    private   int unSelectColor;

    public AppFooterView(Context context) {
        super(context);
        this.context = context;
    }

    public AppFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.apps_footer, null);
        initView(myView);
        addView(myView);
    }

    private void initView(View myView) {
        selectColor = ContextCompat.getColor(context,R.color.color_footer_text_selected);
        unSelectColor = ContextCompat.getColor(context,R.color.color_footer_text_unselected);

        tvMove = myView.findViewById(R.id.tv_move);
        tvDelete = myView.findViewById(R.id.tv_delete);
        tvInfo = myView.findViewById(R.id.tv_info);
        tvLevle = myView.findViewById(R.id.tv_levle);
        tvShare = myView.findViewById(R.id.tv_share);
        tvMore = myView.findViewById(R.id.tv_more);
        tvMove.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        tvLevle.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvMore.setOnClickListener(this);
    }


    public void onEdittingAppChanged(List<AppBean> edittingApps,AppClassfication currentClassification){
        this.edittingApps = edittingApps;
        this.currentClassification = currentClassification;
        if(edittingApps.size()<=0){
            setTextCanNotTouch(tvDelete,R.drawable.ico_footer_del);
            setTextCanNotTouch(tvInfo,R.drawable.ico_footer_info);
            setTextCanNotTouch(tvMore,R.drawable.ico_footer_more);
            setTextCanNotTouch(tvMove,R.drawable.ico_footer_move);
            setTextCanNotTouch(tvLevle,R.drawable.ico_footer_prority);
            setTextCanNotTouch(tvShare,R.drawable.ico_footer_share);
        }else if(edittingApps.size()==1){
            setTextCanTouch(tvDelete,R.drawable.ico_footer_del);
            setTextCanTouch(tvInfo,R.drawable.ico_footer_info);
            setTextCanTouch(tvMore,R.drawable.ico_footer_more);
            setTextCanTouch(tvMove,R.drawable.ico_footer_move);
            setTextCanTouch(tvLevle,R.drawable.ico_footer_prority);
            setTextCanTouch(tvShare,R.drawable.ico_footer_share);
        }else {
            setTextCanTouch(tvDelete,R.drawable.ico_footer_del);
            setTextCanNotTouch(tvInfo,R.drawable.ico_footer_info);
            setTextCanTouch(tvMore,R.drawable.ico_footer_more);
            setTextCanTouch(tvMove,R.drawable.ico_footer_move);
            setTextCanNotTouch(tvLevle,R.drawable.ico_footer_prority);
            setTextCanNotTouch(tvShare,R.drawable.ico_footer_share);
        }
    }


    private void setTextCanTouch(TextView tv, int drawable){
        tv.setEnabled(true);
        tv.setTextColor(selectColor);
        Drawable topDrawable = ContextCompat.getDrawable(context,drawable);
        topDrawable.setColorFilter(selectColor,SRC_ATOP);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        tv.setCompoundDrawables(null, topDrawable, null, null);
    }

    private void setTextCanNotTouch(TextView tv, int drawable){
        tv.setEnabled(false);
        tv.setTextColor(unSelectColor);
        Drawable topDrawable = getResources().getDrawable(drawable);
        topDrawable.setColorFilter(unSelectColor,SRC_ATOP);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        tv.setCompoundDrawables(null, topDrawable, null, null);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_move) {
            AppEditHelper moveHelper = new AppEditHelper(context, edittingApps, currentClassification);
            moveHelper.createMoveDialog();
        } else if (i == R.id.tv_delete) {
            AppEditHelper uninstallHelper = new AppEditHelper(context, edittingApps, currentClassification);
            uninstallHelper.unInstall();
        } else if (i == R.id.tv_info) {
            AppEditHelper infoHelper = new AppEditHelper(context, edittingApps, currentClassification);
            infoHelper.goAppInfoActivity();
        } else if (i == R.id.tv_levle) {
            AppEditHelper levleHelper = new AppEditHelper(context, edittingApps, currentClassification);
            levleHelper.setLevle();
        } else if (i == R.id.tv_share) {
            AppEditHelper sharelHelper = new AppEditHelper(context, edittingApps, currentClassification);
            sharelHelper.share();
        } else if (i == R.id.tv_more) {
        }
    }
}
