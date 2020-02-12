package com.tj24.wanandroid.common.http;

import com.tj24.base.bean.wanandroid.homepage.ArticleBean;
import com.tj24.base.bean.wanandroid.homepage.BannerBean;
import com.tj24.base.bean.wanandroid.homepage.FriendShipLinkBean;
import com.tj24.base.bean.wanandroid.homepage.HotKeyBean;
import com.tj24.base.bean.wanandroid.homepage.NavigationBean;
import com.tj24.base.bean.wanandroid.homepage.TreeBean;
import com.tj24.base.bean.wanandroid.homepage.UserBean;
import com.tj24.wanandroid.common.http.respon.BaseRespon;
import com.tj24.wanandroid.common.http.respon.DataRespon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIClient {

    //    首页相关
    /**
     * 首页文章列表  0代表页数
     */
    @GET("article/list/{page}/json")
    Call<BaseRespon<DataRespon<ArticleBean>>> getHomePageArticles(@Path("page") int page);

    /**
     * 首页最新项目列表  0代表页数
     */
    @GET("article/listproject/{page}/json")
    Call<BaseRespon<DataRespon<ArticleBean>>> getHomePageProjects(@Path("page") int page);
    /**
     * 首页banner
     */
    @GET("banner/json")
    Call<BaseRespon<List<BannerBean>>> getBanners();

    /**
     * 常用网站
     */
    @GET("friend/json")
    Call<BaseRespon<List<FriendShipLinkBean>>> getFriendShipLink();

    /**
     *  搜索热词
     */
    @GET("hotkey/json")
    Call<BaseRespon<List<HotKeyBean>>> getHotKeys();

    /**
     *  置顶文章
     */
    @GET("article/top/json")
    Call<BaseRespon<List<ArticleBean>>> getTopArticle();



    /**
     *  体系数据
     */
    @GET("tree/json")
    Call<BaseRespon<List<TreeBean>>> getSystemTree();

    /**
     *  知识体系下的文章  cid 分类的id，上述二级目录的id
     * 	页码：拼接在链接上，从0开始。
     */
    @GET("article/list/{page}/json?cid=60")
    Call<BaseRespon<List<ArticleBean>>> getTreeArticle(@Path("page") int page);

    /**
     *  按照作者昵称搜索文章
     * 	页码：拼接在链接上，从0开始。 author：作者昵称，不支持模糊匹配。
     */
    @GET("article/list/{page}/json?author={author}")
    Call<BaseRespon<List<NavigationBean>>> getAuthorArticle(@Path("page") int page, @Path("author")String author);


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
    @GET("project/list/{page}/json?cid={cid}")
    Call<BaseRespon<List<DataRespon<ArticleBean>>>> getProjectArticle(@Path("page")int page,@Path("cid")int cid);

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
    public static final String COLLECT_LIST = "/lg/collect/list/0/json";
    /**
     *   收藏站内文章
     *   参数：id:拼接在链接上
     */
    public static final String COLLECT_INSIDE = "/lg/collect/1165/json";
    /**
     *   收藏站外文章
     *   参数：title，author，link
     */
    public static final String COLLECT_OUTSIDE = "/lg/collect/add/json";
    /**
     *   文章列表中取消收藏
     *   参数：id:拼接在链接上
     */
    public static final String UNCOLLECT_ARTICLE = "/lg/uncollect_originId/2333/json";
    /**
     *   我的收藏页面 取消收藏
     *   参数：id:拼接在链接上  originId:列表页下发，无则为-1
     */
    public static final String UNCOLLECT_MINE = "/lg/uncollect/2805/json";
    /**
     *   收藏网站列表
     */
    public static final String COLLECT_TOOLS = "/lg/collect/usertools/json";
    /**
     *   收藏网址
     *   参数：name,link
     */
    public static final String COLLECT_ADD_TOOL = "/lg/collect/addtool/json";
    /**
     *   编辑收藏网站
     *   参数：id,name,link
     */
    public static final String COLLECT_UPDATE_TOOL = "/lg/collect/updatetool/json";
    /**
     *   删除收藏网站
     *   参数：id
     */
    public static final String COLLECT_DELETE_TOOL = "/lg/collect/deletetool/json";

    /**
     *   搜索
     *   参数：页码：拼接在链接上，从0开始。 k ： 搜索关键词
     */
    public static final String SEARCH = "/article/query/0/json";

    /**
     *   新增一个 todo
     *   参数：title: 新增标题（必须）
     * 	        content: 新增详情（必须）
     * 	        date: 2018-08-01 预定完成时间（不传默认当天，建议传）
     *      	type: 大于0的整数（可选）；
     * 	        priority 大于0的整数（可选）；
     */
    public static final String ADD_TODO = "/lg/todo/add/json";
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
    public static final String UPDATE_TODO = "/lg/todo/update/83/json";
    /**
     *   删除一个 Todo
     *   参数：id: 拼接在链接上，为唯一标识
     */
    public static final String DELETE_TODO = "/lg/todo/delete/83/json";
    /**
     *   仅更新完Todo的完成状态
     *   id: 拼接在链接上，为唯一标识
     *  	status: 0或1，传1代表未完成到已完成，反之则反之。
     */
    public static final String UPDATE_TODO_STATUS = "/lg/todo/done/80/json";
    /**
     *   TODO 列表
     *  参数： 页码从1开始，拼接在url 上
     * 	        status 状态， 1-完成；0未完成; 默认全部展示；
     * 	        type 创建时传入的类型, 默认全部展示
     * 	        priority 创建时传入的优先级；默认全部展示
     * 	        orderby 1:完成日期顺序；2.完成日期逆序；3.创建日期顺序；4.创建日期逆序(默认)；
     */
    public static final String TODO_LIST = "/lg/todo/v2/list/1/json";

    /**
     *   积分排行榜接口
     */
    public static final String COIN_RANK = "/coin/rank/1/json";
    /**
     *   获取个人积分
     */
    public static final String MY_COIN = "/lg/coin/userinfo/json";
    /**
     *   获取个人积分 获取方式的列表
     */
    public static final String MY_COIN_LIST = "lg/coin/list/1/json";

    /**
     *   广场列表数据
     *   页码拼接在url上从0开始
     */
    public static final String SQUARE_LIST = "/user_article/list/0/json";
    /**
     *   查看某个分享人对应列表数据
     *   用户id: 拼接在url上
     * 	页码拼接在url上从1开始
     */
    public static final String OTHER_SHARE_LIST = "/user/2/share_articles/1/json";
    /**
     *   自己的分享的文章列表
     * 	页码拼接在url上从1开始
     */
    public static final String MY_SHARE_LIST = "/user/lg/private_articles/1/json";
    /**
     *   删除自己分享的文章
     * 	参数：文章id，拼接在链接上
     */
    public static final String DELETE_MY_SHARE = "/lg/user_article/delete/9475/json";
    /**
     *   分享文章
     * 	参数：title  link
     */
    public static final String SHARE_ARTICLE = "/lg/user_article/add/json";

    /**
     *   问答
     * 	参数：pageId,拼接在链接上，例如上面的1
     */
    public static final String WENDA = "/wenda/list/1/json";
}
