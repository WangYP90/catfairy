package com.tj24.appmanager.model;

import android.app.Activity;
import android.text.InputType;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.utils.TextUtils;
import com.tj24.base.utils.StringUtil;
import com.tj24.base.utils.ToastUtil;
import com.tj24.appmanager.R;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.daohelper.AppClassificationDaoHelper;

import java.util.Iterator;
import java.util.List;

public class AppClassificationEditModel extends BaseAppsManagerModel {
    private OnEditListner onEditListner;
    public AppClassificationEditModel(Activity mContext) {
        super(mContext);
    }

    /**
     * 新增dialog
     */
    public void showNewClassificationDialog(){
        new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.app_creat_new_file))
                .content(mContext.getString(R.string.app_file_name))
                .positiveText(mContext.getString(R.string.app_confirm))
                .negativeText(mContext.getString(R.string.app_cancle))
                .widgetColor(ContextCompat.getColor(mContext,R.color.base_colorPrimary))//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_TEXT)//可以输入的类型
                .inputRange(1,5)
                //前2个一个是hint一个是预输入的文字
                .input(mContext.getString(R.string.app_please_input_file_name), "", new MaterialDialog.InputCallback() {

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
                            ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_creat_file_success));
                            if(onEditListner!=null){
                                onEditListner.onADDClassification(classfication);
                            }
                        }else {
                            ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_had_same_file));
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
    protected boolean isExistType(List<AppClassfication> classfications,String name){

        for (AppClassfication classfication:classfications){
            if(classfication.getSortName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * 编辑dialog
     */
    public void showEditClassificationDialog(AppClassfication appClassfication,int appPosition){
        new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.app_update_file))
                .content(mContext.getString(R.string.app_file_name))
                .widgetColor(ContextCompat.getColor(mContext,R.color.base_colorPrimary))//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_TEXT)//可以输入的类型-电话号码
                .inputRange(1,5)
                //前2个一个是hint一个是预输入的文字
                .input("", appClassfication.getName(), new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if(!TextUtils.isEmpty(input.toString()) && !input.toString().equals(appClassfication.getSortName())){
                            appClassfication.setName(input.toString());
                            AppClassificationDaoHelper.getInstance().insertOrReplaceObj(appClassfication);
                            ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_file_edit_success));
                            if(onEditListner!=null){
                                onEditListner.onUpdateClassification(appClassfication,appPosition);
                            }
                        }
                    }
                }).show();
    }

    /**
     * 选择操作
     */
    public void showSelectOperationDialog(AppClassfication appClassfication,int appPosition) {
        String[] items = new String[]{mContext.getString(R.string.app_delete_file),
                mContext.getString(R.string.app_edit_file_name)};
        new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.app_select_action))
                .items(items)
                .autoDismiss(true)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position){
                            case 0:
                                showDeleteAppClassificationDialog(appClassfication,appPosition);
                                break;
                            case 1:
                                showEditClassificationDialog(appClassfication,appPosition);
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 删除dialog
     * @param appClassfication
     */
    private void showDeleteAppClassificationDialog(AppClassfication appClassfication,int appPosition) {
        new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.app_points))
                .content(mContext.getString(R.string.app_data_priceless_confirm_delete))
                .positiveText(mContext.getString(R.string.app_confirm)).negativeText(mContext.getString(R.string.app_cancle))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AppClassificationDaoHelper.getInstance().deleteObj(appClassfication);
                        List<AppBean> appBeans = AppBeanDaoHelper.getInstance().queryAll();
                        Iterator it = appBeans.iterator();
                        while(it.hasNext()){
                            AppBean appBean = (AppBean) it.next();
                            if(appBean.getType().contains(appClassfication.getName())){
                                AppClassificationDaoHelper.getInstance().deleteObj(appClassfication);
                            }
                        }
                        if(onEditListner!=null){
                            onEditListner.onDeletClassification(appClassfication,appPosition);
                        }
                        dialog.dismiss();
                        ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_delete_success));
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void setOnEditListner(OnEditListner onEditListner){
        this.onEditListner = onEditListner;
    }

  public interface OnEditListner{
        void onADDClassification(AppClassfication classfication);
        void onUpdateClassification(AppClassfication classfication,int position);
        void onDeletClassification(AppClassfication classfication,int position);
    }
}
