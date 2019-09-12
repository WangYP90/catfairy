package com.tj24.base.bean.appmanager.login;

import cn.bmob.v3.BmobUser;

import java.io.Serializable;

public class User extends BmobUser implements Serializable {

    private String nickName;
    private String avanta;
    private String bgImage;
    private String describtion;
    private String nicai;

    public User() {
    }

    public User(String nickName, String avanta, String bgImage, String describtion, String nicai) {
        this.nickName = nickName;
        this.avanta = avanta;
        this.bgImage = bgImage;
        this.describtion = describtion;
        this.nicai = nicai;
    }

    public String getAvanta() {
        return avanta;
    }

    public void setAvanta(String avanta) {
        this.avanta = avanta;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getNicai() {
        return nicai;
    }

    public void setNicai(String nicai) {
        this.nicai = nicai;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
