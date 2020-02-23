package com.tj24.wanandroid.user;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kennyc.view.MultiStateView;
import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends BaseWanAndroidActivity {

    @BindView(R2.id.et_userName)
    TextInputEditText etUserName;
    @BindView(R2.id.inputLayout_userName)
    TextInputLayout inputLayoutUserName;
    @BindView(R2.id.et_pwd)
    TextInputEditText etPwd;
    @BindView(R2.id.inputLayout_pwd)
    TextInputLayout inputLayoutPwd;
    @BindView(R2.id.et_rePwd)
    TextInputEditText etRePwd;
    @BindView(R2.id.inputLayout_rePwd)
    TextInputLayout inputLayoutRePwd;
    @BindView(R2.id.btn_regist)
    Button btnRegist;
    @BindView(R2.id.tv_login)
    TextView tvLogin;
    @BindView(R2.id.msv)
    MultiStateView msv;

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_regist;
    }

    public static void actionStartForResult(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R2.id.btn_regist, R2.id.tv_login})
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_regist) {
            regist();
        } else if (viewId == R.id.tv_login) {
            finish();
        }
    }

    private void regist() {
        String userName = etUserName.getText().toString();
        String pwd = etPwd.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            showShortToast("用户名不能为空");
        } else if (TextUtils.isEmpty(pwd)) {
            showShortToast("密码不能为空");
        } else if (TextUtils.isEmpty(etRePwd.getText().toString())) {
            showShortToast("请再次填写密码");
        } else if (!etPwd.getText().toString().equals(etRePwd.getText().toString())) {
            showShortToast("两次输入密码不一致");
        } else {
            MultiStateUtils.toLoading(msv);
           UserRequest.regist(userName, pwd, pwd, new WanAndroidCallBack<UserBean>() {
               @Override
               public void onSucces(UserBean userBean) {
                   LoginActivity.setResult(mActivity,userName,pwd);
                   MultiStateUtils.toContent(msv);
                   showShortToast("注册成功！");
                   finish();
               }

               @Override
               public void onFail(String fail) {
                   MultiStateUtils.toContent(msv);
                   showShortToast(fail);
               }
           });
        }
    }
}
