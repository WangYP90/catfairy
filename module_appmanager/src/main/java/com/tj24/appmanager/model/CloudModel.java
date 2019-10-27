package com.tj24.appmanager.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tj24.appmanager.R;
import com.tj24.appmanager.common.Const;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.daohelper.AppClassificationDaoHelper;
import com.tj24.appmanager.daohelper.MsgApkDaoHelper;
import com.tj24.base.utils.UserHelper;
import com.tj24.base.bean.AppData;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.base.bean.appmanager.MsgApk;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.constant.BmobErrorCode;
import com.tj24.base.utils.DateUtil;
import com.tj24.base.utils.Sputil;
import com.tj24.base.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

public class CloudModel extends BaseAppsManagerModel {

    ProgressDialog progressDialog;

    public CloudModel(Context mContext) {
        super(mContext);
    }

    /**
     * 准备备份数据
     */
    public void readyPush(boolean isAuto){
        if(isAuto){
            pushDataToServe("自动备份",isAuto);
            return;
        }
        new MaterialDialog.Builder(mContext).title("数据备份")
                .content("设置标签")
                .widgetColor(ContextCompat.getColor(mContext,R.color.base_colorPrimary))//输入框光标的颜色
                .positiveText(mContext.getString(R.string.app_confirm))
                .negativeText(mContext.getString(R.string.app_cancle))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRange(1,15)
                //前2个一个是hint一个是预输入的文字
                .input("请输入标签", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        pushDataToServe(input.toString(),false);
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 推送数据到云端
     */
    private void pushDataToServe(String tags,boolean isAuto){
        if(!isAuto){
            showProgressDialog("","");
        }
        List<AppBean> appBeans = AppBeanDaoHelper.getInstance().queryAll();
        List<AppClassfication> appClassfications = AppClassificationDaoHelper.getInstance().queryAll();
        List<MsgApk> msgApks = MsgApkDaoHelper.getInstance().queryAll();
        User user = UserHelper.getCurrentUser();
        //系统应用中没有设置优先级 且 classfication 没有其他 的 不用上传 没有意义的
        Iterator<AppBean> it = appBeans.iterator();
        while (it.hasNext()){
            AppBean appBean = it.next();
            if(appBean.getType().contains(Const.CLASSFICATION_SYSTEM_NAME)){
                if(appBean.getType().size() == 1 && appBean.getPriority()==0){
                    it.remove();
                }
            }
        }
        Gson gson = new Gson();
        String json_appbeans = gson.toJson(appBeans);
        String json_appClassfications = gson.toJson(appClassfications);
        String json_msgApks = gson.toJson(msgApks);
        String userId = user!=null?user.getObjectId():"";
        String userName =user!=null? user.getNickName():"";


        AppData datas = new AppData();
        datas.setUserId(userId);
        datas.setUserName(userName);
        datas.setAppBean(json_appbeans);
        datas.setAppClassfication(json_appClassfications);
        datas.setMsgApks(json_msgApks);
        datas.setTag(tags);
        datas.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                hideProgressDialog();
                if(e==null){
                    Sputil.save(Const.SP_LAST_UPDATE,System.currentTimeMillis());
                    ToastUtil.showShortToast(mContext,"备份成功");
                }else {
                    ToastUtil.showShortToast(mContext,"备份失败");
                }
            }
        });
    }
    /**
     * 还原数据
     */
    public void readyPull(){
        showProgressDialog("","");
        BmobQuery<AppData>query = new BmobQuery<>();
        query.addWhereEqualTo("userId",UserHelper.getCurrentUser().getObjectId())
                .findObjects(new FindListener<AppData>() {
                    @Override
                    public void done(List<AppData> list, BmobException e) {
                        hideProgressDialog();
                        if(e==null && list!=null){
                            Collections.reverse(list);
                            if(list.size()<=10){
                                showPullDataDialog(list);
                            }else {
                                showPullDataDialog(list);
                                deleteMoreTen(list.subList(10,list.size()));  //只保留最近10条数据
                            }
                        }else {
                            ToastUtil.showShortToast(mContext,BmobErrorCode.getInstance().getErro(e.getErrorCode()));
                        }
                    }
                });
    }

    /**
     * 只保留10条数据 删除多余的
     * @param subList
     */
    private void deleteMoreTen(List<AppData> subList) {
        List<BmobObject> objects = new ArrayList<BmobObject>();
        for(AppData info:subList){
            objects.add(info);
        }
        final BmobBatch bmobBatch = new BmobBatch();
        bmobBatch.deleteBatch(objects).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {

            }
        });
    }

    /**
     * 还原数据的dialog
     * @param appDatas
     */
    private void showPullDataDialog(List<AppData> appDatas){
        List<String> items = new ArrayList<>();
        for(AppData data : appDatas){
            items.add(data.getTag()+ "\n"+DateUtil.formatString(DateUtil.SDF_3,data.getCreatedAt()));
        }
        final AppData[] selectData = new AppData[1];
        new MaterialDialog.Builder(mContext).title("还原数据").content("选择需还原记录")
                .positiveText(mContext.getString(R.string.app_confirm))
                .negativeText(mContext.getString(R.string.app_cancle))
                .items()
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selectData[0] = appDatas.get(which);
                        return false;
                    }
                }).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                saveData(selectData[0]);
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 保存数据
     * @param selectDatum
     */
    private void saveData(AppData selectDatum) {
        String json_appBean = selectDatum.getAppBean();
        String json_appClassification = selectDatum.getAppClassfication();
        String json_msgApks = selectDatum.getMsgApks();
        Gson gson = new Gson();
        List<AppBean> appBeans = gson.fromJson(json_appBean,new TypeToken<List<AppBean>>(){}.getType());
        List<AppClassfication> appClassfications = gson.fromJson(json_appClassification,new TypeToken<List<AppClassfication>>(){}.getType());
        List<MsgApk> msgApks = gson.fromJson(json_msgApks,new TypeToken<List<MsgApk>>(){}.getType());
        if(appBeans!=null &&!appBeans.isEmpty()){
            AppBeanDaoHelper.getInstance().deleteAll();
            AppBeanDaoHelper.getInstance().insertList(appBeans);
        }
        if(appClassfications!=null && !appClassfications.isEmpty()){
            AppClassificationDaoHelper.getInstance().deleteAll();
            AppClassificationDaoHelper.getInstance().insertList(appClassfications);
        }
        if(msgApks!=null && !msgApks.isEmpty()){
            MsgApkDaoHelper.getInstance().deleteAll();
            MsgApkDaoHelper.getInstance().insertList(msgApks);
        }
        ToastUtil.showLongToast(mContext,"还原数据成功");
    }

    private void showProgressDialog(String title,String  message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(mContext,title,message,false,false);
        }
    }

    private void hideProgressDialog() {
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
