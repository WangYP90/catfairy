package com.tj24.module_appmanager.model;

import android.app.Activity;
import android.text.InputType;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.utils.TextUtils;
import com.tj24.library_base.utils.StringUtil;
import com.tj24.library_base.utils.ToastUtil;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.bean.AppBean;
import com.tj24.module_appmanager.bean.AppClassfication;
import com.tj24.module_appmanager.common.OrderConfig;
import com.tj24.module_appmanager.greendao.daohelper.AppBeanDaoHelper;
import com.tj24.module_appmanager.greendao.daohelper.AppClassificationDaoHelper;

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
        new MaterialDialog.Builder(mContext).title("新增文件夹")
                .content("文件夹名称")
                .positiveText("确定")
                .negativeText("取消")
                .widgetColor(ContextCompat.getColor(mContext,R.color.colorPrimary))//输入框光标的颜色
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
                            ToastUtil.showShortToast(mContext,"文件夹创建成功");
                            if(onEditListner!=null){
                                onEditListner.onADDClassification(classfication);
                            }
                        }else {
                            ToastUtil.showShortToast(mContext,"已有相同的文件夹");
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
        new MaterialDialog.Builder(mContext).title("修改文件夹")
                .content("文件夹名称")
                .widgetColor(ContextCompat.getColor(mContext,R.color.colorPrimary))//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_TEXT)//可以输入的类型-电话号码
                .inputRange(1,5)
                //前2个一个是hint一个是预输入的文字
                .input("", appClassfication.getName(), new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if(!TextUtils.isEmpty(input.toString()) && !input.toString().equals(appClassfication.getSortName())){
                            appClassfication.setName(input.toString());
                            AppClassificationDaoHelper.getInstance().insertObj(appClassfication);
                            ToastUtil.showShortToast(mContext,"文件夹名称已修改");
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
        String[] items = new String[]{"删除文件夹","编辑文件夹"};
        new MaterialDialog.Builder(mContext).title("选择操作")
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
        new MaterialDialog.Builder(mContext).title("提示：")
                .content("数据无价，确认删除？")
                .positiveText("确认").negativeText("取消")
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
                        ToastUtil.showShortToast(mContext,"删除成功");
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
