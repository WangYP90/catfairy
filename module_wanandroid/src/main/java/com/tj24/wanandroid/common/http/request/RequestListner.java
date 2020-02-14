package com.tj24.wanandroid.common.http.request;

public interface RequestListner<T> {
    void onSuccess(T data);
    void onFailed(String msg);
}
