package com.tj24.appmanager.common.update;

public class AppUpdate {
    private int versionNumber;
    private String versionName;
    private String url;
    private String updateLog;
    private boolean isForce;
    private int size;
    private String updateTime;

    public AppUpdate() {
    }

    public AppUpdate(int versionNumber, String versionName, String url, String updateLog, boolean isForce, int size, String updateTime) {
        this.versionNumber = versionNumber;
        this.versionName = versionName;
        this.url = url;
        this.updateLog = updateLog;
        this.isForce = isForce;
        this.size = size;
        this.updateTime = updateTime;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
