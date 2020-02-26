package com.tj24.wanandroid.module.search;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.HotKeyBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

import java.util.List;

public class SearchRequest extends BaseRequest {

    /**
     * 获取搜索热词
     * @param callBack
     */
    public static void requestHotKey(WanAndroidCallBack<List<HotKeyBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getHotKeys(), new RequestListner<List<HotKeyBean>>() {
            @Override
            public void onSuccess(List<HotKeyBean> data) {
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
     * 获取搜索内容
     * @param callBack
     */
    public static void requestSearchResult(int page,String key,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().searchArticle(page, key), new RequestListner<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSuccess(ArticleRespon<ArticleBean> data) {
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
