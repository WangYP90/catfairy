package com.tj24.wanandroid.common.http.respon;


import com.tj24.base.bean.wanandroid.CoinBean;

public class ShareRespon {
    private CoinBean coinInfo;
    private ArticleRespon shareArticles;

    public CoinBean getCoinInfo() {
        return coinInfo;
    }

    public void setCoinInfo(CoinBean coinInfo) {
        this.coinInfo = coinInfo;
    }

    public ArticleRespon getShareArticles() {
        return shareArticles;
    }

    public void setShareArticles(ArticleRespon shareArticles) {
        this.shareArticles = shareArticles;
    }
}
