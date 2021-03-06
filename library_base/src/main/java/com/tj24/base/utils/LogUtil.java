package com.tj24.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * @Description:日志工具类
 * @Createdtime:2019/3/3 0:20
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public  class LogUtil {
    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

//        public static final int LEVEL = WARN;
   public static final int LEVEL = VERBOSE;

    public static void v(String tag, String msg){
        if(LEVEL<=VERBOSE){
            Log.v(tag,msg);
        }
    }

    public static void d(String tag, String msg){
        if(LEVEL<=DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if(LEVEL<=INFO){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag, String msg){
        if(LEVEL<=WARN){
            Log.w(tag,msg);
        }
    }

    public static void e(String tag, String msg){
        if(LEVEL<=ERROR){
            Log.e(tag,msg);
        }
    }
    public static void e(String tag, String msg, Throwable e){
        if(LEVEL<=ERROR){
            Log.e(tag,msg,e);
        }
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
