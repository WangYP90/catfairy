package com.tj24.appmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.base.base.ui.BaseActivity;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R2.id.iv_pic)
    ImageView ivPic;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvName.setText(ApkModel.getAppName(this));
        tvVersion.setText(ApkModel.getVersionName(this));
        ivPic.setImageBitmap(ApkModel.getBitmap(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_about;
    }

    /**
     * 启动
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent i = new Intent(context, AboutActivity.class);
        context.startActivity(i);
    }
}
