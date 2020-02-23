package com.tj24.appmanager.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.utils.TextUtils;
import com.tj24.appmanager.R;
import com.tj24.appmanager.bean.event.LaucherEvent;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.daohelper.AppClassificationDaoHelper;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.base.utils.StringUtil;
import com.tj24.base.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

public class AppEditHelper {
    private Context context;
    private List<AppBean> editingApps = new ArrayList<>();
    private MaterialDialog moveDialog = null;
    private AppClassfication currentClassification;
    public AppEditHelper(Context mContext, List<AppBean> editingApps,AppClassfication currentClassification) {
        this.context = mContext;
        this.editingApps = editingApps;
        this.currentClassification = currentClassification;
    }

    public AppEditHelper(Context mContext, AppBean appBean,AppClassfication currentClassification) {
        this.context = mContext;
        this.currentClassification = currentClassification;
        this.editingApps.clear();
        this.editingApps.add(appBean);
    }

    /**
     * 卸载
     */
    public void unInstall(){
        for (AppBean info : editingApps){
            ApkModel.unInstallApp(context,info .getPackageName());
        }
    }

    /**
     * 去应用详情
     */
    public void goAppInfoActivity(){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", editingApps.get(0).getPackageName(), null));
        }
        context.startActivity(localIntent);
    }

    /**
     * 分享apk
     */
    public void share() {
        AppBean currentApp = editingApps.get(0);
        String sourceDir = currentApp.getApkSourceDir();
        if(currentApp.getPackageName().equals(context.getPackageName())){
            sourceDir = context.getPackageResourcePath();
        }
        new Share2.Builder((Activity) context)
                .setContentType(ShareContentType.FILE)
                .setShareFileUri(FileUtil.getFileUri(context, ShareContentType.FILE, new File(sourceDir)))
                .setTitle("Share File")
                .build()
                .shareBySystem();
    }

    /**
     * 设置自定义排序
     */
    public void setLevle() {
        AppBean currentApp = editingApps.get(0);
        new MaterialDialog.Builder(context).title(context.getString(R.string.app_custom_sorting)).content(context.getString(R.string.app_set_serial_number))
                .widgetColor(ContextCompat.getColor(context,R.color.base_colorPrimary))//输入框光标的颜色
                .positiveText(context.getString(R.string.app_confirm))
                .negativeText(context.getString(R.string.app_cancle))
                .inputType(InputType.TYPE_CLASS_NUMBER)
                //前2个一个是hint一个是预输入的文字
                .input("", String.valueOf(currentApp.getPriority()), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if(!TextUtils.isEmpty(input.toString()) && !input.toString().equals(String.valueOf(currentApp.getPriority()))){
                            currentApp.setPriority(Integer.parseInt(input.toString()));
                            AppBeanDaoHelper.getInstance().insertOrReplaceObj(currentApp);
                            ToastUtil.showShortToast(context,context.getString(R.string.app_set_prority_sucess));
                        }
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 创建复制移动的dialog
     */
    public void createMoveDialog() {
        List<String> classificationNames = new ArrayList<>();
        List<AppClassfication> classfications = new ArrayList<>();
        classfications.addAll(AppClassificationDaoHelper.getInstance().queryNoDefaultAppClasfications());
        Iterator it = classfications.iterator();
        while (it.hasNext()){
            AppClassfication classfication = (AppClassfication) it.next();
            if(!currentClassification.getName().equals(classfication.getName())){
                classificationNames.add(classfication.getName());
            }else {
                it.remove();
            }
        }

        if(classificationNames.size()==0){
            moveDialog = new MaterialDialog.Builder(context).title(context.getString(R.string.app_points)).content(context.getString(R.string.app_no_file_and_to_creat))
                    .positiveText(context.getString(R.string.app_creat)).negativeText(context.getString(R.string.app_cancle)).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            addClassification(moveDialog);
                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).show();
        }else {
            moveDialog = new MaterialDialog.Builder(context).positiveText(context.getString(R.string.app_confirm))
                    .negativeText(context.getString(R.string.app_cancle))
                    .neutralText(context.getString(R.string.app_creat_new_file))
                    .title(context.getString(R.string.app_move_apps))
                    .content(context.getString(R.string.app_select_target_file))
                    .items(classificationNames)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if(which == -1){
                                ToastUtil.showShortToast(context,context.getString(R.string.app_please_select_target_file));
                            }else {
                                AppClassfication selectClassification = classfications.get(which);
                                for(AppBean appBean:editingApps){
//                                    List<String> classficationsIds = new ArrayList<>();
//                                    // 如果movingtype为null 或者为默认文件夹 则复制  增加新type
//                                    if(currentClassification==null ||currentClassification.getIsDefault()){
//                                        classficationsIds.addAll(appBean.getType());
//                                        if(!classficationsIds.contains(selectClassification.getId())){
//                                            classficationsIds.add(selectClassification.getId());
//                                        }
//                                        appBean.setType(classficationsIds);
//                                    }else {
//                                        //剪贴  增加新type 移除原来的type
//                                        classficationsIds.addAll(appBean.getType());
//                                        if(!classficationsIds.contains(selectClassification.getId())){
//                                            classficationsIds.add(selectClassification.getId());
//                                        }
//                                        classficationsIds.remove(currentClassification.getId());
//                                        appBean.setType(classficationsIds);
//                                    }
                                    appBean.setCategory(selectClassification.getId());
                                    AppBeanDaoHelper.getInstance().insertOrReplaceObj(appBean);
                                }
                                dialog.dismiss();
                                EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_EXIST_EDITING));
                            }
                            return false;
                        }
                    }).onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            addClassification(moveDialog);
                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    /**
     * 新增dialog
     */
    private void addClassification(MaterialDialog dialog){
        dialog.dismiss();
        new MaterialDialog.Builder(context).title(context.getString(R.string.app_creat_new_file))
                .content(context.getString(R.string.app_file_name))
                .positiveText(context.getString(R.string.app_confirm))
                .negativeText(context.getString(R.string.app_cancle))
                .widgetColor(ContextCompat.getColor(context, R.color.base_colorPrimary))//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_TEXT)//可以输入的类型
                .inputRange(1,5)
                //前2个一个是hint一个是预输入的文字
                .input(context.getString(R.string.app_please_input_file_name), "", new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        List<AppClassfication> classfications = AppClassificationDaoHelper.getInstance().queryAll();
                        if (!TextUtils.isEmpty(input.toString()) && !isExistType(classfications,input.toString())) {
                            AppClassfication classfication = new AppClassfication();
                            classfication.setId(StringUtil.getUuid());
                            classfication.setName(input.toString());
                            classfication.setSortName(OrderConfig.ORDER_INSTALL_TIME);
                            classfication.setOrder(classfications.size());
                            classfication.setIsDefault(false);
                            AppClassificationDaoHelper.getInstance().insertOrReplaceObj(classfication);
                            ToastUtil.showShortToast(context,context.getString(R.string.app_creat_file_success));
                            createMoveDialog();
                        }else {
                            ToastUtil.showShortToast(context,context.getString(R.string.app_had_same_file));
                        }
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 判断新建的文件夹是否存在
     * @param name
     * @param  classfications
     * @return
     */
    private boolean isExistType(List<AppClassfication> classfications,String name){

        for (AppClassfication classfication:classfications){
            if(classfication.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}
