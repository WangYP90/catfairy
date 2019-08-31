package com.tj24.module_appmanager.bean.event;

public class ApkChangeEvent {
    public static final int ACTION_ADD = 1;
    public static final int ACTION_DEL = 2;
    public static final int ACTION_REPLACE = 3;
    private String packageName;
    private int action;

    public ApkChangeEvent() {
    }

    public ApkChangeEvent(String packageName, int action) {
        this.packageName = packageName;
        this.action = action;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
