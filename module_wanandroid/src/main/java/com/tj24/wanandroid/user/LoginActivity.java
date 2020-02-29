package com.tj24.wanandroid.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.utils.TextUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kennyc.view.MultiStateView;
import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.event.LoginEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

@Route(path = ARouterPath.WanAndroid.LOGIN_ACTIVITY)
public class LoginActivity extends BaseWanAndroidActivity {
    public static final int REQ_REGIST = 24;
    private static final String USER_NAME = "userName";
    private static final String PWD = "pwd";

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.et_userName)
    TextInputEditText etUserName;
    @BindView(R2.id.inputLayout_userName)
    TextInputLayout inputLayoutUserName;
    @BindView(R2.id.et_pwd)
    TextInputEditText etPwd;
    @BindView(R2.id.inputLayout_pwd)
    TextInputLayout inputLayoutPwd;
    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.tv_regist)
    TextView tvRegist;
    @BindView(R2.id.msv)
    MultiStateView msv;

    String  userName;
    String  pwd;

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_login;
    }

    @OnClick({R2.id.btn_login, R2.id.tv_regist})
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_login) {
            login();
        } else if (viewId == R.id.tv_regist) {
            RegistActivity.actionStartForResult(this);
        }
    }

    private void login() {
        userName = etUserName.getText().toString();
        pwd = etPwd.getText().toString();
        if(TextUtils.isEmpty(userName)){
            ToastUtil.showShortToast(this,"用户名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            ToastUtil.showShortToast(this,"密码不能为空！");
            return;
        }
        MultiStateUtils.toLoading(msv);
        UserRequest.login(userName, pwd, new WanAndroidCallBack<UserBean>() {
            @Override
            public void onSucces(UserBean userBean) {
                MultiStateUtils.toContent(msv);
                showShortToast("登录成功！");
                LoginEvent.postLoginSuccess();
                finish();
            }

            @Override
            public void onFail(String fail) {
                MultiStateUtils.toContent(msv);
                ToastUtil.showShortToast(LoginActivity.this,fail);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQ_REGIST){
                String userName = data.getStringExtra(USER_NAME);
                String pwd = data.getStringExtra(PWD);
                etUserName.setText(userName);
                etPwd.setText(pwd);
            }
        }
    }

    public static void setResult(Activity activity, String userName, String userPwd) {
        Intent intent = new Intent();
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(PWD, userPwd);
        activity.setResult(RESULT_OK, intent);
    }


    public static void  actionStart(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}
