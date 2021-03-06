package com.tj24.appmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.bean.UserAgrement;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.utils.ListUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserAgreenmentActivity extends BaseActivity {

    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.tv_reload)
    TextView tvReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_user_agreenment;
    }


    private void initView() {
        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    @OnClick(R2.id.tv_reload)
    public void onClick() {
        initData();
    }

    private void initData() {
        showProgressDialog("", "");
        BmobQuery<UserAgrement> query = new BmobQuery<>();
        query.addWhereEqualTo("isUsing", true).findObjects(new FindListener<UserAgrement>() {
            @Override
            public void done(List<UserAgrement> list, BmobException e) {
                hideProgressDialog();
                if (e == null && !ListUtil.isNullOrEmpty(list)) {
                    UserAgrement userAgrement = list.get(0);
                    tvTitle.setText(userAgrement.getTitle());
                    tvContent.setText(userAgrement.getContent());
                    tvReload.setVisibility(View.GONE);
                } else {
                    showShortToast(getString(R.string.app_load_data_fail));
                    tvReload.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 启动
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent i = new Intent(context, UserAgreenmentActivity.class);
        context.startActivity(i);
    }
}
