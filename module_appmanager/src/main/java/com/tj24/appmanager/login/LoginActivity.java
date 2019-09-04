package com.tj24.appmanager.login;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.appmanager.common.SimpleTransitionListener;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.login.Version;

public class LoginActivity extends BaseActivity {
    private static final String EXT_HAS_NEW_VERSION = "hasNewVersion";
    private static final String EXT_VERSION = "version";
    private static final String EXT_IS_WITH_TRANSITION = "isWithTransition";
    
    ImageView ivLoginWall;
    LinearLayout llLoginWall;
    RelativeLayout rlLoginTop;
    ProgressBar prbLogin;
    TextInputEditText etPhoneNumber;
    TextInputLayout inputLayoutPhoneNum;
    TextInputEditText etPwd;
    TextInputLayout inputlayoutPwd;
    Button btnLogin;
    LinearLayout llInputElements;
    FrameLayout flLoginBottom;
    /**
     * 是否正在进行transition动画
     */
    boolean isTranstioning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initTransition();
    }

    private void initTransition() {
        boolean isWithTransition = getIntent().getBooleanExtra(EXT_IS_WITH_TRANSITION,false);
        if(isWithTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            isTranstioning = true;
            getWindow().getSharedElementEnterTransition().addListener(new SimpleTransitionListener(){
                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    isTranstioning = false;
                    fadeElementsIn();
                }
            });
        }else {
            flLoginBottom.setVisibility(View.VISIBLE);
            llLoginWall.setVisibility(View.VISIBLE);
        }
    }


    private void initView() {
        llLoginWall =findViewById(R.id.ll_login_wall);
        ivLoginWall=findViewById(R.id.iv_login_wall);
        rlLoginTop =findViewById(R.id.rl_login_top);
        prbLogin =findViewById(R.id.prb_login);
        etPhoneNumber =findViewById(R.id.et_phoneNumber);
        inputLayoutPhoneNum =findViewById(R.id.inputLayout_phoneNum);
        etPwd =findViewById(R.id.et_pwd);
        inputlayoutPwd =findViewById(R.id.inputlayout_pwd);
        btnLogin =findViewById(R.id.btn_login);
        llInputElements =findViewById(R.id.ll_inputElements);
        flLoginBottom =findViewById(R.id.fl_login_bottom);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_login;
    }

    /**
     * 将LoginActivity的界面元素使用淡入的方式显示出来。
     */
    private void fadeElementsIn() {
        TransitionManager.beginDelayedTransition(flLoginBottom, new Fade());
        flLoginBottom.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(llLoginWall,new Fade());
        llLoginWall.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!isTranstioning) {
            finish();
        }
    }


    public static void actionStart(Activity activity, boolean hasNewVersion, Version version) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(EXT_HAS_NEW_VERSION,hasNewVersion);
        intent.putExtra(EXT_VERSION,version);
        activity.startActivity(intent);
    }

    public static void actionStartWithTransition(Activity activity, View view, boolean hasNewVersion, Version version) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(EXT_HAS_NEW_VERSION,hasNewVersion);
        intent.putExtra(EXT_VERSION,version);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            intent.putExtra(EXT_IS_WITH_TRANSITION,true);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view,
                    activity.getString(R.string.app_login_shareElementName));
            activity.startActivity(intent,options.toBundle());
        }else {
            activity.startActivity(intent);
        }
    }
}
