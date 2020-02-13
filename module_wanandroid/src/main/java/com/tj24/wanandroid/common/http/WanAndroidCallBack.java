package com.tj24.wanandroid.common.http;

import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.common.http.respon.BaseRespon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class WanAndroidCallBack<T> implements Callback<BaseRespon<T>> {

    public abstract void onSucces(T t);
    public abstract void onFail(String fail);

    @Override
    public void onResponse(Call<BaseRespon<T>> call, Response<BaseRespon<T>> response) {
       BaseRespon<T> baseRespon = response.body();
       if(baseRespon == null){
           onFail("没有数据");
       }
       if(baseRespon.getErrorCode() == 0){
            onSucces(baseRespon.getData());
       }else if(baseRespon.getErrorCode() == -1001){
           ToastUtil.showShortToast(BaseApplication.getContext(),"登录失效，需要重新登录");
           onFail(baseRespon.getErrorMsg());
       }else {
           onFail(baseRespon.getErrorMsg());
       }
    }

    @Override
    public void onFailure(Call<BaseRespon<T>> call, Throwable t) {
        onFail(t.getMessage());
    }
}
