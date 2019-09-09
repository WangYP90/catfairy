package com.tj24.appmanager.login;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import com.google.android.material.textfield.TextInputEditText;
import com.tj24.appmanager.R;
import com.tj24.appmanager.common.SimpleTransitionListener;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.constant.BmobErrorCode;

public class RegistActivity extends BaseActivity {

    private ProgressBar prbRegist;
    private TextInputEditText etUserName;
    private TextInputEditText etUserPwd;
    private TextInputEditText etConfirmPwd;
    private Button btnRegist;
    private FrameLayout flElement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initTransition();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_regist;
    }

    private void initView() {
        prbRegist = (ProgressBar) findViewById(R.id.prb_regist);
        etUserName = (TextInputEditText) findViewById(R.id.et_userName);
        etUserPwd = (TextInputEditText) findViewById(R.id.et_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_pwd);
        btnRegist = (Button) findViewById(R.id.btn_regist);
        flElement = findViewById(R.id.fl_inputElements);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etUserName.getText().toString())){
                    showShortToast(getString(R.string.app_email_can_not_null));
                }else if(TextUtils.isEmpty(etUserPwd.getText().toString())) {
                    showShortToast(getString(R.string.app_pwd_can_not_null));
                }else if(TextUtils.isEmpty(etConfirmPwd.getText().toString())) {
                    showShortToast(getString(R.string.app_please_confirm_pwd));
                }else if(!etUserPwd.getText().toString().equals(etConfirmPwd.getText().toString())){
                    showShortToast(getString(R.string.app_pwd_not_same));
                }else {
                    regist();
                }
            }
        });
    }

    /**
     * 注册
     */
    private void regist() {
        prbRegist.setVisibility(View.VISIBLE);
        User user = new User();
        user.setUsername(etUserName.getText().toString());
        user.setEmail(etUserName.getText().toString());
        user.setPassword(etUserPwd.getText().toString());
        user.setNicai(etUserPwd.getText().toString());
        //注意：不能用save方法进行注册
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    prbRegist.setVisibility(View.GONE);
                    showShortToast(getString(R.string.app_regist_success) );
                    LoginActivity.setResult(mActivity,etUserName.getText().toString(),etUserPwd.getText().toString());
                    finish();
                }else{
                    prbRegist.setVisibility(View.GONE);
                    showShortToast(BmobErrorCode.getInstance().getErro(e.getErrorCode()));
                }
            }
        });
    }


    public static void startForResult(Activity activity,View view,int requstCode){
        Intent intent = new Intent(activity,RegistActivity.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view,
                    activity.getString(R.string.app_regist_shareElementName));
            activity.startActivityForResult(intent,requstCode,options.toBundle());
        }else {
            activity.startActivityForResult(intent,requstCode);
        }
    }

    private void initTransition() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            getWindow().getSharedElementEnterTransition().addListener(new SimpleTransitionListener(){
                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
//                     将LoginActivity的界面元素使用淡入的方式显示出来。
                    TransitionManager.beginDelayedTransition(flElement, new Fade());
                    flElement.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
