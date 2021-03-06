package com.tj24.base.constant;

/**
 * @Description:
 * @Createdtime:2019/3/3 0:24
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class ARouterPath {


    /**
     * appmanager模块的
     */
    public static class AppManager{
        /**
         * 需要登录
         */
        public static final int NEED_LOGIN = 8;
        /**
         * 修改密码
         */
        public static final String RESET_PWD_ACTIVITY = "/appManager/resetPwd";
        /**
         * 云空间
         */
        public static final String CLOUD_SETTING_FRAGMENT = "/appManager/cloudsettings";
        /**
         * 编辑个人信息界面
         */
        public static final String EDIT_USER_ACTIVITY = "/appManager/editUser";
        /**
         * 个人主页面
         */
        public static final String USER_HOMEPAGE_ACTIVITY = "/appManager/userHomepage";
        /**
         * 登录页面
         */
        public static final String LOGIN_ACTIVITY = "/appManager/login";
    }

    /**
     * 玩Android 模块的
     */
    public static class WanAndroid{
        /**
         * 需要登录
         */
        public static final int NEED_LOGIN = 24;
        /**
         * 主页面
         */
        public static final String  MAIN_ACTIVITY = "/wanAndroid/main";
        /**
         * 登录页面
         */
        public static final String LOGIN_ACTIVITY = "/wanAndroid/login";
        /**
         * 我的积分
         */
        public static final String COIN_ACTIVITY = "/wanAndroid/coin";
        /**
         * 我的收藏
         */
        public static final String COLLECT_ACTIVITY = "/wanAndroid/collect";
        /**
         * 我的分享
         */
        public static final String SHARE_ACTIVITY = "/wanAndroid/share";
        /**
         * 我的TODO
         */
        public static final String TODO_ACTIVITY = "/wanAndroid/todo";
    }
}
