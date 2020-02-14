package com.tj24.wanandroid.common.http.cache;

public interface CacheListener<E> {
    void onSuccess(E data);
    void onFailed();
}
