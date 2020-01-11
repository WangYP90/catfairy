package com.tj24.appmanager.model;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tj24.appmanager.R;
import com.tj24.appmanager.common.AppConst;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.PinyinUtils;
import com.tj24.base.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by energy on 2018/1/9.
 */

public class ApkModel {
    public static final String TAG = "ApkModel";

    /**
     * 获取packageinfos
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllPackageInfos(Context context){
       return context.getPackageManager().getInstalledPackages(0);
    }
    /**
     * 扫描所有应用并对比更新入库
     * @param
     * @return
     */
    public static List<AppBean> scanLocalInstallAppList(Context context, List<PackageInfo> packageInfos, MaterialDialog dialog) {
        List<AppBean> AppBeans = new ArrayList<AppBean>();
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo packageInfo = packageInfos.get(i);
            AppBean AppBean =  conversToAppInfo(packageInfo,context);
            AppBeans.add(AppBean);
            dialog.incrementProgress(1);
        }
        return AppBeans;
    }

    /**
     * 将packageinfo 转化为 AppBean
     * @param packageInfo
     * @return
     */
    public static AppBean conversToAppInfo(final PackageInfo packageInfo, Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = packageInfo.packageName;
        AppBean appBean = AppBeanDaoHelper.getInstance().queryObjById(packageName);

        boolean isSystem = isSystemApp(packageInfo);
        boolean isCanOpen = packageManager.getLaunchIntentForPackage(packageInfo.packageName)!=null;

        if(AppBeanDaoHelper.getInstance().queryObjById(packageName)==null){
            appBean = new AppBean();
            appBean.setPackageName(packageName);
            List<String> types = new ArrayList<>();
            types.add(isSystem? AppConst.CLASSFICATION_SYSTEM_ID : AppConst.CLASSFICATION_CUSTOM_ID);
            appBean.setType(types);
        }
        String name = null;
        try {
            name = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageInfo.packageName,0));
            String letter = PinyinUtils.getPingYin(name);
            String sortString = letter.substring(0, 1).toUpperCase();
            long firstInstalTime = packageInfo.firstInstallTime;
            long lastUpdateTime = packageInfo.lastUpdateTime;
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            String sourceDir = packageInfo.applicationInfo.sourceDir;
            appBean.setName(name);
            if (sortString.matches("[A-Z]")) {
                appBean.setLetters(sortString.toUpperCase());
            } else {
                appBean.setLetters("#");
            }
            appBean.setIsSystemApp(isSystem);
            appBean.setLastUpdateTime(lastUpdateTime);
            appBean.setFirstIntalTime(firstInstalTime);
            appBean.setVersionCode(versionCode);
            appBean.setVersionName(versionName);
            appBean.setIsCanOpen(isCanOpen);
            appBean.setApkSourceDir(sourceDir);
            if (packageInfo.applicationInfo.loadIcon(packageManager) != null) {
                String image = saveImageToGallery(packageInfo.packageName,packageInfo.applicationInfo.loadIcon(packageManager));
                appBean.setIco(image);
                LogUtil.i(TAG,"存入ico:"+packageInfo.packageName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appBean;
    }

    /**
     * 判断是否是系统应用
     * @param packageInfo
     * @return
     */
    private static boolean isSystemApp(PackageInfo packageInfo){
        if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
            return  true;
        }
        return false;
    }

    /**
     * 将draable对象转换为字节数组
     * @param drawable
     * @return
     */
    public static byte[] drawable2Byte(Drawable drawable){
        try {
            Bitmap bmp = (((BitmapDrawable)drawable).getBitmap());
            //第二步，声明并创建一个输出字节流对象
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //第三步，调用compress将Bitmap对象压缩为PNG格式，第二个参数为PNG图片质量，第三个参数为接收容器，即输出字节流os
            bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
            return os.toByteArray();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将图片保存到手机
     * @param
     * @param ico
     */
    private static String saveImageToGallery(String fileName, Drawable ico) {
        FileOutputStream fos = null;
        File currentFile = null;
        try {
            Bitmap bmp =null;
            if(ico instanceof BitmapDrawable){
                bmp = (((BitmapDrawable)ico).getBitmap());
            }else {
                Bitmap bitmap = Bitmap.createBitmap(ico.getIntrinsicWidth(),ico.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                ico.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                ico.draw(canvas);
                bmp = bitmap;
            }

            // 首先保存图片
//            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
//            String filePath = "cat_xiaoxian";
//            File appDir = new File(file ,filePath);
            File appDir = new File(com.tj24.base.constant.Const.BASE_APP_ICO);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            fileName = fileName + ".png";
            currentFile = new File(appDir, fileName);

            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    return currentFile.getPath();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * 获取顶层app
     * @return
     */
    public static AppBean getTopApp(Context context) {
        String topApp = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            boolean useGranted = isUseGranted();// 是否允许授权
            if (useGranted) {
                topApp = getHigherPackageName(BaseApplication.getContext());
            } else {
                //开启应用授权界面
                Intent i = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        } else {
            topApp = getLowerVersionPackageName(BaseApplication.getContext());
        }
        LogUtil.d(TAG,"topname="+topApp);
        return AppBeanDaoHelper.getInstance().queryObjById(topApp);
    }


    //卸载应用程序
    public static void unInstallApp(Context context, String packageName){
        Intent uninstall_intent = new Intent();
        uninstall_intent.setAction(Intent.ACTION_DELETE);
        uninstall_intent.setData(Uri.parse("package:"+packageName));
        context.startActivity(uninstall_intent);
    }

    /**
     * 高版本：获取顶层的activity的包名
     *
     * @return
     */
    private static String getHigherPackageName(Context context) {
        String topPackageName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)  context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            //time - 1800 * 1000, time 开始时间和结束时间的设置，在这个时间范围内 获取栈顶Activity 有效 ，半个小时内统计有效
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1800 * 1000, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager activityManager =(ActivityManager) context. getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
            topPackageName = topActivity.getPackageName();
        }
        return topPackageName;
    }

    /**
     * 判断  用户查看使用情况的权利是否给予app
     *
     * @return
     */
    public static boolean isUseGranted() {
        Context appContext = BaseApplication.getContext();
        AppOpsManager appOps = (AppOpsManager) appContext
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = -1;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mode = appOps.checkOpNoThrow("android:get_usage_stats",
                    android.os.Process.myUid(), appContext.getPackageName());
        }
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }
    /**
     * 低版本：获取栈顶app的包名
     *
     * @return
     */
    private static String getLowerVersionPackageName(Context context) {
        String topPackageName;//低版本  直接获取getRunningTasks
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
        topPackageName = topActivity.getPackageName();
        return topPackageName;
    }


    /**
     * 根据AppBean打开APP
     * @param clickBean
     */
    public static void openApp(Context context,AppBean clickBean){
        Intent i = context.getPackageManager().getLaunchIntentForPackage(clickBean.getPackageName());
        if (i != null) {
            clickBean.setOpenNum(clickBean.getOpenNum() + 1); //打开次数加1
            clickBean.setLastOpenTime(System.currentTimeMillis());
            AppBeanDaoHelper.getInstance().insertOrReplaceObj(clickBean);
            context.startActivity(i);
        } else {
            ToastUtil.showShortToast(context, context.getString(R.string.app_this_app_canot_start));
        }
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {

        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            String packageName = context.getApplicationContext().getPackageName();
            Drawable drawable = packageManager.getApplicationIcon(packageName);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (drawable instanceof BitmapDrawable) {
                    return ((BitmapDrawable) drawable).getBitmap();
                } else if (drawable instanceof AdaptiveIconDrawable) {
                    Drawable[] drr = new Drawable[2];
                    drr[0] = ((AdaptiveIconDrawable) drawable).getBackground();
                    drr[1] = ((AdaptiveIconDrawable) drawable).getForeground();

                    LayerDrawable layerDrawable = new LayerDrawable(drr);
                    int width = layerDrawable.getIntrinsicWidth();
                    int height = layerDrawable.getIntrinsicHeight();

                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    layerDrawable.draw(canvas);
                    return bitmap;
                }
            } else {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
