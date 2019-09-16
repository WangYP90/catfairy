package com.tj24.base.bean.appmanager;

import cn.bmob.v3.BmobObject;

public class CrashLog extends BmobObject {
    private String logContent;

    public CrashLog(String logContent) {
        this.logContent = logContent;
    }

    public CrashLog(String tableName, String logContent) {
        super(tableName);
        this.logContent = logContent;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
}
