package com.tj24.appmanager.activity;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.textfield.TextInputEditText;
import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.login.LoginInterceptorCallBack;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.constant.ARouterPath;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

@Route(path = ARouterPath.AppManager.RESET_PWD_ACTIVITY, extras = ARouterPath.AppManager.NEED_LOGIN)
public class ResetPwdActivity extends BaseActivity {

    @BindView(R2.id.et_old_pwd)
    TextInputEditText etOldPwd;
    @BindView(R2.id.et_new_pwd)
    TextInputEditText etNewPwd;
    @BindView(R2.id.et_renew_pwd)
    TextInputEditText etRenewPwd;
    @BindView(R2.id.btn_submint)
    Button btnSubmint;

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_reset_pwd;
    }


    @OnClick(R2.id.btn_submint)
    public void onClick() {
        resetPwd();
    }

    /**
     * 修改密码
     */
    private void resetPwd() {
        String oldPwd = etOldPwd.getText().toString().trim();
        String newPwd = etNewPwd.getText().toString().trim();
        String reNewPwd = etRenewPwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            showShortToast(getString(R.string.app_input_old_pwd));
            return;
        }

        if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(reNewPwd)) {
            showShortToast(getString(R.string.app_new_pwd_cant_empy));
            return;
        }

        if (!newPwd.equals(reNewPwd)) {
            showShortToast(getString(R.string.app_pwd_not_same));
            return;
        }
        showProgressDialog(getString(R.string.app_waiting), "");
        BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                hideProgressDialog();
                if (e == null) {
                    showShortToast(getString(R.string.app_reset_pwd_succes));
                } else {
                    showShortToast(getString(R.string.app_reset_pwd_fail) + e.getMessage());
                }
            }
        });
    }


    public static void actionStart(Context context) {
        ARouter.getInstance().build(ARouterPath.AppManager.RESET_PWD_ACTIVITY)
                .navigation(context, new LoginInterceptorCallBack(context));
    }
}
