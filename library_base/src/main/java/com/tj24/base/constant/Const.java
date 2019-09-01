package com.tj24.base.constant;

import android.os.Environment;

/**
 * @Description:全局常量
 * @Createdtime:2019/3/17 23:48
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class Const {
    public static String BASE_PATH = Environment.getExternalStorageState() + "catFairy";
    public static class UserInfo{
        public static final String NICK_NAME = "nickname";
        public static final String AVATAR = "avatar";
        public static final String BG_IMAG = "bgimage";
        public static final String DESCRIPTION = "description";
    }
}
