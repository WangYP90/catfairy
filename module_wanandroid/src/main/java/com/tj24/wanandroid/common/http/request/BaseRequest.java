package com.tj24.wanandroid.common.http.request;

import com.tj24.base.utils.LogUtil;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.cache.CacheListener;
import com.tj24.wanandroid.common.http.cache.CacheWan;
import com.tj24.wanandroid.common.http.respon.BaseRespon;
import com.tj24.wanandroid.common.utils.WanSpUtil;

import retrofit2.Call;

public class BaseRequest {
    private static final String TAG = "BaseRequest";
    /**
     * 只从从网络获取不缓存
     * @param call retrofit的call
     * @param requestListner  请求回调
     * @param <T> 返回实体 类型
     */
    protected static<T> void requestNet(Call<BaseRespon<T>> call, RequestListner<T> requestListner){
        call.enqueue(new WanAndroidCallBack<T>() {
            @Override
            public void onSucces(T t) {
                requestListner.onSuccess(t);
            }

            @Override
            public void onFail(String fail) {
                requestListner.onFailed(fail);
            }
        });
    }

    /**
     * 只从从网络获取并刷新对应的缓存
     * @param key 缓存请求数据的key
     * @param call retrofit的call
     * @param requestListner  请求回调
     * @param <T> 返回实体 类型
     */
    protected static<T> void requestNet(String key,Call<BaseRespon<T>> call, RequestListner<T> requestListner){
        call.enqueue(new WanAndroidCallBack<T>() {
            @Override
            public void onSucces(T t) {
                requestListner.onSuccess(t);
                CacheWan.getInstance().save(key,t);
                LogUtil.v(TAG,"KEY:"+key+"====网络获取成功，并刷新缓存");
            }

            @Override
            public void onFail(String fail) {
                requestListner.onFailed(fail);
                LogUtil.v(TAG,"KEY:"+key+"====网络获取失败");
            }
        });
    }


    /**
     * 直接从缓存加载bean，失败则网络获取
     * @param key  缓存请求数据的key
     * @param call retrofit的call
     * @param requestListner 请求回调
     * @param <S>网络请求返回的实体类型
     */
    protected static<S> void requestCache(Call<BaseRespon<S>> call,String key,RequestListner<S> requestListner){
        request(call,30*24*3600*1000L,key,requestListner);
    }


    /**
     * 如果没有主动区分，则先取缓存后判断是否有效 ，有效加载缓存，无效或者没有则从网络获取
     * @param call  retrofit的call
     * @param userFulTime 缓存有效时间
     * @param key 缓存请求数据的key
     * @param requestListner  请求回调
     * @param <S>  网络请求返回的实体类型
     */
    protected static<S> void request(Call<BaseRespon<S>> call,long userFulTime,
                                     String key, RequestListner<S> requestListner){
        long lastUpdateTime = WanSpUtil.read(key,0L);
        boolean isCacheUsefull = System.currentTimeMillis() - lastUpdateTime < userFulTime;
        if(!isCacheUsefull){
            requestNet(key,call,requestListner);
            LogUtil.v(TAG,"KEY:"+key+"====缓存失效过时，去加载网络数据");
        }else {
            CacheWan.getInstance().getCache(key, new CacheListener() {
                @Override
                public void onSuccess(String cache) {
                    requestListner.onSuccess(cache);
                    LogUtil.v(TAG,"KEY:"+key+"====缓存有效，获取成功 data字符数目:"+cache.length());
                }

                @Override
                public void onFailed() {
                    requestNet(key,call,requestListner);
                    LogUtil.v(TAG,"KEY:"+key+"====缓存有效，但缓存获取失败,然后加载网络数据");
                }
            });
        }
    }
}
