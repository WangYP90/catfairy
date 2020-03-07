package com.tj24.wanandroid.module.mine.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.module.square.SquareRequest;
import com.tj24.wanandroid.module.web.WebViewActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareArticleActivity extends BaseWanAndroidActivity {

    @BindView(R2.id.et_title)
    TextInputEditText etTitle;
    @BindView(R2.id.inputLayout_title)
    TextInputLayout inputLayoutTitle;
    @BindView(R2.id.et_link)
    TextInputEditText etLink;
    @BindView(R2.id.inputLayout_link)
    TextInputLayout inputLayoutLink;
    @BindView(R2.id.tv_test)
    TextView tvTest;
    @BindView(R2.id.tv_tip)
    TextView tvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_share_article;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            share();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShareArticleActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R2.id.tv_test)
    public void onClick() {
        if(TextUtils.isEmpty(etLink.getText().toString())){
            showShortToast("文章地址不能为空");
            return;
        }
        WebViewActivity.actionStart(this,etTitle.getText().toString(),etLink.getText().toString());
    }

    private void share() {
        String title = etTitle.getText().toString();
        String link = etLink.getText().toString();

        SquareRequest.ShareArticle(title, link, new WanAndroidCallBack() {
            @Override
            public void onSucces(Object o) {
                showShortToast("分享成功");
                finish();
            }

            @Override
            public void onFail(String fail) {
                showShortToast(fail);
            }
        });
    }
}
