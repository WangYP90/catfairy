package com.tj24.library_base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Description:网络工具类
 * @Createdtime:2019/3/3 14:02
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class NetWorkUtil {
    /**
     * 网络状态为无网络
     */
    public static final String TYPE_NO_NET = "NO";
    /**
     * 网络状态为WIFI
     */
    public  static final String TYPE_WIFI = "WIFI";
    /**
     * 网络状态为移动商网络
     */
    public  static final String TYPE_MOBLE = "MOBILE";
    /**
     * 网络状态为未知
     */
    public static  final String TYPE_DO_NOT_KNOW = "DO_NOT_KNOW";


    /**
     * 获取当前网络状态类型及是否可用
     * @param context
     * @return
     */
    public  static String getNetworkStateName(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if(mNetworkInfo!=null && mNetworkInfo.isAvailable()){
            //获取网络类型
            int netWorkType =mNetworkInfo.getType();
            if(netWorkType== ConnectivityManager.TYPE_WIFI){
                return TYPE_WIFI;
            }else if(netWorkType== ConnectivityManager.TYPE_MOBILE){
                return TYPE_MOBLE;
            }else{
                return TYPE_DO_NOT_KNOW;
            }

        }else{
            return TYPE_NO_NET;
        }
    }
}