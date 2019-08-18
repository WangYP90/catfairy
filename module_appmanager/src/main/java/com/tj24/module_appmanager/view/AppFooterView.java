package com.tj24.module_appmanager.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.utils.TextUtils;
import com.tj24.library_base.utils.StringUtil;
import com.tj24.library_base.utils.ToastUtil;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.bean.AppBean;
import com.tj24.module_appmanager.bean.AppClassfication;
import com.tj24.module_appmanager.bean.event.LaucherEvent;
import com.tj24.module_appmanager.common.OrderConfig;
import com.tj24.module_appmanager.greendao.daohelper.AppBeanDaoHelper;
import com.tj24.module_appmanager.greendao.daohelper.AppClassificationDaoHelper;
import com.tj24.module_appmanager.model.ApkModel;
import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;

public class AppFooterView extends LinearLayout implements View.OnClickListener {

    private TextView tvMove;
    private TextView tvDelete;
    private TextView tvInfo;
    private TextView tvLevle;
    private TextView tvShare;
    private TextView tvMore;

    private Context context;
    private List<AppBean> edittingApps = new ArrayList<>();
    private  AppClassfication currentClassification;
    private   int selectColor;
    private   int unSelectColor;

    public AppFooterView(Context context) {
        super(context);
        this.context = context;
    }

    public AppFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.apps_footer, null);
        initView(myView);
        addView(myView);
    }

    private void initView(View myView) {
        selectColor = ContextCompat.getColor(context,R.color.color_footer_text_select);
        unSelectColor = ContextCompat.getColor(context,R.color.color_footer_text_unselect);

        tvMove = myView.findViewById(R.id.tv_move);
        tvDelete = myView.findViewById(R.id.tv_delete);
        tvInfo = myView.findViewById(R.id.tv_info);
        tvLevle = myView.findViewById(R.id.tv_levle);
        tvShare = myView.findViewById(R.id.tv_share);
        tvMore = myView.findViewById(R.id.tv_more);
        tvMove.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        tvLevle.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvMore.setOnClickListener(this);
    }


    public void onEdittingAppChanged(List<AppBean> edittingApps,AppClassfication currentClassification){
        this.edittingApps = edittingApps;
        this.currentClassification = currentClassification;
        if(edittingApps.size()<=0){
            setTextCanNotTouch(tvDelete,R.drawable.ico_footer_del);
            setTextCanNotTouch(tvInfo,R.drawable.ico_footer_info);
            setTextCanNotTouch(tvMore,R.drawable.ico_footer_more);
            setTextCanNotTouch(tvMove,R.drawable.ico_footer_move);
            setTextCanNotTouch(tvLevle,R.drawable.ico_footer_prority);
            setTextCanNotTouch(tvShare,R.drawable.ico_footer_share);
        }else if(edittingApps.size()==1){
            setTextCanTouch(tvDelete,R.drawable.ico_footer_del);
            setTextCanTouch(tvInfo,R.drawable.ico_footer_info);
            setTextCanTouch(tvMore,R.drawable.ico_footer_more);
            setTextCanTouch(tvMove,R.drawable.ico_footer_move);
            setTextCanTouch(tvLevle,R.drawable.ico_footer_prority);
            setTextCanTouch(tvShare,R.drawable.ico_footer_share);
        }else {
            setTextCanTouch(tvDelete,R.drawable.ico_footer_del);
            setTextCanNotTouch(tvInfo,R.drawable.ico_footer_info);
            setTextCanTouch(tvMore,R.drawable.ico_footer_more);
            setTextCanTouch(tvMove,R.drawable.ico_footer_move);
            setTextCanNotTouch(tvLevle,R.drawable.ico_footer_prority);
            setTextCanNotTouch(tvShare,R.drawable.ico_footer_share);
        }
    }


    private void setTextCanTouch(TextView tv, int drawable){
        tv.setEnabled(true);
        tv.setTextColor(selectColor);
        Drawable topDrawable = ContextCompat.getDrawable(context,drawable);
        topDrawable.setColorFilter(selectColor,SRC_ATOP);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        tv.setCompoundDrawables(null, topDrawable, null, null);
    }

    private void setTextCanNotTouch(TextView tv, int drawable){
        tv.setEnabled(false);
        tv.setTextColor(unSelectColor);
        Drawable topDrawable = getResources().getDrawable(drawable);
        topDrawable.setColorFilter(unSelectColor,SRC_ATOP);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        tv.setCompoundDrawables(null, topDrawable, null, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_move:
                createMoveDialog();
                break;
            case R.id.tv_delete:
                for (AppBean info : edittingApps){
                    ApkModel.unInstallApp(context,info .getPackageName());
                }
                break;
            case R.id.tv_info:
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", edittingApps.get(0).getPackageName(), null));
                }
                context.startActivity(localIntent);
                break;
            case R.id.tv_levle:
                setLevle();
                break;
            case R.id.tv_share:
                share();
                break;
            case R.id.tv_more:

                break;
        }
    }

    /**
     * 分享apk
     */
    private void share() {
        new Share2.Builder((Activity) context)
                .setContentType(ShareContentType.FILE)
                .setShareFileUri(FileUtil.getFileUri(context, ShareContentType.FILE, new File(edittingApps.get(0).getApkSourceDir())))
                .setTitle("Share File")
                .build()
                .shareBySystem();
    }


    /**
     * 设置自定义排序
     */
    private void setLevle() {
        AppBean currentApp = edittingApps.get(0);
        new MaterialDialog.Builder(context).title("自定义排序").content("设置序列号")
                .widgetColor(ContextCompat.getColor(context,R.color.colorPrimary))//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_NUMBER)
                //前2个一个是hint一个是预输入的文字
                .input("", String.valueOf(currentApp.getPriority()), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if(!TextUtils.isEmpty(input.toString()) && !input.toString().equals(String.valueOf(currentApp.getPriority()))){
                            currentApp.setPriority(Integer.parseInt(input.toString()));
                            AppBeanDaoHelper.getInstance().insertObj(currentApp);
                            ToastUtil.showShortToast(context,"文件夹名称已修改");
                        }
                    }
                }).show();
    }


    MaterialDialog moveDialog = null;
    /**
     * 移动app到别的文件夹
     */
    private void createMoveDialog() {
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
            moveDialog = new MaterialDialog.Builder(context).title("提示").content("还没有文件夹，立即创建？")
                    .positiveText("创建").negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            addClassification(moveDialog);
                        }
                    }).show();
        }else {
            moveDialog = new MaterialDialog.Builder(context).positiveText("确定").negativeText("取消").neutralText("新建文件夹")
                    .title("移动应用").content("选择目标文件夹")
                    .items(classificationNames)
                    .autoDismiss(false)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if(which == -1){
                                ToastUtil.showShortToast(context," 请选择目标文件夹");
                            }else {
                                AppClassfication selectClassification = classfications.get(which);
                                for(AppBean appBean:edittingApps){
                                    List<String> classficationsIds = new ArrayList<>();
                                    // 如果movingtype为null 或者为默认文件夹 则复制  增加新type
                                    if(currentClassification==null ||currentClassification.getIsDefault()){
                                        classficationsIds.addAll(appBean.getType());
                                        if(!classficationsIds.contains(selectClassification.getId())){
                                            classficationsIds.add(selectClassification.getId());
                                        }
                                        appBean.setType(classficationsIds);
                                    }else {
                                        //剪贴  增加新type 移除原来的type
                                        classficationsIds.addAll(appBean.getType());
                                        if(!classficationsIds.contains(selectClassification.getId())){
                                            classficationsIds.add(selectClassification.getId());
                                        }
                                        classficationsIds.remove(currentClassification.getId());
                                        appBean.setType(classficationsIds);
                                    }
                                    AppBeanDaoHelper.getInstance().insertObj(appBean);
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
    public void addClassification(MaterialDialog dialog){
        dialog.dismiss();
        new MaterialDialog.Builder(context).title("新增文件夹")
                .content("文件夹名称")
                .widgetColor(ContextCompat.getColor(context,R.color.colorPrimary))//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_TEXT)//可以输入的类型
                .inputRange(1,5)
                //前2个一个是hint一个是预输入的文字
                .input("请输入文件夹名称", "", new MaterialDialog.InputCallback() {

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
                            AppClassificationDaoHelper.getInstance().insertObj(classfication);
                            ToastUtil.showShortToast(context,"文件夹创建成功");
                            createMoveDialog();
                        }else {
                            ToastUtil.showShortToast(context,"已有相同的文件夹");
                        }
                    }
                }).show();
    }

    /**
     * 判断新建的文件夹是否存在
     * @param name
     * @param  classfications
     * @return
     */
    protected boolean isExistType(List<AppClassfication> classfications,String name){

        for (AppClassfication classfication:classfications){
            if(classfication.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

}
