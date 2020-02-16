package com.tj24.wanandroid.common.http.cache;

public interface CacheListener {
    void onSuccess(String json);
    void onFailed();
}
