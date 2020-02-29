package com.tj24.wanandroid.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.tj24.base.bean.wanandroid.CoinBean;
import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.base.utils.ColorUtil;
import com.tj24.base.utils.DrawableUtil;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.utils.UserUtil;
import com.tj24.wanandroid.module.mine.coin.CoinMeActivity;
import com.tj24.wanandroid.module.mine.coin.CoinRankActivity;
import com.tj24.wanandroid.module.mine.coin.CoinRequest;
import com.tj24.wanandroid.module.mine.collect.CollectActivity;
import com.tj24.wanandroid.module.mine.share.MyShareActivity;
import com.tj24.wanandroid.module.mine.todo.TodoActivity;
import com.tj24.wanandroid.user.LoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.palette.graphics.Palette;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NavigationHelper implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Activity context;

    View headerView;

    TextView tvNickName;
    ImageView ivAvatar;
    ImageView ivCoin;
    ImageView ivBgImag;
    TextView tvId;
    TextView tvLevle;

    CoinBean myCoin;

    public NavigationHelper(DrawerLayout drawerLayout, NavigationView navView, Activity context) {
        this.drawerLayout = drawerLayout;
        this.navView = navView;
        this.context = context;
        initView();
        loadUserInfo();
    }

    private void initView() {
        navView.setNavigationItemSelectedListener(this);
        int count = navView.getHeaderCount();
        if (count == 1) {
            headerView = navView.getHeaderView(0);
            ivBgImag = headerView.findViewById(R.id.iv_nav_login_bg);
            ivAvatar = headerView.findViewById(R.id.iv_nav_avatar);
            ivCoin = headerView.findViewById(R.id.iv_nav_coin);
            tvNickName = headerView.findViewById(R.id.tv_nav_nickname);
            tvId = headerView.findViewById(R.id.tv_nav_id);
            tvLevle = headerView.findViewById(R.id.tv_nav_levle);
            ivCoin.setOnClickListener(this);
            tvNickName.setOnClickListener(this);
            ivAvatar.setOnClickListener(this);
        }
    }

    /**
     * 加载侧滑栏的用户信息
     */
    @SuppressLint("StringFormatMatches")
    public void loadUserInfo() {
        int count = navView.getHeaderCount();
        if(count !=1){
            return;
        }
        if(UserUtil.getInstance().isLogin()){
            UserBean currentUser = UserUtil.getInstance().getUserBean();
            tvId.setVisibility(View.VISIBLE);
            tvLevle.setVisibility(View.VISIBLE);
            tvNickName.setText(currentUser.getNickname());
            tvId.setText(String.format(context.getString(R.string.wanandroid_user_id),currentUser.getId()));
            Glide.with(context)
                    .load(currentUser.getIcon())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .placeholder(R.drawable.base_avatar_default)
                    .error(R.drawable.base_avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivAvatar);
            CoinRequest.getMyCoin(new WanAndroidCallBack<CoinBean>() {
                @Override
                public void onSucces(CoinBean coinBean) {
                    myCoin = coinBean;
                    tvLevle.setText(String.format(context.getString(R.string.wanandroid_user_levle,myCoin.getLevel(),myCoin.getRank())));
                }

                @Override
                public void onFail(String fail) {

                }
            });
        }else {
            Glide.with(context).load(R.drawable.base_avatar_default).into(ivAvatar);
            tvNickName.setText("未登录");
            tvId.setVisibility(View.GONE);
            tvLevle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_nav_coin){
            CoinRankActivity.actionStart(context);
        }else if(v.getId() == R.id.tv_nav_nickname){
            toLogin();
        }else if(v.getId() == R.id.iv_nav_avatar){
            toLogin();
        }
    }


    /**
     * glide 加载图片的监听
     */
    RequestListener<Object, GlideDrawable> navHeaderBgLoadListner = new RequestListener<Object, GlideDrawable>() {
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
                            int color = isDark ? ContextCompat.getColor(context, R.color.base_white_text) : ContextCompat.getColor(context, R.color.base_black_666);
                            tvNickName.setTextColor(color);
                            tvId.setTextColor(color);
                            tvLevle.setTextColor(color);
                        }
                    });
            return false;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.item_coin) {
          CoinMeActivity.actionStart(context, myCoin!=null?myCoin.getCoinCount():0);
        } else if (itemId == R.id.item_collect) {
            CollectActivity.actionStart(context);
        } else if (itemId == R.id.item_share) {
            MyShareActivity.actionStart(context);
        } else if (itemId == R.id.item_todo) {
            TodoActivity.actionStart(context);
        } else if (itemId == R.id.item_loginout) {
            loginout();
        }
        return false;
    }

    private void loginout(){
        if(!UserUtil.getInstance().isLogin()){
            ToastUtil.showShortToast(context,"还未登录呢亲！");
            return;
        }
        new MaterialDialog.Builder(context).content("确定退出登录？").positiveText("确定").negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UserUtil.getInstance().loginout();
                        loadUserInfo();
                    }
                }).show();
    }

    private void toLogin() {
        if(!UserUtil.getInstance().isLogin()){
            LoginActivity.actionStart(context);
        }
    }
}
