package com.tj24.wanandroid.common.http.request;

import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.cache.CacheListener;
import com.tj24.wanandroid.common.http.cache.CacheWan;
import com.tj24.wanandroid.common.http.respon.BaseRespon;

import retrofit2.Call;

public class BaseRequest {

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
            }

            @Override
            public void onFail(String fail) {
                requestListner.onFailed(fail);
            }
        });
    }


    /**
     * 直接从缓存加载bean，失败则网络获取
     * @param isList true返回的数据是list形式的 false是json对象形式的
     * @param key  缓存请求数据的key
     * @param clazz 缓存中保存的 实体的类
     * @param call retrofit的call
     * @param requestListner 请求回调
     * @param <S>网络请求返回的实体类型
     * @param <T>缓存中保存的类 的 类型
     */
    protected static<S,T> void requestCache(boolean isList,String key, Class<T>clazz,Call<BaseRespon<S>> call,RequestListner<S> requestListner){
        request(call,isList,true,key,clazz,requestListner);
    }


    /**
     * 如果没有主动区分，则先取缓存后判断是否有效 ，有效加载缓存，无效或者没有则从网络获取
     * @param call  retrofit的call
     * @param isList  true返回的数据是list形式的 false是json对象形式的
     * @param isCacheUsefull 缓存是否可用
     * @param key 缓存请求数据的key
     * @param clazz  缓存中保存的 实体的类
     * @param requestListner  请求回调
     * @param <S>  网络请求返回的实体类型
     * @param <T>  缓存中保存的类 的 类型
     */
    protected static<S,T> void request(Call<BaseRespon<S>> call,boolean isList,boolean isCacheUsefull,String key, Class<T>clazz, RequestListner<S> requestListner){

        if(!isCacheUsefull){
            requestNet(key,call,requestListner);
        }else {
            CacheWan.getInstance().getCache(isList,key,clazz, new CacheListener<S>() {
                @Override
                public void onSuccess(S data) {
                    requestListner.onSuccess(data);
                }

                @Override
                public void onFailed() {
                    requestNet(key,call,requestListner);
                }
            });
        }
    }
}
