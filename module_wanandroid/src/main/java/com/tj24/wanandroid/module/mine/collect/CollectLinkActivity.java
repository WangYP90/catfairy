package com.tj24.wanandroid.module.mine.collect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tj24.base.bean.wanandroid.NetUrlBean;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.event.EditNetUrlEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.module.web.WebViewActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CollectLinkActivity extends BaseWanAndroidActivity {
    public static final int TYPE_CREAT = 0 ;
    public static final int TYPE_EDIT = 1 ;
    public static final String EXT_TYPE = "startType";
    public static final String EXT_URL = "urlbean";

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

    int startType;
    NetUrlBean netUrlBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startType = getIntent().getIntExtra(EXT_TYPE,TYPE_CREAT);
        netUrlBean = (NetUrlBean) getIntent().getSerializableExtra(EXT_URL);
        if(netUrlBean !=null){
            etTitle.setText(netUrlBean.getName()!=null?netUrlBean.getName():"");
            etTitle.setSelection(etTitle.getText().length());
            etLink.setText(netUrlBean.getLink()!=null?netUrlBean.getLink():"");
            etLink.setSelection(etLink.getText().length());
        }

        if(startType == TYPE_CREAT){
            toolbar.setTitle("网址收藏");
        }else {
            toolbar.setTitle("网址编辑");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_collect_link;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_collect_link, menu);
        menu.findItem(R.id.menu_collect_link).setIcon(R.drawable.wanandroid_ic_done_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_collect_link) {
            if(startType == TYPE_CREAT){
                collectLink();
            }else {
                editLink();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void editLink() {
        String title = etTitle.getText().toString();
        String link = etLink.getText().toString();
        if(TextUtils.isEmpty(title)){
            showShortToast("标题不能为空");
            return;
        }
        if(TextUtils.isEmpty(link)){
            showShortToast("链接不能为空");
            return;
        }
        CollectRequest.updateCollectLink(netUrlBean.getId(), title, link, new WanAndroidCallBack<NetUrlBean>() {
            @Override
            public void onSucces(NetUrlBean o) {
                showShortToast("编辑成功");
                EditNetUrlEvent.postEditNeturlEvent(TYPE_EDIT,o);
                finish();
            }

            @Override
            public void onFail(String fail) {
                showShortToast("编辑失败");
            }
        });
    }

    private void collectLink() {
        String title = etTitle.getText().toString();
        String link = etLink.getText().toString();
        if(TextUtils.isEmpty(title)){
            showShortToast("标题不能为空");
            return;
        }
        if(TextUtils.isEmpty(link)){
            showShortToast("链接不能为空");
            return;
        }
        CollectRequest.collectLink(title, link, new WanAndroidCallBack<NetUrlBean>() {
            @Override
            public void onSucces(NetUrlBean netUrlBean) {
                showShortToast("收藏成功");
                EditNetUrlEvent.postEditNeturlEvent(TYPE_CREAT,netUrlBean);
                finish();
            }

            @Override
            public void onFail(String fail) {
                showShortToast("收藏失败");
            }
        });
    }

    @OnClick(R2.id.tv_test)
    public void onClick() {
        if(TextUtils.isEmpty(etLink.getText().toString())){
            showShortToast("链接不能为空");
            return;
        }
        WebViewActivity.actionStart(this,etTitle.getText().toString(),etLink.getText().toString());
    }

    public static void actionStart(Context context, int type, NetUrlBean netUrlBean) {
        Intent intent = new Intent(context, CollectLinkActivity.class);
        intent.putExtra(EXT_TYPE,type);
        intent.putExtra(EXT_URL,netUrlBean);
        context.startActivity(intent);
    }
}
