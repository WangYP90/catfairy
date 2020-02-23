package com.tj24.wanandroid.user;

import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.request.BaseRequest;
import com.tj24.wanandroid.common.http.request.RequestListner;
import com.tj24.wanandroid.common.utils.UserUtil;

public class UserRequest extends BaseRequest {

    /*
     *登录
     */
    public static void  login(String userName, String pwd, WanAndroidCallBack<UserBean> callBack){

        requestNet(RetrofitWan.getInstance().getApiClient().login(userName, pwd), new RequestListner<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                UserUtil.getInstance().login(data);
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


    /*
     *注册
     */
    public static void  regist(String userName, String pwd,String rePwd, WanAndroidCallBack<UserBean> callBack){

        requestNet(RetrofitWan.getInstance().getApiClient().register(userName, pwd,rePwd), new RequestListner<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
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


    /*
     *注销
     */
    public static void  loginOut(WanAndroidCallBack<UserBean> callBack){

        requestNet(RetrofitWan.getInstance().getApiClient().loginOut(), new RequestListner<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                UserUtil.getInstance().loginout();
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
