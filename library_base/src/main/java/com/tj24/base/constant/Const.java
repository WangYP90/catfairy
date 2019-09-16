package com.tj24.base.constant;

import android.os.Environment;

/**
 * @Description:全局常量
 * @Createdtime:2019/3/17 23:48
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class Const {
    /**
     * bmob application ID
     */
    public static final String BMOB_APPLICATION_ID = "f3be9ad68b19831f11e9138e619d22bd";
    /**
     * basepath
     */
    public static String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/catFairy/";
    /**
     * crash 保存路径
     */
    public static String CRASH_PATH = BASE_PATH + "crash/";

    /**
     * baseAppManager Path
     */
    public static String BASE_APP_PATH = BASE_PATH + "appManager/";
    /**
     * APPMANAGER 的 图片
     */
    public static String BASE_APP_PICTURE = BASE_APP_PATH + "pic/";
    /**
     * APPMANAGER  app ico的 图片
     */
    public static String BASE_APP_ICO = BASE_APP_PICTURE + "icos/";
    /**
     * APPMANAGER  app user的 图像和背景
     */
    public static String BASE_APP_USER = BASE_APP_PICTURE + "user/";


}
