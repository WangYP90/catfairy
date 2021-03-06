package com.tj24.appmanager.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.AboutActivity;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.appmanager.activity.ResetPwdActivity;
import com.tj24.appmanager.activity.UserAgreenmentActivity;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.common.keepAlive.EasyKeepAlive;
import com.tj24.appmanager.common.update.CheckUpdateHelper;
import com.tj24.appmanager.common.update.CheckUpdateListner;
import com.tj24.appmanager.login.LoginInterceptorCallBack;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.appmanager.service.ScanTopService;
import com.tj24.base.utils.ToastUtil;
import com.tj24.base.utils.UserHelper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import cn.bmob.v3.BmobUser;

public class MainSettingsFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "MainSettingsFragment";
    private Context mContext;

    SwitchPreferenceCompat spSwitchHideApp;
    Preference spLookOtherUse;
    Preference spBatteryWhiteList;
    Preference spBackGroudRun;
    MultiSelectListPreference spListCustomOrder;
    SwitchPreferenceCompat spSwitchAutoUpdate;
    Preference spUpdate;
    Preference spAbout;
    Preference spUserAgreement;
    Preference spAppInfo;
    Preference spUpdatePwd;
    Preference spLoginout;

    boolean isLookOtherUse;
    boolean isBatteryWhiteList;
    boolean isBackGroudRun;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_main_preference);
        initPreference();
    }

    private void initPreference() {
        spSwitchHideApp = (SwitchPreferenceCompat) findPreference(getString(R.string.app_sp_hide_cant_open_app));
        spLookOtherUse = findPreference(getString(R.string.app_sp_permission_other_use));
        spBatteryWhiteList = findPreference(getString(R.string.app_sp_battery_white_list));
        spBackGroudRun = findPreference(getString(R.string.app_sp_backgroud_run));
        spListCustomOrder = (MultiSelectListPreference) findPreference(getString(R.string.app_sp_custom_order));
        spSwitchAutoUpdate = (SwitchPreferenceCompat) findPreference(getString(R.string.app_sp_auto_check_update));
        spUpdate = findPreference(getString(R.string.app_sp_check_update));
        spAbout = findPreference(getString(R.string.app_sp_about));
        spUserAgreement = findPreference(getString(R.string.app_sp_user_agreenment));
        spAppInfo = findPreference(getString(R.string.app_sp_app_info));
        spUpdatePwd = findPreference(getString(R.string.app_sp_reset_pwd));
        spLoginout = findPreference(getString(R.string.app_sp_login_out));

        spLookOtherUse.setOnPreferenceClickListener(this);
        spBatteryWhiteList.setOnPreferenceClickListener(this);
        spBackGroudRun.setOnPreferenceClickListener(this);
        spUpdate.setOnPreferenceClickListener(this);
        spAbout.setOnPreferenceClickListener(this);
        spUserAgreement.setOnPreferenceClickListener(this);
        spAppInfo.setOnPreferenceClickListener(this);
        spUpdatePwd.setOnPreferenceClickListener(this);
        spLoginout.setOnPreferenceClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        initPermission();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        setCustomOrders();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initPermission() {
        isLookOtherUse = ApkModel.isUseGranted();
        isBatteryWhiteList = EasyKeepAlive.isWhiteList(mContext);
        spLookOtherUse.setTitle(isLookOtherUse?R.string.app_title_look_other_use_setted:R.string.app_title_look_other_use);
        spBatteryWhiteList.setTitle(isBatteryWhiteList?R.string.app_title_battery_white_list_setted:R.string.app_title_battery_white_list);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(spLookOtherUse.getKey())){
            if(!isLookOtherUse){
                ScanTopService.startSkanTopService(mContext,2);
            }
        }else if(preference.getKey().equals(spBatteryWhiteList.getKey())){
            if(!isBatteryWhiteList){
                EasyKeepAlive.requestSystemWhiteList(mContext);
            }
        }else if(preference.getKey().equals(spBackGroudRun.getKey())){
            showRequestBackRunDialog();
        }else if(preference.getKey().equals(spUpdate.getKey())){
            updateVersion();
        }else if(preference.getKey().equals(spAbout.getKey())){
            AboutActivity.actionStart(mContext);
        }else if(preference.getKey().equals(spUserAgreement.getKey())){
            UserAgreenmentActivity.actionStart(mContext);
        }else if(preference.getKey().equals(spAppInfo.getKey())){
            goAppInfo();
        }else if(preference.getKey().equals(spUpdatePwd.getKey())){
            ResetPwdActivity.actionStart(mContext);
        }else if(preference.getKey().equals(spLoginout.getKey())){
            loginout();
        }
        return false;
    }

    private void showRequestBackRunDialog() {
        new MaterialDialog.Builder(mContext).title(getString(R.string.app_important_more))
                .content(getString(R.string.app_request_back_run_dialog_content))
                .negativeText(R.string.app_cancle)
                .positiveText(getString(R.string.app_go_set))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EasyKeepAlive.reqeustFactoryWhiteList();
                    }
                }).show();
    }

    /**
     * 设置排序方式
     */
    private void setCustomOrders() {
        if(ApkModel.isUseGranted()){
            spListCustomOrder.setEntries(OrderConfig.ordersAll);
            spListCustomOrder.setEntryValues(OrderConfig.orderKeyAll);
        }else {
            spListCustomOrder.setEntries(OrderConfig.ordersWithoutUseTime);
            spListCustomOrder.setEntryValues(OrderConfig.orderKeyWithoutUseTime);
        }
    }

    /**
     * 手动检查更新
     */
    private void updateVersion() {
//        BmobUpdateAgent.update(mContext);
//        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
//            @Override
//            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//                // TODO Auto-generated method stub
//                if (updateStatus == UpdateStatus.Yes) {//版本有更新
////                    ToastUtil.showShortToast(mContext,"ceshi");
//                }else if(updateStatus == UpdateStatus.No){
//                    ToastUtil.showShortToast(mContext, getString(R.string.app_version_neednot_update));
//                }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
//                    ToastUtil.showShortToast(mContext, getString(R.string.app_version_emptyfield));
//                }else if(updateStatus==UpdateStatus.IGNORED){
//                    ToastUtil.showShortToast(mContext, getString(R.string.app_viersion_ignored));
//                }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
//                    ToastUtil.showShortToast(mContext, getString(R.string.app_version_error_sizeformat));
//                }else if(updateStatus==UpdateStatus.TimeOut){
//                    ToastUtil.showShortToast(mContext, getString(R.string.app_version_timeout));
//                }
//            }
//        });
        CheckUpdateHelper updateHelper = new CheckUpdateHelper(mContext);
        updateHelper.setCheckUpdateListner(new CheckUpdateListner() {
            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mContext,fail);
            }

            @Override
            public void onSuccess(String success) {
                ToastUtil.showShortToast(mContext,success);
            }
        });
        updateHelper.checkUpdate(false);
    }

    /**
     * 喵小仙详情
     */
    private void goAppInfo() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", ApkModel.getPackageName(mContext), null));
        }
        mContext.startActivity(localIntent);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(spSwitchHideApp.getKey())){

        }else if(key.equals(spSwitchAutoUpdate.getKey())){

        }else if(key.equals(spListCustomOrder.getKey())){

        }
    }

    /**
     * 退出登录
     */
    private void loginout() {
        if(UserHelper.getCurrentUser() == null){
            LoginInterceptorCallBack.interruptToLogin(mContext);
            return;
        }
        new MaterialDialog.Builder(mContext).content(getString(R.string.app_confirm_login_out))
                .negativeText(R.string.app_cancle).positiveText(R.string.app_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BmobUser.logOut();
                        ToastUtil.showShortToast(mContext,getString(R.string.app_login_out_sucess));
                        getActivity().setResult(MainActivity.REQUEST_LOGIN_OUT);
                    }
                }).show();
    }
}
