package com.tj24.wanandroid.module.homepage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.BannerBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.cache.CacheKey;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

import java.lang.reflect.Type;
import java.util.List;

public class HomePageRequest extends BaseRequest {

    /**
     * 获取banner数据
     * @param callBack
     */
    public static void reqeustBanner(WanAndroidCallBack<List<BannerBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getBanners(), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
                callBack.onSucces((List<BannerBean>) data);
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
     * 获取首页博文数据
     * @param page
     * @param callBack
     */
    public static void requestArticle(int page,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getHomePageArticles(page), 30*60*1000L,
                CacheKey.HOME_PAGE_ARTICLES(page), new RequestListner<ArticleRespon<ArticleBean>>() {
                    @Override
                    public void onSuccess(ArticleRespon<ArticleBean> data) {
                      callBack.onSucces((ArticleRespon<ArticleBean>) data);
                    }

                    @Override
                    public void onSuccess(String cache) {
                            Type type = new TypeToken<ArticleRespon<ArticleBean>>(){}.getType();
                            ArticleRespon<ArticleBean> articleRespon = new Gson().fromJson(cache,type);
                            callBack.onSucces(articleRespon);
                        }

                    @Override
                    public void onFailed(String msg) {
                        callBack.onFail(msg);
                    }
                });
    }
    /**
     * 强制网络刷新首页博文数据
     * @param page
     * @param callBack
     */
    public static void requestArticleByNet(int page,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        requestNet(CacheKey.HOME_PAGE_ARTICLES(page), RetrofitWan.getInstance().getApiClient().getHomePageArticles(page),
                new RequestListner() {
                    @Override
                    public void onSuccess(Object data) {
                        callBack.onSucces((ArticleRespon<ArticleBean>) data);
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
     * 获取首页项目数据
     * @param page
     * @param callBack
     */
    public static void requestProject(int page,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getHomePageProjects(page), 30*60*1000L,
                CacheKey.HOME_PAGE_PROJECTS(page), new RequestListner<ArticleRespon<ArticleBean>>() {
                    @Override
                    public void onSuccess(ArticleRespon<ArticleBean> data) {
                        callBack.onSucces((ArticleRespon<ArticleBean>) data);
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
     * 强制网络刷新首页项目数据
     * @param page
     * @param callBack
     */
    public static void requestProjectByNet(int page,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        requestNet(CacheKey.HOME_PAGE_PROJECTS(page), RetrofitWan.getInstance().getApiClient().getHomePageArticles(page),
                new RequestListner() {
                    @Override
                    public void onSuccess(Object data) {
                        callBack.onSucces((ArticleRespon<ArticleBean>) data);
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
