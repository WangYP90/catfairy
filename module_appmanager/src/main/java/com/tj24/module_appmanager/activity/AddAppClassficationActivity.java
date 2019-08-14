package com.tj24.module_appmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tj24.library_base.base.ui.BaseActivity;
import com.tj24.library_base.common.recyclerview.FlowLayoutManager;
import com.tj24.library_base.common.recyclerview.itemTouchhelper.DragHelper;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.adapter.RcFlowAdapter;
import com.tj24.module_appmanager.bean.AppClassfication;
import com.tj24.module_appmanager.greendao.daohelper.AppClassificationDaoHelper;
import com.tj24.module_appmanager.model.AppClassificationEditModel;
import com.tj24.module_appmanager.model.BusinessModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddAppClassficationActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,AppClassificationEditModel.OnEditListner {

    @BindView(R.id.rc_appclassfication)
    RecyclerView rcAppclassfication;
    @BindView(R.id.fab_add)
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
        appClassfications = AppClassificationDaoHelper.getInstance().queryAll();
        layoutManager = new FlowLayoutManager();
        rcAppclassfication.setLayoutManager(layoutManager);
        initAdapter();
    }

    private void initAdapter() {
        if(flowAdapter == null){
            flowAdapter = new RcFlowAdapter(R.layout.rc_flow_appclassification,appClassfications);
            rcAppclassfication.setAdapter(flowAdapter);
            ItemTouchHelper.Callback callback = new DragHelper(flowAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(rcAppclassfication);
            flowAdapter.setOnItemClickListener(this);
        }else {
            flowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_app_classfication;
    }

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        toolbar.setTitle("编辑文件夹");
        toolbar.setNavigationIcon(R.drawable.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.fab_add)
    public void onClick() {
        mModel.showNewClassificationDialog();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(position>=2){
            mModel.showSelectOperationDialog(appClassfications.get(position),position);
        }
    }

    @Override
    public void onADDClassification(AppClassfication classfication) {
        appClassfications.add(classfication);
        flowAdapter.notifyItemInserted(appClassfications.size());
        Intent intent = new Intent();
        intent.putExtra(BusinessModel.APPCLASSIFICATIONS, (Serializable) appClassfications);
        setResult(RESULT_OK,intent);
    }

    @Override
    public void onUpdateClassification(AppClassfication classfication,int position) {
        appClassfications.set(position,classfication);
        flowAdapter.notifyItemChanged(position);
        Intent intent = new Intent();
        intent.putExtra(BusinessModel.APPCLASSIFICATIONS, (Serializable) appClassfications);
        setResult(RESULT_OK,intent);
    }

    @Override
    public void onDeletClassification(AppClassfication classfication,int position) {
        appClassfications.remove(classfication);
        flowAdapter.notifyItemRemoved(position);
        Intent intent = new Intent();
        intent.putExtra(BusinessModel.APPCLASSIFICATIONS, (Serializable) appClassfications);
        setResult(RESULT_OK,intent);
    }
}
