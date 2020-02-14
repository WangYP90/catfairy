package com.tj24.wanandroid.module.homepage;

import com.tj24.base.bean.wanandroid.BannerBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.cache.CacheKey;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;

import java.util.List;

public class HomePageRequest extends BaseRequest {

    /**
     * 获取banner数据
     * @param callBack
     */
    public static void reqeustBanner(WanAndroidCallBack<List<BannerBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getBanners(),false,
                true, CacheKey.BANNER, BannerBean.class,  new RequestListner<List<BannerBean>>() {
            @Override
            public void onSuccess(List<BannerBean> data) {
                callBack.onSucces(data);
            }

            @Override
            public void onFailed(String msg) {
               callBack.onFail(msg);
            }
        });
    }
}
