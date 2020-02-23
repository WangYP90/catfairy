package com.tj24.wanandroid.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.base.utils.ColorUtil;
import com.tj24.base.utils.DrawableUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.utils.UserUtil;
import com.tj24.wanandroid.module.mine.coin.CoinMeActivity;
import com.tj24.wanandroid.module.mine.coin.CoinRankActivity;
import com.tj24.wanandroid.module.mine.collect.CollectActivity;
import com.tj24.wanandroid.module.mine.settings.SettingsActivity;
import com.tj24.wanandroid.module.mine.share.ShareActivity;
import com.tj24.wanandroid.module.mine.todo.TodoActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.palette.graphics.Palette;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NavigationHelper implements NavigationView.OnNavigationItemSelectedListener{

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
            ivCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CoinRankActivity.actionStart(context);
                }
            });
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
            tvLevle.setText(String.format(context.getString(R.string.wanandroid_user_levle,currentUser.getId(),currentUser.getId())));
            Glide.with(context)
                    .load(currentUser.getIcon())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .placeholder(R.drawable.base_avatar_default)
                    .error(R.drawable.base_avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivAvatar);
        }else {
            Glide.with(context).load(R.drawable.base_avatar_default).into(ivAvatar);
            tvNickName.setText("未登录");
            tvId.setVisibility(View.GONE);
            tvLevle.setVisibility(View.GONE);
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
            CoinMeActivity.actionStart(context);
        } else if (itemId == R.id.item_collect) {
            CollectActivity.actionStart(context);
        } else if (itemId == R.id.item_share) {
            ShareActivity.actionStart(context);
        } else if (itemId == R.id.item_todo) {
            TodoActivity.actionStart(context);
        } else if (itemId == R.id.item_settings) {
            SettingsActivity.actionStart(context);
        }
        return false;
    }
}
