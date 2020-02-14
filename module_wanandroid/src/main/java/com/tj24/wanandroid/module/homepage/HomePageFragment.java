package com.tj24.wanandroid.module.homepage;

import android.view.View;

import com.tj24.base.bean.wanandroid.BannerBean;
import com.tj24.base.bean.wanandroid.UserBean;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.http.RetrofitWan;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;

import java.util.List;

public class HomePageFragment extends BaseWanAndroidFragment {

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_home_page;
    }

    @Override
    public void init(View view) {
        view.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortToast(mActivity,"dddd");
                HomePageRequest.reqeustBanner(new WanAndroidCallBack<List<BannerBean>>() {
                    @Override
                    public void onSucces(List<BannerBean> bannerBeans) {
                        LogUtil.e("tj24",bannerBeans.size()+"");
                    }

                    @Override
                    public void onFail(String fail) {
                        LogUtil.e("tj24",fail);
                    }
                });
            }
        });


        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitWan.getInstance().getApiClient().login("tangjiang24","3267265")
                        .enqueue(new WanAndroidCallBack<UserBean>() {
                            @Override
                            public void onSucces(UserBean userBean) {
                                LogUtil.e("tj","00");
                            }

                            @Override
                            public void onFail(String fail) {
                                LogUtil.e("tj","00");
                            }
                        });
            }
        });
    }

}
