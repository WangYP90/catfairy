package com.tj24.wanandroid.module.treenavigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.NavigationBean;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.cache.CacheKey;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

import java.lang.reflect.Type;
import java.util.List;

public class TreeNaviRequest extends BaseRequest {

    /**
     * 获取知识体系数据  不用缓存
     * @param callBack
     */
    public static void requestKnowledgeWithoutCache(WanAndroidCallBack <List<TreeBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getKnowledgeTree(), new RequestListner<List<TreeBean>>() {
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
     * 获取知识体系数据  智能加载缓存
     * @param callBack
     */
    public static void requestKnowledge(WanAndroidCallBack <List<TreeBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getKnowledgeTree(),
                24*3600*1000L, CacheKey.KNOWLEDGE_TREE,new RequestListner<List<TreeBean>>() {
            @Override
            public void onSuccess(List<TreeBean> data) {
                callBack.onSucces(data);
            }

            @Override
            public void onSuccess(String cache) {
                Type type = new TypeToken<List<TreeBean>>(){}.getType();
                List<TreeBean> treeBeans = new Gson().fromJson(cache,type);
                callBack.onSucces(treeBeans);
            }

            @Override
            public void onFailed(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * 获取导航数据  不用缓存
     * @param callBack
     */
    public static void requestNaviWithoutCache(WanAndroidCallBack <List<NavigationBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getNavigation(), new RequestListner<List<NavigationBean>>() {
            @Override
            public void onSuccess(List<NavigationBean> data) {
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
     * 获取导航数据  智能加载缓存
     * @param callBack
     */
    public static void requestNavi(WanAndroidCallBack <List<NavigationBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getNavigation(),
                24*3600*1000L, CacheKey.NAVIGATIONS,new RequestListner<List<NavigationBean>>() {
                    @Override
                    public void onSuccess(List<NavigationBean> data) {
                        callBack.onSucces(data);
                    }

                    @Override
                    public void onSuccess(String cache) {
                        Type type = new TypeToken<List<NavigationBean>>(){}.getType();
                        List<NavigationBean> navigationBeans = new Gson().fromJson(cache,type);
                        callBack.onSucces(navigationBeans);
                    }

                    @Override
                    public void onFailed(String msg) {
                        callBack.onFail(msg);
                    }
                });
    }

    /**
     * 获取知识体系下的文章 不用缓存
     * @param page
     * @param cid
     * @param callBack
     */
    public static void requestTreeArticleWithOutCache(int page, int cid, WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        requestNet(CacheKey.KNOWLEDGE_ARTICLES(page, cid),
                RetrofitWan.getInstance().getApiClient().getTreeArticle(page, cid),
                new RequestListner<ArticleRespon<ArticleBean>>() {
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
     * 获取知识体系下的文章   用缓存
     * @param page
     * @param cid
     * @param callBack
     */
    public static void requestTreeArticle(int page, int cid , WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getTreeArticle(page, cid),
                24 * 3600 * 1000L, CacheKey.KNOWLEDGE_ARTICLES(page, cid),
                new RequestListner<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSuccess(ArticleRespon<ArticleBean> data) {
                callBack.onSucces(data);
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
}
