package com.tj24.appmanager.model;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.UserEditActivity;
import com.tj24.appmanager.login.UserHelper;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.constant.Const;
import com.tj24.base.utils.ScreenUtil;
import com.tj24.base.utils.ToastUtil;
import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;

public class UserEditModel extends BaseAppsManagerModel {
    private TakePhoto takePhoto;
    public UserEditModel(Activity mContext,TakePhoto takePhoto) {
        super(mContext);
        this.takePhoto = takePhoto;
    }

    public void showTakePhotoDialog(int action){
        String[] actions = new String[]{"拍照","从相册选择"};
        String title = "";
        if(action == UserEditActivity.ACTION_AVATAR){
            title = mContext.getString(R.string.app_avatar_dialog_title);
        }else if(action == UserEditActivity.ACTION_BG){
            title = mContext.getString(R.string.app_bg_dialog_title);
        }
             new MaterialDialog.Builder(mContext).title(title)
                    .items(actions).itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                           startTake(position,action);
                        }
                    }).show();
    }


    private void startTake(int position,int action) {
        TakePhotoOptions.Builder takePhotoOptions = new TakePhotoOptions.Builder();
        takePhotoOptions.setCorrectImage(true);
        takePhotoOptions.setWithOwnGallery(false);
        takePhoto.setTakePhotoOptions(takePhotoOptions.create());

        File file = new File(Const.BASE_APP_USER + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        if(position == 0){
            takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions(action));
        }else if(position == 1){
            takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions(action));
        }
    }

    private CropOptions getCropOptions(int action) {
        int width = ScreenUtil.getInstance(mContext).getScreenWidth();
        int height = width;
        if(action == UserEditActivity.ACTION_BG){
            height = width*5/7;
        }
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(true);
        return builder.create();
    }

    /**
     * 是否保存dialog
     */
    public void showIsSaveDialog() {
        new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.app_points))
                .content(mContext.getString(R.string.app_userinfo_updated_is_save))
                .positiveText(mContext.getString(R.string.app_give_up)).negativeText(mContext.getString(R.string.app_leave))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                mContext.finish();
            }
        }).show();
    }

    /**
     * 保存
     */
    public void save(User user) {
        saveUser(user);
        //向bmob上传文件会报错 应该是没有备案域名
//        if(!TextUtils.isEmpty(user.getAvanta())){
//           saveAvatar(user);
//        }else if(!TextUtils.isEmpty(user.getBgImage())){
//            saveBg(user);
//        }else {
//            saveUser(user);
//        }
    }

    private void saveUser(User user) {
        User currentUser = UserHelper.getCurrentUser();
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_save_success));
                    mContext.setResult(Activity.RESULT_OK);
                    mContext.finish();
                }else {
                    ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_save_fail));
                }
            }
        });
    }

    private void saveBg(User user) {
        BmobFile bgFile = new BmobFile(new File(user.getBgImage()));
        bgFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    user.setBgImage(bgFile.getFileUrl());
                    saveUser(user);
                }else {
                    ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_save_fail));
                }
            }
        });
    }

    private void saveAvatar(User user) {
        BmobFile avataFile = new BmobFile(new File(user.getAvanta()));
        avataFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    user.setAvanta(avataFile.getFileUrl());
                    if(!TextUtils.isEmpty(user.getBgImage())){
                        saveBg(user);
                    }else {
                        saveUser(user);
                    }
                }else {
                    ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_save_fail));
                }
            }
        });
    }
}
