package com.tj24.wanandroid.module.square;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.cache.CacheKey;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.http.respon.ShareRespon;

import java.lang.reflect.Type;

public class SquareRequest extends BaseRequest {

    /**
     * 获取广场数据 智能获取来源
     * @param page
     * @param callBack
     */
    public static void requestSquareArticle(int page,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getSquareArticle(page), 30 * 60 * 1000L, CacheKey.SQUARE_ARTICLES(page), new RequestListner<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSuccess(ArticleRespon<ArticleBean> data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {
                Type type = new TypeToken<ArticleRespon<ArticleBean>>(){}.getType();
                ArticleRespon articleRespon = new Gson().fromJson(cache,type);
                callBack.onSucces(articleRespon);
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * 获取广场数据 从网络强制获取
     * @param page
     * @param callBack
     */
    public static void requestSquareArticleWitoutCache(int page, WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        requestNet(CacheKey.SQUARE_ARTICLES(page),RetrofitWan.getInstance().getApiClient().getSquareArticle(page), new RequestListner<ArticleRespon<ArticleBean>>() {
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

    /**
     * 查看某个分享人的分享列表
     * @param authorId
     * @param page
     * @param callBack
     */
    public static void requstArticleByShareUser(int authorId,int page,WanAndroidCallBack callBack){
        request(RetrofitWan.getInstance().getApiClient().getArticleBySharer(authorId, page), 30 * 60 * 1000L, CacheKey.USER_SHARE_ARTICLES(authorId, page), new RequestListner<ShareRespon>() {
            @Override
            public void onSuccess(ShareRespon data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {
                ShareRespon shareRespon = new Gson().fromJson(cache,ShareRespon.class);
                callBack.onSucces(shareRespon);
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * 刷新某个分享人的分享列表
     * @param authorId
     * @param page
     * @param callBack
     */
    public static void requstArticleByShareUserWithoutCache(int authorId,int page,WanAndroidCallBack callBack){
        requestNet( CacheKey.USER_SHARE_ARTICLES(authorId, page),RetrofitWan.getInstance().getApiClient().getArticleBySharer(authorId, page), new RequestListner<ShareRespon>() {
            @Override
            public void onSuccess(ShareRespon data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {
                ShareRespon shareRespon = new Gson().fromJson(cache,ShareRespon.class);
                callBack.onSucces(shareRespon);
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * 查看自己的分享列表
     * @param page
     * @param callBack
     */
    public static void requstMyShare(int page,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getMyShare(page), new RequestListner<ShareRespon>() {
            @Override
            public void onSuccess(ShareRespon data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {
                ShareRespon shareRespon = new Gson().fromJson(cache,ShareRespon.class);
                callBack.onSucces(shareRespon);
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * 删除自己的分享
     * @param page
     * @param callBack
     */
    public static void deleteMyShare(int page,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().deleteMyShare(page), new RequestListner<String>() {
            @Override
            public void onSuccess(String data) {
                callBack.onSucces("");
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }


    /**
     * 分享
     * @param callBack
     */
    public static void ShareArticle(String title,String link,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().shareArticle(title,link), new RequestListner<String>() {
            @Override
            public void onSuccess(String data) {
                callBack.onSucces("");
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
