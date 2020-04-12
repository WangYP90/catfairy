package com.tj24.appmanager.common.update;

public interface CheckUpdateListner {
    public void onFail(String fail);
    public void onSuccess(String success);
}
