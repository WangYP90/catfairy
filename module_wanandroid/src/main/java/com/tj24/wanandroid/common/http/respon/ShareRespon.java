package com.tj24.wanandroid.common.http.respon;


import com.tj24.base.bean.wanandroid.CoinBean;

public class ShareRespon<T> {
    private CoinBean coinInfo;
    private ArticleRespon<T> shareArticles;

    public CoinBean getCoinInfo() {
        return coinInfo;
    }

    public void setCoinInfo(CoinBean coinInfo) {
        this.coinInfo = coinInfo;
    }

    public ArticleRespon<T> getShareArticles() {
        return shareArticles;
    }

    public void setShareArticles(ArticleRespon<T> shareArticles) {
        this.shareArticles = shareArticles;
    }
}
