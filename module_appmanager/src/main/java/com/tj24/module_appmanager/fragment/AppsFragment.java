package com.tj24.module_appmanager.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.library_base.base.ui.BaseFragment;
import com.tj24.library_base.utils.ToastUtil;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.activity.MainActivity;
import com.tj24.module_appmanager.adapter.RcAppGrideAdapter;
import com.tj24.module_appmanager.adapter.RcAppLinearAdapter;
import com.tj24.module_appmanager.bean.AppBean;
import com.tj24.module_appmanager.bean.AppClassfication;
import com.tj24.module_appmanager.bean.event.LaucherEvent;
import com.tj24.module_appmanager.common.OrderConfig;
import com.tj24.module_appmanager.greendao.daohelper.AppBeanDaoHelper;
import com.tj24.module_appmanager.model.ApkModel;
import com.tj24.module_appmanager.model.BaseAppsManagerModel;
import com.tj24.module_appmanager.model.OrderModel;
import com.tj24.module_appmanager.receiver.ApkChangeReceiver;
import com.tj24.module_appmanager.util.appsSort.AppSortManager;
import com.tj24.module_appmanager.view.AppFooterView;
import com.tj24.module_appmanager.view.SideBar;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AppsFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemLongClickListener,SideBar.OnTouchingLetterChangedListener, ApkChangeReceiver.ApkChangerListner {
    @BindView(R.id.rc_apps)
    RecyclerView rcApps;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    @BindView(R.id.tv_sidebar)
    TextView tvSidebar;
    @BindView(R.id.tv_move)
    TextView tvMove;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_levle)
    TextView tvLevle;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.apps_footer)
    AppFooterView footerView;
    //是否正在编辑
    private boolean isEditing;
    //是否全选 默认为false
    boolean isAllSelected = false;

    //传入的appbean
    private final List<AppBean> appBeans = new ArrayList<>();
    //对应的type
    private AppClassfication appClassfication;
    //长按后 选择的item
    private final List<AppBean> editingApps = new ArrayList<>();

    private RcAppLinearAdapter mLinearAdapter;
    private RcAppGrideAdapter mGrideAdapter;
    private LinearLayoutManager mLinearManager;
    private GridLayoutManager mGrideManager;

    private OrderModel orderModel;

    private ApkChangeReceiver apkChangeReceiver;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.fragment_apps;
    }

    @Override
    public void init(View view) {
        initData();
        initSideBar();
        initRecyclerView();
        regeistReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apkChangeReceiver.unregister(mActivity);
    }

    private void regeistReceiver() {
        apkChangeReceiver = new ApkChangeReceiver();
        apkChangeReceiver.register(mActivity);
        apkChangeReceiver.setApkChangeListner(this);
    }

    private void initSideBar() {
        sideBar.setTextView(tvSidebar);
        sideBar.setOnTouchingLetterChangedListener(this);
        //不是按名称排序
        sideBar.setVisibility(appClassfication.getSortName()==OrderConfig.ORDER_APP_NAME?View.VISIBLE:View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        orderModel = new OrderModel(mActivity);
        appBeans.clear();
        appBeans.addAll((Collection<? extends AppBean>) getArguments().getSerializable(BaseAppsManagerModel.APP_BEANS));
        appClassfication = (AppClassfication) getArguments().getSerializable(BaseAppsManagerModel.APP_CLASSIFICATION);
        AppSortManager.sort(appBeans,appClassfication.getSortName());
    }

    /**
     * 初始化recycleView
     */
    private void initRecyclerView() {
        int scrollPosition = 0;
        if(orderModel.getLayoutType() == OrderConfig.LAYOUT_LINEAR){
            if(mLinearManager != null){
                scrollPosition = mLinearManager.findFirstVisibleItemPosition();
            }
            mLinearManager = new LinearLayoutManager(mActivity);
            mLinearManager.setStackFromEnd(false);
            rcApps.setLayoutManager(mLinearManager);
            mLinearAdapter = new RcAppLinearAdapter(R.layout.rc_apps_linear_item,appBeans,appClassfication,isEditing);
            mLinearManager.scrollToPosition(scrollPosition);
            rcApps.setAdapter(mLinearAdapter);
            mLinearAdapter.setOnItemChildClickListener(this);
            mLinearAdapter.setOnItemClickListener(this);
            mLinearAdapter.setOnItemLongClickListener(this);
        }else if(orderModel.getLayoutType() == OrderConfig.LAYOUT_Gride){
            if(mGrideManager != null){
                scrollPosition = mGrideManager.findFirstVisibleItemPosition();
            }
            mGrideManager = new GridLayoutManager(mActivity,4);
            rcApps.setLayoutManager(mGrideManager);
            mGrideAdapter = new RcAppGrideAdapter(R.layout.rc_apps_gride_item,appBeans,isEditing);
            mGrideManager.scrollToPosition(scrollPosition);
            mGrideAdapter.setOnItemClickListener(this);
            mGrideAdapter.setOnItemLongClickListener(this);
            rcApps.setAdapter(mGrideAdapter);
        }
    }


    public static AppsFragment newInstance(@NotNull List<AppBean> appBeans, AppClassfication appClassfication) {
        AppsFragment f = new AppsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseAppsManagerModel.APP_BEANS, (Serializable) appBeans);
        bundle.putSerializable(BaseAppsManagerModel.APP_CLASSIFICATION, appClassfication);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.tv_info:
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", appBeans.get(position).getPackageName(), null));
                }
                startActivity(localIntent);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        AppBean clickBean = appBeans.get(position);
        if(isEditing && clickBean!=null){
                if(clickBean.getIsSelected()){
                    clickBean.setIsSelected(false);
                    editingApps.remove(clickBean);
                }else {
                    clickBean.setIsSelected(true);
                    editingApps.add(clickBean);
                }
                notifyRecyclerView();
                ((MainActivity)mActivity).onAppSelected(editingApps);
                footerView.onEdittingAppChanged(editingApps,appClassfication);
        }else {
            Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(clickBean.getPackageName());
            if (i != null) {
                clickBean.setOpenNum(clickBean.getOpenNum()+1); //打开次数加1
                clickBean.setLastOpenTime(System.currentTimeMillis());
                AppBeanDaoHelper.getInstance().insertObj(clickBean);
                startActivity(i);
            }else {
                ToastUtil.showShortToast(mActivity,"此应用不支持打开");
            }
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        if(!isEditing){
            EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_START_EDITING));
        }
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUI(LaucherEvent laucherEvent){
        switch (laucherEvent.getEventCode()){
            case LaucherEvent.EVENT_EXIST_EDITING:
                isEditing = false;
                for(AppBean appBean : editingApps){
                    appBean.setIsSelected(false);
                }
                editingApps.clear();
                footerView.setVisibility(View.GONE);
                ((MainActivity)mActivity).onAppSelected(editingApps);
                break;
            case LaucherEvent.EVENT_START_EDITING:
                isEditing = true;
                initRecyclerView();
                footerView.setVisibility(View.VISIBLE);
                footerView.onEdittingAppChanged(editingApps,appClassfication);
                break;
            case LaucherEvent.EVENT_CHANGE_ORDERTYPE:
                initRecyclerView();
                break;
            case LaucherEvent.EVENT_CHANGE_SORT:
                String appSortName = (String) laucherEvent.getValue();
                appClassfication.setSortName(appSortName);
                AppSortManager.sort(appBeans,appSortName);
                sideBar.setVisibility(appClassfication.getSortName().equals(OrderConfig.ORDER_APP_NAME)?View.VISIBLE:View.GONE);
                notifyRecyclerView();
                break;
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        int showType = orderModel.getLayoutType();
        switch (showType){
            case OrderConfig.LAYOUT_LINEAR:
                position = mLinearAdapter.getPositionForSection(s.charAt(0));
                if(position!=-1){
                    mLinearManager.scrollToPositionWithOffset(position, 0);
                }
                break;
            case OrderConfig.LAYOUT_Gride:
                position = mGrideAdapter.getPositionForSection(s.charAt(0));
                if(position!=-1){
                    mGrideManager.scrollToPositionWithOffset(position, 0);
                }
                break;
        }
    }

    @Override
    public void onAddApk(String packageName) {
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(packageName, 0);
            AppBean appBean = ApkModel.conversToAppInfo(packageInfo,mActivity.getPackageManager());
            AppBeanDaoHelper.getInstance().insertObj(appBean);
            if(appBean.getType().contains(appClassfication.getId())){
                appBeans.add(appBean);
                AppSortManager.sort(appBeans,appClassfication.getSortName());
                notifyRecyclerView();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRemoveApk(String packageName) {
       AppBean appBean = AppBeanDaoHelper.getInstance().queryObjById(packageName);
        AppBeanDaoHelper.getInstance().deleteObj(appBean);
       if(appBean != null && appBeans.contains(appBean)){
           appBeans.remove(appBean);
           notifyRecyclerView();
       }
    }

    @Override
    public void onReplacedApk(String packageName) {
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(packageName, 0);
            AppBean appBean = ApkModel.conversToAppInfo(packageInfo,mActivity.getPackageManager());
            AppBeanDaoHelper.getInstance().insertObj(appBean);
            if(appBean.getType().contains(appClassfication.getId())){
                Iterator it = appBeans.iterator();
                while (it.hasNext()){
                    AppBean next = (AppBean) it.next();
                    if(next.getPackageName().equals(packageName)){
                        it.remove();
                        break;
                    }
                }
                appBeans.add(appBean);
                AppSortManager.sort(appBeans,appClassfication.getSortName());
                notifyRecyclerView();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void notifyRecyclerView(){
        if(orderModel.getLayoutType() == OrderConfig.LAYOUT_LINEAR){
            mLinearAdapter.notifyDataSetChanged();
        }else {
            mGrideAdapter.notifyDataSetChanged();
        }
    }
}
