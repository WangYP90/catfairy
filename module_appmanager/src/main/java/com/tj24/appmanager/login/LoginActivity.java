package com.tj24.appmanager.login;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.textfield.TextInputEditText;
import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.appmanager.common.SimpleTransitionListener;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.constant.BmobErrorCode;
import com.tj24.base.utils.UserHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

@Route(path = ARouterPath.AppManager.LOGIN_ACTIVITY)
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXT_IS_WITH_TRANSITION = "isWithTransition";
    private static final String EXT_USER_NAME = "userName";
    private static final String EXT_USER_PWD = "userPwd";
    private static final int REQUEST_CODE_REGIST = 100;

    @BindView(R2.id.fl_login_bottom)
    FrameLayout flLoginBottom;
    @BindView(R2.id.iv_login_wall)
    ImageView ivLoginWall;
    @BindView(R2.id.ll_login_wall)
    LinearLayout llLoginWall;
    @BindView(R2.id.iv_shareElement)
    ImageView ivShareElement;
    @BindView(R2.id.tv_login_next)
    TextView tvLoginNext;
    @BindView(R2.id.rl_login_top)
    RelativeLayout rlLoginTop;
    @BindView(R2.id.prb_login)
    ProgressBar prbLogin;
    @BindView(R2.id.et_phoneNumber)
    TextInputEditText etPhoneNumber;
    @BindView(R2.id.et_pwd)
    TextInputEditText etPwd;
    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.tv_regist)
    TextView tvRegist;
    @BindView(R2.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R2.id.ll_inputElements)
    LinearLayout llInputElements;

    /**
     * 是否正在进行transition动画
     */
    boolean isTranstioning = false;
    /**
     * 用户
     */
    User mUser;

    String userName;
    String userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTransition();
        mUser = new User();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_login;
    }

    private void initTransition() {
        boolean isWithTransition = getIntent().getBooleanExtra(EXT_IS_WITH_TRANSITION, false);
        if (isWithTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isTranstioning = true;
            getWindow().getSharedElementEnterTransition().addListener(new SimpleTransitionListener() {
                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    isTranstioning = false;
                    fadeElementsIn();
                }
            });
        } else {
            flLoginBottom.setVisibility(View.VISIBLE);
            llLoginWall.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 将LoginActivity的界面元素使用淡入的方式显示出来。
     */
    private void fadeElementsIn() {
        TransitionManager.beginDelayedTransition(flLoginBottom, new Fade());
        flLoginBottom.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(llLoginWall, new Fade());
        llLoginWall.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!isTranstioning) {
            finish();
        }
    }


    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void actionStartWithTransition(Activity activity, View view) {
        Intent intent = new Intent(activity, LoginActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.putExtra(EXT_IS_WITH_TRANSITION, true);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view,
                    activity.getString(R.string.app_login_shareElementName));
            activity.startActivity(intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @OnClick({R2.id.tv_login_next, R2.id.btn_login, R2.id.tv_regist, R2.id.tv_forget_pwd})
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_login) {
            login();
        } else if (viewId == R.id.tv_login_next) {
            MainActivity.startMain(mActivity);
            finish();
        } else if (viewId == R.id.tv_forget_pwd) {
            ForgetPwdActivity.actionStart(this, ivShareElement);
        } else if (viewId == R.id.tv_regist) {
            RegistActivity.startForResult(this, ivShareElement, REQUEST_CODE_REGIST);
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
                if (e == null) {        //登录成功
                    prbLogin.setVisibility(View.GONE);
                    mUser = UserHelper.getCurrentUser();
                    MainActivity.startMain(mActivity);
                    finish();
                } else {
                    prbLogin.setVisibility(View.GONE);
                    showShortToast(BmobErrorCode.getInstance().getErro(e.getErrorCode()));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_REGIST) {
                userName = getIntent().getStringExtra(EXT_USER_NAME);
                userPwd = getIntent().getStringExtra(EXT_USER_PWD);
                etPhoneNumber.setText(userName);
                etPwd.setText(userPwd);
            }
        }
    }

    public static void setResult(Activity activity, String userName, String userPwd) {
        Intent intent = new Intent();
        intent.putExtra(EXT_USER_NAME, userName);
        intent.putExtra(EXT_USER_PWD, userPwd);
        activity.setResult(RESULT_OK, intent);
    }
}
