package com.tj24.wanandroid.module.mine.collect;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.NetUrlBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

import java.util.List;

public class CollectRequest extends BaseRequest {

    /**
     * 收藏站内文章
     * @param id
     * @param callBack
     */
    public static void collectArticleInStation(int id,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().collectArticleInStation(id), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
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
     * 收藏站外文章
     * @param
     * @param callBack
     */
    public static void collectArticleOutStation(String title,String author,String link,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().collectOutArticle(title, author, link), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
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
     * 收藏网址
     * @param
     * @param callBack
     */
    public static void collectLink(String name,String link,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().collectUrl(name,link), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
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
     * 编辑收藏的网址
     * @param
     * @param callBack
     */
    public static void updateCollectLink(int urlId,String name,String link,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().updateCollectUrl(urlId,name,link), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
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
     * 删除收藏的网址
     * @param
     * @param callBack
     */
    public static void deleteLink(int urlId,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().deleteCollectUrl(urlId), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
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
     * 在我的收藏中取消收藏
     * @param
     * @param callBack
     */
    public static void unCollectAtMine(int articleId,int originid,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().unCollectAtMine(articleId,originid), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
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
     * 在文中中 中取消收藏
     * @param
     * @param callBack
     */
    public static void unCollectAtArticle(int articleId,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().unCollectAtArticle(articleId), new RequestListner() {
            @Override
            public void onSuccess(Object data) {
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
     * 获取收藏的文章
     * @param
     * @param callBack
     */
    public static void getCollectAarticles(int page,WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getCollectArticles(page), new RequestListner<ArticleRespon<ArticleBean>>() {
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
     * 获取收藏的url
     * @param
     * @param callBack
     */
    public static void getCollectLinks(WanAndroidCallBack callBack){
        requestNet(RetrofitWan.getInstance().getApiClient().getCollectUrls(), new RequestListner<List<NetUrlBean>>() {
            @Override
            public void onSuccess(List<NetUrlBean> data) {
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
