package com.tj24.wanandroid.common.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.wanandroid.common.Const;

public class UserUtil {

    private UserBean userBean = null;

    private static class Holder {
        private static final UserUtil INSTANCE = new UserUtil();
    }

    public static UserUtil getInstance() {
        return Holder.INSTANCE;
    }

    private UserUtil() {
        getUserBean();
    }

    public UserBean getUserBean() {
        if (userBean == null) {
            String json = WanSpUtil.read(Const.SP_USER_BEAN, "");
            if (!TextUtils.isEmpty(json)) {
                try {
                    userBean = new Gson().fromJson(json, UserBean.class);
                } catch (Exception ignore) {
                }
            }
        }
        return userBean;
    }

    public void login(UserBean loginBean) {
        userBean = loginBean;
        String json = new Gson().toJson(loginBean);
        WanSpUtil.save(Const.SP_USER_BEAN, json);
    }

    public void loginout() {
        userBean = null;
        WanSpUtil.clear(Const.SP_USER_BEAN);
        WanSpUtil.clear(Const.SP_COOKIE);
    }

    public void update(UserBean loginBean) {
        userBean = loginBean;
        String json = new Gson().toJson(userBean);
        WanSpUtil.save(Const.SP_USER_BEAN, json);
    }

    public boolean isLogin() {
        UserBean userBean = getUserBean();
        if (userBean == null) {
            return false;
        }
        if (userBean.getId() > 0) {
            return true;
        }
        return false;
    }

    public int getUserId() {
        UserBean userBean = getUserBean();
        if (userBean == null) {
            return 0;
        }
        return userBean.getId();
    }

}
