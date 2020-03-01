package com.tj24.base.bean.wanandroid;

import java.io.Serializable;
import java.util.List;

public class NavigationBean implements Serializable {
    private List<ArticleBean> articles;
    private int cid;
    private String name;

    public List<ArticleBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleBean> articles) {
        this.articles = articles;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
