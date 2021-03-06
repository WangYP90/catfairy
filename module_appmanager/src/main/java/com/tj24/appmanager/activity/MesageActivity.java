package com.tj24.appmanager.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.adapter.MsgAPkAdapter;
import com.tj24.appmanager.bean.MsgApkData;
import com.tj24.appmanager.daohelper.MsgApkDaoHelper;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.MsgApk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MesageActivity extends BaseActivity {

    @BindView(R2.id.rv_msg)
    RecyclerView rvMsg;

    private List<MsgApkData> msgApkDatas = new ArrayList<>();
    private List<String> creatDays = new ArrayList<>();
    private MsgAPkAdapter msgAPkAdapter;
    private LinearLayoutManager linearLayoutManager;
    int pageNum = 0;
    public static final int PAGE_SIZE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msgApkDatas.addAll(filter(MsgApkDaoHelper.getInstance().queryByPage(pageNum)));
        pageNum++;
        linearLayoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(linearLayoutManager);
        msgAPkAdapter = new MsgAPkAdapter(R.layout.app_rv_msg_item, R.layout.app_rv_msg_header, msgApkDatas);
        rvMsg.setAdapter(msgAPkAdapter);
        msgAPkAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        List<MsgApk> msgs = MsgApkDaoHelper.getInstance().queryByPage(pageNum);
                        msgApkDatas.addAll(filter(MsgApkDaoHelper.getInstance().queryByPage(pageNum)));
                        msgAPkAdapter.notifyDataSetChanged();
                        if (msgs.size() < PAGE_SIZE) {
                            msgAPkAdapter.loadMoreEnd();
                        } else {
                            pageNum++;
                            msgAPkAdapter.loadMoreComplete();
                        }
                    }
                });
            }
        }, rvMsg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_mesage;
    }

    private List<MsgApkData> filter(List<MsgApk> msgs) {
        List<MsgApkData> apkDatas = new ArrayList<>();
        for (int i = 0; i < msgs.size(); i++) {
            if (!creatDays.contains(msgs.get(i).getCreatDay())) {
                apkDatas.add(new MsgApkData(true, msgs.get(i).getCreatDay()));
                creatDays.add(msgs.get(i).getCreatDay());
            }
            apkDatas.add(new MsgApkData(msgs.get(i)));
        }
        return apkDatas;
    }
}
