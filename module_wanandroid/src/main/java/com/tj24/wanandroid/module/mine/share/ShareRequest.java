package com.tj24.wanandroid.module.mine.share;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ShareRespon;

public class ShareRequest extends BaseRequest {

    /**
     * 获取自己的分享
     * @param page
     * @param callBack
     */
    public static void requestMyShare(int page, WanAndroidCallBack<ShareRespon<ArticleBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getMyShare(page), new RequestListner<ShareRespon<ArticleBean>>() {
            @Override
            public void onSuccess(ShareRespon<ArticleBean> data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {

            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
