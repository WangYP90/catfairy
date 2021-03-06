package com.tj24.appmanager.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.adapter.RcFlowAdapter;
import com.tj24.appmanager.daohelper.AppClassificationDaoHelper;
import com.tj24.appmanager.model.AppClassificationEditModel;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.base.common.recyclerview.FlowLayoutManager;
import com.tj24.base.common.recyclerview.itemTouchhelper.DragHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
public class AddAppClassficationActivity extends BaseActivity implements
        BaseQuickAdapter.OnItemClickListener, AppClassificationEditModel.OnEditListner {

    @BindView(R2.id.rc_appclassfication)
    RecyclerView rcAppclassfication;
    @BindView(R2.id.fab_add)
    FloatingActionButton fabAdd;

    private FlowLayoutManager layoutManager;
    private List<AppClassfication> appClassfications = new ArrayList<>();
    private RcFlowAdapter flowAdapter;
    private AppClassificationEditModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new AppClassificationEditModel(this);
        mModel.setOnEditListner(this);
        appClassfications = AppClassificationDaoHelper.getInstance().queryAllAndSort();
        layoutManager = new FlowLayoutManager();
        rcAppclassfication.setLayoutManager(layoutManager);
        initAdapter();
    }


    @Override
    public int getLayoutId() {
        return R.layout.app_activity_add_app_classfication;
    }

    private void initAdapter() {
        if (flowAdapter == null) {
            flowAdapter = new RcFlowAdapter(R.layout.app_rc_flow_appclassification, appClassfications);
            rcAppclassfication.setAdapter(flowAdapter);
            ItemTouchHelper.Callback callback = new DragHelper(flowAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(rcAppclassfication);
            flowAdapter.setOnItemClickListener(this);
            setResult(RESULT_OK);
        } else {
            flowAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R2.id.fab_add)
    public void onClick() {
        mModel.showNewClassificationDialog();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (position >= 2) {
            mModel.showSelectOperationDialog(appClassfications.get(position), position);
        }
    }

    @Override
    public void onADDClassification(AppClassfication classfication) {
        appClassfications.add(classfication);
        flowAdapter.notifyItemInserted(appClassfications.size());
    }

    @Override
    public void onUpdateClassification(AppClassfication classfication, int position) {
        appClassfications.set(position, classfication);
        flowAdapter.notifyItemChanged(position);
    }

    @Override
    public void onDeletClassification(AppClassfication classfication, int position) {
        appClassfications.remove(classfication);
        flowAdapter.notifyItemRemoved(position);
    }
}
