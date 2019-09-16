package com.tj24.appmanager.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tj24.appmanager.R;
import com.tj24.appmanager.adapter.UserHomePageAdapter;
import com.tj24.appmanager.login.UserHelper;
import com.tj24.appmanager.util.ViewUtils;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.utils.ColorUtil;
import com.tj24.base.utils.DrawableUtil;
import com.tj24.base.utils.ScreenUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import java.util.ArrayList;
import java.util.List;

public class UserHomePageActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener,
        View.OnClickListener {
    private static final int REQUEST_USER_EDIT = 100;
    private ImageView ivUserBg;
    private ImageView ivAvatan;
    private TextView tvNickName;
    private TextView tvInstalled;
    private TextView tvCollected;
    private TextView tvDescribtion;
    private CollapsingToolbarLayout collToolbarLayout;
    private AppBarLayout appBarLayout;
    private RecyclerView rvView;
    private FloatingActionButton fab;
    private CoordinatorLayout coordLayout;

    int scrollRange = -1;
    boolean isUserBgImageLoaded;
    boolean isUserBgImageDark;
    boolean isToolbarAndStatusbarIconDark;

    String avatar;
    String nickName;
    String describtion;
    String bgImag;

    private UserHomePageAdapter mUserHomePageAdapter;
    private List<AppBean> collectionApps = new ArrayList<>();
    private RequestListener<Object, GlideDrawable> userBgLoadListner = new RequestListener<Object, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if(resource == null){
                return false;
            }
            Bitmap bitmap = DrawableUtil.toBitmap(resource);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            if(bitmapWidth <= 0 || bitmapHeight <= 0){
                return false;
            }
            Palette.from(bitmap).maximumColorCount(3).clearFilters()
                    .setRegion(0,0 ,bitmapWidth-1, (int) (bitmapHeight*0.1))// 测量图片头部的颜色，以确定状态栏和导航栏的颜色
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            isUserBgImageDark = ColorUtil.isBitmapDark(palette,bitmap);
                            if(isUserBgImageDark){
                                isToolbarAndStatusbarIconDark = false;
                                setToolbarAndStatusbarIconIntoLight();
                            }else {
                                isToolbarAndStatusbarIconDark = true;
                                setToolbarAndStatusbarIconIntoDark();
                            }
                            isUserBgImageLoaded = true;
                        }
                    });

            int left = (int) (bitmapWidth * 0.2);
            int right = bitmapWidth - left;
            int top = bitmapHeight / 2;
            int bottom = bitmapHeight - 1;
            Palette.from(bitmap).maximumColorCount(3).clearFilters()
                    .setRegion(left,top,right,bottom) // 测量图片下半部分的颜色，以确定用户信息的颜色
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            boolean isDark = ColorUtil.isBitmapDark(palette,bitmap);
                            int color = isDark?ContextCompat.getColor(mActivity,R.color.base_white_text)
                                    :ContextCompat.getColor(mActivity,R.color.base_black_600);
                            tvNickName.setTextColor(color);
                            tvDescribtion.setTextColor(color);

                        }
                    });
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        initView();
        initRecyclerView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_user_home_page;
    }

    private void initData() {
        User currentUser = UserHelper.getCurrentUser();
        nickName = currentUser!=null?currentUser.getNickName():"";
        describtion = currentUser!=null?currentUser.getDescribtion():"";
        avatar = currentUser!=null?currentUser.getAvanta():"";
        bgImag = currentUser!=null?currentUser.getBgImage():"";
    }


    private void initView() {
        ivUserBg = (ImageView) findViewById(R.id.iv_userBg);
        ivAvatan = (ImageView) findViewById(R.id.iv_avatar);
        tvNickName = (TextView) findViewById(R.id.tv_nickName);
        tvCollected = (TextView) findViewById(R.id.tv_collected);
        tvInstalled = (TextView) findViewById(R.id.tv_installed);
        tvDescribtion = (TextView) findViewById(R.id.tv_describtion);
        collToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        rvView = (RecyclerView) findViewById(R.id.rv_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        coordLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

        ivAvatan.setOnClickListener(this);
        fab.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setAppBarLayoutCanDrag(false);
            }
        });
        collToolbarLayout.setTitle(" "); //隐藏title
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tvNickName.setLetterSpacing(0.1f);
        }

        tvInstalled.setText(getString(R.string.app_installed,20));
        tvCollected.setText(getString(R.string.app_collected,20));
        setupUserInfo();
        popFab();
    }

    private void setupUserInfo() {
        tvNickName.setText(nickName);
        tvDescribtion.setText(describtion);
        if (!TextUtils.isEmpty(describtion)) {
            tvDescribtion.setVisibility(View.VISIBLE);
            tvDescribtion.setText(getString(R.string.app_description_content,describtion));
        } else {
            tvDescribtion.setVisibility(View.INVISIBLE);
        }
        isUserBgImageLoaded = false;
        Glide.with(this).load(avatar)
                .bitmapTransform(new CropCircleTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.app_loading_bg_circle)
                .error(R.drawable.avatar_default)
                .into(ivAvatan);
        if(TextUtils.isEmpty(bgImag)){
            if(!TextUtils.isEmpty(avatar)){
                Glide.with(this).load(bgImag)
                        .bitmapTransform(new BlurTransformation(this, 15))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(userBgLoadListner)
                        .into(ivUserBg);
            }
        }else {
            Glide.with(this).load(bgImag)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(userBgLoadListner)
                    .into(ivUserBg);
        }
    }


    private void initRecyclerView() {
        rvView.setVisibility(View.INVISIBLE);
        rvView.setHasFixedSize(true);
        rvView.setLayoutManager(new LinearLayoutManager(this));
        mUserHomePageAdapter = new UserHomePageAdapter(R.layout.app_rv_collections,collectionApps);
        rvView.setAdapter(mUserHomePageAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fab){
            UserEditActivity.actionStartForResult(mActivity,REQUEST_USER_EDIT,false);
        }else if(v.getId() == R.id.iv_avatar){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_USER_EDIT){
            initData();
            setupUserInfo();
            setResult(RESULT_OK);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(scrollRange == -1){
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if(scrollRange + verticalOffset < ScreenUtil.dip2px(this,8)){
            collToolbarLayout.setTitle(nickName);
        }else {
            collToolbarLayout.setTitle("");
        }
        if(!isUserBgImageLoaded){
            return;
        }
        if(collToolbarLayout.getHeight() + verticalOffset <collToolbarLayout.getScrimVisibleHeightTrigger()){
            // 先判断背景图是否是深色的，因为深色情况下不用改变状态栏和Toolbar的颜色，保持默认即可。
            if (!isUserBgImageDark && isToolbarAndStatusbarIconDark) {
                setToolbarAndStatusbarIconIntoLight();
                isToolbarAndStatusbarIconDark = false;
            }
        }else {    // 用户信息和状态栏分离
            // 先判断背景图是否是深色的，因为深色情况下不用改变状态栏和Toolbar的颜色，保持默认即可。
            if (!isUserBgImageDark && !isToolbarAndStatusbarIconDark) {
                setToolbarAndStatusbarIconIntoDark();
                isToolbarAndStatusbarIconDark = true;
            }
        }
    }

    /**
     * 设置toolbar和状态蓝的颜色为深色
     */
    private void setToolbarAndStatusbarIconIntoDark() {
        ViewUtils.setLightStatusBar(getWindow(), ivUserBg);
        if(toolbar != null){
            ViewUtils.setToolbarIconColor(this, toolbar, true);
        }
    }

    /**
     * 设置toolbar和状态栏的颜色为浅色
     */
    private void setToolbarAndStatusbarIconIntoLight() {
        ViewUtils.setLightStatusBar(getWindow(), ivUserBg);
        if(toolbar != null){
            ViewUtils.setToolbarIconColor(this, toolbar, false);
        }
    }

    /**
     * 设置AppBarLayout是否可拖动。
     * @param canDrag
     * true表示可以拖动，false表示不可以。
     */
    private void setAppBarLayoutCanDrag(boolean canDrag) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) layoutParams.getBehavior();
        if(behavior !=null){
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return canDrag;
                }
            });
        }
    }
    /**
     * 使用pop动画的方式将fab按钮显示出来。
     */
    private void  popFab() {
        fab.setImageResource(R.drawable.ic_edit);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.base_colorAccent)));

        fab.show();
        fab.setAlpha(0f);
        fab.setScaleX(0f);
        fab.setScaleY(0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                fab,
                PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f));
        animator.setStartDelay(200);
        animator.start();
    }

    public static void actionStartForResult(Activity context, int requestCode){
        Intent i = new Intent(context,UserHomePageActivity.class);
        context.startActivityForResult(i,requestCode);
    }
}
