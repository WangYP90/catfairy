package com.tj24.appmanager.login;

import android.text.TextUtils;

import cn.bmob.v3.BmobUser;
import com.tj24.base.bean.appmanager.login.User;
import com.tj24.base.constant.Const;
import com.tj24.base.utils.Sputil;

/**
 * @Description:user 相关
 * @Createdtime:2019/3/17 23:41
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class UserHelper {
    public static  String USER_NICK_NAME = "";
    public static  String USER_AVATAR = "";
    public static  String USER_BG_IMAG = "";
    public static  String USER_DESCRIPTION = "";

    public UserHelper() {
        USER_NICK_NAME = Sputil.read(Const.UserInfo.NICK_NAME,"");
        USER_AVATAR = Sputil.read(Const.UserInfo.AVATAR,"");
        USER_BG_IMAG = Sputil.read(Const.UserInfo.BG_IMAG,"");
        USER_DESCRIPTION = Sputil.read(Const.UserInfo.DESCRIPTION,"");
    }

    public  static void saveNickName(String nickName){
        if(!TextUtils.isEmpty(nickName)){
            Sputil.save(Const.UserInfo.NICK_NAME,nickName);
        }else {
            Sputil.clear(Const.UserInfo.NICK_NAME);
        }
    }

    public static void saveAvatar(String avatar){
        if(!TextUtils.isEmpty(avatar)){
            Sputil.save(Const.UserInfo.AVATAR,avatar);
        }else {
            Sputil.clear(Const.UserInfo.AVATAR);
        }
    }

    public static void saveBgImage(String bgImage){
        if(!TextUtils.isEmpty(bgImage)){
            Sputil.save(Const.UserInfo.BG_IMAG,bgImage);
        }else {
            Sputil.clear(Const.UserInfo.BG_IMAG);
        }
    }

    public static void saveDescription(String description){
        if(!TextUtils.isEmpty(description)){
            Sputil.save(Const.UserInfo.DESCRIPTION,description);
        }else {
            Sputil.clear(Const.UserInfo.DESCRIPTION);
        }
    }

    public static User getCurrentUser() {
        return BmobUser.getCurrentUser(User.class);
    }
}
