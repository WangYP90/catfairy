package com.tj24.wanandroid.module.mine.coin;

import com.tj24.base.bean.wanandroid.CoinBean;
import com.tj24.base.bean.wanandroid.CoinProgressBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

public class CoinRequest extends BaseRequest {

    /**
     * 获取积分排行榜
     * @param page
     * @param callBack
     */
    public static void getCoinRank(int page, WanAndroidCallBack<ArticleRespon<CoinBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getCoinRank(page), new RequestListner<ArticleRespon<CoinBean>>() {
            @Override
            public void onSuccess(ArticleRespon<CoinBean> data) {
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

    /**
     * 获取个人积分
     * @param callBack
     */
    public static void getMyCoin(WanAndroidCallBack<CoinBean>callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getMyCoin(), new RequestListner<CoinBean>() {
            @Override
            public void onSuccess(CoinBean data) {
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

    /**
     * 获取个人积分的 获取历程
     * @param page
     * @param callBack
     */
    public static void getMyCoinLoad(int page,WanAndroidCallBack<ArticleRespon<CoinProgressBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getMyCoinLoad(page), new RequestListner<ArticleRespon<CoinProgressBean>>() {
            @Override
            public void onSuccess(ArticleRespon<CoinProgressBean> data) {
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
