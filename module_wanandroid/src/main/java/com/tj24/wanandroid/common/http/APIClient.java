package com.tj24.wanandroid.common.http;


import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.bean.wanandroid.BannerBean;
import com.tj24.base.bean.wanandroid.CoinBean;
import com.tj24.base.bean.wanandroid.CoinProgressBean;
import com.tj24.base.bean.wanandroid.HotKeyBean;
import com.tj24.base.bean.wanandroid.NavigationBean;
import com.tj24.base.bean.wanandroid.NetUrlBean;
import com.tj24.base.bean.wanandroid.TodoBean;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.base.bean.wanandroid.UseFullUrlBean;
import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;
import com.tj24.wanandroid.common.http.respon.BaseRespon;
import com.tj24.wanandroid.common.http.respon.ShareRespon;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIClient {

    //    首页相关
    /**
     * 首页文章列表  0代表页数
     */
    @GET("article/list/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> getHomePageArticles(@Path("page") int page);

    /**
     * 首页最新项目列表  0代表页数
     */
    @GET("article/listproject/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> getHomePageProjects(@Path("page") int page);
    /**
     * 首页banner
     */
    @GET("banner/json")
    Call<BaseRespon<List<BannerBean>>> getBanners();

    /**
     * 常用网站
     */
    @GET("friend/json")
    Call<BaseRespon<List<UseFullUrlBean>>> getUsefullUrl();

    /**
     *  搜索热词
     */
    @GET("hotkey/json")
    Call<BaseRespon<List<HotKeyBean>>> getHotKeys();

    /**
     *   搜索
     *   参数：页码：拼接在链接上，从0开始。 k ： 搜索关键词
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    Call<BaseRespon<ArticleRespon<ArticleBean>>>searchArticle(@Path("page")int page, @Field("k")String keyWord);

    /**
     *  置顶文章
     */
    @GET("article/top/json")
    Call<BaseRespon<List<ArticleBean>>> getTopArticle();



    /**
     *  体系数据
     */
    @GET("tree/json")
    Call<BaseRespon<List<TreeBean>>> getKnowledgeTree();

    /**
     *  知识体系下的文章  cid 分类的id，上述二级目录的id
     * 	页码：拼接在链接上，从0开始。
     */
    @GET("article/list/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> getTreeArticle(@Path("page") int page,@Query("cid") int cid);

    /**
     *  按照作者昵称搜索文章
     * 	页码：拼接在链接上，从0开始。 author：作者昵称，不支持模糊匹配。
     */
    @GET("article/list/{page}/json")
    Call<BaseRespon<List<NavigationBean>>> getAuthorArticle(@Path("page") int page, @Query("author") String author);


    /**
     *  导航数据
     */
    @GET("navi/json")
    Call<BaseRespon<List<NavigationBean>>> getNavigation();


    /**
     *  项目分类
     */
    @GET("project/tree/json")
    Call<BaseRespon<List<TreeBean>>> getProjectTree();

    /**
     *  某一个分类下项目列表数据
     *  cid 分类的id，上面项目分类接口
     * 	页码：拼接在链接中，从1开始
     */
    @GET("project/list/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> getProjectArticle( @Path("page")int page, @Query("cid") int cid);

    /**
     *  登录
     *  参数 :username，password
     *  登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证
     */
    @POST("user/login")
    @FormUrlEncoded
    Call<BaseRespon<UserBean>> login(@Field("username")String username, @Field("password") String password);
    /**
     *  注册
     *  参数 :username,password,repassword
     */
    @POST("user/register")
    @FormUrlEncoded
    Call<BaseRespon<UserBean>> register(@Field("username")String username,@Field("password") String password,@Field("repassword") String repassword);

    /**
     *  退出
     *  参数 :username,password,repassword
     */
    @GET("user/logout/json")
    Call<BaseRespon<UserBean>> loginOut();

    /**
     *  收藏的文章列表
     */
    @GET("lg/collect/list/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> getCollectArticles(@Path("page") int page);

    /**
     *   收藏站内文章
     *   参数：id:拼接在链接上
     */
    @POST("lg/collect/{id}/json")
    Call<BaseRespon<ArticleRespon>> collectArticleInStation(@Path("id") int articleId);
    /**
     *   收藏站外文章
     *   参数：title，author，link
     */
    @POST("lg/collect/add/json")
    @FormUrlEncoded
    Call<BaseRespon<ArticleRespon>> collectOutArticle(@Field("title") String title, @Field("author") String author, @Field("link") String link);

    /**
     *   文章列表中取消收藏
     *   参数：id:拼接在链接上
     */
    @POST("lg/uncollect_originId/{id}/json")
    Call<BaseRespon<ArticleRespon>> unCollectAtArticle(@Path("id") int articleId);

    /**
     *   我的收藏页面 取消收藏
     *   参数：id:拼接在链接上  originId:列表页下发，无则为-1
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    Call<BaseRespon<ArticleRespon>> unCollectAtMine(@Path("id") int articleId, @Field("originId") int originId);

    /**
     *   收藏的网站列表
     */
    @GET("lg/collect/usertools/json")
    Call<BaseRespon<List<NetUrlBean>>> getCollectUrls();

    /**
     *   收藏网址
     *   参数：name,link
     */
    @POST("lg/collect/addtool/json")
    @FormUrlEncoded
    Call<BaseRespon<NetUrlBean>>collectUrl(@Field("name")String name, @Field("link") String link);

    /**
     *   编辑收藏网站
     *   参数：id,name,link
     */
    @POST("lg/collect/updatetool/json")
    @FormUrlEncoded
    Call<BaseRespon<NetUrlBean>>updateCollectUrl(@Field("id")int netUrlId,@Field("name")String name, @Field("link") String link);

    /**
     *   删除收藏网站
     *   参数：id
     */
    @POST("lg/collect/deletetool/json")
    @FormUrlEncoded
    Call<BaseRespon<ArticleRespon>>deleteCollectUrl(@Field("id")int netUrlId);


    /**
     *   新增一个 todo
     *   参数：title: 新增标题（必须）
     * 	        content: 新增详情（必须）
     * 	        date: 2018-08-01 预定完成时间（不传默认当天，建议传）
     *      	type: 大于0的整数（可选）；
     * 	        priority 大于0的整数（可选）；
     */
    @POST("lg/todo/add/json")
    @FormUrlEncoded
    Call<BaseRespon<TodoBean>> addTodo(@FieldMap Map<String,Object> map);

    /**
     *   更新一个 Todo
     *   参数：id: 拼接在链接上，为唯一标识，列表数据返回时，每个todo 都会有个id标识 （必须）
     *      	title: 更新标题 （必须）
     * 	        content: 新增详情（必须）
     * 	        date: 2018-08-01（必须）
     * 	        status: 0 // 0为未完成，1为完成
     * 	        type: ；
     * 	        priority: ；
     */
    @POST("lg/todo/update/{id}/json")
    @FormUrlEncoded
    Call<BaseRespon<TodoBean>> updateTodo(@Path("id") int todoId,@FieldMap Map<String,Object> map);

    /**
     *   删除一个 Todo
     *   参数：id: 拼接在链接上，为唯一标识
     */
    @POST("lg/todo/delete/{id}/json")
    Call<BaseRespon>deleteTodo(@Path("id")int todoId);

    /**
     *   仅更新完Todo的完成状态
     *   id: 拼接在链接上，为唯一标识
     *  	status: 0或1，传1代表未完成到已完成，反之则反之。
     */
    @POST("lg/todo/done/{id}/json")
    @FormUrlEncoded
    Call<BaseRespon>deleteTodo(@Path("id")int todoId,@Field("status") int status);

    /**
     *   TODO 列表
     *  参数： 页码从1开始，拼接在url 上
     * 	        status 状态， 1-完成；0未完成; 默认全部展示；
     * 	        type 创建时传入的类型, 默认全部展示
     * 	        priority 创建时传入的优先级；默认全部展示
     * 	        orderby 1:完成日期顺序；2.完成日期逆序；3.创建日期顺序；4.创建日期逆序(默认)；
     */
    @POST("lg/todo/v2/list/{page}/json")
    @FormUrlEncoded
    Call<BaseRespon>queryTodo(@Path("page")int page,@FieldMap Map<String,Object> map);


    /**
     *   积分排行榜接口
     */
    @GET("coin/rank/{page}/json")
    Call<BaseRespon<ArticleRespon<CoinBean>>> getCoinRank(@Path("page") int page);

    /**
     *   获取个人积分
     */
    @GET("lg/coin/userinfo/json")
    Call<BaseRespon<CoinBean>> getMyCoin();

    /**
     *   获取个人积分 获取方式的列表
     */
    @GET("lg/coin/list/{page}/json")
    Call<BaseRespon<ArticleRespon<CoinProgressBean>>> getMyCoinLoad(@Path("page")int page);


    /**
     *   广场列表数据
     *   页码拼接在url上从0开始
     */
    @GET("user_article/list/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> getSquareArticle(@Path("page") int page);

    /**
     *   查看某个分享人对应列表数据
     *   用户id: 拼接在url上
     * 	页码拼接在url上从1开始
     */
    @GET("user/{id}/share_articles/{page}/json")
    Call<BaseRespon<ShareRespon>> getArticleBySharer(@Path("id")int authorId,@Path("page") int page);

    /**
     *   自己的分享的文章列表
     * 	页码拼接在url上从1开始
     */
    @GET("user/lg/private_articles/{page}/json")
    Call<BaseRespon<ShareRespon<ArticleBean>>> getMyShare(@Path("page") int page);

    /**
     *   删除自己分享的文章
     * 	参数：文章id，拼接在链接上
     */
    @POST("lg/user_article/delete/{id}/json")
    Call<BaseRespon<String>> deleteMyShare(@Path("id") int articleId);
    /**
     *   分享文章
     * 	参数：title  link
     */
    @POST("lg/user_article/add/json")
    @FormUrlEncoded
    Call<BaseRespon<String>>shareArticle(@Field("title")String title,@Field("link")String link);

    /**
     *   问答
     * 	参数：pageId,拼接在链接上，例如上面的1
     */
    @GET("wenda/list/{page}/json")
    Call<BaseRespon<String>>getWenda(@Path("page")int page);


    /**
     *   获取公众号列表
     *
     */
    @GET("wxarticle/chapters/json")
    Call<BaseRespon<List<TreeBean>>>getWxAccounts();

    /**
     * 获取公众号下的文章
     * id  page 拼在url后面
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> getWXArticle
    (@Path("id")int authorId,@Path("page")int page);

    /**
     * 搜索公众号下的文章
     * id  page 拼在url后面
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Call<BaseRespon<ArticleRespon<ArticleBean>>> searchWXArticle
    (@Path("id")int authorId, @Path("page")int page, @Query("k")String keyWord);
}
