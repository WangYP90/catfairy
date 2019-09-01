package com.tj24.appmanager.fragment;

import android.annotation.TargetApi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.appmanager.adapter.RcAppGrideAdapter;
import com.tj24.appmanager.adapter.RcAppLinearAdapter;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.appmanager.bean.event.ApkChangeEvent;
import com.tj24.appmanager.bean.event.LaucherEvent;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.appmanager.model.BaseAppsManagerModel;
import com.tj24.appmanager.model.OrderModel;
import com.tj24.appmanager.util.ViewUtils;
import com.tj24.appmanager.util.appsSort.AppSortManager;
import com.tj24.appmanager.view.AppFooterView;
import com.tj24.appmanager.view.SideBar;
import com.tj24.appmanager.view.dialog.AppEditPopup;
import com.tj24.base.base.ui.BaseFragment;
import com.tj24.base.utils.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AppsFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemLongClickListener, SideBar.OnTouchingLetterChangedListener, View.OnClickListener {

    private RecyclerView rcApps;
    private View transView;
    private SideBar sideBar;
    private TextView tvSidebar;
    private AppFooterView footerView;

    //是否正在编辑
    private boolean isEditing;

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
    @Override
    public int getCreateViewLayoutId() {
        return R.layout.fragment_apps;
    }

    @Override
    public void init(View view) {
        initView();
        initData();
        initSideBar();
        initRecyclerView();
    }

    private void initView() {
        rcApps = rootView.findViewById(R.id.rc_apps);
        transView = rootView.findViewById(R.id.trans_view);
        sideBar = rootView.findViewById(R.id.sideBar);
        tvSidebar = rootView.findViewById(R.id.tv_sidebar);
        footerView = rootView.findViewById(R.id.apps_footer);

        transView.setOnClickListener(this);
        //只放行点击事件，其他事件全部拦截
        transView.setOnTouchListener(new View.OnTouchListener() {
            float x = 0;
            float y = 0;
            long time = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        time = System.currentTimeMillis();
                        return false;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(x - event.getX()) < 2 && Math.abs(y - event.getY()) < 2 && System.currentTimeMillis() - time < 300) {
                            return false;
                        } else {
                            return true;
                        }
                }
                return false;
            }
        });
    }


    private void initSideBar() {
        sideBar.setTextView(tvSidebar);
        sideBar.setOnTouchingLetterChangedListener(this);
        //不是按名称排序
        sideBar.setVisibility(appClassfication.getSortName() == OrderConfig.ORDER_APP_NAME ? View.VISIBLE : View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        orderModel = new OrderModel(mActivity);
        appBeans.clear();
        appBeans.addAll((Collection<? extends AppBean>) getArguments().getSerializable(BaseAppsManagerModel.APP_BEANS));
        appClassfication = (AppClassfication) getArguments().getSerializable(BaseAppsManagerModel.APP_CLASSIFICATION);
        AppSortManager.sort(appBeans, appClassfication.getSortName());
    }

    /**
     * 初始化recycleView
     */
    private void initRecyclerView() {
        int scrollPosition = 0;
        if (orderModel.getLayoutType() == OrderConfig.LAYOUT_LINEAR) {
            if (mLinearManager != null) {
                scrollPosition = mLinearManager.findFirstVisibleItemPosition();
            }
            mLinearManager = new LinearLayoutManager(mActivity);
            mLinearManager.setStackFromEnd(false);
            rcApps.setLayoutManager(mLinearManager);
            mLinearAdapter = new RcAppLinearAdapter(R.layout.rc_apps_linear_item, appBeans, appClassfication, isEditing);
            mLinearManager.scrollToPosition(scrollPosition);
            rcApps.setAdapter(mLinearAdapter);
            mLinearAdapter.setOnItemChildClickListener(this);
            mLinearAdapter.setOnItemClickListener(this);
            mLinearAdapter.setOnItemLongClickListener(this);
        } else if (orderModel.getLayoutType() == OrderConfig.LAYOUT_Gride) {
            if (mGrideManager != null) {
                scrollPosition = mGrideManager.findFirstVisibleItemPosition();
            }
            mGrideManager = new GridLayoutManager(mActivity, 4);
            rcApps.setLayoutManager(mGrideManager);
            mGrideAdapter = new RcAppGrideAdapter(R.layout.rc_apps_gride_item, appBeans, isEditing);
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
        if (view.getId() == R.id.tv_open) {
            ApkModel.openApp(mActivity, appBeans.get(position));
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        AppBean clickBean = appBeans.get(position);
        if (isEditing && clickBean != null) {
            if (clickBean.getIsSelected()) {
                clickBean.setIsSelected(false);
                editingApps.remove(clickBean);
            } else {
                clickBean.setIsSelected(true);
                editingApps.add(clickBean);
            }
            notifyRecyclerView();
            ((MainActivity) mActivity).onAppSelected(editingApps);
            footerView.onEdittingAppChanged(editingApps, appClassfication);
        } else if (orderModel.getLayoutType() == OrderConfig.LAYOUT_Gride) {
            ApkModel.openApp(mActivity, clickBean);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        if (!isEditing && orderModel.getLayoutType() == OrderConfig.LAYOUT_Gride) {
            EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_START_EDITING));
        }
        if (orderModel.getLayoutType() == OrderConfig.LAYOUT_LINEAR) {
            showEditPopup(view, appBeans.get(position));
        }
        return true;
    }

    public AppEditPopup editPopup;

    /**
     * 弹出popup
     *
     * @param view
     * @param appBean
     */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showEditPopup(View view, AppBean appBean) {
        if (appBean == null) {
            return;
        }
        editPopup = new AppEditPopup(mActivity, appBean, appClassfication);
        View contentView = editPopup.getContentView();
        //需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(ViewUtils.makeDropDownMeasureSpec(editPopup.getWidth()),
                ViewUtils.makeDropDownMeasureSpec(editPopup.getHeight()));
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight = contentView.getMeasuredHeight();
        //itemView 在屏幕中的位置
        int[] itemLocation = new int[2];
        view.getLocationOnScreen(itemLocation);
        //recyclerview 在屏幕中的位置
        int[] rvLocation = new int[2];
        rcApps.getLocationOnScreen(rvLocation);

        int clickX = (int) ((MainActivity) mActivity).getClickPosition()[0];
        int clickY = (int) ((MainActivity) mActivity).getClickPosition()[1];
        int overlapValueX = (int) ScreenUtil.dip2px(mActivity, 20);
        int overlapValueY = (int) ScreenUtil.dip2px(mActivity, 14);

        int offX = clickX;
        int offY = 0;
        //计算Y方向偏移量
        if (itemLocation[1] + view.getHeight() + popupHeight > rvLocation[1] + rcApps.getHeight()) {
            offY = -(view.getHeight() + popupHeight) + overlapValueY;
        } else {
            offY = offY - overlapValueY;
        }

        //计算X方向偏移量 保证左右有margin
        if (clickX < overlapValueX) {
            offX = overlapValueX;
        } else if (clickX + overlapValueX > ScreenUtil.getInstance(mActivity).getScreenWidth()) {
            offX = ScreenUtil.getInstance(mActivity).getScreenWidth() - overlapValueX - popupWidth;
        } else if (clickX + popupWidth > view.getWidth()) {
            offX = clickX - popupWidth;
        }


        PopupWindowCompat.showAsDropDown(editPopup, view, offX, offY, Gravity.START);
        transView.setVisibility(View.VISIBLE);
        editPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                transView.setVisibility(View.GONE);
                view.setBackgroundColor(mActivity.getColor(R.color.white_text));
            }
        });
        view.setBackgroundColor(mActivity.getColor(R.color.item_pressed));
        view.setElevation(4F);
    }


    /**
     * editPopup是否弹出
     *
     * @return
     */
    public boolean isPopupShowing() {
        if (editPopup != null && editPopup.isShowing()) {
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUI(LaucherEvent laucherEvent) {
        switch (laucherEvent.getEventCode()) {
            case LaucherEvent.EVENT_EXIST_EDITING:
                isEditing = false;
                for (AppBean appBean : editingApps) {
                    appBean.setIsSelected(false);
                }
                editingApps.clear();
                footerView.setVisibility(View.GONE);
                ((MainActivity) mActivity).onAppSelected(editingApps);
                break;
            case LaucherEvent.EVENT_START_EDITING:
                isEditing = true;
                initRecyclerView();
                footerView.setVisibility(View.VISIBLE);
                footerView.onEdittingAppChanged(editingApps, appClassfication);
                break;
            case LaucherEvent.EVENT_CHANGE_ORDERTYPE:
                initRecyclerView();
                break;
            case LaucherEvent.EVENT_CHANGE_SORT:
                String appSortName = (String) laucherEvent.getValue();
                appClassfication.setSortName(appSortName);
                AppSortManager.sort(appBeans, appSortName);
                sideBar.setVisibility(appClassfication.getSortName().equals(OrderConfig.ORDER_APP_NAME) ? View.VISIBLE : View.GONE);
                notifyRecyclerView();
                break;
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        int showType = orderModel.getLayoutType();
        switch (showType) {
            case OrderConfig.LAYOUT_LINEAR:
                position = mLinearAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mLinearManager.scrollToPositionWithOffset(position, 0);
                }
                break;
            case OrderConfig.LAYOUT_Gride:
                position = mGrideAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mGrideManager.scrollToPositionWithOffset(position, 0);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApkChanged(ApkChangeEvent event) {
        switch (event.getAction()) {
            case ApkChangeEvent.ACTION_ADD:
                onAddApk(event.getPackageName());
                break;
            case ApkChangeEvent.ACTION_DEL:
                onRemoveApk(event.getPackageName());
                break;
            case ApkChangeEvent.ACTION_REPLACE:
                onReplacedApk(event.getPackageName());
                break;
        }
    }

    /**
     * apk安装后
     *
     * @param packageName
     */
    public void onAddApk(String packageName) {
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(packageName, 0);
            AppBean appBean = ApkModel.conversToAppInfo(packageInfo, mActivity.getPackageManager());
            AppBeanDaoHelper.getInstance().insertObj(appBean);
            if (appBean.getType().contains(appClassfication.getId())) {
                appBeans.add(appBean);
                AppSortManager.sort(appBeans, appClassfication.getSortName());
                notifyRecyclerView();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * apk卸载后
     *
     * @param packageName
     */
    public void onRemoveApk(String packageName) {
        AppBean appBean = AppBeanDaoHelper.getInstance().queryObjById(packageName);
        AppBeanDaoHelper.getInstance().deleteObj(appBean);
        if (appBean != null && appBeans.contains(appBean)) {
            appBeans.remove(appBean);
            notifyRecyclerView();
        }
    }

    /**
     * apk更新后
     *
     * @param packageName
     */
    public void onReplacedApk(String packageName) {
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(packageName, 0);
            AppBean appBean = ApkModel.conversToAppInfo(packageInfo, mActivity.getPackageManager());
            AppBeanDaoHelper.getInstance().insertObj(appBean);
            if (appBean.getType().contains(appClassfication.getId())) {
                Iterator it = appBeans.iterator();
                while (it.hasNext()) {
                    AppBean next = (AppBean) it.next();
                    if (next.getPackageName().equals(packageName)) {
                        it.remove();
                        break;
                    }
                }
                appBeans.add(appBean);
                AppSortManager.sort(appBeans, appClassfication.getSortName());
                notifyRecyclerView();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新recyclerview
     */
    private void notifyRecyclerView() {
        if (orderModel.getLayoutType() == OrderConfig.LAYOUT_LINEAR) {
            mLinearAdapter.notifyDataSetChanged();
        } else {
            mGrideAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.trans_view){
            if (isPopupShowing()) {
                editPopup.dismiss();
            }
        }
    }
}
