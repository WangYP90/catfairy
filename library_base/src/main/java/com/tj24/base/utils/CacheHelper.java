package com.tj24.base.utils;

import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
/**
 * @Description:可以获取应用大小及所有缓存大小
 * @Createdtime:2020/5/26 16:16
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class CacheHelper {
    private GetCacheCallBack cacheCallBack;
    private static final int HANDLER_CACHE_SIZE = 24;
    //缓存总大小
    long totalSize = 0;
    //安装的应用总数
    int appTotal;
    //已计算过缓存的应用数量
    int appScaned;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (HANDLER_CACHE_SIZE == msg.what) {
                if (msg.obj != null && msg.obj instanceof PackageStats) {
                    PackageStats info = (PackageStats) msg.obj;
                    totalSize += info.cacheSize;
                    appScaned ++;
                    if(appScaned == appTotal && cacheCallBack!=null){
                        cacheCallBack.onTotalCache(byteFormat(totalSize));
                    }
                }
            }
        }
    };

    public static CacheHelper getInstance(){
        return new CacheHelper();
    }

    /**
     * 获取所有应用的缓存总大小
     * @param mPackageManager
     * @param cacheCallBack
     */
    public void getAllCacheSize(PackageManager mPackageManager, GetCacheCallBack cacheCallBack){
        this.cacheCallBack = cacheCallBack;
        totalSize = 0;
        List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(0);
        appTotal = packageInfos.size();
        try {
            //通过反射机制获得该隐藏函数
            Method getPackageSizeInfo = mPackageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            for(int i =0;i<packageInfos.size();i++){
                getPackageSizeInfo.invoke(mPackageManager, packageInfos.get(i).packageName,new PkgSizeObserve());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearAllCache(PackageManager mPackageManager) {
        //获取到当前应用程序里面所有的方法
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            Method mFreeStorageAndNotifyMethod = mPackageManager.getClass().getMethod(
                    "freeStorageAndNotify", long.class, IPackageDataObserver.class);
            mFreeStorageAndNotifyMethod.invoke(mPackageManager,
                    (long) stat.getBlockCount() * (long) stat.getBlockSize(),
                    new PkgDataObserve());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     class PkgSizeObserve extends IPackageStatsObserver.Stub{
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            Message msg = mHandler.obtainMessage();
            msg.what = HANDLER_CACHE_SIZE;
            msg.obj = pStats;
            mHandler.sendMessage(msg);
        }
    }

     class PkgDataObserve extends IPackageDataObserver.Stub{
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

        }
    }

    public interface GetCacheCallBack{
        void onTotalCache(String cacheSize);
    }

    private String byteFormat(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString="";
        if(fileSize == 0){
            return 0+"B";
        } else if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
