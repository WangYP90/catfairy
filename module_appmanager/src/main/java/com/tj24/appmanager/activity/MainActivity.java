package com.tj24.appmanager.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.adapter.AppsVpAdater;
import com.tj24.appmanager.bean.event.ApkChangeEvent;
import com.tj24.appmanager.bean.event.LaucherEvent;
import com.tj24.appmanager.common.AppConst;
import com.tj24.appmanager.common.CatFairyHeader.CatFairyHeader;
import com.tj24.appmanager.common.CatFairyHeader.CatFairyHeaderLayout;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.common.keepAlive.EasyKeepAlive;
import com.tj24.appmanager.common.update.CheckUpdateHelper;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.appmanager.model.BusinessModel;
import com.tj24.appmanager.model.CloudModel;
import com.tj24.appmanager.model.OrderModel;
import com.tj24.appmanager.receiver.ApkChangeReceiver;
import com.tj24.appmanager.service.ScanTopService;
import com.tj24.appmanager.view.NavigationViewHelper;
import com.tj24.appmanager.view.dialog.OrderDialog;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.base.ui.PermissionListener;
import com.tj24.base.base.ui.widget.NoScrollViewPager;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.Sputil;
import com.tj24.base.utils.ToastUtil;
import com.tj24.base.utils.UserHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.ErrorCode;
import cn.bmob.v3.listener.FetchUserInfoListener;

public class MainActivity extends BaseActivity implements  View.OnClickListener {

    @BindView(R2.id.tab)
    TabLayout tab;
    @BindView(R2.id.iv_addItem)
    AppCompatImageView ivAddItem;
    @BindView(R2.id.appBar)
    AppBarLayout appBar;
    @BindView(R2.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R2.id.tv_shadow_order)
    TextView tvShadowOrder;
    @BindView(R2.id.catfairy_refresh)
    CatFairyHeaderLayout catFairyRefresh;
    @BindView(R2.id.fbt_compose)
    FloatingActionButton fbtCompose;
    @BindView(R2.id.nav_view)
    NavigationView navView;
    @BindView(R2.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R2.id.trans_view)
    public View transView;

    private NavigationViewHelper navigationViewHelper;

    private CatFairyHeader catFairyHeader;
    //是否正在编辑
    public boolean isEditing = false;

    //已选择数量
    int selectedNum = 0;
    //上次点击back的时间
    double lastClickBackTime = 0;
    //fragmentPageAdapter
    AppsVpAdater vpAdater;

    MenuItem menuItemSelected;

    final List<AppClassfication> appClassfications = new ArrayList<>();
    private OrderModel orderModel;
    private BusinessModel businessModel;
    public static final int REQUEST_EDIT_USER = 101;
    public static final int REQUSET_APPCLASSIFICATION = 111;
    public static final int REQUSET_WHITE_LIST = 112;
    public static final int REQUEST_LOGIN_OUT= 113;
    public static final int MSG_REFRESH = 100;

    boolean isFirstInit = true; //第一次初始化
    private ApkChangeReceiver apkChangeReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REFRESH:
                    hideProgressDialog();
                    progressDialog = null;
                    setVpAdater(1);
                    //保存刷新次数加1
                    int refreshCount = Sputil.read(AppConst.SP_OPEN_COUNT,1);
                    Sputil.save(AppConst.SP_OPEN_COUNT,refreshCount+1);
                    break;
                default:
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        requestPermissions();
        regeistReceiver();
        fetchUserInfo();
        //记录刷新app数据的次数 不要在第一次刷新的时候做太多，影响体验
        int refreshCount = Sputil.read(AppConst.SP_OPEN_COUNT,1);
        if(refreshCount>1){
//            AliveService.startAliveService(this);
            if(!Sputil.read(AppConst.SP_ALARM_PERMISSION,false) || ApkModel.isUseGranted()){
                ScanTopService.startSkanTopService(this, 1);
            }
            autoUpdate();
            requestSystemWhitList();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        catFairyRefresh.finishRefresh();
        if(Sputil.read(AppConst.SP_OPEN_COUNT,1)>1){
            setVpAdater(isFirstInit?1:viewpager.getCurrentItem());
        }
        isFirstInit = false;

        testAutoUpload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apkChangeReceiver.unRegist(mActivity);
    }

    private void regeistReceiver() {
        apkChangeReceiver = new ApkChangeReceiver();
        apkChangeReceiver.regist(mActivity);
    }


    /**
     * 动态申请权限
     */
    private void requestPermissions() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        handlePermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                initData();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                requestPermissions();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestSystemWhitList() {
        if(!EasyKeepAlive.isWhiteList(this)){
            EasyKeepAlive.requestSystemWhiteList(this,REQUSET_WHITE_LIST);
        }
    }

    /**
     * 检测自动备份
     */
    private void testAutoUpload() {
        if(UserHelper.getCurrentUser()!=null){
            if(Sputil.read(getString(R.string.app_sp_auto_upload),true) && Sputil.read(AppConst.SP_LAST_UPDATE, 0L)>0){
                if(System.currentTimeMillis() - (Sputil.read(AppConst.SP_LAST_UPDATE, 0L)) >24*3600*1000){
                    new CloudModel(this).readyPush(true);
                    LogUtil.i(TAG,"自动备份开始！");
                }
            }
        }
    }

    /**
     * 自动更新
     */
    private void autoUpdate() {
        if (Sputil.read(getString(R.string.app_sp_auto_check_update), true)) {
//            BmobUpdateAgent.setUpdateOnlyWifi(false);
//            BmobUpdateAgent.update(this);
            new CheckUpdateHelper(this).checkUpdate(true);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<AppClassfication> classfications = businessModel.queryAllAppClassfications();
        if (ListUtil.isNullOrEmpty(classfications)) {
            businessModel.initDeafultData();
            businessModel.refreshApp(mHandler);
        }
    }


    /**
     * 设置viewpager的adapter
     */
    public void setVpAdater(int position) {
        appClassfications.clear();
        appClassfications.addAll(businessModel.queryAllAppClassfications());
        tab.setupWithViewPager(viewpager);
        if (vpAdater == null) {
            vpAdater = new AppsVpAdater(getSupportFragmentManager(), appClassfications);
            viewpager.setAdapter(vpAdater);
            viewpager.setCurrentItem(position);
        } else {
            vpAdater.notifyDataSetChanged();
        }
    }

    public void setupViews() {
        orderModel = new OrderModel(this);
        businessModel = new BusinessModel(this);
        navigationViewHelper = new NavigationViewHelper(mActivity,navView,drawerLayout);
        animateToolbar();
        drawerLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                navView.getViewTreeObserver().removeOnPreDrawListener(this);
                navigationViewHelper.loadUserInfo();
                return false;
            }
        });

        catFairyHeader = new CatFairyHeader(this);
        catFairyRefresh.setRefreshHeader(catFairyHeader);
        catFairyRefresh.setEnableLoadMore(false);
        catFairyRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //空实现  不然有个默认时间结束刷新
            }
        });
        initTransView();
    }

    //只放行点击事件，其他事件全部拦截
    private void initTransView() {
        transView = findViewById(R.id.trans_view);
        transView.setOnClickListener(this);
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

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        setTitle(isEditing ? getString(R.string.app_edit) : getString(R.string.app_app_name));
        toolbar.setNavigationIcon(isEditing ? R.drawable.md_nav_back : R.drawable.app_ico_footer_more);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_EXIST_EDITING));
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        transparentStatusBar();
    }


    /**
     * 使用缩放动画的方式将Toolbar标题显示出来。
     */
    private void animateToolbar() {
        if (toolbar == null) return;
        View t = toolbar.getChildAt(0);
        if (t != null && t instanceof TextView) {
            t.setAlpha(0f);
            t.setScaleX(0.8f);
            t.animate().alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900);
//                    .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(this));
        }
    }

    @OnClick({R2.id.iv_addItem, R2.id.fbt_compose})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_addItem) {
            startActivityForResult(new Intent(this, AddAppClassficationActivity.class), REQUSET_APPCLASSIFICATION);
        } else if (i == R.id.fbt_compose) {
        } else if (i == R.id.trans_view) {
            if (vpAdater.getCurrentFragment() != null && vpAdater.getCurrentFragment().isPopupShowing()) {
                vpAdater.getCurrentFragment().editPopup.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu_toobar_apps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 动态刷新菜单栏
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        toolbar.setTitle(isEditing ? getString(R.string.app_selected, selectedNum) : getString(R.string.app_app_name));
        toolbar.setNavigationIcon(isEditing ? R.drawable.md_nav_back : R.drawable.app_ic_person_outline_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_EXIST_EDITING));
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        menu.findItem(R.id.menu_search).setVisible(!isEditing);
        menu.findItem(R.id.menu_order).setVisible(!isEditing);
        menu.findItem(R.id.menu_refresh).setVisible(!isEditing);
        menu.findItem(R.id.menu_notice).setVisible(!isEditing);
        menu.findItem(R.id.menu_showtype).setVisible(!isEditing);
        menuItemSelected = menu.findItem(R.id.menu_selected);
        menuItemSelected.setVisible(isEditing);
        menuItemSelected.setTitle(getString(R.string.app_select_all));
        return super.onPrepareOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
        } else if (i == R.id.menu_search) {// Android 5.0版本启用transition动画会存在一些效果上的异常，因此这里只在Android 5.1以上启用此动画
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                        getString(R.string.app_transition_search_back)).toBundle();
                SearchActivity.actionStartWithOptions(this, options);
            } else {
                SearchActivity.actionStart(this);
            }
            // 当进入搜索界面键盘弹出时，composeFab会随着键盘往上偏移。暂时没查到原因，使用隐藏的方式先进行规避
            fbtCompose.setVisibility(View.GONE);
        } else if (i == R.id.menu_order) {
            OrderDialog dialogOrder = new OrderDialog(mActivity, appClassfications.get(viewpager.getCurrentItem()), tvShadowOrder);
            dialogOrder.show();
        } else if (i == R.id.menu_refresh) {
            businessModel.refreshApp(mHandler);
        } else if (i == R.id.menu_showtype) {
            if (orderModel.getLayoutType() == OrderConfig.LAYOUT_LINEAR) {
                orderModel.setLayoutType(OrderConfig.LAYOUT_Gride);
            } else if (orderModel.getLayoutType() == OrderConfig.LAYOUT_Gride) {
                orderModel.setLayoutType(OrderConfig.LAYOUT_LINEAR);
            }
            EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_CHANGE_ORDERTYPE));
        } else if (i == R.id.menu_notice) {
            startActivity(new Intent(this, MesageActivity.class));
        } else if (i == R.id.menu_selected) {
            EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_ALL_SELECTED));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //如果fragment有edipopup弹出 则先隐藏
        if (vpAdater.getCurrentFragment() != null && vpAdater.getCurrentFragment().isPopupShowing()) {
            vpAdater.getCurrentFragment().editPopup.dismiss();
        } else if (isEditing) {   //   //如果处于编辑状态则需要先退出编辑状态
            EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_EXIST_EDITING));
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) { //如果drawerlayout未关闭，则需要先关闭
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - lastClickBackTime < 1500) {
                ToastUtil.cancle();
                super.onBackPressed();
            } else {
                showShortToast(getString(R.string.app_click_more_to_exist));
                lastClickBackTime = System.currentTimeMillis();
            }
        }
    }


    /**
     * 接受到安装卸载或更新的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApkChanged(ApkChangeEvent event) {
        switch (event.getAction()) {
            case ApkChangeEvent.ACTION_ADD:

            case ApkChangeEvent.ACTION_DEL:

            case ApkChangeEvent.ACTION_REPLACE:
                setVpAdater(viewpager.getCurrentItem());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUI(LaucherEvent laucherEvent) {
        View mAppBarChildAt = appBar.getChildAt(0);
        AppBarLayout.LayoutParams mAppBarParams = (AppBarLayout.LayoutParams) mAppBarChildAt.getLayoutParams();
        int editPosition = 1;
        switch (laucherEvent.getEventCode()) {
            case LaucherEvent.EVENT_EXIST_EDITING:
                mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
                mAppBarChildAt.setLayoutParams(mAppBarParams);

                invalidateOptionsMenu();
                viewpager.setNoScroll(false);
                tab.setVisibility(View.VISIBLE);
                ivAddItem.setVisibility(View.VISIBLE);
                setVpAdater(editPosition);
                isEditing = false;
                break;
            case LaucherEvent.EVENT_START_EDITING:
                editPosition = viewpager.getCurrentItem();
                mAppBarParams.setScrollFlags(0);
                viewpager.setNoScroll(true);
                invalidateOptionsMenu();
                tab.setVisibility(View.GONE);
                ivAddItem.setVisibility(View.GONE);
                isEditing = true;
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_EDIT_USER) {
            navigationViewHelper.loadUserInfo();
        }else if (requestCode == REQUEST_LOGIN_OUT) {
            navigationViewHelper.loadUserInfo();
        }else if(requestCode == REQUSET_WHITE_LIST){
            if(!EasyKeepAlive.isWhiteList(this)){
                EasyKeepAlive.requestSystemWhiteList(this,REQUSET_WHITE_LIST);
            }else {
                EasyKeepAlive.reqeustFactoryWhiteList();
            }
        }
    }

    /**
     * 编辑状态下对app进行了选择操作后
     *
     * @param appBeans
     */
    @SuppressLint("StringFormatMatches")
    public void onAppSelected(List<AppBean> appBeans) {
        toolbar.setTitle(isEditing ? getString(R.string.app_selected, appBeans.size()) : getString(R.string.app_app_name));
    }

    /**
     * 编辑状态下 点击了全选
     *
     * @param isAllSelected
     * @param edittingNum
     */
    public void onAppAllSelected(boolean isAllSelected, int edittingNum) {
        menuItemSelected.setTitle(isAllSelected ? getString(R.string.app_select_none) : getString(R.string.app_select_all));
        toolbar.setTitle(isEditing ? getString(R.string.app_selected, edittingNum) : getString(R.string.app_app_name));
    }

    float clickX;
    float clickY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickX = event.getX();
                clickY = event.getY();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 给fragment提供获取点击屏幕坐标的方法
     *
     * @return
     */
    public float[] getClickPosition() {
        return new float[]{clickX, clickY};
    }

    public static void startMain(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    /**
     * 更新本地用户信息
     * 注意：需要先登录，否则会报9024错误
     *
     * @see ErrorCode#E9024S
     */
    private void fetchUserInfo() {
        if (UserHelper.getCurrentUser() == null) {
            return;
        }
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                } else {

                }
            }
        });
    }
}
