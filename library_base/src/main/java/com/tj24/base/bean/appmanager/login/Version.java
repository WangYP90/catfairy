package com.tj24.base.bean.appmanager.login;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 版本升级实体类
 */
@Entity
public class Version implements Serializable {
    private static final long serialVersionUID = 13L;
    @Id
    private int versionCode;
    private String versionName;
    private boolean isForce;
    private String changeLog;
    private String url;
    @Generated(hash = 1499075358)
    public Version(int versionCode, String versionName, boolean isForce,
            String changeLog, String url) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.isForce = isForce;
        this.changeLog = changeLog;
        this.url = url;
    }
    @Generated(hash = 1433053919)
    public Version() {
    }
    public int getVersionCode() {
        return this.versionCode;
    }
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
    public String getVersionName() {
        return this.versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public boolean getIsForce() {
        return this.isForce;
    }
    public void setIsForce(boolean isForce) {
        this.isForce = isForce;
    }
    public String getChangeLog() {
        return this.changeLog;
    }
    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
