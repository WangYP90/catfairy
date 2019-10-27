package com.tj24.base.utils;

import com.tj24.base.bean.appmanager.login.User;

import cn.bmob.v3.BmobUser;

/**
 * @Description:user 相关
 * @Createdtime:2019/3/17 23:41
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class UserHelper {

    public static void saveUser(){

    }

    public static User getCurrentUser() {
        return BmobUser.getCurrentUser(User.class);
    }
}
