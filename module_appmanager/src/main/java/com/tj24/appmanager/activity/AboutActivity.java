package com.tj24.appmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tj24.appmanager.R;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.base.base.ui.BaseActivity;

public class AboutActivity extends BaseActivity {

    TextView tvName;
    TextView tvVersion;
    ImageView ivPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvName = findViewById(R.id.tv_name);
        tvVersion = findViewById(R.id.tv_version);
        ivPic = findViewById(R.id.iv_pic);

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
     * @param context
     */
    public static void actionStart(Context context){
        Intent i = new Intent(context,AboutActivity.class);
        context.startActivity(i);
    }
}
