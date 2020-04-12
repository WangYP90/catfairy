package com.tj24.appmanager.common.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tj24.appmanager.R;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.appmanager.provider.UpdateProvider;
import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.constant.Const;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.Sputil;
import com.tj24.base.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import androidx.annotation.NonNull;
import okhttp3.Call;

public class CheckUpdateHelper {
    private static final String TAG = "CheckUpdateHelper";
    private static final String SP_IGNORE_VERSION = "ignore_version";
    private Context mContext;
    private CheckUpdateListner checkUpdateListner;
    String checkUrl = "https://raw.githubusercontent.com/tangjiang24/catfairy/master/apk/version.json";
//    String checkUrl = "https://gitee.com/tj24/tj24cloud/raw/master/catfiry/version.json";

    private MaterialDialog progressDialog;

    public CheckUpdateHelper(Context context) {
        this.mContext = context;
    }


    public void setCheckUpdateListner(CheckUpdateListner checkUpdateListner) {
        this.checkUpdateListner = checkUpdateListner;
    }

    public void checkUpdate(boolean isAutoCheck){
        if(isAutoCheck && Sputil.read(SP_IGNORE_VERSION,false)){
            return;
        }

        OkHttpUtils.get().url(checkUrl).tag(checkUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(checkUpdateListner!=null){
                  checkUpdateListner.onFail("检查更新失败，请重新尝试");
                }
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    LogUtil.e(TAG,"版本检测："+response);
                    AppUpdate appUpdate = new Gson().fromJson(response, AppUpdate.class);
                    if(appUpdate.getVersionNumber()> ApkModel.getVersionCode(BaseApplication.getContext())){
                        showUpdateDialog(appUpdate);
                    }else {
                        if(checkUpdateListner!=null){
                            checkUpdateListner.onSuccess("已经是最新版本");
                        }
                    }
                } catch (JsonSyntaxException e) {
                    if(checkUpdateListner!=null){
                        checkUpdateListner.onFail("检查更新出错");
                    }
                    e.printStackTrace();
                }
            }
        });
    }


    private void showUpdateDialog(AppUpdate appUpdate) {

        TextView tvVersionName;
        TextView tvSize;
        TextView tvUpdateLog;
        View customView = LayoutInflater.from(mContext).inflate(R.layout.app_dialog_update,null);
        tvVersionName = customView.findViewById(R.id.tv_versionName);
        tvSize = customView.findViewById(R.id.tv_size);
        tvUpdateLog = customView.findViewById(R.id.tv_updateLog);
        tvVersionName.setText(appUpdate.getVersionName());
        tvSize.setText(getPrintSize(appUpdate.getSize()));
        tvUpdateLog.setText(appUpdate.getUpdateLog());

      MaterialDialog.Builder updateDialogBuilder = new MaterialDialog.Builder(mContext).title("喵小仙更新啦")
                .customView(customView,true)
                .positiveText("立即更新")
                .canceledOnTouchOutside(!appUpdate.isForce())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                downLoadApk(appUpdate.getUrl(),appUpdate.getSize());
            }
        });

      if(!appUpdate.isForce()){
          updateDialogBuilder.checkBoxPrompt("不再提醒", false, new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if(isChecked){
                      Sputil.save(SP_IGNORE_VERSION,appUpdate.getVersionNumber());
                  }
              }
          }).negativeText("下次再说");
      }
      updateDialogBuilder.show();
    }

    private void downLoadApk(String url,int size) {
        progressDialog = new MaterialDialog.Builder(mContext).content("正在下载，请等待...")
                .progress(false,size,true)
                .canceledOnTouchOutside(false)
                .contentGravity(GravityEnum.CENTER).build();
        progressDialog.show();
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Const.BASE_PATH,"catfairy.apk") {
            @Override
            public void onError(Call call, Exception e, int id) {
                progressDialog.dismiss();
                ToastUtil.showShortToast(BaseApplication.getContext(),"下载出错");
            }

            @Override
            public void onResponse(File file, int id) {
                progressDialog.setContent("下载完成");
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = UpdateProvider.getUriForFile(mContext,ApkModel.getPackageName(mContext)+".update.fileprovider",file);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                }
                mContext.startActivity(intent);
                LogUtil.e(TAG,"下载完成");
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                progressDialog.setMaxProgress((int) (total));
                progressDialog.setProgress((int) (progress*total));
                LogUtil.e(TAG,"progress:"+(int) (progress));
            }
        });
    }

    private  String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }
}
