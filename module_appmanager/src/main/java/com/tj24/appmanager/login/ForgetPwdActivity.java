package com.tj24.appmanager.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import com.google.android.material.textfield.TextInputEditText;
import com.tj24.appmanager.R;
import com.tj24.appmanager.common.SimpleTransitionListener;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.constant.BmobErrorCode;

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    private TextInputEditText etEmail;
    private Button btnEmail;
    private Button btnCall;
    private LinearLayout llElement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initTransition();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_forget_pwd;
    }

    private void initView() {
        etEmail = (TextInputEditText) findViewById(R.id.et_email);
        btnEmail = (Button) findViewById(R.id.btn_reback_pwd);
        btnCall = (Button) findViewById(R.id.btn_call);
        llElement = findViewById(R.id.ll_inputElements);
        btnEmail.setOnClickListener(this);
        btnCall.setOnClickListener(this);
    }

    private void initTransition() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            getWindow().getSharedElementEnterTransition().addListener(new SimpleTransitionListener(){
                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    // 界面元素使用淡入的方式显示出来。
                    TransitionManager.beginDelayedTransition(llElement, new Fade());
                    llElement.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_reback_pwd){
            final String email = etEmail.getText().toString();
            BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                @SuppressLint("StringFormatMatches")
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        showLongToast(getString(R.string.app_request_restet_pwd_success_to_email_reset_pwd,email));
                        finish();
                    }else{
                        showShortToast(BmobErrorCode.getInstance().getErro(e.getErrorCode()));
                    }
                }
            });
        }else if(v.getId() == R.id.btn_call){
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            Uri dialData = Uri.parse("tel://18691788126");
            dialIntent.setData(dialData);
            startActivity(dialIntent);
        }
    }

    public static void actionStart(Activity activity, View view){
        Intent intent = new Intent(activity,ForgetPwdActivity.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view,
                    activity.getString(R.string.app_reback_pwd_shareElementName));
            activity.startActivity(intent,options.toBundle());
        }else {
            activity.startActivity(intent);
        }
    }
}
