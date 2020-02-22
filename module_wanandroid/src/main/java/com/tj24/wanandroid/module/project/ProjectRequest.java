package com.tj24.wanandroid.module.project;

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

public class ProjectRequest extends BaseRequest {

    /**
     * 获取项目分类
     * @param callBack
     */
    public static void requestProjectWithoutCache(WanAndroidCallBack<List<TreeBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getProjectTree(), new RequestListner<List<TreeBean>>() {
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
     * 获取项目某个分类下的文章  强制从网络获取
     * @param page
     * @param cid
     */
    public static void requestProjectArticleWithoutCache(int page,int cid,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getProjectArticle(page, cid), new RequestListner<ArticleRespon<ArticleBean>>() {
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
     * 获取项目某个分类下的文章  智能获取
     * @param page
     * @param cid
     */
    public static void requestProjectArticle(int page,int cid,WanAndroidCallBack<ArticleRespon<ArticleBean>> callBack){
        request(RetrofitWan.getInstance().getApiClient().getProjectArticle(page, cid),
                24*3600*1000L, CacheKey.PROJECT_ARTICLE(page,cid),
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
