package com.tj24.appmanager.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.tj24.appmanager.R;
import com.tj24.appmanager.adapter.AppsVpAdater;
import com.tj24.appmanager.bean.event.LaucherEvent;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.login.UserHelper;
import com.tj24.appmanager.model.BusinessModel;
import com.tj24.appmanager.model.OrderModel;
import com.tj24.appmanager.receiver.ApkChangeReceiver;
import com.tj24.appmanager.view.NoScrollViewPager;
import com.tj24.appmanager.view.dialog.OrderDialog;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.base.ui.PermissionListener;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.utils.*;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TabLayout tab;
    private AppCompatImageView ivAddItem;
    private AppBarLayout appBarLayout;
    private NoScrollViewPager viewpager;
    private TextView tvShadowOrder;
    private FloatingActionButton fbtCompose;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private User currentUser;

    //是否正在编辑
    public boolean isEditing = false;

    //已选择数量
    int selectedNum = 0;
    //上次点击back的时间
    double lastClickBackTime = 0;
    //fragmentPageAdapter
    AppsVpAdater vpAdater;

    TextView tvNickName;
    ImageView ivAvatar;
    TextView tvDescription;
    ImageView ivEdit;

    MenuItem menuItemSelected;

    final List<AppClassfication> appClassfications = new ArrayList<>();
    private OrderModel orderModel;
    private BusinessModel businessModel;
    private static final int REQUEST_EDIT_USER = 101;
    private static final int REQUSET_APPCLASSIFICATION = 111;
    public static final int MSG_REFRESH = 100;

    int editPosition = 1;
    private ApkChangeReceiver apkChangeReceiver;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_REFRESH:
                    hideProgressDialog();
                    progressDialog = null;
                    setVpAdater(1);
                    break;
                default:break;
            }
        }
    };


    /**
     * glide 加载图片的监听
     */
    RequestListener<Object,GlideDrawable> navHeaderBgLoadListner = new RequestListener<Object, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable glideDrawable, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if (glideDrawable == null) {
                return false;
            }
            Bitmap bitmap = DrawableUtil.toBitmap(glideDrawable);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            if (bitmapWidth <= 0 || bitmapHeight <= 0) {
                return false;
            }
            int left = (int) (bitmapWidth * 0.2);
            int top = bitmapHeight / 2;
            int bottom = bitmapHeight - 1;
            int right = bitmapWidth - left;
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(left, top, right, bottom) // 测量图片下半部分的颜色，以确定用户信息的颜色
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            boolean isDark = ColorUtil.isBitmapDark(palette, bitmap);
                            int color = isDark?ContextCompat.getColor(MainActivity.this,R.color.base_white_text):ContextCompat.getColor(mActivity,R.color.base_black_600);
                            tvNickName.setTextColor(color);
                            tvDescription.setTextColor(color);
                            ivEdit.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
                        }
                    });
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser = UserHelper.getCurrentUser();
        super.onCreate(savedInstanceState);
        setupViews();
        requestPermissions();
        regeistReceiver();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_main;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        apkChangeReceiver.unregister(mActivity);
    }

    private void regeistReceiver() {
        apkChangeReceiver = new ApkChangeReceiver();
        apkChangeReceiver.register(mActivity);
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


    /**
     * 初始化数据
     */
    private void initData() {
        orderModel = new OrderModel(this);
        businessModel = new BusinessModel(this);
        List<AppClassfication> classfications = businessModel.queryAllAppClassfications();
        if(ListUtil.isNullOrEmpty(classfications)){
            businessModel.initDeafultData();
            businessModel.refreshApp(mHandler);
        }else {
            setVpAdater(1);
        }
    }


    /**
     * 设置viewpager的adapter
     */
    public void setVpAdater(int position) {
        appClassfications.clear();
        appClassfications.addAll(businessModel.queryAllAppClassfications());
        tab.setupWithViewPager(viewpager);

        if(vpAdater==null){
        vpAdater = new AppsVpAdater(getSupportFragmentManager(),appClassfications);
        viewpager.setAdapter(vpAdater);
        viewpager.setCurrentItem(position);
        }else {
            vpAdater.notifyDataSetChanged();
        }
    }

    public void setupViews() {
        toolbar = findViewById(R.id.toolbar);
        tab = findViewById(R.id.tab);
        ivAddItem = findViewById(R.id.iv_addItem);
        appBarLayout = findViewById(R.id.appBar);
        viewpager = findViewById(R.id.viewpager);
        tvShadowOrder = findViewById(R.id.tv_shadow_order);
        fbtCompose = findViewById(R.id.fbt_compose);
        navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        ivAddItem.setOnClickListener(this);
        fbtCompose.setOnClickListener(this);
        navView.setNavigationItemSelectedListener(this);
        animateToolbar();
        drawerLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                navView.getViewTreeObserver().removeOnPreDrawListener(this);
                loadUserInfo();
                return false;
            }
        });
    }

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        setTitle(isEditing?getString(R.string.app_edit):getString(R.string.app_app_name));
        toolbar.setNavigationIcon(isEditing?R.drawable.md_nav_back:R.drawable.ico_footer_more);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditing) {
                    EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_EXIST_EDITING));
                }else {
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
        if(toolbar==null) return;
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_addItem) {
            startActivityForResult(new Intent(this, AddAppClassficationActivity.class), REQUSET_APPCLASSIFICATION);
        } else if (i == R.id.fbt_compose) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu_toobar_apps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 动态刷新菜单栏
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        toolbar.setTitle(isEditing?getString(R.string.app_selected,selectedNum):getString(R.string.app_app_name));
        toolbar.setNavigationIcon(isEditing?R.drawable.md_nav_back:R.drawable.ic_person_outline_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditing){
                    EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_EXIST_EDITING));
                }else {
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
               SearchActivity.actionStartWithOptions(this,options);
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
        }else if (i == R.id.menu_selected) {
            EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_ALL_SELECTED));
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        //如果fragment有edipopup弹出 则先隐藏
        if(vpAdater.getCurrentFragment()!=null && vpAdater.getCurrentFragment().isPopupShowing()){
           vpAdater.getCurrentFragment().editPopup.dismiss();
        }else if(isEditing) {   //   //如果处于编辑状态则需要先退出编辑状态
            EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_EXIST_EDITING));
        } else if(drawerLayout.isDrawerOpen(GravityCompat.START)){ //如果drawerlayout未关闭，则需要先关闭
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if(System.currentTimeMillis() - lastClickBackTime<1500){
                ToastUtil.cancle();
                super.onBackPressed();
            }else {
                showShortToast(getString(R.string.app_click_more_to_exist));
                lastClickBackTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * 加载侧滑栏的用户信息
     */
    private void loadUserInfo() {
        int count = navView.getHeaderCount();
        if (count == 1) {
            String nickname = currentUser!=null?currentUser.getNickName():"";
            String avatar = currentUser!=null?currentUser.getAvanta():"";
            String description = currentUser!=null?currentUser.getDescribtion():"";
            String bgImage = currentUser!=null?currentUser.getBgImage():"";
            View headerView = navView.getHeaderView(0);
            LinearLayout userLayout = headerView.findViewById(R.id.ll_nav_user);
            LinearLayout descriptionLayout = headerView.findViewById(R.id.ll_nav_describe);
            ImageView ivBgImag = headerView.findViewById(R.id.iv_nav_login_bg);
            ivAvatar = headerView.findViewById(R.id.iv_nav_avatar);
            ivEdit = headerView.findViewById(R.id.iv_nav_edit);
            tvNickName = headerView.findViewById(R.id.tv_nav_nickname);
            tvDescription = headerView.findViewById(R.id.tv_nav_describe);
            tvNickName.setText(nickname);
            if (TextUtils.isEmpty(description)) {
                tvDescription.setText(getString(R.string.app_edit_personal_profile));
            } else {
                tvDescription.setText(getString(R.string.app_profile)+description);
            }
            Glide.with(this)
                    .load(avatar)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .placeholder(R.drawable.app_loading_bg_circle)
                    .error(R.drawable.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivAvatar);

            if (TextUtils.isEmpty(bgImage)) {
                if (!TextUtils.isEmpty(avatar)) {
                    Glide.with(this).load(avatar)
                            .bitmapTransform(new BlurTransformation(this, 15))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .listener(navHeaderBgLoadListner)
                            .into(ivBgImag);
                }
            } else {
                int bgImageWidth = navView.getWidth();
                //* 25为补偿系统状态栏高度，不加这个高度值图片顶部会出现状态栏的底色 */)
                float bgImageHeight = ScreenUtil.dip2px(mActivity,250+25);
                Glide.with(this).load(bgImage)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(bgImageWidth, (int) bgImageHeight)
                        .listener(navHeaderBgLoadListner)
                        .into(ivBgImag);
            }
            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    UserHomePageActivity.actionStartForResult(mActivity, REQUEST_EDIT_USER);
                }
            });
            descriptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    UserEditActivity.actionStartForResult(mActivity, REQUEST_EDIT_USER,true);
                }
            });
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUI(LaucherEvent laucherEvent){
        View mAppBarChildAt = appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams  mAppBarParams = (AppBarLayout.LayoutParams) mAppBarChildAt.getLayoutParams();

        switch (laucherEvent.getEventCode()){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == REQUSET_APPCLASSIFICATION){
            appClassfications.clear();
            appClassfications.addAll(businessModel.queryAllAppClassfications());
            vpAdater.notifyDataSetChanged();
        }else if(requestCode == REQUEST_EDIT_USER){
           currentUser = UserHelper.getCurrentUser();
           loadUserInfo();
        }
    }

    /**
     * 编辑状态下对app进行了选择操作后
     * @param appBeans
     */
    @SuppressLint("StringFormatMatches")
    public void onAppSelected(List<AppBean> appBeans){
        toolbar.setTitle(isEditing?getString(R.string.app_selected,appBeans.size()):getString(R.string.app_app_name));
    }

    /**
     * 编辑状态下 点击了全选
     * @param isAllSelected
     * @param edittingNum
     */
    public void onAppAllSelected(boolean isAllSelected,int edittingNum){
        menuItemSelected.setTitle(isAllSelected?getString(R.string.app_select_none):getString(R.string.app_select_all));
        toolbar.setTitle(isEditing?getString(R.string.app_selected,edittingNum):getString(R.string.app_app_name));
    }

    float clickX;
    float clickY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                clickX = event.getX();
                clickY = event.getY();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 给fragment提供获取点击屏幕坐标的方法
     * @return
     */
    public float[] getClickPosition(){
        return new float[]{clickX,clickY};
    }

    public static void startMain(Context context){
        Intent i = new Intent(context,MainActivity.class);
        context.startActivity(i);
    }
}
