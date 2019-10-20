package com.tj24.base.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * @Description: 上传克隆 数据实体
 * @Createdtime:2019/10/20 20:21
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class AppData extends BmobObject implements Serializable {
    private String userId;
    private String userName;
    private String appBean;
    private String msgApks;
    private String appClassfication;
    private String tag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppBean() {
        return appBean;
    }

    public void setAppBean(String appBean) {
        this.appBean = appBean;
    }

    public String getMsgApks() {
        return msgApks;
    }

    public void setMsgApks(String msgApks) {
        this.msgApks = msgApks;
    }

    public String getAppClassfication() {
        return appClassfication;
    }

    public void setAppClassfication(String appClassfication) {
        this.appClassfication = appClassfication;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
