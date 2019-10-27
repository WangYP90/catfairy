package com.tj24.appmanager.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tj24.appmanager.R;
import com.tj24.appmanager.common.AfterTextWacher;
import com.tj24.appmanager.login.LoginInterceptorCallBack;
import com.tj24.appmanager.model.UserEditModel;
import com.tj24.appmanager.util.ViewUtils;
import com.tj24.base.base.ui.CatTakePhotoActivity;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.ColorUtil;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.UserHelper;

import org.devio.takephoto.model.TResult;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

@Route(path = ARouterPath.AppManager.EDIT_USER_ACTIVITY,extras = ARouterPath.NEED_LOGIN)
public class UserEditActivity extends CatTakePhotoActivity implements View.OnClickListener {

    private static final String NICK_NAME_REG_EXP = "^[\u4E00-\u9FA5A-Za-z0-9_\\-]+$";
    private static final String IS_EDIT_DESCRIBTION = "isEditDescribtion";
    private ImageView ivAvatar;
    private ImageView ivBg;
    private TextView tvNickName;
    private TextView tvDescribtion;
    private TextInputLayout etLayoutNickName;
    private TextInputLayout etLayoutDescrition;
    private ImageView ivAvatarCamera;
    private ImageView ivBgCamera;
    private TextInputEditText etNickName;
    private TextInputEditText etDescrition;
    private TextView tvSave;

    //是否是点击编辑简介按钮跳转进来
    @Autowired(name = IS_EDIT_DESCRIBTION)
    public boolean isEditDescribtion;
    //model
    UserEditModel userEditModel;
    //拍照后的图像 背景
    String srcAvatar = "";
    String srcBgImag = "";
    //动作 （更换图像呢还是背景呢）
    int action;
    public static final int ACTION_AVATAR = 0;
    public static final int ACTION_BG = 1;
    //修改前的
    String avatar = "";
    String bgImag = "";
    String nickName = "";
    String descrition = "";

    TextWatcher nickNameWatcher = new AfterTextWacher() {
        @Override
        public void afterTextChanged(Editable s) {
            tvNickName.setText(s.toString());
            if(!TextUtils.isEmpty(s.toString()) && !TextUtils.isEmpty(validateNickname(s.toString()))){
                etLayoutNickName.setErrorEnabled(true);
                etLayoutNickName.setError(validateNickname(s.toString()));
            }else {
                etLayoutNickName.setErrorEnabled(false);
            }
        }
    };

    TextWatcher describtionWatcher = new AfterTextWacher() {
        @Override
        public void afterTextChanged(Editable s) {
            if(TextUtils.isEmpty(s.toString())){
                tvDescribtion.setVisibility(View.GONE);
            }else {
                tvDescribtion.setVisibility(View.VISIBLE);
                tvDescribtion.setText(getString(R.string.app_description_content,s.toString()));
                if(s.toString().length()>50){
                    etLayoutDescrition.setErrorEnabled(true);
                    etLayoutDescrition.setError(getString(R.string.app_over_counter));
                }
            }
        }
    };

    RequestListener<Object, Bitmap> bgLoadListener = new RequestListener<Object, Bitmap>() {
        @Override
        public boolean onException(Exception e, Object model, Target<Bitmap> target, boolean isFirstResource) {
            return false;
        }
        @Override
        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if(resource == null){
                return false;
            }
            int bitmapWidth = resource.getWidth();
            int bitmapHeight = resource.getHeight();
            if(bitmapHeight <= 0 || bitmapWidth <= 0){
                return false;
            }
            Palette.from(resource).maximumColorCount(3).clearFilters()
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            boolean isDark = ColorUtil.isBitmapDark(palette,resource);
                            if(isDark){
                                setToolbarAndStatusbarIconIntoLight();
                                tvSave.setTextColor(ContextCompat.getColorStateList(mActivity,R.color.app_save_bg_light));
                            }else {
                                setToolbarAndStatusbarIconIntoDark();
                                tvSave.setTextColor(ContextCompat.getColorStateList(mActivity,R.color.app_save_bg_dark));
                            }
                        }
                    });
            int left = (int) (bitmapWidth * 0.2);
            int right = bitmapWidth - left;
            int top = bitmapHeight / 2;
            int bottom = bitmapHeight - 1;
            Palette.from(resource).maximumColorCount(3).clearFilters()
                    .setRegion(left, top, right, bottom) // 测量图片下半部分的颜色，以确定用户信息的颜色
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            boolean isDark = ColorUtil.isBitmapDark(palette, resource);
                            int color = isDark? ContextCompat.getColor(mActivity, R.color.base_white_text)
                                    :ContextCompat.getColor(mActivity, R.color.base_black_600);
                            tvNickName.setTextColor(color);
                            tvDescribtion.setTextColor(color);
                        }
                    });
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = UserHelper.getCurrentUser();
        nickName = (user!=null && user.getNickName()!=null)?user.getNickName():"";
        avatar = user!=null && user.getAvanta()!=null?user.getAvanta():"";
        bgImag = user!=null && user.getBgImage()!=null?user.getBgImage():"";
        descrition = user!=null && user.getDescribtion()!=null?user.getDescribtion():"";

//        isEditDescribtion = getIntent().getBooleanExtra(IS_EDIT_DESCRIBTION,false);
        userEditModel = new UserEditModel(mActivity,getTakePhoto());
        initView();
        setupViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_user_edit;
    }

    private void initView() {
        ivAvatar = (ImageView) findViewById(R.id.iv_avanta);
        ivBg = findViewById(R.id.iv_bg);
        tvNickName = (TextView) findViewById(R.id.tv_nickName);
        tvDescribtion = (TextView) findViewById(R.id.tv_describtion);
        etLayoutNickName = findViewById(R.id.etLayout_nickName);
        etLayoutDescrition = findViewById(R.id.etLayout_descrition);
        ivAvatarCamera = (ImageView) findViewById(R.id.avatarCamera);
        ivBgCamera = (ImageView) findViewById(R.id.bgImageCamera);
        etNickName = (TextInputEditText) findViewById(R.id.et_nickName);
        etDescrition = (TextInputEditText) findViewById(R.id.et_describtion);
        tvSave = findViewById(R.id.tv_save);

        ivAvatarCamera.setOnClickListener(this);
        ivBgCamera.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        etNickName.addTextChangedListener(nickNameWatcher);
        etDescrition.addTextChangedListener(describtionWatcher);
    }

    private void setupViews() {
        setTitle(" ");
        transparentStatusBar();
        loadBgImage();
        Glide.with(this).load(avatar).asBitmap()
                .transform(new CropCircleTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.app_loading_bg_circle)
                .error(R.drawable.base_avatar_default)
                .into(ivAvatar);

        etNickName.setText(nickName);
        etNickName.setSelection(nickName.length());
        if(TextUtils.isEmpty(descrition)){
            tvDescribtion.setVisibility(View.GONE);
        }else {
            tvDescribtion.setVisibility(View.VISIBLE);
            etDescrition.setText(descrition);
        }

        if(isEditDescribtion){ //点击编辑describiton 跳转而来
            etDescrition.setSelection(etDescrition.getText().length());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSoftKeyboard(etDescrition);
                }
            },100);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.avatarCamera){
            action = ACTION_AVATAR;
            userEditModel.showTakePhotoDialog(action);
        }else if(v.getId()==R.id.bgImageCamera){
            action = ACTION_BG;
            userEditModel.showTakePhotoDialog(action);
        }else if(v.getId() == R.id.tv_save){
                User user = new User();
                if(!nickName.equals(etNickName.getText().toString().trim())){
                    user.setNickName(etNickName.getText().toString().trim());
                }
                if(!descrition.equals(etDescrition.getText().toString().trim())){
                    user.setDescribtion(etDescrition.getText().toString().trim());
                }
                if(!TextUtils.isEmpty(srcAvatar)){
                    user.setAvanta(srcAvatar);
                }
                if(!TextUtils.isEmpty(srcBgImag)){
                    user.setBgImage(srcBgImag);
                }
                userEditModel.save(user);
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String path = result.getImage().getOriginalPath();
        LogUtil.d(TAG, "拍照返回path="+path);
        if(TextUtils.isEmpty(path)){
            return;
        }
        if(action == ACTION_AVATAR){
            srcAvatar = path;
            Glide.with(this).load(srcAvatar).asBitmap()
                    .transform(new CropCircleTransformation(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.app_loading_bg_circle)
                    .error(R.drawable.base_avatar_default)
                    .into(ivAvatar);
            if (TextUtils.isEmpty(srcAvatar) && TextUtils.isEmpty(srcBgImag)) {
                Glide.with(this).load(srcAvatar).asBitmap()
                        .transform(new BlurTransformation(this, 20))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(bgLoadListener)
                        .into(ivBg);
            }
        }else if(action == ACTION_BG){
            srcBgImag = path;
            Glide.with(this).load(srcBgImag).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(bgLoadListener)
                    .into(ivBg);
        }
    }


    @Override
    public void onBackPressed() {
        exist();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            exist();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exist() {
        if (isUserInfoChanged()) {
            userEditModel.showIsSaveDialog();
        } else {
            finish();
        }
    }


    /**
     * 判断用户昵称是否合法。用户昵称长度必须在2-30个字符之间，并且只能包含中英文、数字、下划线和横线。
     */
    private String validateNickname(String nickName) {
        String vlidate = "";
        if (nickName.length() < 2) {
            vlidate = getString(R.string.app_nickname_length_too_short);
        } else if(nickName.length() >12){
            vlidate = getString(R.string.app_nickname_length_too_long);
        }else if (!nickName.matches(NICK_NAME_REG_EXP)) {
            vlidate = getString(R.string.app_nickname_invalidate);
        }
        return vlidate;
    }

    /**
     * 是否有改动
     * @return
     */
    private boolean isUserInfoChanged(){
        if(nickName.equals(etNickName.getText().toString())
                && descrition.equals(etDescrition.getText().toString())
                && TextUtils.isEmpty(srcBgImag)
                && TextUtils.isEmpty(srcAvatar)){
            return false;
        }
        return true;
    }

    private void loadBgImage() {
        if (TextUtils.isEmpty(bgImag)) {
            if (!TextUtils.isEmpty(avatar)) {
                Glide.with(this).load(avatar).asBitmap()
                        .transform(new BlurTransformation(this, 15))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(bgLoadListener)
                        .into(ivBg);
            }
        } else {
            Glide.with(this).load(bgImag).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(bgLoadListener)
                    .into(ivBg);
        }
    }

    /**
     * 设置toolbar和状态蓝的颜色为深色
     */
    private void setToolbarAndStatusbarIconIntoDark() {
        ViewUtils.setLightStatusBar(getWindow(), ivBg);
        if(toolbar != null){
            ViewUtils.setToolbarIconColor(this, toolbar, true);
        }
    }

    /**
     * 设置toolbar和状态栏的颜色为浅色
     */
    private void setToolbarAndStatusbarIconIntoLight() {
        ViewUtils.setLightStatusBar(getWindow(), ivBg);
        if(toolbar != null){
            ViewUtils.setToolbarIconColor(this, toolbar, false);
        }
    }

    public static void actionStartForResult(Activity context,int requstCode, boolean isEditDescribtion){
        ARouter.getInstance().build(ARouterPath.AppManager.EDIT_USER_ACTIVITY)
                .withBoolean(IS_EDIT_DESCRIBTION,isEditDescribtion)
                .navigation(context, requstCode, new LoginInterceptorCallBack(context));
    }
}
