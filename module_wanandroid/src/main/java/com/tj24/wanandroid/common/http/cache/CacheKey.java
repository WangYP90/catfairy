package com.tj24.wanandroid.common.http.cache;

import com.tj24.wanandroid.common.utils.UserUtil;

/**
 * @Description:请求需要缓存时，对应的 key
 * @Createdtime:2020/2/14 19:52
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class CacheKey {

    private static final String HOME_PAGE_ARTICLES = "article/list/%d/json";  //page
    private static final String HOME_PAGE_PROJECTS = "article/listproject/%d/json";   //page
    public static final String BANNER = "banner/json";
    public static final String USEFUL_WEBS = "friend/json";
    public static final String HOT_KEYS = "hotkey/json";
    private static final String SEARCH = "article/query/%d/json?key=%s"; //page+key
    public static final String TOP_ARTICLES = "article/top/json";


    public static final String KNOWLEDGE_TREE = "tree/json";
    private static final String KNOWLEDGE_ARTICLES = "article/list/%d/json?cid=%d";//page+id
    private static final String AUTHOR_ARTICLES = "article/list/%d/json?author=%d";//page+author


    public static final String NAVIGATIONS = "navi/json";
    public static final String PROJECT_TREE = "project/tree/json";
    private static final String PROJECT_ARTICLE = "project/list/%d/json?cid=%d";//page+id


    private static final String COLLECT_ARTICLE = "lg/collect/list/%d/json";//page
    private static final String COLLECT_LINKS = "lg/collect/usertools/json";

    private static final String SQUARE_ARTICLES = "user_article/list/%d/json"; //page
    private static final String USER_SHARE_ARTICLES = "user/%d/share_articles/%d/json";//userId+page
    private static final String MINE_SHARE_ARTICLE = "user/lg/private_articles/%d/json";//page

    public static final String  WX_CHAPTERS = "wxarticle/chapters/json";
    private static final String WX_ARTICLES = "wxarticle/list/%d/%d/json";//id+page
    private static final String WXARTICLE_SEARCH = "wxarticle/list/%d/%d/json?key=%s";//id+page+key


    private static String addUserId(String key) {
        int userId = UserUtil.getInstance().getUserId();
        return userId + "@" + key;
    }

    public static String HOME_PAGE_ARTICLES( int page) {
        return String.format(HOME_PAGE_ARTICLES, page);
    }

    public static String HOME_PAGE_PROJECTS(int page) {
        return String.format(HOME_PAGE_PROJECTS, page);
    }

    public static String SEARCH( int page,String key) {
        return String.format(SEARCH, page, key);
    }

    public static String KNOWLEDGE_ARTICLES( int page,int id) {
        return String.format(KNOWLEDGE_ARTICLES, page, id);
    }

    public static String AUTHOR_ARTICLES( int page,String author) {
        return String.format(AUTHOR_ARTICLES, page, author);
    }

    public static String PROJECT_ARTICLE( int page,int id) {
        return String.format(PROJECT_ARTICLE, page, id);
    }


    public static String COLLECT_ARTICLE(int page) {
        return addUserId(String.format(COLLECT_ARTICLE, page));
    }

    public static String COLLECT_LINKS() {
        return addUserId(COLLECT_LINKS);
    }

    public static String SQUARE_ARTICLES(int page) {
        return String.format(SQUARE_ARTICLES, page);
    }

    public static String USER_SHARE_ARTICLES(int id, int page) {
        return String.format(USER_SHARE_ARTICLES, page, id);
    }

    public static String MINE_SHARE_ARTICLE( int page) {
        return addUserId(String.format(MINE_SHARE_ARTICLE, page));
    }

    public static String WX_ARTICLES(int id, int page) {
        return String.format(WX_ARTICLES, id, page);
    }

    public static String WXARTICLE_SEARCH(int id, int page, String key) {
        return String.format(WXARTICLE_SEARCH, id, page, key);
    }

}
