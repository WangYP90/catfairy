package com.tj24.base.base.ui;

/**
 * @Description: 在Activity或Fragment中进行网络请求所需要经历的生命周期函数。
 * @Createdtime:2019/3/10 19:44
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public interface RequestLifecycle {


    void startLoading();

    void loadFinished();

    void loadFailed(String msg);
}
