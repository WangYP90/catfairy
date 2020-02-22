package com.tj24.appmanager.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.UserEditActivity;
import com.tj24.appmanager.activity.UserHomePageActivity;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.utils.ColorUtil;
import com.tj24.base.utils.DrawableUtil;
import com.tj24.base.utils.ScreenUtil;
import com.tj24.base.utils.UserHelper;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.tj24.appmanager.activity.MainActivity.REQUEST_EDIT_USER;

public class NavigationViewHelper {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Activity context;

    View headerView;
    LinearLayout descriptionLayout;
    LinearLayout userLayout;
    TextView tvNickName;
    ImageView ivAvatar;
    TextView tvDescription;
    ImageView ivEdit;
    ImageView ivBgImag;

    String nickname;
    String avatar;
    String description;
    String bgImage;

    public NavigationViewHelper(Activity context, NavigationView navView,DrawerLayout drawerLayout) {
        this.context = context;
        this.navView = navView;
        this.drawerLayout = drawerLayout;
        initView();
        loadUserInfo();
    }


    private void initView() {
        int count = navView.getHeaderCount();
        if (count == 1) {
            headerView = navView.getHeaderView(0);
            userLayout = headerView.findViewById(R.id.ll_nav_user);
            descriptionLayout = headerView.findViewById(R.id.ll_nav_describe);
            ivBgImag = headerView.findViewById(R.id.iv_nav_login_bg);
            ivAvatar = headerView.findViewById(R.id.iv_nav_avatar);
            ivEdit = headerView.findViewById(R.id.iv_nav_edit);
            tvNickName = headerView.findViewById(R.id.tv_nav_nickname);
            tvDescription = headerView.findViewById(R.id.tv_nav_describe);
            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    UserHomePageActivity.actionStartForResult(context, REQUEST_EDIT_USER);
                }
            });
            descriptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    UserEditActivity.actionStartForResult(context, REQUEST_EDIT_USER, true);
                }
            });
        }
    }

    /**
     * 加载侧滑栏的用户信息
     */
    public void loadUserInfo() {
        int count = navView.getHeaderCount();
        if(count !=1){
            return;
        }
        User currentUser = UserHelper.getCurrentUser();
        nickname = currentUser != null ? currentUser.getNickName() : "";
        avatar = currentUser != null ? currentUser.getAvanta() : "";
        description = currentUser != null ? currentUser.getDescribtion() : "";
        bgImage = currentUser != null ? currentUser.getBgImage() : "";

        tvNickName.setText(nickname);
        if (TextUtils.isEmpty(description)) {
            tvDescription.setText(context.getString(R.string.app_edit_personal_profile));
        } else {
            tvDescription.setText(context.getString(R.string.app_profile) + description);
        }
        Glide.with(context)
                .load(avatar)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.app_loading_bg_circle)
                .error(R.drawable.base_avatar_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivAvatar);

        if (TextUtils.isEmpty(bgImage)) {
            if (!TextUtils.isEmpty(avatar)) {
                Glide.with(context).load(avatar)
                        .bitmapTransform(new BlurTransformation(context, 15))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(navHeaderBgLoadListner)
                        .into(ivBgImag);
            }
        } else {
            navView.post(new Runnable() {
                @Override
                public void run() {
                    int bgImageWidth = navView.getWidth();
                    //* 25为补偿系统状态栏高度，不加这个高度值图片顶部会出现状态栏的底色 */)
                    float bgImageHeight = ScreenUtil.dip2px(context, 250 + 25);
                    Glide.with(context).load(bgImage)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .override(bgImageWidth, (int) bgImageHeight)
                            .listener(navHeaderBgLoadListner)
                            .into(ivBgImag);
                }
            });
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
                            tvDescription.setTextColor(color);
                            ivEdit.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        }
                    });
            return false;
        }
    };
}
