package com.tj24.appmanager.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.tj24.appmanager.R;
import com.tj24.appmanager.daohelper.SuggestionDaoHelper;
import com.tj24.base.utils.UserHelper;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.base.ui.PermissionListener;
import com.tj24.base.bean.appmanager.login.Suggestion;
import com.tj24.base.utils.StringUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class HelpSuggestActivity extends BaseActivity implements View.OnClickListener {

    Button btnCall;
    Button btnQQ;
    Button btnCommit;
    TextInputEditText etConcatInfo;
    TextInputEditText etSuggestContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        btnCall = findViewById(R.id.btn_call);
        btnQQ = findViewById(R.id.btn_qq);
        btnCommit = findViewById(R.id.btn_submint);
        etConcatInfo = findViewById(R.id.et_concat_info);
        etSuggestContent = findViewById(R.id.et_suggest_content);
        btnCall.setOnClickListener(this);
        btnQQ.setOnClickListener(this);
        btnCommit.setOnClickListener(this);

        btnCall.setText(getString(R.string.app_click_to_phone,getString(R.string.app_custom_service_phone)));
        btnQQ.setText(getString(R.string.app_click_copy_qq,getString(R.string.app_custom_service_qq)));
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_help_suggest;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_call){
            callCustomService();
        }else if(v.getId() == R.id.btn_qq){
            copyQQ();
        }else if(v.getId() == R.id.btn_submint){
            submintSuggestion();
        }
    }

    /**
     * 提交建议反馈
     */
    private void submintSuggestion() {
        String concatInfo = etConcatInfo.getText().toString();
        String suggestContent = etSuggestContent.getText().toString();

        if(TextUtils.isEmpty(suggestContent)){
            showShortToast(getString(R.string.app_suggestion_cant_empy));
            return;
        }
        // 5分钟内不能再次提交
        if(System.currentTimeMillis() - SuggestionDaoHelper.getInstance().queryLastCreatTime()<5*60*1000){
            showShortToast(getString(R.string.app_cant_submit_frequently));
            return;
        }
        showProgressDialog(getString(R.string.app_uploading),"");
        Suggestion suggestion = new Suggestion();
        suggestion.setId(StringUtil.getUuid());
        if(UserHelper.getCurrentUser()!=null){
            suggestion.setSenderId(UserHelper.getCurrentUser().getObjectId());
            suggestion.setSenderName(UserHelper.getCurrentUser().getUsername());
        }
        suggestion.setConcatInfo(concatInfo);
        suggestion.setContent(suggestContent);
        suggestion.setCreatTime(System.currentTimeMillis());
        suggestion.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    SuggestionDaoHelper.getInstance().insertObj(suggestion);
                    showShortToast(getString(R.string.app_upload_success));
                    hideProgressDialog();
                }
            }
        });
    }

    /**
     * 拨打客服电话，需要先判断是否有拨打电话权限
     */
    private void callCustomService() {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
            handlePermissions(permissions, new PermissionListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+getString(R.string.app_custom_service_phone)));
                    startActivity(intent);
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                    callCustomService();
                }
            });
    }

    /**
     * 复制QQ号
     */
    private void copyQQ() {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", getString(R.string.app_custom_service_qq));
        clipboard.setPrimaryClip(clip);
        showShortToast(getString(R.string.app_copy_success));
    }

    /**
     * 启动
     * @param context
     */
    public static void actionStart(Context context){
        Intent i = new Intent(context,HelpSuggestActivity.class);
        context.startActivity(i);
    }
}
