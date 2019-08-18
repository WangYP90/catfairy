package com.tj24.module_appmanager.activity;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.tj24.library_base.base.ui.BaseActivity;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.adapter.MsgAPkAdapter;
import com.tj24.module_appmanager.bean.MsgApk;
import com.tj24.module_appmanager.bean.MsgApkData;
import com.tj24.module_appmanager.greendao.daohelper.MsgApkDaoHelper;

import java.util.ArrayList;
import java.util.List;

public class MesageActivity extends BaseActivity {

    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;

    private List<MsgApkData> msgApkDatas = new ArrayList<>();
    private MsgAPkAdapter msgAPkAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        linearLayoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(linearLayoutManager);
        msgAPkAdapter = new MsgAPkAdapter(R.layout.rv_msg_item,R.layout.rv_msg_header,msgApkDatas);
        rvMsg.setAdapter(msgAPkAdapter);
    }


    private void initData() {
        List<MsgApk> msgs = MsgApkDaoHelper.getInstance().queryAll();

        for(MsgApk msg : msgs){
            msgApkDatas.add(new MsgApkData(msg));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mesage;
    }

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        toolbar.setTitle("消息通知");
        toolbar.setNavigationIcon(R.drawable.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
