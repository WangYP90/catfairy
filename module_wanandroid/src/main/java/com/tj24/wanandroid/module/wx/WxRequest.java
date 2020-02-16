package com.tj24.wanandroid.module.wx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.cache.CacheKey;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

import java.lang.reflect.Type;
import java.util.List;

public class WxRequest extends BaseRequest {

    /**
     * 获取公众号列表
     * @param callBack
     */
    public static void getWxs( WanAndroidCallBack<List<TreeBean>> callBack){
        requestCache(RetrofitWan.getInstance().getApiClient().getWxAccounts(), CacheKey.WX_CHAPTERS, new RequestListner<List<TreeBean>>() {
            @Override
            public void onSuccess(List<TreeBean> data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {
                Type type = new TypeToken<TreeBean>(){}.getType();
                callBack.onSucces(new Gson().fromJson(cache,type));
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * 强制刷新获取公众号列表
     * @param callBack
     */
    public static void getWxsWithoutCache( WanAndroidCallBack<List<TreeBean>> callBack){
       requestNet(CacheKey.WX_CHAPTERS, RetrofitWan.getInstance().getApiClient().getWxAccounts(), new RequestListner<List<TreeBean>>() {
           @Override
           public void onSuccess(List<TreeBean> data) {
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
     * 获取公众号下的文章
     * @param callBack
     */
    public static void getWxArticle(int authorId,int page, WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getWXArticle(authorId, page), 30 * 60 * 1000L, CacheKey.WX_ARTICLES(authorId, page), new RequestListner<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSuccess(ArticleRespon<ArticleBean> data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {
                Type type = new TypeToken<ArticleRespon<ArticleBean>>(){}.getType();
                ArticleRespon<ArticleBean> articleRespon = new Gson().fromJson(cache.toString(),type);
                callBack.onSucces(articleRespon);
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * 强制刷新 获取公众号下的文章
     * @param callBack
     */
    public static void getWxArticleWithoutCache(int authorId,int page, WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        requestNet( CacheKey.WX_ARTICLES(authorId, page), RetrofitWan.getInstance().getApiClient().getWXArticle(authorId, page), new RequestListner<ArticleRespon<ArticleBean>>() {
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
