package com.tj24.module_appmanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class MsgApk implements Serializable {
    public static final long serialVersionUID = 1L;
    @Generated(hash = 71398669)
    public MsgApk() {
    }

    @Generated(hash = 1630522074)
    public MsgApk(String id, String packageName, String appName,
            long creatTimeMills, String creatDay, String creatHour, String action) {
        this.id = id;
        this.packageName = packageName;
        this.appName = appName;
        this.creatTimeMills = creatTimeMills;
        this.creatDay = creatDay;
        this.creatHour = creatHour;
        this.action = action;
    }

    public String getPackageName() {
        return this.packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public long getCreatTimeMills() {
        return this.creatTimeMills;
    }
    public void setCreatTimeMills(long creatTimeMills) {
        this.creatTimeMills = creatTimeMills;
    }
    public String getCreatDay() {
        return this.creatDay;
    }
    public void setCreatDay(String creatDay) {
        this.creatDay = creatDay;
    }
    public String getCreatHour() {
        return this.creatHour;
    }
    public void setCreatHour(String creatHour) {
        this.creatHour = creatHour;
    }
    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;
    /**
     * 包名
     */
    private String packageName;
    /**
     * APP名称
     */
    private String appName;
    /**
     * 消息创建时间
     */
    private long creatTimeMills;
    /**
     * 消息创建的日期：2016-08-03
     */
    private String creatDay;
    /**
     * 消息创建的时间：23:55:45
     */
    private String creatHour;
    /**
     * 动作:安装  卸载  更新
     */
    private String action;


}
