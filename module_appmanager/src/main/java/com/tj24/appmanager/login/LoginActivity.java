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
import androidx.annotation.Nullable;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import com.google.android.material.textfield.TextInputEditText;
import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.appmanager.common.SimpleTransitionListener;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.bean.appmanager.login.Version;
import com.tj24.base.constant.BmobErrorCode;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String EXT_HAS_NEW_VERSION = "hasNewVersion";
    private static final String EXT_VERSION = "version";
    private static final String EXT_IS_WITH_TRANSITION = "isWithTransition";
    private static final String EXT_USER_NAME = "userName";
    private static final String EXT_USER_PWD = "userPwd";
    private static final int REQUEST_CODE_REGIST = 100;
    ImageView ivLoginWall;
    LinearLayout llLoginWall;
    RelativeLayout rlLoginTop;
    ProgressBar prbLogin;
    TextInputEditText etPhoneNumber;
    TextInputEditText etPwd;
    LinearLayout llInputElements;
    FrameLayout flLoginBottom;
    ImageView ivShareElement;
    Button btnLogin;
    TextView tvLoginNext;
    TextView tvForgetPwd;
    TextView tvRegist;
    String userName;
    String userPwd;

    /**
     * 是否正在进行transition动画
     */
    boolean isTranstioning = false;
    /**
     * 用户
     */
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initTransition();
        mUser = new User();
    }
    @Override
    public int getLayoutId() {
        return R.layout.app_activity_login;
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
        etPwd =findViewById(R.id.et_pwd);
        btnLogin =findViewById(R.id.btn_login);
        llInputElements =findViewById(R.id.ll_inputElements);
        flLoginBottom =findViewById(R.id.fl_login_bottom);
        tvForgetPwd = findViewById(R.id.tv_forget_pwd);
        tvLoginNext = findViewById(R.id.tv_login_next);
        tvRegist = findViewById(R.id.tv_regist);
        ivShareElement = findViewById(R.id.iv_shareElement);

        btnLogin.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
        tvLoginNext.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            login();
        }else if(v.getId() == R.id.tv_login_next){
            MainActivity.startMain(mActivity);
            finish();
        }else if(v.getId() == R.id.tv_forget_pwd){
            ForgetPwdActivity.actionStart(this,ivShareElement);
        }else if(v.getId() == R.id.tv_regist){
           RegistActivity.startForResult(this,ivShareElement,REQUEST_CODE_REGIST);
        }
    }

    /**
     * 登录
     */
    private void login() {
        mUser.setUsername(etPhoneNumber.getText().toString().trim());
        mUser.setPassword(etPwd.getText().toString().trim());
        prbLogin.setVisibility(View.VISIBLE);
        mUser.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if(e==null){        //登录成功
                    prbLogin.setVisibility(View.GONE);
                    mUser  = UserHelper.getCurrentUser();
                    MainActivity.startMain(mActivity);
                    finish();
                }else{
                    prbLogin.setVisibility(View.GONE);
                    BmobErrorCode.getInstance().getErro(e.getErrorCode());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_REGIST){
                userName = getIntent().getStringExtra(EXT_USER_NAME);
                userPwd = getIntent().getStringExtra(EXT_USER_PWD);
                etPhoneNumber.setText(userName);
                etPwd.setText(userPwd);
            }
        }
    }

    public static void setResult(Activity activity,String userName,String userPwd){
        Intent intent = new Intent();
        intent.putExtra(EXT_USER_NAME,userName);
        intent.putExtra(EXT_USER_PWD,userPwd);
        activity.setResult(RESULT_OK,intent);
    }
}
